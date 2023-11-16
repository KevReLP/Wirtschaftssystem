package de.kevrecraft.wirtschaftssystem.utilitys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class BarralShop {

    /*
    OWNER
    AMOUNT
    B:S
    ITEMTYPE
     */

    private final Sign sign;
    private final UUID owner;
    private int amount;
    private int sell;
    private int buy;
    private final Material type;

    public BarralShop(Sign sign, UUID owner, int amount, Material type) {
        this.sign = sign;
        this.owner = owner;
        this.amount = amount;
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSell(int amount) {
        this.sell = amount;
    }

    public void setBuy(int amount) {
        this.sell = amount;
    }

    public void save(YamlConfiguration config, int id) {
        config.set(id + ".sign", sign.getLocation());
        config.set(id + ".owner", owner.toString());
        config.set(id + ".amount", amount);
        config.set(id + ".sell", sell);
        config.set(id + ".buy", buy);
        config.set(id + ".material", type.name().toLowerCase());
    }

    public static BarralShop load(YamlConfiguration config, int id) {
        if(!config.contains(id + ""))
            return null;

        Sign sign = (Sign) config.getLocation(id + ".sign").getBlock().getState();
        if(sign == null) {
            return null;
        }
        sign.setWaxed(true);
        UUID owner = UUID.fromString(config.getString(id + ".owner"));
        int amount = config.getInt(id + ".amount");
        Material type = Material.getMaterial(config.getString(id + ".material").toUpperCase());

        BarralShop barralShop = new BarralShop(sign, owner, amount, type);

        int buy = config.getInt(id + ".buy");
        int sell = config.getInt(id + ".sell");
        barralShop.setBuy(buy);
        barralShop.setSell(sell);

        setSide(sign, Side.FRONT, barralShop);
        setSide(sign, Side.BACK, barralShop);
        return barralShop;

    }

    private static void setSide(Sign sign, Side side, BarralShop barralShop) {
        sign.getSide(side).setLine(0, ChatColor.BOLD + Bukkit.getPlayer(barralShop.owner).getName());
        sign.getSide(side).setLine(1, barralShop.amount + "");
        if(barralShop.sell == 0) {
            sign.getSide(side).setLine(2, ChatColor.AQUA + "B" + barralShop.buy);
        } else {
            sign.getSide(side).setLine(2, ChatColor.AQUA + "B" + barralShop.buy + " : S" + barralShop.sell);
        }
        sign.getSide(side).setLine(3, ChatColor.GRAY + barralShop.type.name().toLowerCase());
    }

}
