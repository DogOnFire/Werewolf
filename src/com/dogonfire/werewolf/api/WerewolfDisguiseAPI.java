package com.dogonfire.werewolf.api;

import org.bukkit.entity.Player;

// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public class WerewolfDisguiseAPI
{	
	public class WerewolfDisguise
	{		
		public WerewolfDisguise(String werewolfName, String account)
		{
			
		}		
		
		public void setCustomName(String customWerewolfName)
		{
			//skin.getWatcher().setCustomName(customWerewolfName);			
		}
		
		public void setCustomNameVisible(boolean visible)
		{
			//skin.getWatcher().setCustomNameVisible(visible);			
		}
		
		public void setCapeEnabled(boolean capeEnabled)
		{
			//skin.getWatcher().setCapeEnabled(capeEnabled);						
		}
	}	
	
	public static WerewolfDisguise newDisguise(String werewolfName, String account)
	{
		// TODO implement factory here		
		return null;		
	}
		
	public static boolean isViewSelfToggled(Player player)
	{
		// TODO implement support for all kind of plugins here		
		return false;
	}
	
	public static boolean setViewDisguiseToggled(Player player, boolean value)
	{
		// TODO implement support for all kind of plugins here		
		return false;
	}
	
	public static void disguiseToAll(Player player, WerewolfDisguise skin)
	{
		// TODO implement support for all kind of plugins here		
	}

	public static void undisguiseToAll(Player player)
	{
		// TODO implement support for all kind of plugins here				
	}
}