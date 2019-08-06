package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.api.WerewolfDisguiseAPI;
import com.dogonfire.werewolf.api.WerewolfDisguiseAPI.WerewolfDisguise;

public class SkinManager
{
	private Werewolf						plugin;
	private WerewolfDisguiseAPI 			disguiseAPI;
	private HashMap<UUID, WerewolfDisguise>	skins	= new HashMap<UUID, WerewolfDisguise>();
	protected int							nextID	= -2147483648;

	SkinManager(Werewolf p)
	{
		this.plugin = p;
		this.disguiseAPI = new WerewolfDisguiseAPI(p);
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

		UUID accountId;
		String accountName;

		ClanManager.ClanType clantype = Werewolf.getWerewolfManager().getWerewolfClan(player.getUniqueId());

		// Get the correct account, if alpha, alphaskin, else the clanskin
		if (plugin.useClans && Werewolf.getClanManager().isAlpha(player.getUniqueId()))
		{
			accountId = Werewolf.getClanManager().getWerewolfAccountIdForAlpha(clantype);
			accountName = Werewolf.getClanManager().getWerewolfAccountForAlpha(clantype);
		}
		else
		{
			accountId = Werewolf.getClanManager().getWerewolfAccountIdForClan(clantype);
			accountName = Werewolf.getClanManager().getWerewolfAccountForClan(clantype);
		}

		if (werewolfName.isEmpty() || werewolfName == null)
		{
			werewolfName = "Werewolf";
		}

		WerewolfDisguise skin = disguiseAPI.newDisguise(werewolfName, accountId, accountName);
		if (skin == null) {
			plugin.log("Skin is null!");
		}
		plugin.logDebug("Skin: " + skin.toString() + ". Skin others: " + skin.getSkinAccountName() + " " + skin.getWerewolfName());
		disguiseAPI.disguise(player, skin);

		this.skins.put(player.getUniqueId(), skin);

		return true;
	}

	public void unsetWerewolfSkin(Player player)
	{
		if (!this.skins.containsKey(player.getUniqueId()))
		{
			return;
		}

		disguiseAPI.undisguise(player);

		this.skins.remove(player.getUniqueId());
	}
}
