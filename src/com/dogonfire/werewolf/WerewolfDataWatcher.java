package com.dogonfire.werewolf;

import org.inventivetalent.reflection.minecraft.DataWatcher;

import net.minecraft.server.v1_12_R1.Entity;

public class WerewolfDataWatcher extends net.minecraft.server.v1_12_R1.DataWatcher
{
	
	public WerewolfDataWatcher(Entity arg0)
	{
		super(arg0);
	}

	public void watch(int paramInt, Object paramObject)
	{
		
		Object localWatchableObject = null;
		try
		{
			localWatchableObject = DataWatcher.V1_9.newDataWatcherItem(this, paramInt);
			DataWatcher.V1_9.setItemValue(localWatchableObject, paramObject);
		}
		catch (Exception localException)
		{
		}
	}
}
