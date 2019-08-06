package com.dogonfire.werewolf.api;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;

import eu.blackfire62.myskin.bukkit.MySkin;
import eu.blackfire62.myskin.shared.SkinProperty;
import net.md_5.bungee.api.ChatColor;


// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public class MySkinDisguise implements IWerewolfDisguiseFactory
{	
	private MySkin mySkin;
	
	public class MySkinWerewolfDisguise extends WerewolfDisguise
	{		
		private Player player;
		private UUID playerSkinId;
		private UUID disguiseSkinId;
		private SkinProperty playerSkinProperty;
		private SkinProperty disguiseSkinProperty;
		private String werewolfName;
		
		public MySkinWerewolfDisguise(String werewolfName, Player player, UUID disguiseAccountId)
		{
			super(werewolfName, player, disguiseAccountId);
			
			this.player = player;
			this.werewolfName = werewolfName;
			playerSkinId = mySkin.getCache().loadSkinOfPlayer(player.getUniqueId());
			disguiseSkinId = mySkin.getCache().loadSkinOfPlayer(disguiseAccountId);
	
			playerSkinProperty = mySkin.getCache().loadSkinProperty(playerSkinId);
			disguiseSkinProperty = mySkin.getCache().loadSkinProperty(disguiseSkinId);
		}		
		
		public void setCustomName(String customWerewolfName)
		{
			player.setCustomName(customWerewolfName);			
		}
		
		public void setCustomNameVisible(boolean visible)
		{
			//skin.getWatcher().setCustomNameVisible(visible);
			player.setCustomNameVisible(visible);
		}
		
		public void setCapeEnabled(boolean capeEnabled)
		{
			//skin.getWatcher().setCapeEnabled(capeEnabled);						
		}
			
		public void disguise()
		{
			mySkin.getHandler().setSkinProperty(player, disguiseSkinProperty);
			mySkin.getHandler().update(player);
		}

		public void unDisguise()
		{
			mySkin.getHandler().setSkinProperty(player, playerSkinProperty);	
			mySkin.getHandler().update(player);
		}
	}	
		
	public MySkinDisguise(MySkin mySkin)
	{
		this.mySkin = mySkin;
		
		mySkin.getCache().loadSkinOfPlayer(UUID.fromString(Werewolf.instance().potionAccountUUID));
		mySkin.getCache().loadSkinOfPlayer(UUID.fromString(Werewolf.instance().werewolfBiteAccountUUID));
		mySkin.getCache().loadSkinOfPlayer(UUID.fromString(Werewolf.instance().wildBiteAccountUUID));
	}
		
	public MySkinWerewolfDisguise newDisguise(String werewolfName, Player player, UUID disguiseAccountId)
	{
		return new MySkinWerewolfDisguise(werewolfName, player, disguiseAccountId);
	}
	
}