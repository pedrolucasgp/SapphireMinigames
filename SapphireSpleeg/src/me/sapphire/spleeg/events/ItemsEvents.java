package me.sapphire.spleeg.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Egg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.sapphire.spleeg.main.Main;
import me.sapphire.spleeg.structures.GameStateEnum;
import me.sapphire.spleeg.structures.GameStateManager;
import me.sapphire.spleeg.structures.SimplesScore;
import me.sapphire.spleeg.utils.BukkitConfig;
import me.sapphire.spleeg.utils.Mine;

public class ItemsEvents implements Listener {
	
	BukkitConfig config = new BukkitConfig("config.yml");
	 private Integer totalDoubleJumpTime;
	 private Integer doubleJumpTime;
	 GameStateManager gameStateManager = new GameStateManager();
	 BukkitTask timer = null;
	 
	   @EventHandler
	    public void onPlayerInteract(PlayerInteractEvent e) {
	        Player p = e.getPlayer();
	        ItemStack item = e.getItem();

	        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	            if (e.getMaterial() == Material.IRON_SPADE) {
	                p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 0.5f, 0.5f);
	                Egg egg = p.launchProjectile(Egg.class);
	                egg.setMetadata("playerName", new FixedMetadataValue(Main.getPlugin(Main.class), p.getName()));
	            }
	        }
	        
	        if (p.getItemInHand().getItemMeta() != null) {
	            if (p.getItemInHand().equals(GameStateManager.doubleJumpActive)) {
	                doubleJump(p);
	            }
	        }
	        
	    }
	   
	   @EventHandler
	    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
	        Player p = e.getPlayer();

	        if (!gameStateManager.isPlayerSpectator(p)) {
	            e.setCancelled(true);
	        }

	        if (GameStateManager.currentState == GameStateEnum.GAME) {
	            if (e.isFlying()) {
	                if (timer == null) {
	                    if (p.getLocation().subtract(0, 2, 0).getBlock().getType() != Material.AIR) {
	                        doubleJump(p);
	                    }

	                }
	                e.setCancelled(true);
	            }
	        }

	    }

	    @EventHandler
	    public void onProjectileHit(ProjectileHitEvent e) {
	        if (e.getEntity() instanceof Egg) {
	            Egg egg = (Egg) e.getEntity();

	            if (egg.hasMetadata("playerName")) {
	                String playerName = egg.getMetadata("playerName").get(0).asString();
	                Player p = Bukkit.getPlayer(playerName);

	                Block hitBlock = e.getEntity().getLocation().getBlock();
	                Location hitBlockLocation = hitBlock.getLocation();
	                Location hitBlockRealLocation = new Location(hitBlockLocation.getWorld(), hitBlockLocation.getX(),
	                        hitBlockLocation.getY()-1, hitBlockLocation.getZ(), hitBlockLocation.getYaw(), hitBlockLocation.getPitch());

	                Block realBlock = hitBlockRealLocation.getBlock();


	                if(realBlock.getType() == Material.TNT){
	                    Location location = realBlock.getLocation();
	                    World world = location.getWorld();
	                    
	                    float power = 4.0F;
	                    boolean setFire = false;

	                    world.createExplosion(location.getX(),location.getY(),location.getZ(), power, setFire, false);
	                    realBlock.setType(Material.AIR);
	                    breakBlocksAround(location, 1);
	                    removePrimedTNT(realBlock);
	                }

	                realBlock.setType(Material.AIR);

	            }

	        }
	    }
	    
	    public void removePrimedTNT(Block block){
	        for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1)){
	            if(entity.getType() == EntityType.PRIMED_TNT){
	                entity.remove();
	                break;
	            }
	        }
	    }
	    
	    public void breakBlocksAround(Location location, int radius) {
	        World world = location.getWorld();
	        int blockX = location.getBlockX();
	        int blockY = location.getBlockY();
	        int blockZ = location.getBlockZ();

	        for (int x = blockX - radius; x <= blockX + radius; x++) {
	            for (int y = blockY - radius; y <= blockY + radius; y++) {
	                for (int z = blockZ - radius; z <= blockZ + radius; z++) {
	                    Location blockLocation = new Location(world, x, y, z);
	                    if (location.distance(blockLocation) <= radius) {
	                        if (blockLocation.getBlock().getType() != Material.AIR) {
	                            blockLocation.getBlock().setType(Material.AIR);
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    public void doubleJump(Player p) {

	        Integer doubleJumpCount = GameStateManager.doubleJumpCount.get(p);

	        if (doubleJumpCount == null) {
	            GameStateManager.doubleJumpCount.put(p, GameStateManager.maxDoubleJumpValue);
	            doubleJumpCount = GameStateManager.doubleJumpCount.get(p);
	        }

	        if (GameStateManager.doubleJumpCount.get(p) > 0) {
	            p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
	            int doubleJumpCountCopy = doubleJumpCount -= 1;
	            GameStateManager.doubleJumpCount.put(p, doubleJumpCountCopy);

	            p.getInventory().setItem(Mine.getPosition(1, 9), GameStateManager.doubleJumpInactive);

	            SimplesScore.atualizar(p);

	            new BukkitRunnable() {
	                @Override
	                public void run() {
	                   Vector currentVelocity = p.getVelocity();

	                    p.setVelocity(new Vector(currentVelocity.getX(), 0.7, currentVelocity.getZ()));
	                }
	            }.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 1);

	            totalDoubleJumpTime = 4;
	            doubleJumpTime = totalDoubleJumpTime;

	            p.setAllowFlight(false);

	            if (timer == null) {
	                timer = new BukkitRunnable() {

	                    @Override
	                    public void run() {
	                        doubleJumpTime--;

	                        if (GameStateManager.doubleJumpCount.get(p) > 0) {
	                            p.setLevel(doubleJumpTime);

	                            float percentage = (float) doubleJumpTime / totalDoubleJumpTime;
	                            float step = percentage;

	                            p.setExp(step);
	                        }

	                        if (doubleJumpTime <= 0) {

	                            if (GameStateManager.doubleJumpCount.get(p) > 0) {
	                                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
	                                p.getInventory().setItem(Mine.getPosition(1, 9), GameStateManager.doubleJumpActive);
	                                p.setAllowFlight(true);
	                            }

	                            timer = null;
	                            cancel();
	                        }
	                    }
	                }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
	            }
	        }
	    }

	}
	    
