package me.sapphire.spleeg.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ApiUnloadWorldCommand extends CommandManager {
	public ApiUnloadWorldCommand() {
		super("unloadworld", "descarregarmundo", "desligarmundo");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			if (Mine.existsWorld(sender, args[1])) {
				Mine.unloadWorld(args[1]);
				sender.sendMessage("§aVoce descarregou o mundo " + args[1]);
			}
		}

		return true;
	}

}
