package com.gildedgames.util.world.common.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class WorldMinecraftFactoryServer implements IWorldFactory<WorldMinecraft>
{

	@Override
	public WorldMinecraft create(int dimId, boolean isRemote)
	{
		World world = MinecraftServer.getServer().worldServerForDimension(dimId);

		if (world == null)
		{
			throw new RuntimeException("Tried to create a wrapper for a missing world.");
		}

		return new WorldMinecraft(world);
	}

}