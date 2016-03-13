package com.dogonfire.werewolf.tasks;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.LanguageManager;
import com.dogonfire.werewolf.Werewolf;

public class InfoTask implements Runnable
{
	private Werewolf plugin;
	private UUID playerId = null;
	private String name = null;
	private LanguageManager.LANGUAGESTRING message = null;
	private int amount = 0;
	private ChatColor color;

	public InfoTask(Werewolf instance, ChatColor color, UUID playerId, LanguageManager.LANGUAGESTRING m, int amount, String name)
	{
		this.plugin = instance;
		this.playerId = playerId;
		this.message = m;
		this.name = name;
		this.amount = amount;
		this.color = color;
	}

	public void run()
	{
		Player player = this.plugin.getServer().getPlayer(this.playerId);
		
		if (player == null)
		{
			return;
		}
		
		this.plugin.getLanguageManager().setPlayerName(this.name);
				
		this.plugin.getLanguageManager().setAmount("" + this.amount);

		String questionMessage = this.plugin.getLanguageManager().getLanguageString(this.message, this.color);

		player.sendMessage(

		this.color + questionMessage);
	}
}