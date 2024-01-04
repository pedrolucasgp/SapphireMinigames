package me.sapphire.bowspleef.utils;

import java.util.Map;
import java.util.Map.Entry;


public class ReferenceMap extends ReferenceBase{
	private Map<Object,Integer> map;
	public ReferenceMap(Map<Object,Integer> map, Object instance) {
		super(null, instance);
		setMap(map);
	}

	@Override
	public void update() {
		@SuppressWarnings("unchecked")
		Map<Object,Object>newMap =  (Map<Object, Object>) getInstance();
		for (Entry<Object, Integer> entry : map.entrySet()) {
			newMap.put(entry.getKey(), StorageAPI.getObjectById(entry.getValue()));
		}
		
		
	}

	public Map<Object,Integer> getMap() {
		return map;
	}

	public void setMap(Map<Object,Integer> map) {
		this.map = map;
	}

}
