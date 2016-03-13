package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.Packet;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WerewolfSkinManager
{
	private Werewolf						plugin;
	private HashMap<UUID, WerewolfSkin>		skins			= new HashMap<UUID, WerewolfSkin>();
	protected int							nextID			= -2147483648;

	WerewolfSkinManager(Werewolf p)
	{
		this.plugin = p;
	}

	public WerewolfSkin getSkin(Player player)
	{
		return (WerewolfSkin) this.skins.get(player.getUniqueId());
	}

	public int getNextAvailableID()
	{
		return this.nextID++;
	}
	
	public int setWerewolfSkin(Player player)
	{
		if (this.skins.containsKey(player.getUniqueId()))
		{
			return ((WerewolfSkin) this.skins.get(player.getUniqueId())).getEntityID();
		}
		
		int entityID = getNextAvailableID();

		ClanManager.ClanType clantype = Werewolf.getWerewolfManager().getWerewolfClan(player.getUniqueId());
		WerewolfSkin skin = null;
		
		if(plugin.useClans && Werewolf.getClanManager().isAlpha(player.getUniqueId()))
		{
			skin = new WerewolfSkin(
					Werewolf.getClanManager().getWerewolfAccountForAlpha(clantype), 
					Werewolf.getClanManager().getWerewolfAccountIdForAlpha(clantype), 
					Werewolf.getClanManager().getWerewolfTextureForAlpha(clantype), 
					Werewolf.getClanManager().getWerewolfTextureSignatureForAlpha(clantype), 
					entityID);
						
		}
		else
		{
			skin = new WerewolfSkin(
				Werewolf.getClanManager().getWerewolfAccountForClan(clantype), 
				Werewolf.getClanManager().getWerewolfAccountIdForClan(clantype), 
				Werewolf.getClanManager().getWerewolfTextureForClan(clantype), 
				Werewolf.getClanManager().getWerewolfTextureSignatureForClan(clantype), 
				entityID);
		}
		this.skins.put(player.getUniqueId(), skin);

		Packet spawnPacket = skin.getPlayerSpawnPacket(player.getLocation(), (short) player.getItemInHand().getTypeId());
		Packet infoPacket = skin.getPlayerInfoPacket(player, true);

		// 1.8 client will not render a player that is not on the tab player list
		disguiseToWorld(player.getWorld(), player, new Packet[] { infoPacket, spawnPacket });

		return skin.getEntityID();
	}

	public void disguiseToObserver(Player player, Player observer)
	{
		if (!this.skins.containsKey(player.getUniqueId()))
		{
			return;
		}
		WerewolfSkin skin = (WerewolfSkin) this.skins.get(player.getUniqueId());

		observer.hidePlayer(player);

		Packet spawnPacket = skin.getPlayerSpawnPacket(player.getLocation(), (short) player.getItemInHand().getTypeId());
		Packet infoPacket = skin.getPlayerInfoPacket(player, true);
		//Packet headRotationPacket = skin.getHeadRotatePacket(player.getLocation());

		// 1.8 client will not render a player that is not on the tab player list
		((CraftPlayer) observer).getHandle().playerConnection.sendPacket(infoPacket);
		((CraftPlayer) observer).getHandle().playerConnection.sendPacket(spawnPacket);
		//((CraftPlayer) observer).getHandle().playerConnection.sendPacket(headRotationPacket);
	}

	public void unsetWerewolfSkin(Player player)
	{
		if (!this.skins.containsKey(player.getUniqueId()))
		{
			return;
		}
		WerewolfSkin skin = (WerewolfSkin) this.skins.get(player.getUniqueId());

		Packet packet = skin.getEntityDestroyPacket();
		Packet packet2 = skin.getPlayerInfoPacket(player, false);

		undisguiseToWorld(player.getWorld(), player, new Packet[] { packet, packet2 });

		this.skins.remove(player.getUniqueId());
	}

	public void removeSkinFromWorld(World world, Player player)
	{
		if (!this.skins.containsKey(player.getUniqueId()))
		{
			return;
		}
		WerewolfSkin skin = (WerewolfSkin) this.skins.get(player.getUniqueId());

		Packet destroyPacket = skin.getEntityDestroyPacket();
		Packet infoPacket = skin.getPlayerInfoPacket(player, false);
		
		for (Player observer : world.getPlayers())
		{
			if (!observer.getUniqueId().equals(player.getUniqueId()))
			{
				((CraftPlayer) observer).getHandle().playerConnection.sendPacket(destroyPacket);
				((CraftPlayer) observer).getHandle().playerConnection.sendPacket(infoPacket);
			}
		}
		
		this.skins.remove(player.getUniqueId());
	}

	public void sendMovement(Player werewolfPlayer, Vector velocity, Location to)
	{
		WerewolfSkin skin = (WerewolfSkin) this.skins.get(werewolfPlayer.getUniqueId());
		
		if ((skin == null) || (to == null))
		{
			return;
		}
		
		MovementValues movement = skin.getMovement(to);
		
		if ((movement.x < -128) || (movement.x > 128) || (movement.y < -128) || (movement.y > 128) || (movement.z < -128) || (movement.z > 128))
		{
			Packet packet = skin.getEntityTeleportPacket(to);

			sendPacketsToWorld(werewolfPlayer.getWorld(), new Packet[] { packet });
		}
		else if ((movement.x == 0) && (movement.y == 0) && (movement.z == 0))
		{
			Packet packet = skin.getEntityLookPacket(to);
			Packet packet2 = skin.getHeadRotatePacket(to);

			sendPacketsToWorld(werewolfPlayer.getWorld(), new Packet[] { packet, packet2 });
		}
		else
		{
			//Packet moveLookPacket = skin.getEntityMoveLookPacket(to);
			Packet teleportPacket = skin.getEntityTeleportPacket(to);
			Packet headPacket = skin.getHeadRotatePacket(to);

			sendPacketsToWorld(werewolfPlayer.getWorld(), new Packet[] { headPacket, teleportPacket });
		}
	}

	public void sendWorldChange(Player player, World fromWorld)
	{
		WerewolfSkin skin = getSkin(player);

		if(skin==null)
		{
			return;
		}
		
		Packet killPacket = skin.getEntityDestroyPacket();
		Packet killListPacket = skin.getPlayerInfoPacket(player, false);
		Packet revivePlayerPacket = skin.getPlayerSpawnPacket(player.getLocation(), (short) player.getItemInHand().getTypeId());
		Packet reviveListPacket = skin.getPlayerInfoPacket(player, true);
		
		if (killListPacket == null)
		{
			undisguiseToWorld(fromWorld, player, new Packet[] { killPacket });
		}
		else
		{
			undisguiseToWorld(fromWorld, player, new Packet[] { killPacket, killListPacket });
		}
		disguiseToWorld(player.getWorld(), player, new Packet[] { reviveListPacket, revivePlayerPacket });
	}

	public void sendPacketsToWorld(World world, Packet[] packets)
	{
		for (Player observer : world.getPlayers())
		{
			for (Packet p : packets)
			{
				((CraftPlayer) observer).getHandle().playerConnection.sendPacket(p);
			}
		}
	}

	public void disguiseToWorld(World world, Player player, Packet[] packets)
	{
		for (Player observer : world.getPlayers())
		{
			if (observer.getEntityId() != player.getEntityId() && !observer.getUniqueId().equals(player.getUniqueId()))
			{
				plugin.logDebug("Disguising " + player.getName() + " to " + observer.getName());

				observer.hidePlayer(player);

				for (Packet p : packets)
				{
					plugin.logDebug("disguiseToWorld(): Sending packet to " + observer.getName());

					((CraftPlayer) observer).getHandle().playerConnection.sendPacket(p);
				}
			}
		}

	}

	public void showWorldDisguises(Player observer)
	{
		for (UUID playerId : Werewolf.getWerewolfManager().getOnlineWerewolvesInWolfForm())
		{
			Player werewolf = this.plugin.getServer().getPlayer(playerId);
			if (werewolf != null && !playerId.equals(observer.getUniqueId()))
			{
				if (werewolf.getWorld() == observer.getWorld())
				{
					disguiseToObserver(werewolf, observer);
				}
			}
		}
	}

	public void undisguiseToWorld(World world, Player player, Packet[] packets)
	{
		for (Player observer : world.getPlayers())
		{
			if (observer.getEntityId() != player.getEntityId())
			{
				this.plugin.logDebug("undisguiseToWorld(): Sending packets and making visible to " + observer.getName());
				for (Packet p : packets)
				{
					((CraftPlayer) observer).getHandle().playerConnection.sendPacket(p);
				}
				
				observer.showPlayer(player);
			}
		}
	}

	public void setVisibleToWorld(Player player)
	{
		for (Player observer : player.getWorld().getPlayers())
		{
			if (!observer.getUniqueId().equals(player.getUniqueId()))
			{
				this.plugin.logDebug("Making " + player.getName() + " visible to " + observer.getName());
				observer.showPlayer(player);
			}
		}
	}
}
