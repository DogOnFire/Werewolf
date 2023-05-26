package com.dogonfire.werewolf.listeners;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;

import com.dogonfire.werewolf.ClanManager;
import com.dogonfire.werewolf.DamageManager;
import com.dogonfire.werewolf.LanguageManager;
import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.disguises.IWerewolfDisguiseFactory.WerewolfDisguise;

public class DamageListener implements Listener
{
	private Werewolf plugin;

	public DamageListener(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamage1(EntityDamageEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}

		if (!event.isCancelled() && (event.getEntity() instanceof Player))
		{
			Player player = (Player) event.getEntity();

			// Before trying to use Disguises, check if Disguises is
			// enabled...
			if (Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()) && plugin.disguisesEnabled)
			{
				WerewolfDisguise skin = Werewolf.getSkinManager().getSkin(player);

				if (skin != null)
				{
					Werewolf.getWerewolfManager().setPouncing(player.getUniqueId());
				}
				else
				{
					this.plugin.logDebug("onEntityDamage: Skin is null for " + player.getName());
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage2(EntityDamageEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}

		if (event.getEntity() instanceof Player)
		{
			Player werewolfPlayer = (Player) event.getEntity();

			if (Werewolf.getWerewolfManager().hasWerewolfSkin(werewolfPlayer.getUniqueId()))
			{
				double damage = event.getDamage();
				switch (event.getCause())
				{
					case FALL:
						damage = 0;
						break;
					case CONTACT:
					case LAVA:
                    case FIRE:
					case LIGHTNING:
					case BLOCK_EXPLOSION:
					case ENTITY_ATTACK:
                    case ENTITY_SWEEP_ATTACK:
                    case PROJECTILE:
                    case THORNS:
                    case FREEZE:
						damage *= DamageManager.SilverArmorMultiplier;
						break;

					default:
						break;
				}

				if (Werewolf.getClanManager().isAlpha(werewolfPlayer.getUniqueId()))
				{
					damage /= 2.0D;
				}

				event.setDamage(damage);

				if (damage == 0.0D)
				{
					event.setCancelled(true);
					return;
				}

				try
				{
					WerewolfDisguise skin = Werewolf.getSkinManager().getSkin(werewolfPlayer);

					if (skin != null)
					{
						werewolfPlayer.playEffect(EntityEffect.HURT);

						Werewolf.getWerewolfManager().growl(werewolfPlayer);
					}
				}
				catch (NullPointerException e)
				{
					// well, looks like there is no Lib's Disguises there
					werewolfPlayer.playEffect(EntityEffect.HURT);

					Werewolf.getWerewolfManager().growl(werewolfPlayer);
				}
			}
		}

		if (event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			if (damageEvent.getDamager() instanceof Player)
			{
				Player player = (Player) damageEvent.getDamager();

				this.plugin.logDebug(player.getName() + " is doing damage to " + damageEvent.getEntityType().name());

				if (Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
				{
					double damage = 0.0D;

					if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
					{
						damage = DamageManager.werewolfHandDamage;
					}
					else
					{
						damage = DamageManager.werewolfItemDamage;
					}

					if (Werewolf.getClanManager().isAlpha(player.getUniqueId()))
					{
						damage *= 2.0D;
					}

					event.setDamage(damage);

					if (damageEvent.getEntity() instanceof Player)
					{
						Player victim = (Player) damageEvent.getEntity();

						if (!this.plugin.isVampire(victim))
						{
							if (Werewolf.getPermissionsManager().hasPermission(player, "werewolf.infectother") && Werewolf.getPermissionsManager().hasPermission(victim, "werewolf.becomeinfected") && !Werewolf.getWerewolfManager().isWerewolf(victim)) {
								this.plugin.logDebug(player.getName() + " is doing risk damage to " + victim.getName());

								if (Math.random() < this.plugin.werewolfInfectionRisk) {
									ClanManager.ClanType clanType = Werewolf.getWerewolfManager().getWerewolfClan(player.getUniqueId());

									Werewolf.getWerewolfManager().makeWerewolf(victim, false, clanType);

									Werewolf.getLanguageManager().setPlayerName(player.getName());
									victim.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BiteVictim, ChatColor.LIGHT_PURPLE));

									Werewolf.getLanguageManager().setPlayerName(victim.getName());
									player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BiteAttacker, ChatColor.LIGHT_PURPLE));

									victim.getLocation().getWorld().playEffect(player.getLocation(), Effect.SMOKE, 100);
									victim.getLocation().getWorld().playEffect(player.getLocation().add(new Vector(0, 1, 0)), Effect.SMOKE, 100);
									victim.getLocation().getWorld().playEffect(player.getLocation().add(new Vector(0, 2, 0)), Effect.SMOKE, 100);

									this.plugin.log(player.getName() + " infected " + victim.getName() + " with the werewolf infection!");
								}
							}
						}
					}
				}
			}

			if (event.getEntity() != null && (event.getEntity() instanceof Player))
			{
				Player player = (Player) event.getEntity();
				if (!Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()) && (Werewolf.getPermissionsManager().hasPermission(player, "werewolf.becomeinfected") || player.isOp())
						&& (!Werewolf.getWerewolfManager().isWerewolf(player) && (damageEvent.getDamager() instanceof Wolf) && Math.random() < this.plugin.wildWolfInfectionRisk))
				{
					Tameable tameable = (Tameable) damageEvent.getDamager();

					if (!tameable.isTamed())
					{
						Random random = new Random();
						Werewolf.getWerewolfManager().makeWerewolf(player, false, ClanManager.ClanType.values()[random.nextInt(ClanManager.ClanType.values().length)]);

						player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BiteVictim, ChatColor.LIGHT_PURPLE));

						plugin.log(player.getName() + " contracted the werewolf infection from a wild wolf!");
					}
				}
			}
		}

		if (event.getDamage() <= 0.0D)
		{
			event.setDamage(1.0D);
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (event.getEntity().getKiller() == null)
		{
			return;
		}
		if (!(event.getEntity().getKiller() instanceof Player))
		{
			return;
		}

		Player killer = event.getEntity().getKiller();

		if (Werewolf.getWerewolfManager().hasWerewolfSkin(killer.getUniqueId()))
		{
			int health = 0;
			switch (event.getEntity().getType())
			{
                case WARDEN:
                case ENDER_DRAGON:
                case WITHER:
                    health = 5;
                    break;
				case PLAYER:
				case VILLAGER:
				case WANDERING_TRADER:
				case PILLAGER:
				case VINDICATOR:
				case EVOKER:
				case ILLUSIONER:
				case RAVAGER:
				case HORSE:
				case DONKEY:
				case MULE:
				case COW:
				case MUSHROOM_COW:
				case ZOGLIN:
				case PIGLIN_BRUTE:
				case POLAR_BEAR:
                case ELDER_GUARDIAN:
					health = 4;
					break;
				case ENDERMAN:
				case ZOMBIE:
				case ZOMBIE_VILLAGER:
				case LLAMA:
				case TRADER_LLAMA:
				case PANDA:
				case DROWNED:
				case HUSK:
				case DOLPHIN:
				case PIGLIN:
				case STRIDER:
				case ZOMBIFIED_PIGLIN:
				case WITCH:
                case GUARDIAN:
                case CAMEL:
                case SNIFFER:
					health = 3;
					break;
				case SHEEP:
				case GOAT:
				case WOLF:
				case CAT:
				case OCELOT:
				case RABBIT:
				case PIG:
				case TURTLE:
				case FOX:
				case SPIDER:
                case SQUID:
                case GLOW_SQUID:
                case ALLAY:
                case PHANTOM:
					health = 2;
					break;
				case SKELETON:
				case WITHER_SKELETON:
                case BLAZE:
				case STRAY:
				case CHICKEN:
				case PARROT:
				case VEX:
				case AXOLOTL:
				case BEE:
				case SILVERFISH:
				case COD:
				case SALMON:
				case PUFFERFISH:
				case TROPICAL_FISH:
                case FROG:
                case TADPOLE:
                case ENDERMITE:
                case BAT:
                case MAGMA_CUBE:
                case SNOWMAN:
					health = 1;
					break;
				default:
					break;
			}

			if (health > 0 && killer.getHealth() < killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue())
			{
				Werewolf.getLanguageManager().setAmount("" + health);
				killer.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.KilledMob, ChatColor.LIGHT_PURPLE));

				if (killer.getHealth() + health > killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue())
				{
					killer.setHealth(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
				}
				else
				{
					killer.setHealth(killer.getHealth() + health);
				}
			}

			if (this.plugin.useClans)
			{
				ClanManager.ClanType clan = Werewolf.getWerewolfManager().getWerewolfClan(killer.getUniqueId());
				Werewolf.getClanManager().handleMobKill(killer, clan, event.getEntity().getType());
			}

			if (event.getEntity() instanceof Player)
			{
				 if(plugin.isVampire((Player)event.getEntity()))
				 {
					 Werewolf.getWerewolfScoreboardManager().setVampireKillsForPlayer(killer,
							 Werewolf.getStatisticsManager().increaseVampireKills(killer.getUniqueId()));
				 }
				 else
				 {
					 Werewolf.getWerewolfScoreboardManager().setHumanKillsForPlayer(killer,
							 Werewolf.getStatisticsManager().increaseHumanKills(killer.getUniqueId()));
				 }
			}
			else
			{
				Werewolf.getWerewolfScoreboardManager().setMobKillsForPlayer(killer, Werewolf.getStatisticsManager().increaseMobKills(killer.getUniqueId()));
			}
		}

		if (!(event.getEntity() instanceof Player))
		{
			return;
		}

		Player werewolf = (Player) event.getEntity();

		if (!Werewolf.getWerewolfManager().hasWerewolfSkin(werewolf.getUniqueId()))
		{
			return;
		}

		if (this.plugin.vaultEnabled && !Werewolf.getWerewolfManager().hasWerewolfSkin(killer.getUniqueId()))
		{
			Werewolf.getHuntManager().handleKill(killer.getUniqueId(), killer.getName());
		}
	}
}