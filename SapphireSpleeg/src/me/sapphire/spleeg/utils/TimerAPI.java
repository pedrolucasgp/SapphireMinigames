package me.sapphire.spleeg.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class TimerAPI {
	
	public static long getTempo(BukkitConfig config, String nome) {
		return config.getLong(nome);
	}
	
	public static void timer(int tempo, BukkitConfig config, JavaPlugin main, String nome) {
	if(!config.contains(nome)) {
		new BukkitRunnable() {
			long i = tempo;
			@Override
			public void run() {
				i--;
				config.set(nome, i);
				config.saveConfig();
				if(config.getLong(nome) <= 0) {
					cancel();
				}
			}
		}.runTaskTimer(JavaPlugin.getPlugin(main.getClass()), 20, 20);
	}else {
		new BukkitRunnable() {
			long i = config.getLong(nome);
			@Override
			public void run() {
				i--;
				config.set(nome, i);
				config.saveConfig();
				if(config.getLong(nome) <= 0) {
					cancel();
				}
			}
		}.runTaskTimer(JavaPlugin.getPlugin(main.getClass()), 20, 20);
	}
}

	public static int getTempoCorrendo(BukkitConfig config, String nome) {
		return config.getInt(nome);
	}
	
}
