package com.gildedgames.util.universe;

import net.minecraftforge.fml.relauncher.Side;

import com.gildedgames.util.core.UtilCore;
import com.gildedgames.util.player.common.IPlayerHookPool;
import com.gildedgames.util.player.common.PlayerHookPool;
import com.gildedgames.util.universe.common.UniverseAPI;
import com.gildedgames.util.universe.common.player.PlayerUniverse;
import com.gildedgames.util.universe.common.player.PlayerUniverseFactory;

public class UniverseServices
{

	public IPlayerHookPool<PlayerUniverse> players;

	public UniverseAPI api;

	private Side side;

	public UniverseServices(Side side)
	{
		this.side = side;
	}

	public IPlayerHookPool<PlayerUniverse> getPlayers()
	{
		if (this.players == null)
		{
			this.players = new PlayerHookPool<PlayerUniverse>(UtilCore.MOD_ID, new PlayerUniverseFactory(), this.side);
		}

		return this.players;
	}

	public UniverseAPI getAPI()
	{
		if (this.api == null)
		{
			this.api = new UniverseAPI();
		}

		return this.api;
	}

}
