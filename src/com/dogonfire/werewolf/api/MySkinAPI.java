package com.dogonfire.werewolf.api;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.api.MojangAPI.MojangSkin;
import com.dogonfire.werewolf.api.WerewolfDisguiseAPI.WerewolfDisguise;

import eu.blackfire62.myskin.bukkit.MySkin;
import eu.blackfire62.myskin.shared.SkinProperty;


// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public class MySkinAPI
{	
	private MySkin 		mySkin;
	private MojangAPI 	mojangAPI;
	private Werewolf 	plugin;
	
	public MySkinAPI(Werewolf instance)
	{
		this.plugin = instance;
		this.mojangAPI = new MojangAPI(plugin);
		this.mySkin = (MySkin) Bukkit.getPluginManager().getPlugin("MySkin");
		
		/*
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().potionAccountUUID), UUID.fromString(Werewolf.instance().potionAccountUUID));
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().werewolfBiteAccountUUID), UUID.fromString(Werewolf.instance().werewolfBiteAccountUUID));
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().wildBiteAccountUUID), UUID.fromString(Werewolf.instance().wildBiteAccountUUID));
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().alphaAccountUUID), UUID.fromString(Werewolf.instance().alphaAccountUUID));
		*/
	}
	
	public void disguise(Player player, SkinProperty disguiseSkinProperty)
	{
		mySkin.getHandler().setSkinProperty(player, disguiseSkinProperty);
		mySkin.getHandler().update(player);
	}

	public void undisguise(Player player, SkinProperty playerSkinProperty)
	{
		mySkin.getCache().resetSkinOfPlayer(player.getUniqueId());
		mySkin.getHandler().update(player);
	}

	public boolean setDisguise(Player player, WerewolfDisguise werewolfDisguise)
	{
		try
		{
			// Load the Disguise skin
			mySkin.getCache().saveSkinProperty(werewolfDisguise.getSkinAccountUUID(), new SkinProperty(werewolfDisguise.getWerewolfName(), werewolfDisguise.getSkinTextureValue(player), werewolfDisguise.getSkinTextureSignature(player)));

			SkinProperty disguiseSkinProperty = mySkin.getCache().loadSkinProperty(werewolfDisguise.getSkinAccountUUID());
			if (disguiseSkinProperty == null)
			{
				plugin.log("[MySkin] Tried Disguising as a non-cached player! Disguise failed!");
				return false;
			}

			// Apply the loaded SkinProperty to the player
			disguise(player, disguiseSkinProperty);

			return true;
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't disguise player... MySkin not found!");
		}
		return false;
	}
	
	public boolean removeDisguise(Player player)
	{
		try
		{
			// Load the player's old skin
			Map<UUID, MojangSkin> userSkinDetails = mojangAPI.getSkinDetails(player);
			if (userSkinDetails.isEmpty())
			{
				return false;
			}
			String value = userSkinDetails.get(player.getUniqueId()).value;
			String signature = userSkinDetails.get(player.getUniqueId()).signature;

			mySkin.getCache().saveSkinProperty(player.getUniqueId(), new SkinProperty(player.getName(), value, signature));
			SkinProperty playerSkinProperty = mySkin.getCache().loadSkinProperty(player.getUniqueId());
			if (playerSkinProperty == null)
			{
				plugin.log("[MySkin] Undisguise - Tried Disguising as a non-cached player! Disguise failed!");
				return false;
			}

			// Apply the Player's old skin back
			undisguise(player, playerSkinProperty);
			
			return true;
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't undisguise player... MySkin not found!");
		}
		return false;
	}
}