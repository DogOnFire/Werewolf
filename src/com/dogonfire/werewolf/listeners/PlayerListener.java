package com.dogonfire.werewolf.listeners;

import java.util.Collection;
import java.util.Random;

import com.dogonfire.werewolf.ClanManager;
import com.dogonfire.werewolf.DamageManager;
import com.dogonfire.werewolf.LanguageManager;
import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.tasks.CheckTransformationTask;
import com.dogonfire.werewolf.tasks.PotionEffectTask;
import com.dogonfire.werewolf.versioning.UpdateNotifier;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWolf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener
{
	private Werewolf	plugin	= null;
	private Random		random	= new Random();

	public PlayerListener(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}
		Werewolf.getWerewolfManager().unsetWerewolfSkin(event.getPlayer().getUniqueId(), true);

		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new CheckTransformationTask(plugin, event.getPlayer().getUniqueId()), 20L);
		
		if (this.plugin.useUpdateNotifications && (event.getPlayer().isOp() || Werewolf.getPermissionsManager().hasPermission(event.getPlayer(), "werewolf.updates")))
		{
			this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new UpdateNotifier(this.plugin, event.getPlayer()));
		}
	}

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event)
	{
		Werewolf.getWerewolfManager().unsetWerewolfSkin(event.getPlayer().getUniqueId(), true);
	}

	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}

		if (!event.isCancelled())
		{
			if (event.getAnimationType() != PlayerAnimationType.ARM_SWING)
			{
				return;
			}

			Player nearestWerewolf = Werewolf.getWerewolfManager().getNearestWerewolf(event.getPlayer().getUniqueId());
			if (nearestWerewolf != null && DamageManager.canPlayerHit(event.getPlayer(), nearestWerewolf.getLocation()))
			{
				double damage = DamageManager.getDamageFromItemInHand(event.getPlayer());

				plugin.logDebug("Doing " + damage + " damage to " + nearestWerewolf.getName());

				nearestWerewolf.damage(damage, event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();
		if (!Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
		{
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}
		if (!Werewolf.getWerewolfManager().hasWerewolfSkin(event.getPlayer().getUniqueId()))
		{
			return;
		}
		Player player = event.getPlayer();
		
		if (Math.random() < 0.05D)
		{
			int wolfsOwned = 0;
			for (Entity entity : player.getNearbyEntities(this.plugin.wolfdistance, this.plugin.wolfdistance, this.plugin.wolfdistance))
			{
				if ((entity instanceof CraftWolf))
				{
					CraftWolf wolf = (CraftWolf) entity;
					if ((!wolf.isTamed()) || (wolf.getOwner() == null))
					{
						player.sendMessage(ChatColor.LIGHT_PURPLE + "A wild wolf joins your pack!");

						wolf.setMaxHealth(wolf.getMaxHealth());
						wolf.setHealth(wolf.getMaxHealth());
						wolf.setOwner(player);
						wolf.setTamed(true);

						wolfsOwned++;

						Werewolf.getWerewolfManager().increaseNumberOfPackWolvesForPlayer(player.getUniqueId());
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (event.getEntity().getKiller() == null || event.getEntity().getKiller().getUniqueId() == null)
		{
			if (this.plugin.renameWerewolves)
			{
				if (Werewolf.getWerewolfManager().hasWerewolfSkin(event.getEntity().getUniqueId()))
				{
					switch (this.random.nextInt(3))
					{
						case 0:
							event.setDeathMessage("Werewolf died");
							break;
						case 1:
							event.setDeathMessage("Werewolf was killed");
							break;
						case 2:
							event.setDeathMessage("Werewolf died");
					}
				}
			}
			return;
		}

		String killerName = event.getEntity().getKiller().getName();
		String victimName = event.getEntity().getName();

		if (this.plugin.renameWerewolves)
		{
			if (Werewolf.getWerewolfManager().hasWerewolfSkin(event.getEntity().getKiller().getUniqueId()))
			{
				switch (this.random.nextInt(4))
				{
					case 0:
						event.setDeathMessage(victimName + " was slaughtered by a Werewolf");
						break;
					case 1:
						event.setDeathMessage(victimName + " was ripped apart by a Werewolf");
						break;
					case 2:
						event.setDeathMessage(victimName + " was torn to pieces by a Werewolf");
						break;
					case 3:
						event.setDeathMessage(victimName + " was eaten by a Werewolf");
				}
			}
			else if (Werewolf.getWerewolfManager().hasWerewolfSkin(event.getEntity().getUniqueId()))
			{
				if (killerName != null)
				{
					event.setDeathMessage("Werewolf was slain by " + killerName);
				}
				else
				{
					event.setDeathMessage("Werewolf died");
				}
			}
		}

		if (this.plugin.useTrophies)
		{
			if (Werewolf.getWerewolfManager().hasWerewolfSkin(event.getEntity().getUniqueId()))
			{
				Player killer = this.plugin.getServer().getPlayer(killerName);

				if (killer != null)
				{
					switch (killer.getInventory().getItemInMainHand().getType())
					{
						case DIAMOND_SWORD:
						case GOLD_SWORD:
						case IRON_SWORD:
						case STONE_SWORD:
						{
							org.bukkit.inventory.ItemStack trophy = Werewolf.getTrophyManager().getTrophyFromWerewolfPlayer(killer.getUniqueId(), event.getEntity().getUniqueId());
							event.getDrops().add(trophy);

							event.getEntity().getKiller().sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.YouCutTheHeadOfWerewolf, ChatColor.WHITE));
						} break;

						default:
						  break;
					}
				}
			}
		}

		if (this.plugin.useClans)
		{
			if (Werewolf.getWerewolfManager().isWerewolf(event.getEntity().getUniqueId()))
			{
				if (Werewolf.getWerewolfManager().isWerewolf(event.getEntity().getKiller().getUniqueId()))
				{
					ClanManager.ClanType killerClan = Werewolf.getWerewolfManager().getWerewolfClan(event.getEntity().getKiller().getUniqueId());
					ClanManager.ClanType victimClan = Werewolf.getWerewolfManager().getWerewolfClan(event.getEntity().getUniqueId());
					
					if (killerClan == victimClan)
					{
						if (Werewolf.getClanManager().isAlpha(victimClan, event.getEntity().getUniqueId()))
						{
							Werewolf.getClanManager().assignAlphaInClan(killerClan, event.getEntity().getKiller().getUniqueId());

							Werewolf.getLanguageManager().setPlayerName(killerName);
							Werewolf.getWerewolfManager().sendMessageToClan(killerClan, Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.NewClanAlpha, ChatColor.WHITE));
						}
					}
				}
			}
		}

		if (Werewolf.getWerewolfManager().hasWerewolfSkin(event.getEntity().getUniqueId()))
		{
			if (this.plugin.cureWerewolfWhenSlain)
			{
				Werewolf.getWerewolfManager().unmakeWerewolf(event.getEntity().getUniqueId());
			}
			else
			{
				Werewolf.getWerewolfManager().unsetWerewolfSkin(event.getEntity().getUniqueId(), false);

				Werewolf.getWerewolfManager().setInfectedThisNight(event.getEntity().getUniqueId(), event.getEntity().getName(), true);
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(EntityPickupItemEvent event)
	{
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.PLAYER) {
			Player player = (Player) entity;
			
			if (!Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
			{
				return;
			}
			
			Material material = event.getItem().getItemStack().getType();
			switch (material)
			{
				case GOLD_CHESTPLATE:
				case LEATHER_CHESTPLATE:
				case DIAMOND_CHESTPLATE:
				case CHAINMAIL_CHESTPLATE:
				case IRON_CHESTPLATE:
				case WOOD_SWORD:
				case GOLD_SWORD:
				case STONE_SWORD:
				case DIAMOND_SWORD:
				case IRON_SWORD:
				case CHAINMAIL_BOOTS:
				case GOLD_BOOTS:
				case LEATHER_BOOTS:
				case IRON_BOOTS:
				case DIAMOND_BOOTS:
				case GOLD_LEGGINGS:
				case CHAINMAIL_LEGGINGS:
				case LEATHER_LEGGINGS:
				case IRON_LEGGINGS:
				case DIAMOND_LEGGINGS:
				case CHAINMAIL_HELMET:
				case GOLD_HELMET:
				case LEATHER_HELMET:
				case IRON_HELMET:
				case DIAMOND_HELMET:
				case BOW:
					event.setCancelled(true);
					return;
				default:
			}
	
			if (this.plugin.keepWerewolfHandsFree)
			{
				if (player.getInventory().getItemInMainHand().getType() == Material.AIR)
				{				
					event.setCancelled(true);
					
					//final Inventory inventory = player.getInventory();
					//inventory.addItem(event.getItem().getItemStack());
				}				
			}	
		}				
	}	
	
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event)
	{
		if (!Werewolf.getItemManager().isWolfbanePotion(event.getPotion().getItem())) 
		{
			return;
		}
		
		Collection<LivingEntity> affectedEntity = event.getAffectedEntities();
		for (LivingEntity livingEntity : affectedEntity)
		{
			if (livingEntity instanceof Player)
			{
				Player player = (Player) livingEntity;

				if (Werewolf.getWerewolfManager().isWolfForm(player.getUniqueId()))
				{
					if (random.nextInt(100) < plugin.wolfbaneUntransformChance)
					{
						Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, player, new PotionEffect(PotionEffectType.POISON, 4 * 20, 2)), 16L);
						
						player.getLocation().getWorld().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 100);
						player.getLocation().getWorld().playEffect(player.getLocation().add(new Vector(0, 1, 0)), Effect.COLOURED_DUST, 100);
						player.getLocation().getWorld().playEffect(player.getLocation().add(new Vector(0, 2, 0)), Effect.COLOURED_DUST, 100);

						plugin.untransform(player);
					}
				}
			}
		}

		event.setCancelled(true);
	}
}
