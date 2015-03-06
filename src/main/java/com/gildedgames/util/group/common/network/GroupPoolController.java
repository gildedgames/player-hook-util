package com.gildedgames.util.group.common.network;

import com.gildedgames.util.group.common.IGroup;
import com.gildedgames.util.group.common.IGroupPool;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class GroupPoolController implements IGroupPoolController
{
	
	private IGroupPool targetPool;
	
	private final Side side;
	
	public GroupPoolController(Side side)
	{
		this.side = side;
	}
	
	@Override
	public String getID()
	{
		return this.targetPool.getID();
	}

	@Override
	public List getGroups()
	{
		return this.targetPool.getGroups();
	}

	@Override
	public void setGroups(List groups)
	{

	}

	@Override
	public void add(IGroup group)
	{

	}

	@Override
	public void remove(IGroup group)
	{
		
	}

	@Override
	public IGroup get(String name)
	{
		return this.targetPool.get(name);
	}

	@Override
	public void setTargetPool(IGroupPool targetPool)
	{
		this.targetPool = targetPool;
	}

}