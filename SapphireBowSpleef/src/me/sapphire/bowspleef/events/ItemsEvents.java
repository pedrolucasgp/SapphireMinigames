package me.sapphire.bowspleef.events;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

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

import com.mysql.cj.util.SaslPrep;

import me.sapphire.bowspleef.main.Main;
import me.sapphire.bowspleef.structures.GameStateManager;
import me.sapphire.bowspleef.utils.BukkitConfig;
import me.sapphire.bowspleef.utils.Mine;
import me.sapphire.bowspleef.structures.GameStateEnum;
import me.sapphire.bowspleef.structures.SimplesScore;

public class ItemsEvents implements Listener {
	BukkitConfig config = new BukkitConfig("config.yml");
	 private Integer totalDoubleJumpTime;
	 private Integer doubleJumpTime;
	 GameStateManager gameStateManager = new GameStateManager();
	 BukkitTask timer = null;
	 
	   @EventHandler
	    public void onPlayerInteract(PlayerInteractEvent e) {
	        Player p = e.getPlayer();
	        
	        if(p.getItemInHand().getType() == Material.BOW) {
	        	if(e.getAction().toString().contains("LEFT")) {
	        		shootTripleArrows(p);
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
	   
	   public void shootTripleArrows(Player p) {
		   Location eyeLocation = p.getEyeLocation();
		   Vector direction = eyeLocation.getDirection();
		   
		   Integer tripleShotCount = GameStateManager.tripleShotCount.get(p);
			
			if (tripleShotCount == null) {
				GameStateManager.tripleShotCount.put( p, GameStateManager.maxTripleShotValue);
				tripleShotCount = GameStateManager.tripleShotCount.get(p);
			}
			if (GameStateManager.tripleShotCount.get(p) > 0) {
	            p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.0f);
	            int tripleShotCountCopy = tripleShotCount -= 1;
	            GameStateManager.tripleShotCount.put(p, tripleShotCountCopy);
	            SimplesScore.atualizar(p);
	            Arrow mainArrow = shootArrow(p, eyeLocation, direction);
	 		   
	 		   shootArrow(p, eyeLocation, direction);
	 		   
	 		   Vector left = rotateVector(direction, Math.PI / 22);
	 	       Vector right = rotateVector(direction, -Math.PI / 22);
	 	       
	 	       Location leftLocation = eyeLocation.clone().add(left);
	 	       Location rightLocation = eyeLocation.clone().add(right);
	 	       
	 	       
	 	       Arrow arrowLeft = shootArrow(p, leftLocation , left);
	 	       Arrow arrowRight = shootArrow(p, rightLocation , right);
	 		   
	 	       for (Arrow arrow : new Arrow[] {mainArrow, arrowLeft, arrowRight}) {
	 	    	   arrow.setFireTicks(100);
	 	    	   arrow.setMetadata("FlamingArrow", new FixedMetadataValue(Main.getPlugin(Main.class), "FlamingArrow"));
	 	       }

			}
		   
		  
		   
	   }
	   
	   public Vector rotateVector(Vector vector, double angle) {
		   double cos = Math.cos(angle);
		   double sin = Math.sin(angle);
		   double x = vector.getX() * cos - vector.getZ() * sin;
		   double z = vector.getX() * sin + vector.getZ() * cos;
		   return new Vector(x, vector.getY(), z);
		   
	   }

	   public Arrow shootArrow(Player p, Location location, Vector direction) {
		   Arrow arrow = location.getWorld().spawn(location, Arrow.class);
		   arrow.setShooter(p);
		   arrow.setVelocity(direction.multiply(2));
		   return arrow;
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
