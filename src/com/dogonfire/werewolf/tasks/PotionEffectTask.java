package com.dogonfire.werewolf.tasks;

import com.dogonfire.werewolf.Werewolf;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PotionEffectTask implements Runnable
{
	private Werewolf		plugin;
	private Player			player			= null;
	private PotionEffect	potionEffect	= null;

	public PotionEffectTask(Werewolf instance, Player p, PotionEffect pe)
	{
		this.plugin = instance;
		this.player = p;
		this.potionEffect = pe;
	}

	public void run()
	{
		if (this.player == null)
		{
			this.plugin.logDebug("PotionEffectTask::Run(): Player is null!");
			return;
		}
		Werewolf.pu.addPotionEffectNoGraphic(this.player, this.potionEffect);
	}
}