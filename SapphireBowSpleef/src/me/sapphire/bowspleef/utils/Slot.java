package me.sapphire.bowspleef.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Slot implements Storable {
	private int positionX, positionY;
	private ItemStack item;
	private EffectManager effects;

	public Slot(ItemStack item, int index) {
		setItem(item);
		setIndex(index);
	}

	public int getIndex() {
		return Extra.getIndex(positionY, positionX);
	}

	public Slot() {
		// TODO Auto-generated constructor stub
	}
	public boolean equals(ItemStack item) {
		return this.item.equals(item);
	}

	public Slot(ItemStack item, int positionX, int positionY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setItem(item);
		// TODO Auto-generated constructor stub
	}

	public void setIndex(int index) {
		setPositionX(Extra.getColumn(index));
		setPositionY(Extra.getLine(index));
	}

	public void setPosition(int collumn, int line) {
		setPositionX(collumn);
		setPositionY(line);
	}

	public ItemStack getItem() {

		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void give(Inventory inventory) {
		inventory.setItem(getIndex(), item);
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public EffectManager getEffects() {
		return effects;
	}

	public void setEffects(EffectManager effects) {
		this.effects = effects;
	}

}