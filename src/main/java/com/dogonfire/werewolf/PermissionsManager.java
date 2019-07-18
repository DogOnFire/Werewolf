package com.dogonfire.werewolf;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermissionsManager
{
	private String				pluginName			= "null";
	private Werewolf			plugin;
	private Permission			vaultPermission		= null;
    private static Chat 		vaultChat 			= null;

	public PermissionsManager(Werewolf p)
	{
		this.plugin = p;
		
		if (p.vaultEnabled) {
			RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
			vaultPermission = permissionProvider.getProvider();
			
			RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(Chat.class);
	        vaultChat = chatProvider.getProvider();
		}
	}

	public void load()
	{
		// Nothing
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
		if (this.plugin.vaultEnabled) { 
			return vaultPermission.has(player, node);
		}
		return false;
	}

	public boolean isGroup(String groupName)
	{
		if (this.plugin.vaultEnabled) {
			for(String str: vaultPermission.getGroups()) {
			    if(str.contains(groupName))
			       return true;
			}
		}
		return false;
	}

	public String getGroup(String playerName)
	{
		if (this.plugin.vaultEnabled) {
			return vaultPermission.getPrimaryGroup(plugin.getServer().getPlayer(playerName));
		}
		return "";
	}

	public String getPrefix(String playerName)
	{
		if (this.plugin.vaultEnabled) {
			Player player = plugin.getServer().getPlayer(playerName);
			return vaultChat.getPlayerPrefix(player);
		}
		return "";
	}

	public void setGroup(String playerName, String groupName)
	{
		if (this.plugin.vaultEnabled) {
			Player player = plugin.getServer().getPlayer(playerName);
			vaultPermission.playerAddGroup(player, groupName);
		}
	}
}