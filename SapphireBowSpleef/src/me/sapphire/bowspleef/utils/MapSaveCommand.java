
package me.sapphire.bowspleef.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MapSaveCommand extends CommandManager {

	public MapSaveCommand() {
		super("save");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage("§c/map save <name>");
		} else {

			if (Mine.onlyPlayer(sender)) {
				Player p = (Player) sender;
				if (!Mine.MAPS_CACHE.containsKey(p)) {
					p.sendMessage("§bEduardAPI §2Primeiro copie um Mapa:§a /e copy");
					return true;
				}
				Mine.MAPS.put(args[1].toLowerCase(), Mine.MAPS_CACHE.get(p));
				p.sendMessage("§bEduardAPI §6Mapa salvado com sucesso!");
			}
		}
		return true;
	}

}
