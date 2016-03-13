package com.dogonfire.werewolf;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;


import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HuntManager
{
	private Werewolf			plugin;
	private int					totalBounty			= 0;
	private FileConfiguration	huntersConfig		= null;
	private File				huntersConfigFile	= null;
	private Random				random				= new Random();

	HuntManager(Werewolf p)
	{
		this.plugin = p;
	}

	public void load()
	{
		if (this.huntersConfigFile == null)
		{
			this.huntersConfigFile = new File(this.plugin.getDataFolder(), "hunters.yml");
		}
		this.huntersConfig = YamlConfiguration.loadConfiguration(this.huntersConfigFile);

		this.plugin.log("Loaded " + this.huntersConfig.getKeys(false).size() + " werewolf hunters.");
	}

	public void save()
	{
		if ((this.huntersConfig == null) || (this.huntersConfigFile == null))
		{
			return;
		}
		try
		{
			this.huntersConfig.save(this.huntersConfigFile);
		}
		catch (Exception ex)
		{
			this.plugin.log("Could not save config to " + this.huntersConfigFile + ": " + ex.getMessage());
		}
	}

	public boolean isHunting(UUID playerId)
	{
		return CompassTracker.hasWatcher(playerId);
	}

	public void setHunting(UUID playerId, boolean hunting)
	{
		if (hunting)
		{
			CompassTracker.addWatcher(playerId);
		}
		else
		{
			CompassTracker.removeWatcher(playerId);
		}
	}

	public void addBounty(String playerName, int bounty)
	{
		Player player = this.plugin.getServer().getPlayer(playerName);
		if (!Werewolf.getEconomy().has(playerName, bounty))
		{
			player.sendMessage(ChatColor.RED + "You do not have that much.");
			return;
		}
		Werewolf.getEconomy().withdrawPlayer(playerName, bounty);
		this.totalBounty += bounty;

		Werewolf.getLanguageManager().setPlayerName(playerName);
		Werewolf.getLanguageManager().setAmount(Werewolf.getEconomy().format(bounty));
		this.plugin.getServer().broadcastMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BountyPlayerAdded, ChatColor.AQUA));

		Werewolf.getLanguageManager().setAmount(getFormattedBounty());
		this.plugin.getServer().broadcastMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BountyTotal, ChatColor.AQUA));

		this.plugin.log(playerName + " added " + Werewolf.getEconomy().format(bounty) + " to the Werewolf bounty. Bounty is now " + Werewolf.getEconomy().format(this.totalBounty));
	}

	public void autoAddBounty()
	{
		if (this.random.nextInt(100 + this.totalBounty / 5) > 0)
		{
			return;
		}
		
		if (this.totalBounty >= this.plugin.autoBountyMaximum)
		{
			return;
		}
		
		if (Werewolf.getWerewolfManager().getOnlineWerewolvesInWolfForm().size() == 0)
		{
			return;
		}
		
		int bounty = 10 + this.random.nextInt(10) * 10;

		this.totalBounty += bounty;
		if (this.totalBounty >= this.plugin.autoBountyMaximum)
		{
			this.totalBounty = this.plugin.autoBountyMaximum;
		}
		Werewolf.getLanguageManager().setAmount(Werewolf.getEconomy().format(bounty));
		this.plugin.getServer().broadcastMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BountyServerAdded, ChatColor.AQUA));

		Werewolf.getLanguageManager().setAmount(getFormattedBounty());
		this.plugin.getServer().broadcastMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.BountyTotal, ChatColor.AQUA));
	}

	public int getBounty()
	{
		return this.totalBounty;
	}

	public String getFormattedBounty()
	{
		return Werewolf.getEconomy().format(this.totalBounty);
	}

	public void handleKill(UUID killerId, String killerName)
	{
		if(Werewolf.getWerewolfManager().isWerewolf(killerId))
		{
			return;
		}
		
		if(!Werewolf.getHuntManager().isHunting(killerId))
		{
			return;
		}

		String message;
		String pattern = "HH:mm dd-MM-yyyy";
		DateFormat formatter = new SimpleDateFormat(pattern);
		Date thisDate = new Date();
		
		if (this.totalBounty > 0)
		{
			Werewolf.getLanguageManager().setPlayerName(killerName);
			Werewolf.getLanguageManager().setAmount(Werewolf.getEconomy().format(this.totalBounty));
			message = Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.KilledWerewolfBounty, ChatColor.AQUA);
		}
		else
		{
			message = Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.KilledWerewolfNoBounty, ChatColor.AQUA);
		}
		
		this.plugin.getServer().broadcastMessage(message);
		this.plugin.log(message);

		Werewolf.getEconomy().depositPlayer(killerName, this.totalBounty);
		this.totalBounty = 0;

		int kills = this.huntersConfig.getInt(killerName + ".Kills");
		this.huntersConfig.set(killerName + ".Kills", Integer.valueOf(kills + 1));
		this.huntersConfig.set(killerName + ".LastKill", formatter.format(thisDate));

		save();
	}

	public int getHunterKills(String hunterName)
	{
		return this.huntersConfig.getInt(hunterName + ".Kills");
	}

	public Set<String> getHunters()
	{
		Set<String> hunters = this.huntersConfig.getKeys(false);

		return hunters;
	}
}
