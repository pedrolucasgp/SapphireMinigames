package me.sapphire.bowspleef.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;


public class BukkitStorables {

	public static void load() {
		StorageAPI.register(Item.class, new ItemStorable());
		StorageAPI.register(Chunk.class, new ChunkStorable());
		StorageAPI.register(World.class, new WorldStorable());
		StorageAPI.register(Vector.class, new VectorStorable());
		StorageAPI.register(ItemStack.class, new ItemStackStorable());
		StorageAPI.register(Enchantment.class, new EnchantmentStorable());
		StorageAPI.register(PotionEffect.class, new PotionEffectStorable());
		StorageAPI.register(Location.class, new LocationStorable());
		StorageAPI.register(OfflinePlayer.class, new OfflinePlayerStorable());
		StorageAPI.register(MaterialData.class, new MaterialDataStorable());
		StorageAPI.register(Inventory.class, new InventoryStorable());
	}

}
