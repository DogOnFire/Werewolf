package com.dogonfire.werewolf.tasks;

import com.dogonfire.werewolf.Werewolf;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DropItemsTask implements Runnable
{
	private Werewolf	plugin;
	private Player		player	= null;

	private void dropHandItem(Player player)
	{
		PlayerInventory inventory = player.getInventory();

		ItemStack stack = inventory.getItemInHand();
		if ((stack == null) || (stack.getAmount() == 0) || (stack.getType().equals(Material.AIR)))
		{
			return;
		}
		if (this.plugin.dropArmorOnTransform)
		{
			player.getWorld().dropItemNaturally(player.getLocation(), stack);
			inventory.remove(stack);
		}
		else
		{
			int slot = player.getInventory().firstEmpty();
			if (slot > -1)
			{
				player.getInventory().setItem(slot, stack);
			}
			else
			{
				player.getWorld().dropItemNaturally(player.getLocation(), stack);
			}
		}
	}

	public DropItemsTask(Werewolf instance, Player p)
	{
		this.plugin = instance;
		this.player = p;

		this.plugin.log("CALLING DROPITEMSTASK!");
	}

	public void run()
	{
		dropHandItem(this.player);
	}
}