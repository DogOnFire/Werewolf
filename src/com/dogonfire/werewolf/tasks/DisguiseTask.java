package com.dogonfire.werewolf.tasks;

import com.dogonfire.werewolf.ClanManager;
import com.dogonfire.werewolf.LanguageManager;
import com.dogonfire.werewolf.Werewolf;

import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import net.gravitydevelopment.anticheat.api.AntiCheatAPI;
import net.gravitydevelopment.anticheat.check.CheckType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.ScoreboardManager;

public class DisguiseTask implements Runnable
{
	private Werewolf	plugin;
	private Player		player	= null;

	public DisguiseTask(Werewolf instance, Player p)
	{
		this.plugin = instance;
		this.player = p;
	}

	private void dropArmor()
	{
		PlayerInventory inventory = this.player.getInventory();
		for (ItemStack stack : inventory.getArmorContents())
		{
			if (stack != null && !stack.getType().equals(Material.AIR) && stack.getAmount() != 0)
			{
				if (Werewolf.getWerewolfManager().getNumberOfTransformations(this.player.getUniqueId()) < this.plugin.transformsForNoDropItems)
				{
					this.player.getWorld().dropItemNaturally(this.player.getLocation(), stack);
					inventory.remove(stack);
				}
				else
				{
					int slot = this.player.getInventory().firstEmpty();
					if (slot > -1)
					{
						this.player.getInventory().setItem(slot, stack);
					}
					else
					{
						this.player.getWorld().dropItemNaturally(this.player.getLocation(), stack);
						inventory.remove(stack);
					}
				}
			}
		}
		
		this.player.getInventory().setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
	}

	private void dropHandItem()
	{
		PlayerInventory inventory = this.player.getInventory();

		ItemStack stack = inventory.getItemInHand();
		if ((stack == null) || (stack.getAmount() == 0) || (stack.getType().equals(Material.AIR)))
		{
			return;
		}
		if (Werewolf.getWerewolfManager().getNumberOfTransformations(this.player.getUniqueId()) < this.plugin.transformsForNoDropItems)
		{
			this.player.getWorld().dropItemNaturally(this.player.getLocation(), stack);
			inventory.remove(stack);
		}
		else
		{
			int slot = this.player.getInventory().firstEmpty();
			if (slot > -1)
			{
				this.player.getInventory().setItem(slot, stack);
				this.player.setItemInHand(null);
			}
			else
			{
				this.player.getWorld().dropItemNaturally(this.player.getLocation(), stack);
				inventory.remove(stack);
			}
		}
	}

	public void run()
	{
		if (this.player == null)
		{
			this.plugin.logDebug("DisguiseTask::Run(): Player is null!");
			return;
		}
		
		if (Werewolf.getWerewolfManager().hasWerewolfSkin(this.player.getUniqueId()))
		{
			return;
		}
		
		if (this.plugin.noCheatPlusEnabled)
		{
			NCPExemptionManager.exemptPermanently(this.player);
		}
		
		if (this.plugin.antiCheatEnabled)
		{
			AntiCheatAPI.exemptPlayer(this.player, CheckType.FLY);
			AntiCheatAPI.exemptPlayer(this.player, CheckType.SPEED);
		}
		
		ClanManager.ClanType clan = Werewolf.getWerewolfManager().getWerewolfClan(this.player.getUniqueId());

		Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.CONFUSION, 100, 1)), 1L);
		Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.HUNGER, 32000, 2)), 8L);
		Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.NIGHT_VISION, 32000, 1)), 16L);
		
		switch (clan)
		{
			case Potion:
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.JUMP, 32000, 3)), 16L);
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.SPEED, 32000, 3)), 32L);

				this.player.setWalkSpeed(1.0F);
				break;
			case WerewolfBite:
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.JUMP, 32000, 2)), 16L);
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.SPEED, 32000, 1)), 32L);
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.REGENERATION, 32000, 2)), 64L);

				this.player.setWalkSpeed(0.5F);
				break;
			case WildBite:
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.JUMP, 32000, 2)), 16L);
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.SPEED, 32000, 1)), 32L);
				Werewolf.server.getScheduler().scheduleSyncDelayedTask(this.plugin, new PotionEffectTask(this.plugin, this.player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 32000, 2)), 64L);

				this.player.setWalkSpeed(0.5F);
		}
		
		dropArmor();
		dropHandItem();

		Werewolf.getSkinManager().setWerewolfSkin(this.player);
		Werewolf.getWerewolfManager().pushPlayerData(this.player);
		Werewolf.getStatisticsManager().clearStatistics(this.player.getUniqueId());
		
		if(plugin.useScoreboards)
		{
			Werewolf.getWerewolfScoreboardManager().newPlayerHuntingScoreboard(this.player);
		}
		
		if (this.plugin.isFullMoonInWorld(this.player.getWorld()) && !Werewolf.getWerewolfManager().hasRecentTransform(this.player.getUniqueId()))
		{
			Werewolf.getWerewolfManager().incrementNumberOfFullMoonTransformations(this.player.getUniqueId());
		}
		
		Werewolf.getWerewolfManager().setLastTransformation(this.player.getUniqueId());
		
		//if (this.plugin.healthBarEnabled)
		//{
		//	ScoreboardManager localScoreboardManager = Bukkit.getScoreboardManager();
		//}
		
		if (this.plugin.useWerewolfGroupName)
		{
			String originalGroup = Werewolf.getPermissionsManager().getGroup(this.player.getName());
			Werewolf.getWerewolfManager().setOriginalPermissionGroup(this.player.getUniqueId(), originalGroup);
			Werewolf.getPermissionsManager().setGroup(this.player.getName(), this.plugin.werewolfGroupName);
		}
		
		int n = 1;
		Boolean renamed = false;
		while (!renamed)
		{
			renamed = true;
			try
			{
				player.setPlayerListName(ChatColor.GOLD + "Werewolf" + n);
			} 
			catch (Exception ex)
			{
				n++;

				renamed = false;
			}
		}	
		

		Werewolf.getLanguageManager().setAmount("" + Werewolf.getWerewolfManager().getNumberOfTransformations(this.player.getUniqueId()));

		this.player.sendMessage(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.Transform, ChatColor.LIGHT_PURPLE));
		
		if (Werewolf.getPermissionsManager().hasPermission(this.player, "werewolf.howl"))
		{
			Werewolf.getLanguageManager().setType("/howl");
			String message = Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfoCommandHowl, ChatColor.AQUA);
			this.player.sendMessage(message);
		}
		
		if (Werewolf.getPermissionsManager().hasPermission(this.player, "werewolf.growl"))
		{
			Werewolf.getLanguageManager().setType("/growl");
			String message = Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfoCommandGrowl, ChatColor.AQUA);
			this.player.sendMessage( message);
		}
		
		this.plugin.log(this.player.getName() + " turned into a werewolf!");
	}
}
