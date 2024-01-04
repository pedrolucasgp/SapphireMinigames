package me.sapphire.bowspleef.utils;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;


@StorageAttributes(inline=true)
public class MaterialDataStorable implements Storable {

	@Override
	public Object newInstance() {

		return null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public Object store(Object object) {
		if (object instanceof MaterialData) {
			MaterialData materialData = (MaterialData) object;
			return materialData.getItemTypeId() + ":" + materialData.getData();

		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			StorageAPI.debug("Foda "+string );
			try {
				String[] split = string.split(":");
				return new MaterialData(Material.getMaterial(Mine.toInt(split[0])), Mine.toByte(split[1]));
			} catch (Exception e) {
				return new MaterialData(Material.STONE);
			}

		}
		return null;
	}

}
