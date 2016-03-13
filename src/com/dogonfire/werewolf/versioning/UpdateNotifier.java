package com.dogonfire.werewolf.versioning;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;

public class UpdateNotifier implements Runnable
{
	final Werewolf	plugin;
	Player			player;

	public UpdateNotifier(Werewolf plugin, Player player)
	{
		this.plugin = plugin;
		this.player = player;
	}

	public void run()
	{
		if (!this.player.isOnline())
		{
			return;
		}

		UpdateChecker updateChecker = new UpdateChecker();
		int thisVersionNumber;
		int bukkitVersionNumber;

		String latestVersionName = updateChecker.getLatestVersionName();
		if (latestVersionName == null)
		{
			this.plugin.log("Could not get latest version name!");
			return;
		}
		try
		{
			thisVersionNumber = Integer.parseInt(this.plugin.getDescription().getVersion().replace(".", ""));
		}
		catch (NumberFormatException e)
		{
			this.plugin.log("Could not parse this plugin version number (from " + this.plugin.getDescription().getVersion() + ")");
			return;
		}

		try
		{
			plugin.logDebug("Newest werewolf plugin version available on bukkit is '" + latestVersionName.substring(9, 14) + "'");
			bukkitVersionNumber = Integer.parseInt(latestVersionName.substring(9, 14).replace(".", ""));
		}
		catch (NumberFormatException e)
		{
			this.plugin.log("Could not parse latest version number (from " + latestVersionName + ")");
			return;
		}
		try
		{
			if (thisVersionNumber < bukkitVersionNumber)
			{
				this.player.sendMessage(ChatColor.AQUA + "There is a new update for Werewolf available: " + ChatColor.GOLD + latestVersionName + ChatColor.AQUA + " for " + ChatColor.GOLD + updateChecker.getLatestVersionGameVersion());
				this.player.sendMessage(ChatColor.AQUA + "Download it at " + ChatColor.GOLD + updateChecker.getLatestVersionLink());
			}
		}
		catch (NumberFormatException e)
		{
			this.plugin.log("Could not compare version numbers!");
		}

	}
}