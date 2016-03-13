package com.dogonfire.werewolf;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.Calculable;
import de.bananaco.bpermissions.api.CalculableType;
import de.bananaco.bpermissions.api.WorldManager;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsManager
{
	private String				pluginName			= "null";
	private PluginManager		pluginManager		= null;
	private Werewolf			plugin;
	private PermissionsPlugin	permissionsBukkit	= null;
	private PermissionManager	pex					= null;
	private GroupManager		groupManager		= null;

	public PermissionsManager(Werewolf p)
	{
		this.plugin = p;
	}

	public void load()
	{
		this.pluginManager = this.plugin.getServer().getPluginManager();
		if (this.pluginManager.getPlugin("PermissionsBukkit") != null)
		{
			this.plugin.log("Using PermissionsBukkit.");
			this.pluginName = "PermissionsBukkit";
			this.permissionsBukkit = ((PermissionsPlugin) this.pluginManager.getPlugin("PermissionsBukkit"));
		}
		else if (this.pluginManager.getPlugin("PermissionsEx") != null)
		{
			this.plugin.log("Using PermissionsEx.");
			this.pluginName = "PermissionsEx";
			this.pex = PermissionsEx.getPermissionManager();
		}
		else if (this.pluginManager.getPlugin("GroupManager") != null)
		{
			this.plugin.log("Using GroupManager");
			this.pluginName = "GroupManager";
			this.groupManager = ((GroupManager) this.pluginManager.getPlugin("GroupManager"));
		}
		else if (this.pluginManager.getPlugin("bPermissions") != null)
		{
			this.plugin.log("Using bPermissions.");
			this.pluginName = "bPermissions";
		}
		else
		{
			this.plugin.log("No permissions plugin detected! Defaulting to superperm");
			this.pluginName = "SuperPerm";
		}
	}

	public Plugin getPlugin()
	{
		return this.plugin;
	}

	public String getPermissionPluginName()
	{
		return this.pluginName;
	}

	public boolean hasPermission(Player player, String node)
	{
		if (this.pluginName.equals("PermissionsBukkit"))
		{
			return player.hasPermission(node);
		}
		if (this.pluginName.equals("PermissionsEx"))
		{
			return this.pex.has(player, node);
		}
		if (this.pluginName.equals("GroupManager"))
		{
			AnjoPermissionsHandler handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
			if (handler == null)
			{
				return false;
			}
			return handler.permission(player, node);
		}
		if (this.pluginName.equals("bPermissions"))
		{
			return ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), node);
		}
		if (this.pluginName.equals("zPermissions"))
		{
			return player.hasPermission(node);
		}
		return player.hasPermission(node);
	}

	public boolean isGroup(String groupName)
	{
		if (this.pluginName.equals("PermissionsBukkit"))
		{
			if (this.permissionsBukkit.getGroup(groupName) == null)
			{
				return false;
			}
			return true;
		}
		if (this.pluginName.equals("PermissionsEx"))
		{
			if (this.pex.getGroup(groupName) == null)
			{
				return false;
			}
			return true;
		}
		if (this.pluginName.equals("GroupManager"))
		{
			if (this.permissionsBukkit.getGroup(groupName) == null)
			{
				return false;
			}
			return true;
		}
		this.pluginName.equals("bPermissions");

		return false;
	}

	public String getGroup(String playerName)
	{
		if (this.pluginName.equals("PermissionsBukkit"))
		{
			if (this.permissionsBukkit.getGroups(playerName) == null)
			{
				return "";
			}
			if (this.permissionsBukkit.getGroups(playerName).size() == 0)
			{
				return "";
			}
			return ((com.platymuus.bukkit.permissions.Group) this.permissionsBukkit.getGroups(playerName).get(0)).getName();
		}
		if (this.pluginName.equals("PermissionsEx"))
		{
			if ((this.pex.getUser(playerName).getGroups() == null) || (this.pex.getUser(playerName).getGroups().length == 0))
			{
				return "";
			}
			return this.pex.getUser(playerName).getGroups()[0].getName();
		}
		if (this.pluginName.equals("GroupManager"))
		{
			AnjoPermissionsHandler handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(playerName);
			if (handler == null)
			{
				this.plugin.logDebug("PermissionManager(): No handler for player " + playerName);
				return "";
			}
			return handler.getGroup(playerName);
		}
		if (this.pluginName.equals("bPermissions"))
		{
			de.bananaco.bpermissions.api.World w = WorldManager.getInstance().getWorld(playerName);
			if (w == null)
			{
				return "";
			}
			if (w.getUser(playerName).getGroupsAsString().size() == 0)
			{
				return "";
			}
			return (String) w.getUser(playerName).getGroupsAsString().toArray()[0];
		}
		return "";
	}

	public String getPrefix(String playerName)
	{
		if (this.pluginName.equals("PermissionsBukkit"))
		{
			return "";
		}
		if (this.pluginName.equals("PermissionsEx"))
		{
			return this.pex.getUser(this.pluginName).getOwnSuffix();
		}
		if (this.pluginName.equals("GroupManager"))
		{
			AnjoPermissionsHandler handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(playerName);
			if (handler == null)
			{
				return "";
			}
			return handler.getUserPrefix(playerName);
		}
		if (this.pluginName.equals("bPermissions"))
		{
			de.bananaco.bpermissions.api.World w = WorldManager.getInstance().getWorld(playerName);
			if (w == null)
			{
				return "";
			}
			Calculable c = w.get(playerName, CalculableType.USER);

			return c.getValue("prefix");
		}
		return "";
	}

	public void setGroup(String playerName, String groupName)
	{
		if (this.pluginName.equals("PermissionsBukkit"))
		{
			if (this.permissionsBukkit.getServer().getPlayer(playerName) != null)
			{
				if (this.permissionsBukkit.getServer().getPlayer(playerName).getGameMode() == GameMode.CREATIVE)
				{
					this.permissionsBukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gm " + playerName);
				}
			}
			this.permissionsBukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player setgroup " + playerName + " " + groupName);
		}
		else
		{
			String[] groups;
			if (this.pluginName.equals("PermissionsEx"))
			{
				PermissionUser user = PermissionsEx.getPermissionManager().getUser(playerName);

				groups = new String[] { groupName };
				user.setGroups(groups);
			}
			else if (this.pluginName.equals("bPermissions"))
			{
				for (org.bukkit.World world : this.plugin.getServer().getWorlds())
				{
					ApiLayer.setGroup(world.getName(), CalculableType.USER, playerName, groupName);
				}
			}
			else if (this.pluginName.equals("GroupManager"))
			{
				OverloadedWorldHolder owh = this.groupManager.getWorldsHolder().getWorldDataByPlayerName(playerName);
				if (owh == null)
				{
					return;
				}
				org.anjocaido.groupmanager.data.User user = owh.getUser(playerName);
				if (user == null)
				{
					this.plugin.log("No player with the name '" + groupName + "'");
					return;
				}
				org.anjocaido.groupmanager.data.Group group = owh.getGroup(groupName);
				if (group == null)
				{
					this.plugin.log("No group with the name '" + groupName + "'");
					return;
				}
				user.setGroup(group);

				Player p = Bukkit.getPlayer(playerName);
				if (p != null)
				{
					GroupManager.BukkitPermissions.updatePermissions(p);
				}
			}
		}
	}
}