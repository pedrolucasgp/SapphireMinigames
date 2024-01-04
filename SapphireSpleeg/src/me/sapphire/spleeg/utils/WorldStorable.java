package me.sapphire.spleeg.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;


@StorageAttributes(inline = true)
public class WorldStorable implements Storable {

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String world = (String) object;
			return Bukkit.getWorld(world);

		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof World) {
			World world = (World) object;
			return world.getName();

		}
		return null;
	}

}
