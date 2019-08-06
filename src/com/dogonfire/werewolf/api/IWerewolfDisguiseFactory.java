package com.dogonfire.werewolf.api;

import java.util.UUID;

import org.bukkit.entity.Player;


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