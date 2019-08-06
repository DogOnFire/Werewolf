package com.dogonfire.werewolf.api;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;

import eu.blackfire62.myskin.bukkit.MySkin;
import net.md_5.bungee.api.ChatColor;


// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public interface IWerewolfDisguiseFactory
{	
	public abstract class WerewolfDisguise
	{			
		public WerewolfDisguise(String werewolfName, Player player, UUID disguiseAccountId)
		{}
		
		public void setCustomName(String customWerewolfName)
		{}
		
		public void setCustomNameVisible(boolean visible)
		{}
		
		public void setCapeEnabled(boolean capeEnabled)
		{}
			
		public void disguise()
		{}

		public void unDisguise()
		{}
	}	
	
	public WerewolfDisguise newDisguise(String werewolfName, Player player, UUID disguiseAccountId);		
}