package me.sapphire.bowspleef.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sapphire.bowspleef.utils.BukkitConfig;

public class SetLocationCommand implements CommandExecutor {
	private BukkitConfig config = new BukkitConfig("config.yml");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			List<String> argumments = Arrays.asList("spawn");
			
	        List<String> unsetLocations = new ArrayList<>();
			
			for(String argumment : argumments) {
				if(config.getLocation(argumment+"Location") == null) {
					unsetLocations.add(argumment);
				}
			}
			
			 String unsetLocationString = unsetLocations.stream().collect(Collectors.joining(", "));

			
			p.sendMessage("§a§lUnset locations: §e"+unsetLocationString+"\n ");
			
			String argummentsListString = argumments.stream().collect(Collectors.joining("/"));
			
			if (args.length == 0) {
				p.sendMessage("§c/setlocation <" + argummentsListString + ">.");
				return false;
			}

			String locationString = args[0];

			try {
				if (argumments.contains(locationString) && !locationString.endsWith("c1")
						&& !locationString.endsWith("c2")) {
					config.set(locationString + "Location", p.getLocation());
					config.saveConfig();
					
					p.sendMessage("§a" + locationString + " has been set.");
					
				} else if(locationString.endsWith("c1") && locationString.endsWith("c2")) {
					p.sendMessage("§cThe location '" + locationString + "' was not found.");
				}

				if (locationString.endsWith("c1") || locationString.endsWith("c2")) {
					HashSet<Byte> transparentBlocks = new HashSet<>();
					transparentBlocks.add((byte) 0);

					Location targetLocation = p.getTargetBlock(transparentBlocks, 5).getLocation();

					Block targetBlock = targetLocation.getBlock();
					Location targetBlockLocation = targetBlock.getLocation();
					
					config.set(locationString + "Location", targetBlockLocation);
					config.saveConfig();
					
					p.sendMessage("§a" + locationString + " has been set.");
					
					return false;
				}
			} catch (Exception e) {
				p.sendMessage("§cAn error occurred. Please contact support at support@sapphiremc.cc");
				e.printStackTrace();
			}
		}
		return false;
	}

}
