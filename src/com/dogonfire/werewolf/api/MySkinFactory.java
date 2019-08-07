package com.dogonfire.werewolf.api;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.api.MojangAPI.MojangSkin;

import eu.blackfire62.myskin.bukkit.MySkin;
import eu.blackfire62.myskin.shared.SkinProperty;


public class MySkinFactory implements IWerewolfDisguiseFactory
{	
	private MySkin 		mySkin;
	private MojangAPI 	mojangAPI;
		
	public class MySkinWerewolfDisguise extends WerewolfDisguise
	{
		private SkinProperty disguiseSkinProperty;
		
		public MySkinWerewolfDisguise(UUID disguiseAccountId, String disguiseAccountName)
		{
			super(disguiseAccountId, disguiseAccountName);

			// Load the Disguise skin
			disguiseSkinProperty = mySkin.getCache().loadSkinProperty(getSkinAccountUUID());
		}		
		
		public boolean disguise(Player player, String werewolfName)
		{
			Werewolf.instance().logDebug("Disguising player " + player.getName() + " using MySkin...");

			if(!Werewolf.instance().getServer().getPluginManager().getPlugin("MySkin").isEnabled())
			{
				Werewolf.instance().logDebug("Didn't disguise player... MySkin is not enabled!");
				return false;	
			}

			try
			{
				// Save the original player skin
				mySkin.getCache().saveSkinProperty(getSkinAccountUUID(), new SkinProperty(werewolfName, getSkinTextureValue(player), getSkinTextureSignature(player)));

				if (disguiseSkinProperty == null)
				{
					Werewolf.instance().log("[MySkin] Tried Disguising as a non-cached player! Disguise failed!");
					return false;
				}

				// Apply the loaded SkinProperty to the player
				mySkin.getHandler().setSkinProperty(player, disguiseSkinProperty);
				mySkin.getHandler().update(player);

				return true;
			}
			catch (NoClassDefFoundError e)
			{
				Werewolf.instance().logDebug("Couldn't disguise player... MySkin not found!");
			}
			
			return false;
		}
		
		public boolean undisguise(Player player)
		{
			if(Werewolf.instance().getServer().getPluginManager().getPlugin("MySkin").isEnabled())
			{
				Werewolf.instance().logDebug("Did't disguise player... Libs Disguises not enabled!");
				return false;	
			}

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
					Werewolf.instance().log("[MySkin] Undisguise - Tried Disguising as a non-cached player! Disguise failed!");
					return false;
				}

				// Apply the Player's old skin back
				mySkin.getHandler().setSkinProperty(player, playerSkinProperty);
				mySkin.getHandler().update(player);
				
				return true;
			}
			catch (NoClassDefFoundError e)
			{
				Werewolf.instance().logDebug("Couldn't undisguise player... MySkin not found!");
			}
			return false;
		}
	}	
	
	
	public MySkinFactory()
	{
		this.mojangAPI = new MojangAPI();
		this.mySkin = (MySkin) Bukkit.getPluginManager().getPlugin("MySkin");
		
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().potionAccountUUID), UUID.fromString(Werewolf.instance().potionAccountUUID));
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().werewolfBiteAccountUUID), UUID.fromString(Werewolf.instance().werewolfBiteAccountUUID));
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().wildBiteAccountUUID), UUID.fromString(Werewolf.instance().wildBiteAccountUUID));
		mySkin.getCache().saveSkinOfPlayer(UUID.fromString(Werewolf.instance().alphaAccountUUID), UUID.fromString(Werewolf.instance().alphaAccountUUID));
	}
	
	@Override
	public WerewolfDisguise newDisguise(UUID disguiseAccountId, String disguiseAccountName)
	{		
		return new MySkinWerewolfDisguise(disguiseAccountId, disguiseAccountName);
	}	
}