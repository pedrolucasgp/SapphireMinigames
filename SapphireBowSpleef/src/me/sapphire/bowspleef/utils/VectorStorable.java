package me.sapphire.bowspleef.utils;

import java.util.Map;

import org.bukkit.util.Vector;


public class VectorStorable implements Storable {

	@Override
	public Object restore(Map<String, Object> map) {
		return new Vector();
	}

	@Override
	public void store(Map<String, Object> map, Object object) {

	}

}
