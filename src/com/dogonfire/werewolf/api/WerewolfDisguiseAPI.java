package com.dogonfire.werewolf.api;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.api.IWerewolfDisguiseFactory.WerewolfDisguise;

import net.md_5.bungee.api.ChatColor;

// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public abstract class WerewolfDisguiseAPI
{
	static  IWerewolfDisguiseFactory werewolfDisguiser;
	static private HashMap<UUID, WerewolfDisguise> disguises = new HashMap<UUID, WerewolfDisguise>(); 
	static private HashMap<UUID, UUID> disguisedPlayers = new HashMap<UUID, UUID>(); 
	
	public static boolean init()
	{
		PluginManager pm = Werewolf.instance.getServer().getPluginManager();
		
		if (pm.getPlugin("LibsDisguises") != null)
		{
			Werewolf.instance().log("Lib's Disguises found, using it for werewolf disguise!");
			werewolfDisguiser = new LibsDisguisesFactory();
			return true;
		}
		else if (pm.getPlugin("MySkin") != null && pm.getPlugin("MySkin").isEnabled())
		{
			Werewolf.instance().log("MySkin found, using it for werewolf disguises!");
			werewolfDisguiser = new MySkinFactory();
			return true;
		}

		Werewolf.instance().log(ChatColor.RED + "No supported disguise plugin found... Werewolves are disabled!");		
		return false;
	}

	public static WerewolfDisguise getDisguise(UUID disguiseAccountId, String disguiseAccountName)
	{
		if(disguises.containsKey(disguiseAccountId))
		{
			return disguises.get(disguiseAccountId);
		}
		
		Werewolf.instance().logDebug("Creating new werewolf disguise - AccountId: " + disguiseAccountId + " AccountName: " + disguiseAccountName);
		
		WerewolfDisguise werewolfDisguise = werewolfDisguiser.newDisguise(disguiseAccountId, disguiseAccountName);
		disguises.put(disguiseAccountId, werewolfDisguise);
		return werewolfDisguise;
	}

	public static boolean disguise(Player player, WerewolfDisguise skin, String werewolfName)
	{				
		if(werewolfDisguiser==null)
		{
			Werewolf.instance().log("[ERROR] Tried disguising a player without a disguise plugin!");
			return false;
		}
		
		if(!skin.disguise(player, werewolfName))
		{
			Werewolf.instance().log("[ERROR] Could not disguise player " + player.getName() + "!");
			return false;
		}

		disguises.put(player.getUniqueId(), skin);
		
		Werewolf.instance().logDebug("Disguised player " + player.getName() + "!");

		disguisedPlayers.put(player.getUniqueId(), skin.getSkinAccountUUID());

		return true;		
	}

	public static boolean undisguise(Player player)
	{
		if(werewolfDisguiser==null)
		{
			Werewolf.instance().log("[ERROR] Tried undisguising a player without a disguise plugin!");
			return false;
		}
		
		if(!disguises.get(player.getUniqueId()).undisguise(player))
		{
			return false;
		}

		Werewolf.instance().logDebug("Undisguised player " + player.getName() + "!");

		disguisedPlayers.remove(player.getUniqueId());
		
		return true;
	}
}