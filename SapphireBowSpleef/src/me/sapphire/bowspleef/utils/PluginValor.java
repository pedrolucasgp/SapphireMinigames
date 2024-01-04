package me.sapphire.bowspleef.utils;

import org.bukkit.plugin.messaging.PluginMessageListener;


public class PluginValor {

	public static void register() {
		Extra.setPrice(Menu.class, 4);
		Extra.setPrice(Shop.class, 1);
		Extra.setPrice(CommandManager.class, 0.25);
		Extra.setPrice(EventsManager.class, 1);
		Extra.setPrice(DBManager.class, 15);
		Extra.setPrice(PluginMessageListener.class, 10);
		Extra.setPrice(DisplayBoard.class, 5);
		
	}

}
