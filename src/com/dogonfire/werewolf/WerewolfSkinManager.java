package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.v1_12_R1.Packet;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

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

	public int getNextAvailableID()
	{
		return this.nextID++;
	}
	
	public boolean setWerewolfSkin(Player player)
	{
		if (this.skins.containsKey(player.getUniqueId()))
		{
			return true;
		}
		
		int entityID = getNextAvailableID();
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
		
		PlayerDisguise skin = new PlayerDisguise(player.getName());
		skin.setSkin(account);
		this.skins.put(player.getUniqueId(), skin);
		DisguiseAPI.disguiseToAll(player, skin);

		return true;
	}

	public void unsetWerewolfSkin(Player player)
	{
		if (!this.skins.containsKey(player.getUniqueId()))
		{
			return;
		}

		DisguiseAPI.undisguiseToAll(player);

		this.skins.remove(player.getUniqueId());
	}
}
