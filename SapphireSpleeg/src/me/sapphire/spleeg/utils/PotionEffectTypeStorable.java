package me.sapphire.spleeg.utils;

import org.bukkit.potion.PotionEffectType;

@StorageAttributes(inline=true)
public class PotionEffectTypeStorable implements Storable {


	@Override
	public Object restore(Object object) {
		if (object instanceof String) {

			String string = (String) object;
			String[] split = string.split(";");
			return PotionEffectType.getByName(split[1]);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object store(Object object) {
		if (object instanceof PotionEffectType) {
			PotionEffectType potionEffectType = (PotionEffectType) object;
			return potionEffectType.getName() + ";" + potionEffectType.getId();

		}
		return null;
	}

}
