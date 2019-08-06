package com.dogonfire.werewolf.api;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.dogonfire.werewolf.ClanManager.ClanType;
import com.dogonfire.werewolf.Werewolf;

// Let's abstract disguise commands away, so that we don't depend on a particular Disguise plugin
public class WerewolfDisguiseAPI
{
	private Werewolf		plugin;
	private PluginManager 	pm;
	private int				method = 0;

	public WerewolfDisguiseAPI(Werewolf instance)
	{
		this.plugin = instance;
		this.pm = plugin.getServer().getPluginManager();
	}

	public class WerewolfDisguise
	{
		String werewolfName;
		UUID accountId;
		String accountName;

		public WerewolfDisguise(String werewolfName, UUID accountId, String accountName)
		{
			plugin.logDebug("Constructer Start! - Werewolfname: " + werewolfName + " - AccountId: " + accountId + " - AccountName: " + accountName);
			if (werewolfName == null || werewolfName == "")
			{
				werewolfName = "Werewolf";
			}

			if (accountId == null)
			{
				// Account called Werewolf. No clan has this skin...
				accountId = UUID.fromString("c21f069b-137c-4992-8edc-e1a12995ff0f");
			}

			if (accountName == null || accountName == "")
			{
				// Account called Werewolf. No clan has this skin...
				accountName = "Werewolf";
			}

			this.werewolfName = werewolfName;
			this.accountId = accountId;
			this.accountName = accountName;
		}

		public WerewolfDisguise(String werewolfName, String accountName)
		{
			if (werewolfName == null || werewolfName == "")
			{
				werewolfName = "Werewolf";
			}

			if (accountName == null || accountName == "")
			{
				// Account called Werewolf. No clan has this skin...
				accountName = "Werewolf";
				this.accountId = UUID.fromString("c21f069b-137c-4992-8edc-e1a12995ff0f");
			}
			else
			{
				// TODO create a way to load the correct UUID's from Werewolf.java, if even possible...
				switch(accountName)
				{
				case "WerewolfAlpha":
					this.accountId = UUID.fromString("e0d074bd-6722-47fc-95d3-f28e2899e155");
					break;
				case "WF_Werewolf":
					this.accountId = UUID.fromString("c61647e5-fc2f-4536-abe9-c851911ad22f");
					break;
				case "SM_Werewolf":
					this.accountId = UUID.fromString("b68a8f00-7d24-4c52-b6ad-1423bfbe26ee");
					break;
				case "BM_Werewolf":
					this.accountId = UUID.fromString("da508ecc-dbd9-46c5-8095-47b91aa4ff5f");
					break;
				default:
					// Account called Werewolf. No clan has this skin...
					this.accountId = UUID.fromString("c21f069b-137c-4992-8edc-e1a12995ff0f");
					break;
				}
			}

			this.werewolfName = werewolfName;
			this.accountName = accountName;
		}

		public WerewolfDisguise(String werewolfName, ClanType clanType)
		{
			if (werewolfName == null || werewolfName == "")
			{
				werewolfName = "Werewolf";
			}

			if (clanType == null)
			{
				// Account called Werewolf. No clan has this skin...
				this.accountName = "Werewolf";
				this.accountId = UUID.fromString("c21f069b-137c-4992-8edc-e1a12995ff0f");
			}
			else
			{
				this.accountName = Werewolf.getClanManager().getWerewolfAccountForClan(clanType);
				this.accountId = Werewolf.getClanManager().getWerewolfAccountIdForClan(clanType);
			}

			this.werewolfName = werewolfName;
		}
		
		public String getWerewolfName() {
			return werewolfName;
		}
		
		public String getSkinAccountName() {
			return accountName;
		}
		
		public UUID getSkinAccountUUID() {
			return accountId;
		}

		public void setWerewolfName(String customName) {
			werewolfName = customName;
		}

		public void setSkinAccountName(String skinAccountname) {
			accountName = skinAccountname;
		}

		public void setSkinAccountUUID(UUID skinAccountId) {
			accountId = skinAccountId;
		}

		public void setSkinAccountUUID(String skinAccountId) {
			accountId = UUID.fromString(skinAccountId);
		}
	}	
	
	public WerewolfDisguise newDisguise(String werewolfName, UUID accountId, String accountName)
	{
		plugin.logDebug("Werewolfname: " + werewolfName + " - AccountId: " + accountId + " - AccountName: " + accountName);
		WerewolfDisguise disguise = new WerewolfDisguise(werewolfName, accountId, accountName);
		return disguise;		
	}
	
	public void disguise(Player player, WerewolfDisguise skin)
	{
		// TODO implement support for all kind of plugins here
		boolean disguised = false;
		
		// Loop to go through each plugin (or other method) given below. If a plugin fails to apply the disguise, go to next one and try.
		for (method = 1; method < 2; method++)
		{
			// Lib's Disguises \\
			if (method == 1 && pm.getPlugin("LibsDisguises") != null && pm.getPlugin("LibsDisguises").isEnabled()) 
			{
				plugin.logDebug("Lib's Disguises found, using that for the disguise!");
	
				if (new LibsDisguiseAPI(plugin).setDisguise(player, skin)) { disguised = true; }
			}
			else
			{
				plugin.logDebug("No Lib's Disguises found... Next plugin check!");
				continue;
			}
	
	
			// End Check \\
			if (disguised)
			{
				Werewolf.getWerewolfManager().howl(player);
			}
		}
	}

	public void undisguise(Player player)
	{
		// TODO implement support for all kind of plugins here
		boolean undisguised = false;

		if (method == 0) {
			plugin.log("[ERROR] Tried undisguising a disguise with an unknown disguise method!");
			return;
		}

		// Lib's Disguises \\
		if (method == 1 && pm.getPlugin("LibsDisguises") != null && pm.getPlugin("LibsDisguises").isEnabled()) 
		{
			plugin.logDebug("Lib's Disguises found, using that for the undisguise!");

			if (new LibsDisguiseAPI(plugin).removeDisguise(player)) { undisguised = true; }
		}

		// End Check \\
		if (undisguised == false)
		{
			plugin.log("[ERROR] Could not undisguise player, " + player.getName() +"!");
			plugin.logDebug("[ERROR] Method: " + method);
		}
		else
		{
			Werewolf.getWerewolfManager().howl(player);
		}
	}
}