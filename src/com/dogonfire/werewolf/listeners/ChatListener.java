package com.dogonfire.werewolf.listeners;

import com.dogonfire.werewolf.PermissionsManager;
import com.dogonfire.werewolf.Werewolf;
import com.dogonfire.werewolf.WerewolfManager;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
	private Werewolf	plugin	= null;
	private Random		random	= new Random();

	public ChatListener(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	private String getWerewolfLanguage(String message)
	{
		message = message.toLowerCase().replace("a", "arr");
		if (this.random.nextInt(2) == 0)
		{
			message = message.replace("r", "rr");
		}
		if (this.random.nextInt(2) == 0)
		{
			message = message.replace("f", "woof");
		}
		else
		{
			message = message.replace("o", "awoo");
		}
		return message;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		if (!Werewolf.pluginEnabled || !this.plugin.wolfChat)
		{
			return;
		}
		
		Player player = e.getPlayer();
		if (Werewolf.getWerewolfManager().hasWerewolfSkin(player.getUniqueId()))
		{
			e.setCancelled(true);

			Random r = new Random();

			String alternativeMessage = (String) this.plugin.wolfMessage.toArray()[r.nextInt(this.plugin.wolfMessage.size())];

			String message = getWerewolfLanguage(e.getMessage());
			for (Player receiver : e.getRecipients())
			{
				if ((receiver.isOp()) || (Werewolf.getPermissionsManager().hasPermission(player, "werewolf.listener")))
				{
					receiver.sendMessage("<" + Werewolf.getWerewolfManager().getPlayerListName(player) + ">(Werewolf): " + e.getMessage());
				}
				else if (Werewolf.getWerewolfManager().isWerewolf(receiver.getUniqueId()))
				{
					receiver.sendMessage("<" + Werewolf.getWerewolfManager().getPlayerListName(player) + ">: " + ChatColor.RED + message);
				}
				else
				{
					receiver.sendMessage(plugin.getChatPrefix() + alternativeMessage);
				}
			}
			this.plugin.log("<" + Werewolf.getWerewolfManager().getPlayerListName(player) + ">(Werewolf): " + message);

			Werewolf.getWerewolfManager().growl(player);
		}
	}
}