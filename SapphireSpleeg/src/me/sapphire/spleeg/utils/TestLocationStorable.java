package me.sapphire.spleeg.utils;

import org.bukkit.Location;
import org.bukkit.World;



public class TestLocationStorable {

	public static void test() {
		World wordlVasio = Mine.newEmptyWorld("mundovasio");
		
		BukkitConfig c = new BukkitConfig("testloc.yml", EduardAPI.getInstance());
		c.set("loc", new Location(wordlVasio,0,5,0));
		c.saveConfig();
		
		c.reloadConfig();
		System.out.println(c.get("loc"));
		
	}
	public static void init() {
//		Mine.TIME.timer(20, ()->test());
	
	}
}
