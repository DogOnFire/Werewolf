package com.dogonfire.werewolf;

import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class WerewolfEntity extends EntityPlayer
{
	public WerewolfEntity(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager, PlayerConnection conn)
	{
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);

		this.playerConnection = conn;
	}

	public static WerewolfEntity newWerewolfEntity(Location loc, GameProfile gp, PlayerConnection conn)
	{
		WorldServer wS = ((CraftWorld) loc.getWorld()).getHandle();

		WerewolfEntity entity = new WerewolfEntity(((CraftServer) Bukkit.getServer()).getServer(), wS, gp, new PlayerInteractManager(wS), conn);

		entity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

		return entity;
	}
}
