package com.dogonfire.werewolf;

import java.util.UUID;

import com.clanjhoo.vampire.VampireAPI;
import com.dogonfire.werewolf.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.dogonfire.werewolf.Werewolf;

public class WerewolfAPI
{	
	public static void setFullWerewolf(UUID playerId)
	{
		// Werewolf.getWerewolfManager().makeWerewolf(player, turnNow, clan);
	}

	public static ItemStack newWerewolfSilverSword()
	{
		return Werewolf.getItemManager().newSilverSword(1);
	}

	public static ItemStack newWerewolfLoreBook()
	{
		return Werewolf.getItemManager().newLoreBook();
	}

	public static ItemStack newWerewolfCurePotion()
	{
		return Werewolf.getItemManager().newCurePotion();
	}

	public static ItemStack newWerewolfInfectionPotion()
	{
		return Werewolf.getItemManager().newInfectionPotion();
	}

	public static ItemStack newWerewolfWolfbanePotion()
	{
		return Werewolf.getItemManager().newWolfbanePotion();
	}

	public static boolean isWerewolfSilverSword(ItemStack item)
	{
		return Werewolf.getItemManager().isSilverSword(item);
	}

	public static boolean isWerewolfLoreBook(ItemStack item)
	{
		return Werewolf.getItemManager().isLoreBook(item);
	}

	public static boolean isWerewolfCurePotion(ItemStack item)
	{
		return Werewolf.getItemManager().isCurePotion(item);
	}

	public static boolean isWerewolfInfectionPotion(ItemStack item)
	{
		return Werewolf.getItemManager().isInfectionPotion(item);
	}

	public static boolean isWerewolfWolfbanePotion(ItemStack item)
	{
		return Werewolf.getItemManager().isWolfbanePotion(item);
	}

	public static boolean isWerewolf(UUID playerId)
	{
		return Werewolf.getWerewolfManager().isWerewolf(playerId);
	}

	public static boolean isWerewolf(Player player)
	{
		return isWerewolf(player.getUniqueId());
	}
	public static boolean isVampire(Player player)
	{
		if (Werewolf.instance().vampireEnabled)
		{
			return VampireAPI.isVampire(player);
		}
		return false;
	}

	public static boolean isVampire(UUID playerId)
	{
		if (Werewolf.instance().vampireEnabled)
		{
			Player player = Bukkit.getPlayer(playerId);
			if (player == null) {
				player = (Player) Bukkit.getOfflinePlayer(playerId);
			}
			return VampireAPI.isVampire(player);
		}
		return false;
	}

	public static boolean isHuman(UUID playerId)
	{
		return !isWerewolf(playerId) && !isVampire(playerId);
	}

	public static boolean isHuman(Player player)
	{
		return isHuman(player.getUniqueId());
	}

	public static boolean isAlpha(UUID playerId)
	{
		return Werewolf.getClanManager().isAlpha(playerId);
	}

	public static boolean isAlpha(Player player)
	{
		return Werewolf.getClanManager().isAlpha(player.getUniqueId());
	}

	public static int getNumberOfWerewolves()
	{
		return Werewolf.getWerewolfManager().getNumberOfWerewolves();
	}

	public static ClanManager.ClanType getWerewolfClan(UUID playerId) {
		if (!isWerewolf(playerId)) {
			return null;
		}
		return Werewolf.getWerewolfManager().getWerewolfClan(playerId);
	}
}
