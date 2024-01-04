
package me.sapphire.bowspleef.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MapPos2Command extends CommandManager {

	public MapPos2Command() {
		super("pos2");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			Schematic schema = Mine.getSchematic(p);
			schema.setHigh(p.getLocation().toVector());
			p.sendMessage("§bEduardAPI §6Posi§§o 2 setada!");
		}
		return true;
	}

}
