package com.dogonfire.werewolf;

import org.bukkit.entity.Player;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class PlayerPositionUpdater
  implements Runnable
{
  final Werewolf plugin;
  final Player player;
  final PlayerDisguise skin;
  
  public PlayerPositionUpdater(Werewolf plugin, Player player, PlayerDisguise skin)
  {
    this.plugin = plugin;
    this.player = player;
    this.skin = skin;
  }
  
  public void run() {}
}
