
package me.sapphire.spleeg.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MapCopyCommand extends CommandManager {

	public MapCopyCommand() {
		super("copy","copiar");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			Schematic schema = Mine.getSchematic(p);
			
			if (!schema.hasFirstLocation()) {
				p.sendMessage("§bEduardAPI §2Posi§§o 1 n§o foi setada!");
				return true;
			}
			if (!schema.hasSecondLocation()) {
				p.sendMessage("§bEduardAPI §2Posi§§o 2 n§o foi setada!");
				return true;
			}
			schema.copy(p.getLocation());
			p.sendMessage("§bEduardAPI §6Mapa copiado!");
		}
		return true;
	}

}
