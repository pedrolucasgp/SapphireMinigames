package me.sapphire.spleeg.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;


public class LaunchPadManager extends EventsManager {

	public static final Map<World, Boolean> WORLDS = new HashMap<>();
	public static final FallManager NO_FALL = new FallManager();

	private int blockHigh;
	private int blockId = 20;
	private int blockData = -1;
	public LaunchPadManager() {
	}
	




	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(PlayerMoveEvent e) {
		if (!Mine.equals2(e.getFrom(), e.getTo())) {
			Player p = e.getPlayer();
			Block block = e.getTo().getBlock().getRelative(0, blockHigh, 0);
			if (blockData != -1 && blockData != block.getData())
				return;
			if (block.getTypeId() != blockId)
				return;
			if (WORLDS.get(p.getWorld())==null){
				WORLDS.put(p.getWorld(), true);
			}
			if (!WORLDS.get(p.getWorld()))return;
			if (!NO_FALL.getPlayers().contains(p))
				NO_FALL.getPlayers().add(p);

		}
	}
	public int getBlockHigh() {
		return blockHigh;
	}
	public void setBlockHigh(int blockHigh) {
		this.blockHigh = blockHigh;
	}
	public int getBlockId() {
		return blockId;
	}
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	public int getBlockData() {
		return blockData;
	}
	public void setBlockData(int blockData) {
		this.blockData = blockData;
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
