package com.gildedgames.util.testutil.player;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

import com.gildedgames.util.player.common.IPlayerHookPool;
import com.gildedgames.util.player.common.player.IPlayerHook;
import com.gildedgames.util.player.common.player.IPlayerProfile;

public class TestPlayerHook implements IPlayerHook
{

	IPlayerProfile profile;

	IPlayerHookPool<TestPlayerHook> pool;

	private int idForCompare;

	private TestPlayerHook()
	{

	}

	public TestPlayerHook(IPlayerProfile profile, IPlayerHookPool<TestPlayerHook> pool, int idForCompare)
	{
		this.idForCompare = idForCompare;
		this.profile = profile;
		this.pool = pool;
	}

	@Override
	public void write(NBTTagCompound output)
	{
		output.setInteger("id", this.idForCompare);
	}

	@Override
	public void read(NBTTagCompound input)
	{
		this.idForCompare = input.getInteger("id");
	}

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	public void markDirty()
	{

	}

	@Override
	public void markClean()
	{

	}

	@Override
	public void syncTo(ByteBuf buf, SyncSide to)
	{

	}
	
	@Override
	public void syncFrom(ByteBuf buf, SyncSide from)
	{

	}

	@Override
	public IPlayerHookPool<TestPlayerHook> getParentPool()
	{
		return this.pool;
	}

	@Override
	public void entityInit(EntityPlayer player)
	{

	}

	@Override
	public IPlayerProfile getProfile()
	{
		return this.profile;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (super.equals(obj))
		{
			return true;
		}

		if (obj instanceof TestPlayerHook)
		{
			TestPlayerHook hook = (TestPlayerHook) obj;
			return this.idForCompare == hook.idForCompare && hook.getProfile().equals(this.getProfile());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return this.idForCompare;
	}

}