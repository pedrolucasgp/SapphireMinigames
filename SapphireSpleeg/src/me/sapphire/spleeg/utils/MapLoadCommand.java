
package me.sapphire.spleeg.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MapLoadCommand extends CommandManager {

	public MapLoadCommand() {
		super("load", "carregar");
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage("§c/map load <name>");
		} else {
			if (Mine.onlyPlayer(sender)) {
				Player p = (Player) sender;
				if (Mine.MAPS.containsKey(args[1].toLowerCase())) {
					Mine.MAPS_CACHE.put(p, Mine.MAPS.get(args[1].toLowerCase()));
					p.sendMessage("§bEduardAPI §6Mapa carregado com sucesso!");
				} else {
					p.sendMessage("§bEduardAPI §cMapa invalido: §f" + args[1]);
				}

			}
		}

		return true;
	}

}
