package me.sapphire.bowspleef.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


@StorageAttributes(inline=true)
public class InventoryStorable implements Storable {

	public Object newInstance() {
		return  Bukkit.createInventory(null, 6*9);
	}


	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			String[] split = string.split("//");
			try {
				Integer lines = Mine.toInt(split[0]);
				ItemStack[] contents = Mine.fromBase64toItems(split[0]);
				Inventory inv = Bukkit.createInventory(null, lines*9);
				inv.setContents(contents);
				return inv;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		return newInstance();
	}

	public Object store(Object object) {
		if (object instanceof Inventory) {
			Inventory inventory = (Inventory) object;

			int lines = inventory.getSize() / 9;
			ItemStack[] content = inventory.getContents();
			return lines + "//" + Mine.fromItemsToBase64(content);

		}

		return "";
	}

}
