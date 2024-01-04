package me.sapphire.spleeg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

/**
 * Craft simples
 * <br><br>
 * V Antigas:
 * <br>
 * net.eduard.craft.CraftSetup1 1.0
 * <br>
 * net.eduard_api.game.craft.Crafts 2.0
 * <br>
 * net.eduard.eduard_api.game.craft.simples.Craft 3.0 
 * <br>
 * net.eduard.eduard_api.game.craft.CraftSimples 4.0
 * <br>
 * net.eduard.api.gui.SimpleCraft 5.0
 * <br>
 * net.eduard.api.setup.Mine$SimpleCraft 6.0
 * @version 7.0
 * @since EduardAPI 1.0
 * @author Eduard
 *
 */
public class SimpleRecipe implements Storable {
	public boolean addRecipe() {
		if (getResult() == null)
			return false;
		return Bukkit.addRecipe(getRecipe());
	}

	private ItemStack result = null;
	private List<ItemStack> items = new ArrayList<>();

	public SimpleRecipe() {
		// TODO Auto-generated constructor stub
	}

	public SimpleRecipe(ItemStack result) {
		setResult(result);
	}

	public SimpleRecipe add(Material material) {
		return add(new ItemStack(material));
	}

	public SimpleRecipe add(Material material, int data) {
		return add(new ItemStack(material, 1, (short) data));
	}

	public SimpleRecipe add(ItemStack item) {
		items.add(item);
		return this;
	}

	public SimpleRecipe remove(ItemStack item) {
		items.remove(item);
		return this;
	}

	public ItemStack getResult() {

		return result;
	}

	public ShapelessRecipe getRecipe() {
		if (result == null)
			return null;
		ShapelessRecipe recipe = new ShapelessRecipe(result);
		for (ItemStack item : items) {
			recipe.addIngredient(item.getData());
		}
		return recipe;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

}
