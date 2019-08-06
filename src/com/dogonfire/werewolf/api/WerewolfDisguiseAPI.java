package com.dogonfire.werewolf.api;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.api.IWerewolfDisguiseFactory.WerewolfDisguise;

import eu.blackfire62.myskin.bukkit.MySkin;
import net.md_5.bungee.api.ChatColor;


// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public abstract class WerewolfDisguiseAPI
{	
	
	
	//static private DisguiseCraft disguiseCraft; 
	//static private DisguiseAPI libsDisguises; 
	//static private DisguiseAPI iDisguise; 
	static private MySkin mySkin;
	static private IWerewolfDisguiseFactory werewolfDisguiser;
	static private HashMap<UUID, WerewolfDisguise> disguises = new HashMap<UUID, WerewolfDisguise>();
		
	public static void init()
	{
		//disguiseCraft = (DisguiseCraft) Bukkit.getPluginManager().getPlugin("DisguiseCraft");
		//libsDisguises = (DisguiseAPI) Bukkit.getPluginManager().getPlugin("LibsDisguises");		
		//iDisguise = (DisguiseAPI) Bukkit.getPluginManager().getPlugin("iDisguise");		
		MySkin mySkin = (MySkin) Bukkit.getPluginManager().getPlugin("MySkin");

		//if(libsDisguises!=null)
		//{
		//	Werewolf.instance().log("Using LibsDisguises plugin.");			
		//}
		//else
		//if(DisguiseCraft!=null)
		//{
		//	Werewolf.instance().log("Using DisguiseCraft plugin.");			
		//}
		//else
		//if(idisguiseAPI!=null)
		//{
		//	Werewolf.instance().log("Using iDisguise plugin.");			
		//} else
		if(mySkin!=null)
		{
			Werewolf.instance().log("Using MySkin plugin.");	
			
			werewolfDisguiser = new MySkinDisguise(mySkin);
		}
		else						
		{
			Werewolf.instance().log(ChatColor.DARK_RED + "No disguise plugin found. Werewolves disabled.");		
			Werewolf.instance().onDisable();
		}		
	}

	public static WerewolfDisguise newDisguise(String werewolfName, Player player, UUID disguiseAccountId)
	{
		WerewolfDisguise werewolfDisguise = werewolfDisguiser.newDisguise(werewolfName, player, disguiseAccountId);
		
		disguises.put(player.getUniqueId(), werewolfDisguise);

		return werewolfDisguise;
	}
		
	public static boolean isViewSelfToggled(Player player)
	{
		return false;
	}	
	
	public static void setViewDisguiseToggled(Player player, boolean value)
	{
	}
	
	public static void disguiseToAll(Player player, WerewolfDisguise skin)
	{
		skin.disguise();
	}

	public static void undisguiseToAll(Player player)
	{
		disguises.get(player.getUniqueId()).unDisguise();
	}
}