package de.kevrecraft.wirtschaftssystem.utilitys;

import de.kevrecraft.wirtschaftssystem.managers.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.UUID;

public class BarralShop {

    /*
    OWNER
    AMOUNT
    B:S
    ITEMTYPE
     */

    private final Sign sign;
    private final Barrel barrel;
    private final UUID owner;
    private int amount;
    private int sell;
    private int buy;
    private final Material type;

    public BarralShop(Sign sign, Barrel barrel, UUID owner, int amount, Material type) {
        this.sign = sign;
        this.barrel = barrel;
        this.owner = owner;
        this.amount = amount;
        this.type = type;
        this.buy = -1;
        this.sell = -1;
    }

    public void update() {
        setSide(sign, Side.FRONT);
        setSide(sign, Side.BACK);
        sign.update(true);
        sign.setWaxed(true);
    }

    public Location getLocation() {
        return this.sign.getLocation();
    }

    public Location getBarrelLocation() {
        return this.barrel.getLocation();
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setAmount(int amount) {
        if(amount < 1)
            amount = 1;
        this.amount = amount;
    }

    public void setSell(int amount) {
        if(amount < 0)
            amount = -1;
        this.sell = amount;
    }

    public void setBuy(int amount) {
        if(amount < 0)
            amount = -1;
        this.buy = amount;
    }

    public void buy(Player player) {
        if(this.buy == -1)
            return;
        UUID uuid = player.getUniqueId();
        if(MoneyManager.get(uuid) < this.buy) {
            player.sendMessage(ChatColor.RED + "Du hast nicht genug Geld!");
            return;
        }
        ItemStack item = take(barrel.getInventory());
        if(item == null) {
            player.sendMessage(ChatColor.RED + "Der Shop hat keine Items mehr!");
            return;
        }
        if(!give(player.getInventory(), item)) {
            player.sendMessage(ChatColor.RED + "Du hast nicht genug Platz in deinem Inventar!");
            barrel.getInventory().addItem(item);
            return;
        }
        MoneyManager.set(uuid, MoneyManager.get(uuid) - this.buy);
        MoneyManager.set(owner, MoneyManager.get(owner) + this.buy);
        player.sendMessage(ChatColor.GREEN + "Du hast das Item gekauft.");
    }

    public void sell(Player player) {
        if(this.sell == -1)
            return;
        UUID uuid = player.getUniqueId();
        if(MoneyManager.get(owner) < this.sell) {
            player.sendMessage(ChatColor.RED + "Der Shop Owner hat nicht genug Geld!");
            return;
        }
        ItemStack item = take(player.getInventory());
        if(item == null) {
            player.sendMessage(ChatColor.RED + "Du hast nicht genug Items!");
            return;
        }
        if(!give(barrel.getInventory(), item)) {
            player.sendMessage(ChatColor.RED + "Das Fass hat nicht genug Platz!");
            player.getInventory().addItem(item);
            return;
        }
        MoneyManager.set(uuid, MoneyManager.get(uuid) + this.buy);
        MoneyManager.set(owner, MoneyManager.get(owner) - this.buy);
        player.sendMessage(ChatColor.GREEN + "Du hast das Item verkauft.");
    }


    private boolean give(Inventory inventory, ItemStack item) {
        if(inventory.firstEmpty() != -1) {
            inventory.addItem(item);
            return true;
        }
        return false;
    }

    private ItemStack take(Inventory inventory) {
        for(ItemStack item : inventory) {
            if(item.getType().equals(this.type)) {
                if(item.getAmount() >= this.amount) {
                    ItemStack itemStack = item.clone();
                    itemStack.setAmount(this.amount);
                    item.setAmount(item.getAmount() - this.amount);
                    return itemStack;
                }
            }
        }
        return null;
    }

    public void save(YamlConfiguration config, int id) {
        config.set(id + ".sign", sign.getLocation());
        config.set(id + ".barrel", barrel.getLocation());
        config.set(id + ".owner", owner.toString());
        config.set(id + ".amount", amount);
        config.set(id + ".sell", sell);
        config.set(id + ".buy", buy);
        config.set(id + ".material", type.name().toLowerCase());
    }

    private void setSide(Sign sign, Side side) {
        sign.getSide(side).setLine(0, ChatColor.BOLD + Bukkit.getOfflinePlayer(this.owner).getName());
        sign.getSide(side).setLine(1, this.amount + "");
        if(this.sell == -1 && this.buy == -1)
            sign.getSide(side).setLine(2, ChatColor.DARK_RED + "CLOSED");
        else if(this.sell == -1)
            sign.getSide(side).setLine(2, ChatColor.GREEN + "B" + this.buy);
        else if(this.buy == -1)
            sign.getSide(side).setLine(2, ChatColor.RED + "S" + this.buy);
        else
            sign.getSide(side).setLine(2, ChatColor.GREEN + "B" + this.buy + " : " + ChatColor.RED + "S" + this.sell);
        sign.getSide(side).setLine(3, ChatColor.GRAY + this.type.name().toLowerCase());
    }

    // ------------------------------------------- STATIC -------------------------------------------------------------

    public static BarralShop load(YamlConfiguration config, int id) {
        if(!config.contains(id + ""))
            return null;

        Sign sign = (Sign) config.getLocation(id + ".sign").getBlock().getState();
        if(sign == null) {
            return null;
        }
        Barrel barrel = (Barrel) config.getLocation(id + ".barrel").getBlock().getState();
        if(barrel == null) {
            return null;
        }

        sign.setWaxed(true);
        UUID owner = UUID.fromString(config.getString(id + ".owner"));
        int amount = config.getInt(id + ".amount");
        Material type = Material.getMaterial(config.getString(id + ".material").toUpperCase());

        BarralShop barralShop = new BarralShop(sign, barrel, owner, amount, type);

        int buy = config.getInt(id + ".buy");
        int sell = config.getInt(id + ".sell");
        barralShop.setBuy(buy);
        barralShop.setSell(sell);

        barralShop.setSide(sign, Side.FRONT);
        barralShop.setSide(sign, Side.BACK);
        return barralShop;

    }
}
