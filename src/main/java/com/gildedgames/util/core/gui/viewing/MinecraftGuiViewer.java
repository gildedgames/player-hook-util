package com.gildedgames.util.core.gui.viewing;

import net.minecraft.client.Minecraft;

import com.gildedgames.util.modules.ui.common.GuiFrame;
import com.gildedgames.util.modules.ui.common.GuiViewer;
import com.gildedgames.util.modules.ui.data.TickInfo;
import com.gildedgames.util.modules.ui.graphics.Graphics2D;
import com.gildedgames.util.modules.ui.input.InputProvider;

public class MinecraftGuiViewer implements GuiViewer
{
	
	private final static MinecraftGuiViewer INSTANCE = new MinecraftGuiViewer();
	
	private final InputProvider Input = new MinecraftInputProvider(Minecraft.getMinecraft());
	
	private final TickInfo TickInfo = new MinecraftGuiWrapperEvents();
	
	private final Graphics2D Graphics = new MinecraftGraphics2D(Minecraft.getMinecraft());
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private MinecraftGuiViewer()
	{
		
	}
	
	public static MinecraftGuiViewer instance()
	{
		return MinecraftGuiViewer.INSTANCE;
	}

	@Override
	public void open(GuiFrame frame)
	{
		this.mc.displayGuiScreen(new MinecraftGuiWrapper(frame));
	}

	@Override
	public void close(GuiFrame frame)
	{
		this.mc.displayGuiScreen(null);
	}

	@Override
	public InputProvider getInputProvider()
	{
		return MinecraftGuiViewer.INSTANCE.Input;
	}
	
	@Override
	public TickInfo getTickInfo()
	{
		return MinecraftGuiViewer.INSTANCE.TickInfo;
	}
	
	@Override
	public Graphics2D getGraphics()
	{
		return MinecraftGuiViewer.INSTANCE.Graphics;
	}

}
