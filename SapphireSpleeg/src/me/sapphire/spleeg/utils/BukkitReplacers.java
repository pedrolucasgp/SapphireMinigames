package me.sapphire.spleeg.utils;

import java.text.DecimalFormat;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import me.sapphire.spleeg.utils.Mine.Replacer;


public class BukkitReplacers {
	public static void registerRplacers() {
		if (Mine.hasPlugin("Vault")) {
			Mine.addReplacer("$player_group", new Replacer() {

				@Override
				public Object getText(Player p) {
					return VaultAPI.getPermission().getPrimaryGroup(p);
				}
			});
			Mine.addReplacer("$player_prefix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p));
				}
			});
			Mine.addReplacer("$player_suffix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p)));
				}
			});
			Mine.addReplacer("$group_prefix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(
							VaultAPI.getChat().getGroupPrefix("null", VaultAPI.getPermission().getPrimaryGroup(p)));
				}
			});
			Mine.addReplacer("$group_suffix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(
							VaultAPI.getChat().getGroupSuffix("null", VaultAPI.getPermission().getPrimaryGroup(p)));
				}
			});
			Mine.addReplacer("$player_money", new Replacer() {

				@Override
				public Object getText(Player p) {
					if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

						DecimalFormat decimal = new DecimalFormat("#,##0.00");
						return decimal.format(VaultAPI.getEconomy().getBalance(p));

					}
					return "0.00";
				}
			});
		}

		Mine.addReplacer("$players_online", new Replacer() {

			@Override
			public Object getText(Player p) {
				return Mine.getPlayers().size();
			}
		});
		Mine.addReplacer("$player_world", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getWorld().getName();
			}
		});
		Mine.addReplacer("$player_displayname", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getDisplayName();
			}
		});
		Mine.addReplacer("$player_name", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getName();
			}
		});
		Mine.addReplacer("$player_health", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getHealth();
			}
		});
		Mine.addReplacer("$player_maxhealth", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getMaxHealth();
			}
		});
		Mine.addReplacer("$player_level", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLevel();
			}
		});
		Mine.addReplacer("$player_xp", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getTotalExperience();
			}
		});
		Mine.addReplacer("$player_kills", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.PLAYER_KILLS);
			}
		});
		Mine.addReplacer("$player_deaths", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.DEATHS);
			}
		});
		Mine.addReplacer("$player_kdr", new Replacer() {

			@Override
			public Object getText(Player p) {
				int kill = p.getStatistic(Statistic.PLAYER_KILLS);
				int death = p.getStatistic(Statistic.DEATHS);
				if (kill == 0)
					return 0;
				if (death == 0)
					return 0;
				return kill / death;
			}
		});
		Mine.addReplacer("$player_kill/death", new Replacer() {

			@Override
			public Object getText(Player p) {
				int kill = p.getStatistic(Statistic.PLAYER_KILLS);
				int death = p.getStatistic(Statistic.DEATHS);
				if (kill == 0)
					return 0;
				if (death == 0)
					return 0;
				return kill / death;
			}
		});

		Mine.addReplacer("$player_x", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getX();
			}
		});
		Mine.addReplacer("$player_y", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getY();
			}
		});
		Mine.addReplacer("$player_z", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getZ();
			}
		});
		// if (Mine.hasPlugin("mcMMO")) {
		// Mine.addReplacer("$mcmmo_level", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// McMMOPlayer usuario = UserManager.getPlayer(p);
		// int nivel = usuario.getPowerLevel();
		// return nivel;
		// }
		// });
		//
		// }

	}
}
