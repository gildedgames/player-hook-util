package com.gildedgames.util.ui.data;

import java.util.ArrayList;
import java.util.List;

public abstract class Dim2DGetter<S> extends Dim2DSeeker<S>
{
	
	public Dim2DGetter()
	{
		
	}
	
	public Dim2DGetter(S seekFrom)
	{
		this.seekFrom = seekFrom;
	}
	
	@Override
	public void setDim(Dim2D dim)
	{
		
	}

}
