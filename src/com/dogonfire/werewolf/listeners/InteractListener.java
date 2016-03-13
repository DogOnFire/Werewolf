package com.dogonfire.werewolf.listeners;

import com.dogonfire.werewolf.ClanManager;
import com.dogonfire.werewolf.ClanManager.ClanType;
import com.dogonfire.werewolf.LanguageManager;
import com.dogonfire.werewolf.LanguageManager.LANGUAGESTRING;
import com.dogonfire.werewolf.PermissionsManager;
import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.WerewolfManager;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class InteractListener implements Listener
{
	private Werewolf	plugin	= null;

	public InteractListener(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	private boolean checkForEatingFood(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		Material type = player.getItemInHand().getType();
		switch (type)
		{
			case APPLE:
			case BREAD:
			case PUMPKIN:
			case MELON:
			case MUSHROOM_SOUP:
			case BAKED_POTATO:
			case POTATO:
			case CAKE:
			case COOKIE:
			case CARROT:
				player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WerewolfTryEat, ChatColor.RED));
				event.setCancelled(true);
				return true;
		}
		
		return false;
	}

	private boolean checkForDrinkingWerewolfCurePotion(PlayerItemConsumeEvent event)
	{
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();
		
		if(!plugin.getItemManager().isCurePotion(handItem))
		{
			return false;
		}

		/*
		if (!handItem.hasItemMeta())
		{
			return false;
		}
		
		if (handItem.getItemMeta().getDisplayName().length() < 10)
		{
			return false;
		}
		
		if (!handItem.getItemMeta().getDisplayName().substring(2).contains("Werewolf cure"))
		{
			return false;
		}
*/		
		if (!Werewolf.getPermissionsManager().hasPermission(player, "werewolf.potion.cure.drink") && !player.isOp())
		{
			return false;
		}
		
		if (!Werewolf.getWerewolfManager().isWerewolf(player.getUniqueId()))
		{
			player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.DrinkCureFailure, ChatColor.LIGHT_PURPLE));
		}
		else if (!Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
		{
			if (Math.random() < this.plugin.cureChance)
			{
				Werewolf.getWerewolfManager().unmakeWerewolf(player.getUniqueId());
				player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.DrinkCureSuccess, ChatColor.LIGHT_PURPLE));
			}
			else
			{
				player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.DrinkCureFailure, ChatColor.LIGHT_PURPLE));
			}
		}
		else
		{
			return false;
		}
		
		if (player.getItemInHand().getAmount() == 1)
		{
			player.getInventory().remove(handItem);
		}
		else
		{
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		}
		return false;
	}

	private boolean checkForDrinkingWerewolfInfectionPotion(PlayerItemConsumeEvent event)
	{
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();

		if(!plugin.getItemManager().isInfectionPotion(handItem))
		{
			return false;
		}

/*		
		if (!handItem.hasItemMeta())
		{
			return false;
		}
		if (!(player instanceof Player))
		{
			this.plugin.logDebug("Non-player tried to drink a werewolf potion!");
			return false;
		}
		if (handItem.getItemMeta().getDisplayName().length() < 10)
		{
			return false;
		}
*/
		
		if (!handItem.getItemMeta().getDisplayName().substring(2).contains("Werewolf infection"))
		{
			return false;
		}
		
		if ((!Werewolf.getPermissionsManager().hasPermission(player, "werewolf.potion.infection.drink")) && (!player.isOp()))
		{
			return false;
		}
		
		if (Werewolf.getWerewolfManager().isWerewolf(player))
		{
			player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.DrinkInfectionFailure, ChatColor.LIGHT_PURPLE));
		}
		else if (this.plugin.isFullMoonInWorld(player.getWorld()))
		{
			player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.DrinkInfectionSuccess, ChatColor.LIGHT_PURPLE));
			plugin.log(player.getName() + " contracted the werewolf infection by drinking a potion!");
			
			final Player targetPlayer = player;

			this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
			{
				public void run()
				{
					Werewolf.getWerewolfManager().makeWerewolf(targetPlayer, true, ClanManager.ClanType.Potion);
				}
			}, 140L);
		}
		else
		{
			player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.DrinkInfectionFailure, ChatColor.LIGHT_PURPLE));
		}
		
		if (player.getItemInHand().getAmount() == 1)
		{
			player.getInventory().remove(handItem);
		}
		else
		{
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		}
		
		return true;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}
		Player player = event.getPlayer();
		if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
		{
			if (Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
			{
				checkForEatingFood(event);
			}
		}
	}

	@EventHandler
	public void onPlayerConsumeEvent(PlayerItemConsumeEvent event)
	{
		if (!Werewolf.pluginEnabled)
		{
			return;
		}
		if (Werewolf.getWerewolfManager().isWerewolf(event.getPlayer()))
		{
			if (!checkForDrinkingWerewolfCurePotion(event))
			{
				checkForDrinkingWerewolfInfectionPotion(event);
			}
		}
		else if (!checkForDrinkingWerewolfInfectionPotion(event))
		{
			checkForDrinkingWerewolfCurePotion(event);
		}
	}
}
