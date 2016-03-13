package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class WerewolfScoreboardManager
{
	private Werewolf plugin;
	private ScoreboardManager manager;
	private HashMap<UUID, Objective> playerHuntingObjectives = new HashMap<UUID, Objective>();
	
	WerewolfScoreboardManager(Werewolf plugin)
	{
		this.plugin = plugin;

		manager = Bukkit.getScoreboardManager();
	}

	public void newPlayerHuntingScoreboard(Player player)
	{
		Scoreboard board = manager.getNewScoreboard();
	
		//Team team = board.registerNewTeam("teamname");
	
		Objective objective = board.registerNewObjective("test", "dummy");
	
		// Setting where to display the scoreboard/objective (either SIDEBAR, PLAYER_LIST or BELOW_NAME)
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	 
		// Setting the display name of the scoreboard/objective
		objective.setDisplayName(ChatColor.DARK_AQUA + "Hunting Status");
		
		playerHuntingObjectives.put(player.getUniqueId(), objective);
		
		player.setScoreboard(board);		
		
		setMobKillsForPlayer(player, 0);
		setHumanKillsForPlayer(player, 0);
		
		if(plugin.vampireEnabled)
		{
			setVampireKillsForPlayer(player, 0);			
		}		
	}
	

	public void removePlayerHuntingScoreboard(Player player)
	{
		player.setScoreboard(manager.getNewScoreboard());
	}
	
	public void setMobKillsForPlayer(Player player, int kills)
	{
		Score score = playerHuntingObjectives.get(player.getUniqueId()).getScore(ChatColor.RED + "Mobs killed:"); //Get a fake offline player
		score.setScore(kills);
	}

	public void setHumanKillsForPlayer(Player player, int kills)
	{
		Score score = playerHuntingObjectives.get(player.getUniqueId()).getScore(ChatColor.RED + "Humans slain:"); //Get a fake offline player
		score.setScore(kills);
	}

	public void setVampireKillsForPlayer(Player player, int kills)
	{
		Score score = playerHuntingObjectives.get(player.getUniqueId()).getScore(ChatColor.RED + "Vampires slain:"); //Get a fake offline player
		score.setScore(kills);
	}
}
