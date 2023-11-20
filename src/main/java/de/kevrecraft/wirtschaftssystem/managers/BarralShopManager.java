package de.kevrecraft.wirtschaftssystem.managers;

import de.kevrecraft.wirtschaftssystem.Wirtschaftssystem;
import de.kevrecraft.wirtschaftssystem.utilitys.BarralShop;
import de.kevrecraft.wirtschaftssystem.utilitys.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BarralShopManager implements Listener {

    private static final YamlConfiguration config = new YamlConfiguration();
    private static File file;
    private static final ArrayList<BarralShop> shops = new ArrayList<>();


    private static Wirtschaftssystem plugin;

    public BarralShopManager(Wirtschaftssystem pl) {
        plugin = pl;
        loadShops();
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }

    private void loadShops() {
        file = new File(plugin.getDataFolder().getAbsolutePath(), "BarralShops.yml");
        if(!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        for (String key: config.getKeys(false)) {
            shops.add(BarralShop.load(config, Integer.parseInt(key)));
        }
        file.delete();
    }

    public void onDisable() {
        int i = 0;
        for (BarralShop shop : shops) {
            shop.save(config, i);
            i++;
        }

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            for(BarralShop shop : shops) {
                if(shop.getLocation().equals(event.getClickedBlock().getLocation())) {
                    shop.buy(event.getPlayer());
                    event.setCancelled(true);
                }
            }
        } else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            for(BarralShop shop : shops) {
                if(shop.getLocation().equals(event.getClickedBlock().getLocation())) {
                    shop.sell(event.getPlayer());
                    event.setCancelled(true);
                }
                if(event.getClickedBlock().getLocation().equals(shop.getBarrelLocation())) {
                    if(!event.getPlayer().getUniqueId().equals(shop.getOwner())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if(!Utility.isInt(event.getLine(1))) {
                    return;
                }
                int amount = Integer.parseInt(event.getLine(1));

                Material material = Material.getMaterial(event.getLine(3).toUpperCase());
                if(material == null) {
                    return;
                }

                Barrel barrel = getBarrel(event.getBlock().getLocation());
                if(barrel == null) {
                    return;
                }

                BarralShop barralShop = new BarralShop((Sign) event.getBlock().getState(), barrel, event.getPlayer().getUniqueId(), amount, material);


                if(event.getLine(2).replace(" ", "").contains(":")) {
                    String[] list = event.getLine(2).split(":");
                    if(list.length == 2) {
                        if(Utility.isInt(list[0]) && Utility.isInt(list[1])) {
                            barralShop.setBuy(Integer.parseInt(list[0]));
                            barralShop.setSell(Integer.parseInt(list[1]));
                        }
                    }
                } else if (Utility.isInt(event.getLine(2).replace(" ", ""))) {
                    barralShop.setBuy(Integer.parseInt(event.getLine(2)));
                }
                barralShop.update();
                shops.add(barralShop);
                event.setCancelled(true);
            }
        }, 1);
    }

    private Barrel getBarrel(Location location) {
        if(location.clone().add(0,0,1).getBlock().getType().equals(Material.BARREL)) {
            return (Barrel) location.clone().add(0,0,1).getBlock().getState();
        }
        else if(location.clone().add(0,0,-1).getBlock().getType().equals(Material.BARREL)) {
            return (Barrel) location.clone().add(0,0,-1).getBlock().getState();
        }
        else if(location.clone().add(0,1,0).getBlock().getType().equals(Material.BARREL)) {
            return (Barrel) location.clone().add(0,1,0).getBlock().getState();
        }
        else if(location.clone().add(0,-1,0).getBlock().getType().equals(Material.BARREL)) {
            return (Barrel) location.clone().add(0,-1,0).getBlock().getState();
        }
        else if(location.clone().add(1,0,0).getBlock().getType().equals(Material.BARREL)) {
            return (Barrel) location.clone().add(1,0,0).getBlock().getState();
        }
        else if(location.clone().add(-1,0,0).getBlock().getType().equals(Material.BARREL)) {
            return (Barrel) location.clone().add(-1,0,0).getBlock().getState();
        }
        return null;
    }

}
