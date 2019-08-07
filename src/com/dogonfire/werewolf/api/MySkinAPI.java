package com.dogonfire.werewolf.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.api.WerewolfDisguiseAPI.WerewolfDisguise;

import eu.blackfire62.myskin.bukkit.MySkin;
import eu.blackfire62.myskin.shared.SkinProperty;
import eu.blackfire62.myskin.shared.util.MojangAPI;
import eu.blackfire62.myskin.shared.util.MojangAPIException;


// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public class MySkinAPI
{
	private MySkin 		mySkin;
	private Werewolf 	plugin;

	public MySkinAPI(Werewolf instance)
	{
		this.plugin = instance;
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
		mySkin.getHandler().setSkinProperty(player, playerSkinProperty);
		mySkin.getHandler().update(player);
		mySkin.getCache().resetSkinOfPlayer(player.getUniqueId());
	}

	public boolean setDisguise(Player player, WerewolfDisguise werewolfDisguise)
	{
		try
		{
			// Load the Disguise skin from WerewolfDisguise and create a new SkinProperty
			plugin.logDebug("[MySkin] Started Disguising of player: " + player.getName() + ". WerewolfAccountName: " + werewolfDisguise.getSkinAccountName());
			SkinProperty werewolfSkinProperty = new SkinProperty(werewolfDisguise.getSkinTextureValue(player), werewolfDisguise.getSkinTextureSignature(player));
			if (werewolfSkinProperty.value == null || werewolfSkinProperty.value.isEmpty() || werewolfSkinProperty.signature == null || werewolfSkinProperty.signature.isEmpty())
			{
				plugin.logDebug("[MySkin] Using the MojangAPI since the WerewolfDisguise did not have texture values!");
				werewolfSkinProperty = MojangAPI.getSkinProperty(werewolfDisguise.getSkinAccountUUID());
			}
			// Get the SkinProperty for the current player
			SkinProperty playerSkinProperty = mySkin.getHandler().getSkinProperty(player);

			// Save the Disguise skin to the cache
			mySkin.getCache().saveSkinProperty(werewolfDisguise.getSkinAccountUUID(), werewolfSkinProperty);
			// Save the player's skin to the cache
			mySkin.getCache().saveSkinProperty(player.getUniqueId(), playerSkinProperty);

			// Try loading the Disguise skin from the cache
			SkinProperty disguiseSkinProperty = mySkin.getCache().loadSkinProperty(werewolfDisguise.getSkinAccountUUID());
			if (disguiseSkinProperty == null)
			{
				plugin.log("[MySkin] Tried Disguising as a non-cached werewolf! Disguise failed!");
				return false;
			}

			// Apply the loaded Disguise skin to the player
			disguise(player, disguiseSkinProperty);

			return true;
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't disguise player... MySkin not found!");
		} catch (MojangAPIException e) {
			e.printStackTrace();
			plugin.log("[ERROR] Couldn't disguise player... MojangAPI Error, check the logs!");
		}
		return false;
	}

	public boolean removeDisguise(Player player)
	{
		try
		{
			// Load the player's old skin
			plugin.logDebug("[MySkin] Undisguise - Start undisguise!");

			// Load the Player's original skin from the cache
			SkinProperty playerSkinProperty = mySkin.getCache().loadSkinProperty(player.getUniqueId());
			if (playerSkinProperty == null)
			{
				plugin.logDebug("[MySkin] Could not fetch Player's original skin from cache, trying to get from MojangAPI");
				playerSkinProperty = MojangAPI.getSkinProperty(player.getUniqueId());
				if (playerSkinProperty == null)
				{
					plugin.log("[MySkin] Undisguise - Tried Disguising as a non-cached player! Undisguise failed!");
					return false;
				}
				plugin.logDebug("[MySkin] Sucessfully fetched the player's original skin from MojangAPI");
			}

			// Apply the Player's old skin back
			undisguise(player, playerSkinProperty);

			return true;
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't undisguise player... MySkin not found!");
		} catch (MojangAPIException e) {
			e.printStackTrace();
			plugin.log("[ERROR] Couldn't undisguise player... MojangAPI Error, check the logs!");
		}
		return false;
	}
}
