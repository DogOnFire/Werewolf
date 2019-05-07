package com.dogonfire.werewolf.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CentralMessageTask implements Runnable
{
	private String		messageText	= null;
	private Sound		sound;
	private World		world;

	public CentralMessageTask(World world, String messageText, Sound sound)
	{
		this.messageText = messageText;
		this.sound = sound;
		this.world = world;
	}

	public void run()
	{
		for (Player player : world.getPlayers())
		{
			String translatedMessage = ChatColor.translateAlternateColorCodes('&', messageText);
			player.sendMessage(translatedMessage);
			
			world.playSound(player.getLocation(), sound, 10.0F, 1.0F);
		}
	}
}
