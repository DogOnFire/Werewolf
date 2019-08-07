package com.dogonfire.werewolf.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class LibsDisguisesFactory implements IWerewolfDisguiseFactory
{
	public class LibsDisguisesDisguise extends WerewolfDisguise
	{
		public LibsDisguisesDisguise(UUID werewolfDisguiseAccountId, String werewolfDisguiseAccountName)
		{
			super(werewolfDisguiseAccountId, werewolfDisguiseAccountName);
		}		
				
		public boolean disguise(Player player, String werewolfName)
		{
			Werewolf.instance().logDebug("Disguising player " + player.getName() + " using LibsDisguises...");

			if(!Werewolf.instance().getServer().getPluginManager().getPlugin("LibsDisguises").isEnabled())
			{
				Werewolf.instance().logDebug("Didn't disguise player... Libs Disguises not enabled!");
				return false;	
			}
			
			try
			{
				if (DisguiseAPI.isViewSelfToggled(player))
				{
					DisguiseAPI.setViewDisguiseToggled(player, false);
				}

				Werewolf.instance().logDebug("WerewolfName: " + werewolfName + " - WerewolfAccountName: " + getSkinAccountName());

				PlayerDisguise skin = new PlayerDisguise(werewolfName, getSkinAccountName());
				skin.getWatcher().setCapeEnabled(false);

				if (Werewolf.instance().werewolfNamesEnabled)
				{
					String customWerewolfName = Werewolf.getWerewolfManager().getWerewolfName(player.getUniqueId());
					if (customWerewolfName != null && !customWerewolfName.isEmpty())
					{
						skin.getWatcher().setCustomName(customWerewolfName);
						skin.getWatcher().setCustomNameVisible(true);
					}
				}

				DisguiseAPI.disguiseToAll(player, skin);

				return true;
			}
			catch (NoClassDefFoundError e)
			{
				Werewolf.instance().logDebug("Couldn't disguise player... Libs Disguises not found!");
			}
			return false;		
		}
		
		public boolean undisguise(Player player)
		{
			try
			{
				DisguiseAPI.undisguiseToAll(player);
				player.setCustomNameVisible(false);
				return true;
			}
			catch (NoClassDefFoundError e)
			{
				Werewolf.instance().logDebug("Couldn't un disguise player... Libs Disguises not found!");
			}
			return false;
						
		}		
	}
	
	private DisguiseAPI disguiseAPI;
	
	public LibsDisguisesFactory()
	{
		this.disguiseAPI = (DisguiseAPI) Bukkit.getPluginManager().getPlugin("LibsDisguises");
		
	}

	/*
	public boolean setDisguise(Player player, WerewolfDisguise werewolfDisguise)
	{
		try
		{
			if (DisguiseAPI.isViewSelfToggled(player))
			{
				DisguiseAPI.setViewDisguiseToggled(player, false);
			}

			Werewolf.instance().logDebug("WerewolfName: " + werewolfDisguise.getWerewolfName() + " - WerewolfAccountName: " + werewolfDisguise.getSkinAccountName());

			PlayerDisguise skin = new PlayerDisguise(werewolfDisguise.getWerewolfName(), werewolfDisguise.getSkinAccountName());
			skin.getWatcher().setCapeEnabled(false);

			if (Werewolf.instance().werewolfNamesEnabled)
			{
				String customWerewolfName = Werewolf.getWerewolfManager().getWerewolfName(player.getUniqueId());
				if (customWerewolfName != null && !customWerewolfName.isEmpty())
				{
					skin.getWatcher().setCustomName(customWerewolfName);
					skin.getWatcher().setCustomNameVisible(true);
				}
			}

			DisguiseAPI.disguiseToAll(player, skin);

			return true;
		}
		catch (NoClassDefFoundError e)
		{
			Werewolf.instance().logDebug("Couldn't disguise player... Libs Disguises not found!");
		}
		return false;
	}*/

	/*
	public boolean removeDisguise(Player player)
	{
		try
		{
			DisguiseAPI.undisguiseToAll(player);
			player.setCustomNameVisible(false);
			return true;
		}
		catch (NoClassDefFoundError e)
		{
			Werewolf.instance().logDebug("Couldn't disguise player... Libs Disguises not found!");
		}
		return false;
	}*/

	@Override
	public LibsDisguisesDisguise newDisguise(UUID disguiseAccountId, String disguiseAccountName)
	{
		return new LibsDisguisesDisguise(disguiseAccountId, disguiseAccountName);
	}
}
