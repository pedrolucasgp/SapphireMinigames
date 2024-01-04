
package me.sapphire.spleeg.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class ApiWorldsCommand extends CommandManager {

	public ApiWorldsCommand() {
		super("worlds","mundos");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		StringBuilder mundos = new StringBuilder();

		for (World world : Bukkit.getWorlds()) {
			if (mundos.length() > 0) {
				mundos.append("§a, §f");
			}
			mundos.append("" + world.getName());
		}
		sender.sendMessage("§aOs mundo existentes s§o: §f" + mundos.toString());

		return true;
	}

}
