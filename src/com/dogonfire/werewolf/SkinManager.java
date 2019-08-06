package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.api.IWerewolfDisguiseFactory.WerewolfDisguise;
import com.dogonfire.werewolf.api.WerewolfDisguiseAPI;

//import me.libraryaddict.disguise.DisguiseAPI;
//import me.libraryaddict.disguise.disguisetypes.Disguise;
//import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class SkinManager
{
	private Werewolf						plugin;
	private HashMap<UUID, WerewolfDisguise>	skins	= new HashMap<UUID, WerewolfDisguise>();
	protected int							nextID	= -2147483648;

	SkinManager(Werewolf p)
	{
		this.plugin = p;
	}

	public WerewolfDisguise getSkin(Player player)
	{
		return this.skins.get(player.getUniqueId());
	}

	public WerewolfDisguise getDisguise(Player player)
	{
		return this.skins.get(player.getUniqueId());
	}

	public int getNextAvailableID()
	{
		return this.nextID++;
	}

	public boolean setWerewolfSkin(Player player, String werewolfName)
	{
		if (this.skins.containsKey(player.getUniqueId()))
		{
			return true;
		}

		UUID disguiseAccountId;

		ClanManager.ClanType clantype = Werewolf.getWerewolfManager().getWerewolfClan(player.getUniqueId());

		// Get the correct account, if alpha, alphaskin, else the clanskin
		if (plugin.useClans && Werewolf.getClanManager().isAlpha(player.getUniqueId()))
		{
			disguiseAccountId = Werewolf.getClanManager().getWerewolfAccountIdForAlpha(clantype);
		}
		else
		{
			disguiseAccountId = Werewolf.getClanManager().getWerewolfAccountIdForClan(clantype);
		}

		// since it may be missing..
		try
		{
			if (WerewolfDisguiseAPI.isViewSelfToggled(player))
			{
				WerewolfDisguiseAPI.setViewDisguiseToggled(player, false);
			}

			if (werewolfName.isEmpty() || werewolfName == null)
			{
				werewolfName = "Werewolf";
			}

			WerewolfDisguise skin = WerewolfDisguiseAPI.newDisguise(werewolfName, player, disguiseAccountId);
			skin.setCapeEnabled(false);

			if (plugin.werewolfNamesEnabled)
			{
				String customWerewolfName = Werewolf.getWerewolfManager().getWerewolfName(player.getUniqueId());
				if (customWerewolfName != null && !customWerewolfName.isEmpty())
				{
					skin.setCustomName(customWerewolfName);
					skin.setCustomNameVisible(true);
				}
			}

			WerewolfDisguiseAPI.disguiseToAll(player, skin);

			this.skins.put(player.getUniqueId(), skin);

			return true;
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't disguise player: " + e.getMessage());
			Werewolf.getWerewolfManager().howl(player);
			return false;
		}
	}

	public void unsetWerewolfSkin(Player player)
	{
		if (!this.skins.containsKey(player.getUniqueId()))
		{
			return;
		}

		try
		{
			WerewolfDisguiseAPI.undisguiseToAll(player);
			player.setCustomNameVisible(false);
		}
		catch (NoClassDefFoundError e)
		{
			plugin.logDebug("Couldn't un-disguise player: " + e.getMessage());
			Werewolf.getWerewolfManager().howl(player);
		}

		this.skins.remove(player.getUniqueId());
	}
}
