package com.dogonfire.werewolf.tasks;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;

public class CentralMessageTask implements Runnable
{
	private Werewolf	plugin;
	private String		messageText	= null;
	private Sound		sound;
	private World		world;

	public CentralMessageTask(Werewolf plugin, World world, String messageText, Sound sound)
	{
		this.plugin = plugin;
		this.messageText = messageText;
		this.sound = sound;
		this.world = world;
	}

	public void run()
	{
		for (Player player : world.getPlayers())
		{
			String translatedMessage = ChatColor.translateAlternateColorCodes('&', messageText);
			IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + translatedMessage + "\"}");
			PacketPlayOutChat chatPacket = new PacketPlayOutChat(chatComponent, (byte) 2);

			((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatPacket);
			
			world.playSound(player.getLocation(), sound, 10.0F, 1.0F);
		}
	}
}
