package com.gildedgames.util.tab.client.social;

import com.gildedgames.util.core.UtilCore;
import com.gildedgames.util.group.client.GuiGroup;
import com.gildedgames.util.tab.common.util.TabGeneric;
import com.gildedgames.util.ui.UiCore;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TabGroup extends TabGeneric
{
	private static final ResourceLocation TEXTURE_GROUP = new ResourceLocation(UtilCore.MOD_ID, "textures/gui/tab_icons/group.png");

	@Override
	public String getUnlocalizedName()
	{
		return "tab.group.name";
	}

	@Override
	public boolean isTabValid(GuiScreen gui)
	{
		return UiCore.locate().containsFrame(gui, GuiGroup.class);
	}

	@Override
	public void onOpen(EntityPlayer player)
	{
		UiCore.locate().open("", new GuiGroup(player));
	}

	@Override
	public void onClose(EntityPlayer player)
	{

	}

	@Override
	public Container getCurrentContainer(EntityPlayer player, World world, int posX, int posY, int posZ)
	{
		return null;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public boolean isRemembered()
	{
		return true;
	}

	@Override
	public ResourceLocation getIconTexture()
	{
		return TEXTURE_GROUP;
	}

}