package me.sapphire.spleeg.utils;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

@StorageAttributes(inline=true)
public class OfflinePlayerStorable implements Storable {
	
	@Override
	public Object newInstance() {
		return new FakePlayer();
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String id = (String) object;
			if (id.contains(";")) {
				String[] split = id.split(";");
				return new FakePlayer(split[0], UUID.fromString(split[1]));

			}
		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof OfflinePlayer) {
			OfflinePlayer p = (OfflinePlayer) object;
			return p.getName() + ";" + p.getUniqueId().toString();

		}
		return null;
	}

}
