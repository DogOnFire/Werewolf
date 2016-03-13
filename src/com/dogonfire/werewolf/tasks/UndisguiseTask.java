package com.dogonfire.werewolf.tasks;

import java.util.UUID;

import com.dogonfire.werewolf.PacketUtils;
import com.dogonfire.werewolf.PermissionsManager;
import com.dogonfire.werewolf.WerewolfSkinManager;
import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.WerewolfManager;

import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import net.gravitydevelopment.anticheat.api.AntiCheatAPI;
import net.gravitydevelopment.anticheat.check.CheckType;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.ScoreboardManager;

public class UndisguiseTask implements Runnable
{
	public Werewolf	plugin;
	public UUID	playerId;
	private boolean	makeVisible;
	private boolean	forever;
	private World	world;

	public UndisguiseTask(Werewolf instance, World world, UUID playerId, boolean visible, boolean forever)
	{
		this.plugin = instance;
		this.playerId = playerId;
		this.makeVisible = visible;
		this.forever = forever;
		this.world = world;
	}

	public void run()
	{
		Player player = this.plugin.getServer().getPlayer(this.playerId);
		ScoreboardManager manager;
		
		if (player != null)
		{
			if (this.plugin.noCheatPlusEnabled)
			{
				NCPExemptionManager.unexempt(player);
			}
			if (this.plugin.antiCheatEnabled)
			{
				AntiCheatAPI.unexemptPlayer(player, CheckType.FLY);
				AntiCheatAPI.unexemptPlayer(player, CheckType.SPEED);
			}
			
			Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.CONFUSION);
			Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.SPEED);
			Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.HUNGER);
			Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.NIGHT_VISION);
			Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.INCREASE_DAMAGE);
			Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.REGENERATION);
			
			player.setWalkSpeed(0.2F);
			if (!this.plugin.usePounce)
			{
				Werewolf.pu.removePotionEffectNoGraphic(player, PotionEffectType.JUMP);
			}
			else
			{
				player.setAllowFlight(false);
			}
			
			String playerListName = Werewolf.getWerewolfManager().getPlayerListName(player);
			if (playerListName == null)
			{
				this.plugin.logDebug("Could not find playerlist name for " + player.getName());
			}
			else
			{
				player.setPlayerListName(playerListName);
			}
			if (this.makeVisible)
			{
				Werewolf.getSkinManager().unsetWerewolfSkin(player);
			}
			if (this.plugin.healthBarEnabled)
			{
				manager = Bukkit.getScoreboardManager();
				player.setScoreboard(manager.getMainScoreboard());
			}
			
			if(plugin.useScoreboards)
			{
				Werewolf.getWerewolfScoreboardManager().removePlayerHuntingScoreboard(player);
			}
			
			if (!this.makeVisible)
			{
				if (this.world != null)
				{
					Werewolf.getSkinManager().removeSkinFromWorld(this.world, player);
				}
				else
				{
					for (World world : this.plugin.getServer().getWorlds())
					{
						Werewolf.getSkinManager().removeSkinFromWorld(world, player);
					}
				}
			}			
		}
				
		Werewolf.getWerewolfManager().popPlayerData(this.playerId);
		
		if (this.plugin.useWerewolfGroupName)
		{
			String groupName = Werewolf.getWerewolfManager().getOriginalPermissionGroup(playerId);
			if ((groupName != null) && (!groupName.equals(this.plugin.werewolfGroupName)))
			{
				this.plugin.logDebug("Putting " + player.getName() + " into group " + groupName);

				String playerName = plugin.getServer().getOfflinePlayer(playerId).getName();
				
				Werewolf.getPermissionsManager().setGroup(playerName, groupName);
			}
		}
		
		if (this.forever)
		{
			Werewolf.getWerewolfManager().setNotWerewolf(this.playerId);
		}
		else
		{
			Werewolf.getWerewolfManager().setHumanForm(this.playerId, player.getName());
		}
		
		Werewolf.getWerewolfManager().clearNumberOfPackWolvesForPlayer(this.playerId);
		
		if(player!=null)
		{
			Werewolf.getStatisticsManager().clearStatistics(player.getUniqueId());
		}
	}
}