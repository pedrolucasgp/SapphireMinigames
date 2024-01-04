package me.sapphire.bowspleef.utils;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PotionEffectStorable implements Storable {
	@Override
	public Object newInstance() {
		return new PotionEffect(PotionEffectType.ABSORPTION, 20, 0);
	}
}
