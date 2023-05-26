package com.dogonfire.werewolf;

import java.util.UUID;

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

	public static ItemStack newWerewolfLorebook()
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

	public static boolean isWerewolf(UUID playerId)
	{
		return Werewolf.getWerewolfManager().isWerewolf(playerId);
	}

	public static boolean isWerewolf(Player player)
	{
		return Werewolf.getWerewolfManager().isWerewolf(player.getUniqueId());
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
}
