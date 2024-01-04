
package me.sapphire.spleeg.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class ApiLoadCommand extends CommandManager {

	public ApiLoadCommand() {
		super("load","carregar");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length ==0) {
			
		}
		
		return true;
	}

}
