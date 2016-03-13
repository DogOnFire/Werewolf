package com.dogonfire.werewolf;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;

import com.dogonfire.werewolf.ClanManager.ClanType;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class TrophyManager implements Listener
{
	private Werewolf			plugin				= null;
	private FileConfiguration	skullsConfig		= null;
	private File				skullsConfigFile	= null;

	TrophyManager(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	public void load()
	{
		if (this.skullsConfigFile == null)
		{
			this.skullsConfigFile = new File(this.plugin.getDataFolder(), "trophies.yml");
		}
		this.skullsConfig = YamlConfiguration.loadConfiguration(this.skullsConfigFile);

		this.plugin.log("Loaded " + this.skullsConfig.getKeys(false).size() + " Werewolf trophies.");

		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	public void save()
	{
		if ((this.skullsConfig == null) || (this.skullsConfig == null))
		{
			return;
		}
		try
		{
			this.skullsConfig.save(this.skullsConfigFile);
		}
		catch (Exception ex)
		{
			this.plugin.log("Could not save config to " + this.skullsConfig + ": " + ex.getMessage());
		}
	}

	public ItemStack getTrophyFromWerewolfPlayer(UUID killerId, UUID werewolfId)
	{
		ItemStack skullTrophy = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

		SkullMeta skullMeta = (SkullMeta) skullTrophy.getItemMeta();

		Date thisDate = new Date();
		String pattern = "dd MMMM yyyy";
		DateFormat formatter = new SimpleDateFormat(pattern);

		ClanType clan = Werewolf.getWerewolfManager().getWerewolfClan(werewolfId);
				
		skullMeta.setOwner(Werewolf.getClanManager().getWerewolfAccountForClan(clan));
		skullMeta.setDisplayName(ChatColor.GOLD + "Werewolf Head");

		String killerName = plugin.getServer().getPlayer(killerId).getName();
		
		Werewolf.getLanguageManager().setPlayerName(killerName);
		Werewolf.getLanguageManager().setAmount(formatter.format(thisDate));
		String lorePage = Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.TrophyDescription, ChatColor.GRAY);

		List<String> lorePages = new ArrayList<String>();
		lorePages.add(lorePage);
		skullMeta.setLore(lorePages);

		skullTrophy.setItemMeta(skullMeta);

		return skullTrophy;
		/*
		ItemMeta headMeta = skullTrophy.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes());
		
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		
		Field profileField = null;
		try 
		{
			profileField = headMeta.getClass().getDeclaredField("profile");
		} 
		catch (NoSuchFieldException | SecurityException e) 
		{
			e.printStackTrace();
		}
		
		profileField.setAccessible(true);
		
		try 
		{
			profileField.set(headMeta, profile);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		
		skullTrophy.setItemMeta(headMeta);
		*/
	}

	private int hashVector(Location location)
	{
		return location.getBlockX() * 73856093 ^ location.getBlockY() * 19349663 ^ location.getBlockZ() * 83492791;
	}

	public void handlePlaceSkull(ItemStack skull, Location location)
	{
		this.plugin.logDebug("Setting skull at " + location);
		this.skullsConfig.set(hashVector(location) + ".Description", skull.getItemMeta().getLore());

		save();
	}

	public boolean handleBreakSkull(ItemStack skullItem, Location location)
	{			
		String description = this.skullsConfig.getString(hashVector(location) + ".Description");
		if (description == null)
		{
			return false;
		}
		SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
		skullMeta.setDisplayName(ChatColor.GOLD + "Werewolf Head");

		List<String> lorePages = new ArrayList();
		lorePages.add(description);
		skullMeta.setLore(lorePages);

		this.skullsConfig.set(String.valueOf(hashVector(location)), null);

		save();

		skullItem.setItemMeta(skullMeta);

		return true;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (event.getBlock().getType() != Material.SKULL)
		{
			return;
		}
		ItemStack item = (ItemStack) event.getBlock().getDrops().toArray()[0];
		if (handleBreakSkull(item, event.getBlock().getLocation()))
		{
			event.getBlock().setType(Material.AIR);
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (event.getItemInHand().getType() != Material.SKULL_ITEM)
		{
			return;
		}
		handlePlaceSkull(event.getItemInHand(), event.getBlockPlaced().getLocation());
	}
}
