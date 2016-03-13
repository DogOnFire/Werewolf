package com.dogonfire.werewolf.tasks;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;


public class CheckTransformationTask implements Runnable
{
	private UUID		playerId;
	private Werewolf 	plugin;

	public CheckTransformationTask(Werewolf plugin, UUID playerId)
	{
		this.playerId = playerId;
		this.plugin = plugin;
	}

	public void run()
	{
		Player player = plugin.getServer().getPlayer(playerId);
		
		if(player==null)
		{
			return;
		}		
		
		if (Werewolf.getWerewolfManager().canTransform(player))
		{
			this.plugin.transform(player);
		}
		else if (Werewolf.getWerewolfManager().canUntransform(player))
		{
			this.plugin.untransform(player);
		}
	}
}