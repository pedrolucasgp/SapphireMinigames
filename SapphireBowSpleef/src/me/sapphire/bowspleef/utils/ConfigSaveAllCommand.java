
package me.sapphire.bowspleef.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class ConfigSaveAllCommand extends CommandManager {

	public ConfigSaveAllCommand() {
		super("all","todas");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Config.reloadConfigs();
		Mine.chat(sender,
				"§aTodas configura§§es de todos plugins foram salvadas!");
		
		return true;
	}

}
