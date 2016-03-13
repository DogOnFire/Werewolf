package com.dogonfire.werewolf;

import org.bukkit.entity.Player;

public class PlayerPositionUpdater
  implements Runnable
{
  final Werewolf plugin;
  final Player player;
  final WerewolfSkin skin;
  
  public PlayerPositionUpdater(Werewolf plugin, Player player, WerewolfSkin skin)
  {
    this.plugin = plugin;
    this.player = player;
    this.skin = skin;
  }
  
  public void run() {}
}
