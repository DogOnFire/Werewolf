package com.dogonfire.werewolf;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class CompassTracker implements Runnable
{
	private static ArrayList<UUID>		watchList	= new ArrayList<UUID>();
	private static int					taskId		= -2;
	private static Werewolf				plugin;
	private static long					updateRate	= 100L;

	public static void setPlugin(Werewolf newPlugin)
	{
		plugin = newPlugin;
	}

	public static void setUpdateRate(long newUpdateRate)
	{
		updateRate = newUpdateRate;

		stop();
		start();
	}

	public static boolean start()
	{
		if (!isRunning())
		{
			taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new CompassTracker(), 40L, updateRate);
			if (isRunning())
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isRunning()
	{
		return taskId > 0;
	}

	public static void stop()
	{
		if (isRunning())
		{
			plugin.getServer().getScheduler().cancelTask(taskId);
		}
		taskId = -2;
	}

	public static boolean hasWatcher(UUID watcherId)
	{
		return watchList.contains(watcherId);
	}

	public static void addWatcher(UUID watcherId)
	{
		watchList.add(watcherId);

		start();
	}


	public static void removeWatcher(UUID watcherId)
	{
		if (watchList.contains(watcherId))
		{
			watchList.remove(watcherId);
		}
		
		if (watchList.isEmpty())
		{
			stop();
		}
	}

	public void run()
	{
		Server server = plugin.getServer();
		
		for (UUID playerId : watchList)
		{
			Player watcher = server.getPlayer(playerId);
			if (watcher != null)
			{
				Player watched = Werewolf.getWerewolfManager().getNearestWerewolf(watcher.getUniqueId());
				if (watched == null)
				{
					watcher.setCompassTarget(watcher.getLocation());
					return;
				}
				watcher.setCompassTarget(watched.getLocation());

				watcher.saveData();
			}
		}
	}
}