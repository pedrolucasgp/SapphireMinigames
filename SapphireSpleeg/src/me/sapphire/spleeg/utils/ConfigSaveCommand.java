
package me.sapphire.spleeg.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public class ConfigSaveCommand extends CommandManager {

	public ConfigSaveCommand() {
		super("save","salvar");
		register(new ConfigSaveAllCommand());
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String cmd = args[1];
			if (Mine.existsPlugin(sender, cmd)) {
				Plugin pl = Mine.getPlugin(cmd);
				if (args.length == 2) {
					Config.saveConfigs(pl);
					Mine.chat(sender,
							"§aSalvandos todas configura§§es do Plugin "
									+ pl.getName());
				} else {

				}
			}

		}

		
		return true;
	}

}
