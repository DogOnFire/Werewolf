package com.dogonfire.werewolf;

import java.io.PrintStream;
import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutRemoveEntityEffect;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PacketUtils
{
	private Werewolf	plugin;

	PacketUtils(Werewolf p)
	{
		this.plugin = p;
	}

	
	public void addPotionEffectNoGraphic(Player player, PotionEffect pe)
	{
		PacketPlayOutEntityEffect pm = new PacketPlayOutEntityEffect();
		try
		{
			{
				Field field = pm.getClass().getDeclaredField("a");
				field.setAccessible(true);
				field.setInt(pm, player.getEntityId());
			}

			{
				Field field = pm.getClass().getDeclaredField("b");
				field.setAccessible(true);
				field.setByte(pm, (byte) (pe.getType().getId() & 0xFF));
			}

			{
				Field field = pm.getClass().getDeclaredField("c");
				field.setAccessible(true);
				field.setByte(pm, (byte) (pe.getAmplifier() & 0xFF));
			}

			short duration = 32767;
			if (pe.getDuration() < 32767)
			{
				duration = (short) pe.getDuration();
			}

			{
				Field field = pm.getClass().getDeclaredField("d");
				field.setAccessible(true);
				field.setShort(pm, duration);
			}
		}
		catch (Exception e)
		{
			System.out.println("Werewolf could not access a PacketPlayOutEntityEffect package!");
			e.printStackTrace();
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(pm);
	}

	public void removePotionEffectNoGraphic(Player player, PotionEffectType pe)
	{
		PacketPlayOutRemoveEntityEffect pr = new PacketPlayOutRemoveEntityEffect();
		try
		{
			{
				Field field = pr.getClass().getDeclaredField("a");
				field.setAccessible(true);
				field.setInt(pr, player.getEntityId());
				field.setAccessible(!field.isAccessible());
			}

			{
				Field field = pr.getClass().getDeclaredField("b");
				field.setAccessible(true);
				field.setByte(pr, (byte) pe.getId());
				field.setAccessible(!field.isAccessible());
			}
		}
		catch (Exception e)
		{
			System.out.println("Werewolf could not access a PacketPlayOutEntityEffect package!");
			e.printStackTrace();
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(pr);
	}
}
