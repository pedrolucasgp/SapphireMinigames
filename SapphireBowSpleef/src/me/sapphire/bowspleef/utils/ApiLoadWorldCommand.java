package me.sapphire.bowspleef.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class ApiLoadWorldCommand extends CommandManager {
	public ApiLoadWorldCommand() {
		super("loadworld", "carregarmundo", "ligarmundo");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			Mine.loadWorld(args[1]);
			sender.sendMessage("§aVoce carregou o mundo " + args[1]);
		}
		return true;
	}}
