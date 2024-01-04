package me.sapphire.bowspleef.utils;

import org.bukkit.plugin.java.JavaPlugin;


public class EduardLIB extends JavaPlugin{

	@Override
	public void onEnable() {
		
	
		BukkitBungeeAPI.requestCurrentServer();
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.game");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.menu");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.manager");
		StorageAPI.registerClasses(Mine.class);
		BukkitStorables.load();
	}
}
