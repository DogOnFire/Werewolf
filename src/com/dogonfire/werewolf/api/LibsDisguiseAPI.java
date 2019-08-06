package com.dogonfire.werewolf.api;

import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.api.WerewolfDisguiseAPI.WerewolfDisguise;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class LibsDisguiseAPI
{
	private Werewolf	plugin;
	
	public LibsDisguiseAPI(Werewolf instance)
	{
		this.plugin = instance;
	}

	public boolean setDisguise(Player player, WerewolfDisguise werewolfDisguise)
	{
		try
		{
			if (DisguiseAPI.isViewSelfToggled(player)) {
				DisguiseAPI.setViewDisguiseToggled(player, false);
			}
			
			plugin.logDebug("WerewolfName: " + werewolfDisguise.getWerewolfName() + " - WerewolfAccountName: " + werewolfDisguise.getSkinAccountName());
			
			PlayerDisguise skin = new PlayerDisguise(werewolfDisguise.getWerewolfName(), werewolfDisguise.getSkinAccountName());
			skin.getWatcher().setCapeEnabled(false);
			
			if (plugin.werewolfNamesEnabled) {
				String customWerewolfName = Werewolf.getWerewolfManager().getWerewolfName(player.getUniqueId());
				if (customWerewolfName != null && !customWerewolfName.isEmpty()) {
					skin.getWatcher().setCustomName(customWerewolfName);
					skin.getWatcher().setCustomNameVisible(true);
				}
			}
			
			DisguiseAPI.disguiseToAll(player, skin);
			
			return true;
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't disguise player... Libs Disguises not found!");
		}
		return false;
	}

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
			plugin.logDebug("Couldn't disguise player... Libs Disguises not found!");
		}
		return false;
	}
}
