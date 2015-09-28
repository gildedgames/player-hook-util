package com.gildedgames.util.core.gui.util.wrappers;

import net.minecraft.item.ItemStack;

import com.gildedgames.util.ui.common.GuiFrame;
import com.gildedgames.util.ui.data.Dim2D;
import com.gildedgames.util.ui.graphics.Graphics2D;
import com.gildedgames.util.ui.input.InputProvider;

public class MinecraftButtonItemStack extends GuiFrame
{

	protected MinecraftItemStackRender itemStackRender;
	
	public MinecraftButtonItemStack(ItemStack stack)
	{
		this(Dim2D.build().area(20, 20).flush(), stack);
	}

	public MinecraftButtonItemStack(Dim2D dim, ItemStack stack)
	{
		super(dim);

		this.itemStackRender = new MinecraftItemStackRender(stack);
	}
	
	@Override
	public void initContent(InputProvider input)
	{
		this.content().set("button", new MinecraftButton(Dim2D.flush(), ""));
		this.content().set("itemStackRender", this.itemStackRender);
		
		this.itemStackRender.modDim().center(true).x(this.getDim().width() / 2).y(this.getDim().height() / 2).addX(2).addY(2).flush();
	}
	
	@Override
	public void draw(Graphics2D graphics, InputProvider input)
	{
		this.content().get("button", MinecraftButton.class).modDim().buildWith(this).area().build().flush();
	}
	
	public ItemStack getItemStack()
	{
		return this.itemStackRender.getItemStack();
	}

}