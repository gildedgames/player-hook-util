package com.gildedgames.util.core.gui.util;

import com.gildedgames.util.ui.data.AssetLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class MinecraftAssetLocation implements AssetLocation
{
	
	protected final String domain, path;
	
	public MinecraftAssetLocation(String path)
	{
		this("minecraft", path);
	}
	
	public MinecraftAssetLocation(String... paths)
	{
		this.domain = paths[0];
		this.path = paths[1];
		
		Validate.notNull(this.path);
	}

	@Override
	public String getDomain()
	{
		return this.domain;
	}

	@Override
	public String getPath()
	{
		return this.path;
	}

	@Override
	public byte[] getInputBytes()
	{
		return null;
	}

}