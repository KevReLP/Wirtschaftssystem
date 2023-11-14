package de.kevrecraft.wirtschaftssystem.commands;

import de.kevrecraft.wirtschaftssystem.Wirtschaftssystem;
import de.kevrecraft.wirtschaftssystem.managers.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    final Wirtschaftssystem plugin;

    public MoneyCommand(Wirtschaftssystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Das darf nur ein Spieler tun!");
                    return;
                }

                if(args.length == 0) {
                    sendMoney(sender, ((Player) sender));
                    return;
                }
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("help")) {
                        sendHelp(sender);
                        return;
                    }
                }
                if(args.length == 3) {
                    if(args[0].equalsIgnoreCase("pay")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if(target != null) {
                            // TODO: check if args[2] is a NUMER!!!!!!!!!!
                            payMoney(sender, ((Player) sender), target, Integer.parseInt(args[2]));
                            return;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Der Spieler " + args[1] + " konnte nicht gefunden werden!");
                            return;
                        }
                    }
                }

                sender.sendMessage(ChatColor.RED + "Fehler! Benutze /money help für eine Hilfestellung!");
            }
        });
        return true;
    }

    private void sendMoney(CommandSender sender, Player player) {
        if(sender.getName().equalsIgnoreCase(player.getName()))
            sender.sendMessage(ChatColor.GREEN + "Du hast " + MoneyManager.get(player.getUniqueId()) + " Geld!");
        else
            sender.sendMessage(ChatColor.GREEN + "Der Spieler " + player.getName() + " hat " + MoneyManager.get(player.getUniqueId()) + " Geld!");
    }

    private void payMoney(CommandSender sender, Player from, Player target, int value) {
        if(sender.getName().equalsIgnoreCase(from.getName())) {
            sender.sendMessage(ChatColor.GREEN + "Du hast " + target.getName() + " " + value + " gegeben!");
            target.sendMessage(ChatColor.GREEN + "Der Spieler " + from.getName() + " hat dir " + value + " gegeben!");
            MoneyManager.set(from.getUniqueId(), MoneyManager.get(from.getUniqueId()) - value);
            MoneyManager.set(target.getUniqueId(), MoneyManager.get(target.getUniqueId()) + value);
        } else {
            sender.sendMessage(ChatColor.GREEN + "Der Spieler " + from.getName() + " hat " + target.getName() + " " + value + " gegeben!");
            from.sendMessage(ChatColor.GREEN + "Du hast " + target.getName() + " " + value + " gegeben!");
            target.sendMessage(ChatColor.GREEN + "Der Spieler " + from.getName() + " hat dir " + value + " gegeben!");
            MoneyManager.set(from.getUniqueId(), MoneyManager.get(from.getUniqueId()) - value);
            MoneyManager.set(target.getUniqueId(), MoneyManager.get(target.getUniqueId()) + value);
        }
    }

    private void sendHelp(CommandSender sender) {
        String color = ChatColor.BLUE.toString();
        String commandColor = ChatColor.GRAY.toString();
        String arrow = ChatColor.WHITE.toString() + "→";

        sender.sendMessage(color + ChatColor.BOLD + "------- Help: Money -------");
        sender.sendMessage(commandColor + "/money" +  arrow + color +" Zeigt dir dein Geld an.");
        sender.sendMessage(commandColor + "/money pay <player> <value>" +  arrow + color +" Überweise dem Spieler Geld.");
    }

}
