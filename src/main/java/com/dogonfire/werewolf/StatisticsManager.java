package com.dogonfire.werewolf;

import java.util.HashMap;
import java.util.UUID;


public class StatisticsManager
{
	public class WerewolfStatistics
	{
		public int numberOfMobKills;
		public int numberOfHumanKills;
		public int numberOfVampireKills;
		
		WerewolfStatistics()
		{}
	}
		
	private Werewolf	plugin;
	private HashMap<UUID, WerewolfStatistics> werewolfStatistics = new HashMap<UUID, WerewolfStatistics>();
		
	StatisticsManager(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	public void clearStatistics(UUID playerId)
	{
		werewolfStatistics.remove(playerId);
	}
	
	public int increaseHumanKills(UUID playerId)
	{
		if(!werewolfStatistics.containsKey(playerId))
		{
			werewolfStatistics.put(playerId, new WerewolfStatistics());
		}
		
		WerewolfStatistics statistics = werewolfStatistics.get(playerId);
		
		statistics.numberOfHumanKills++;

		werewolfStatistics.put(playerId, statistics);
		
		return statistics.numberOfHumanKills;
	}	
	
	public int increaseMobKills(UUID playerId)
	{
		if(!werewolfStatistics.containsKey(playerId))
		{
			werewolfStatistics.put(playerId, new WerewolfStatistics());
		}
		
		WerewolfStatistics statistics = werewolfStatistics.get(playerId);
		
		statistics.numberOfMobKills++;

		werewolfStatistics.put(playerId, statistics);
		
		return statistics.numberOfMobKills;
	}	
	
	public int increaseVampireKills(UUID playerId)
	{
		if(!werewolfStatistics.containsKey(playerId))
		{
			werewolfStatistics.put(playerId, new WerewolfStatistics());
		}
		
		WerewolfStatistics statistics = werewolfStatistics.get(playerId);
		
		statistics.numberOfVampireKills++;

		werewolfStatistics.put(playerId, statistics);
		
		return statistics.numberOfVampireKills;
	}	
}
