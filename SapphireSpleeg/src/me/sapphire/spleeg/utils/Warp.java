package me.sapphire.spleeg.utils;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Warp implements Storable {

	private String name;
	private Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
	private int delayInSeconds = 1;
	private boolean canMoveOnDelay = true;
	private String message = "§6Voce foi teleportado para o Warp §e$warp";
	private boolean enableGui = true;
	private ItemStack guiIcon;

	public Warp(String name) {
		this.name = name;
		guiIcon = Mine.newItem(Material.ENDER_PEARL, "§bTeleporte para " + name);

	}

	public Warp() {
	}

	public void teleport(Player p) {
		Mine.TIME.asyncDelay(new Runnable() {

			@Override
			public void run() {
				p.teleport(spawn);
				if (message != null)
					p.sendMessage(message.replace("$warp", name));
				
			}
		}, delayInSeconds * 20L);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public int getDelayInSeconds() {
		return delayInSeconds;
	}

	public void setDelayInSeconds(int delayInSeconds) {
		this.delayInSeconds = delayInSeconds;
	}

	public boolean isCanMoveOnDelay() {
		return canMoveOnDelay;
	}

	public void setCanMoveOnDelay(boolean canMoveOnDelay) {
		this.canMoveOnDelay = canMoveOnDelay;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

	public boolean isEnableGui() {
		return enableGui;
	}

	public void setEnableGui(boolean enableGui) {
		this.enableGui = enableGui;
	}

	public ItemStack getGuiIcon() {
		return guiIcon;
	}

	public void setGuiIcon(ItemStack guiIcon) {
		this.guiIcon = guiIcon;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}

}
