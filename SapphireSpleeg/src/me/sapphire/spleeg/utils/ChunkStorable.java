package me.sapphire.spleeg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;


@StorageAttributes(inline=true)
public class ChunkStorable implements Storable {

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			String[] split = string.split(";");
			return Bukkit.getWorld(split[0]).getChunkAt(Extra.toInt(split[1]), Extra.toInt(split[2]));

		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof Chunk) {
			Chunk chunk = (Chunk) object;
			return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
		}

		return null;
	}


}
