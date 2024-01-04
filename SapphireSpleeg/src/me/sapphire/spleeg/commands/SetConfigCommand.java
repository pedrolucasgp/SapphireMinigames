package me.sapphire.spleeg.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sapphire.spleeg.utils.BukkitConfig;

public class SetConfigCommand implements CommandExecutor {
    private BukkitConfig config = new BukkitConfig("config.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> argumments = Arrays.asList("maxplayers", "doublejumpnumber", "launchnumber");

            List<String> unsetConfigs = new ArrayList<>();

            for (String argumment : argumments) {
                if (config.get(argumment) == null) {
                    unsetConfigs.add(argumment);
                }
            }

            String unsetConfigsString = unsetConfigs.stream().collect(Collectors.joining(", "));

            p.sendMessage("§a§lUnset configs: §e" + unsetConfigsString + "\n ");

            String argummentsListString = argumments.stream().collect(Collectors.joining("/"));

            if (args.length < 1) {
                p.sendMessage("§c/setconfig <" + argummentsListString + "> <value>.");
                return false;
            }

            String configString = args[0];
            String value = args[1];

            try {
                if (argumments.contains(configString)) {
                    config.set(configString, value);
                    config.saveConfig();

                    p.sendMessage("§a" + configString + " has been set.");
                    return false;
                }
                p.sendMessage("§cThe config '" + configString + "' was not found.");

            } catch (Exception e) {
                p.sendMessage("§cAn error occurred. Please contact support at support@sapphiremc.cc");
                e.printStackTrace();
            }
        }
        return false;
    }

}