package me.sapphire.spleeg.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class MapHelpCommand extends CommandManager {
	public MapHelpCommand() {
		super("help");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("§a/map §2help");
		sender.sendMessage("§a/map §f<pos1|pos2>");
		sender.sendMessage("§a/map §3<copy|paste>");
		sender.sendMessage("§a/map §b<save|load> §3<name>");
		return true;
	}
}
