package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;

public class WerewolfSkinManager
{
	private Werewolf						plugin;
	private HashMap<UUID, PlayerDisguise>	skins			= new HashMap<UUID, PlayerDisguise>();
	protected int							nextID			= -2147483648;

	WerewolfSkinManager(Werewolf p)
	{
		this.plugin = p;
	}

	public PlayerDisguise getSkin(Player player)
	{
		return (PlayerDisguise) this.skins.get(player.getUniqueId());
	}
	
	public Disguise getDisguise(Player player)
	{
		return (Disguise) this.skins.get(player.getUniqueId());
	}

	public int getNextAvailableID()
	{
		return this.nextID++;
	}
	
	// TODO: Only disguise for everyone BUT the player, so they don't have to see their own face
	public boolean setWerewolfSkin(Player player, String werewolfName)
	{
		if (this.skins.containsKey(player.getUniqueId()))
		{
			return true;
		}
		
		String account;

		ClanManager.ClanType clantype = Werewolf.getWerewolfManager().getWerewolfClan(player.getUniqueId());
		
		if(plugin.useClans && Werewolf.getClanManager().isAlpha(player.getUniqueId()))
		{
			account = Werewolf.getClanManager().getWerewolfAccountForAlpha(clantype);
						
		}
		else
		{
			account = Werewolf.getClanManager().getWerewolfAccountForClan(clantype);
		}
		
		// since it may be missing..
		try {
			if (DisguiseAPI.isViewSelfToggled(player)) {
				DisguiseAPI.setViewDisguiseToggled(player, false);
			}
			
			if (werewolfName.isEmpty() || werewolfName == null) {
				werewolfName = "Werewolf";
			}
			
			PlayerDisguise skin = new PlayerDisguise(werewolfName, account);
			skin.getWatcher().setCapeEnabled(false);
			DisguiseAPI.disguiseToAll(player, skin);

			this.skins.put(player.getUniqueId(), skin);
			
			
			Werewolf.getWerewolfManager().howl(player);

			return true;
		}
		catch (NoClassDefFoundError e) {
			plugin.logDebug("Couldn't disguise player... Libs Disguises not found!");
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
		
		try {
			DisguiseAPI.undisguiseToAll(player);
			player.setCustomNameVisible(false);
		}
		catch (NoClassDefFoundError e) {
			plugin.logDebug("Couldn't disguise player... Libs Disguises not found!");
			Werewolf.getWerewolfManager().howl(player);
		}

		this.skins.remove(player.getUniqueId());
	}
}
