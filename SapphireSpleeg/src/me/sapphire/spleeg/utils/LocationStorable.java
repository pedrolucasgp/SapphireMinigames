package me.sapphire.spleeg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationStorable implements Storable {


	@Override
	public Object newInstance() {
		return new Location(Bukkit.getWorlds().get(0), 1, 1, 1);
	}
	

}
