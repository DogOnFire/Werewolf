package com.dogonfire.werewolf.listeners;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.WerewolfManager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener
{
	private Werewolf	plugin	= null;

	public InventoryListener(Werewolf p)
	{
		this.plugin = p;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getInventory().getType() != InventoryType.PLAYER)
		{
			return;
		}

		if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventory(InventoryCloseEvent event)
	{
		if (!Werewolf.getWerewolfManager().isWolfForm(event.getPlayer().getUniqueId()))
		{
			return;			
		}
				
		if (event.getView().getType() == InventoryType.CRAFTING)
		{
			preventArmor((Player) event.getPlayer());
		}
	}

	private void preventArmor(Player player)
	{
		PlayerInventory pinv = player.getInventory();
		ItemStack helmet = pinv.getHelmet();
		ItemStack chestplate = pinv.getChestplate();
		ItemStack leggings = pinv.getLeggings();
		ItemStack boots = pinv.getBoots();

		if (helmet != null)
		{
			if (pinv.firstEmpty() >= 0)
			{
				pinv.addItem(new ItemStack[] { helmet });
			}
			else
			{
				player.getWorld().dropItem(player.getLocation(), helmet);
			}

			pinv.setHelmet(new ItemStack(0));
		}

		if (chestplate != null)
		{
			if (pinv.firstEmpty() >= 0)
			{
				pinv.addItem(new ItemStack[] { chestplate });
			}
			else
			{
				player.getWorld().dropItem(player.getLocation(), chestplate);
			}
			pinv.setChestplate(new ItemStack(0));
		}

		if (leggings != null)
		{
			if (pinv.firstEmpty() >= 0)
			{
				pinv.addItem(new ItemStack[] { leggings });
			}
			else
			{
				player.getWorld().dropItem(player.getLocation(), leggings);
			}
			pinv.setLeggings(new ItemStack(0));
		}

		if (boots != null)
		{
			if (pinv.firstEmpty() >= 0)
			{
				pinv.addItem(new ItemStack[] { boots });
			}
			else
			{
				player.getWorld().dropItem(player.getLocation(), boots);
			}
			pinv.setBoots(new ItemStack(0));
		}
	}

	/*
	 * @EventHandler public void onPrepareItemCraft(PrepareItemCraftEvent event)
	 * { if (event.getView().getType() != InventoryType.WORKBENCH) {
	 * this.plugin.logDebug("Not workbench. Was " +
	 * event.getView().getType().name()); return; }
	 * 
	 * this.plugin.logDebug("PrepareItemCraftEvent");
	 * 
	 * CraftingInventory craftingInventory = event.getInventory(); if
	 * (!craftingInventory.contains(Material.IRON_SWORD)) {
	 * this.plugin.logDebug("Not sword"); return; }
	 * 
	 * if (!craftingInventory.contains(Material.QUARTZ)) {
	 * this.plugin.logDebug("Not quartz"); return; }
	 * 
	 * ItemStack silverSword = new ItemStack(Material.IRON_SWORD);
	 * 
	 * ItemMeta itemMeta = silverSword.getItemMeta();
	 * 
	 * List<String> list = new ArrayList(); list.add(ChatColor.DARK_GREEN +
	 * "Good for slaying Werewolves!"); itemMeta.setDisplayName(ChatColor.GOLD +
	 * "Werewolf Slayer"); itemMeta.setLore(list);
	 * 
	 * silverSword.setItemMeta(itemMeta);
	 * 
	 * craftingInventory.setResult(silverSword); }
	 */

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if ((!Werewolf.pluginEnabled) || (!this.plugin.dropArmorOnTransform))
		{
			return;
		}
		if (!(event.getPlayer() instanceof Player))
		{
			return;
		}
		Player player = (Player) event.getPlayer();
		if (!Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
		{
			return;
		}
		if (player.getInventory().getHelmet() != null)
		{
			switch (player.getInventory().getHelmet().getType())
			{
				case LEATHER_HELMET:
				case IRON_HELMET:
				case CHAINMAIL_HELMET:
				case GOLD_HELMET:
				case DIAMOND_HELMET:
					player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getHelmet());
					player.getInventory().setHelmet(null);
					break;
			}
		}
		if (player.getInventory().getChestplate() != null)
		{
			switch (player.getInventory().getChestplate().getType())
			{
				case LEATHER_CHESTPLATE:
				case IRON_CHESTPLATE:
				case CHAINMAIL_CHESTPLATE:
				case GOLD_CHESTPLATE:
				case DIAMOND_CHESTPLATE:
					player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getChestplate());
					player.getInventory().setChestplate(null);
					break;
			}
		}
		if (player.getInventory().getLeggings() != null)
		{
			switch (player.getInventory().getLeggings().getType())
			{
				case LEATHER_LEGGINGS:
				case IRON_LEGGINGS:
				case CHAINMAIL_LEGGINGS:
				case GOLD_LEGGINGS:
				case DIAMOND_LEGGINGS:
					player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getLeggings());
					player.getInventory().setLeggings(null);
					break;
			}
		}
		if (player.getInventory().getBoots() != null)
		{
			switch (player.getInventory().getBoots().getType())
			{
				case LEATHER_BOOTS:
				case IRON_BOOTS:
				case CHAINMAIL_BOOTS:
				case GOLD_BOOTS:
				case DIAMOND_BOOTS:
					player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getBoots());
					player.getInventory().setBoots(null);
					break;
			}
		}
	}
}
