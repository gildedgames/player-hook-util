package com.gildedgames.util.core.gui.util.wrappers;

import java.util.concurrent.Callable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.gildedgames.util.ui.common.GuiFrame;
import com.gildedgames.util.ui.data.rect.Dim2D;
import com.gildedgames.util.ui.data.rect.Rect;
import com.gildedgames.util.ui.graphics.Graphics2D;
import com.gildedgames.util.ui.input.InputProvider;

public class MinecraftItemStackRender extends GuiFrame
{
	
	protected final static Minecraft MC = Minecraft.getMinecraft();
	
	protected ItemStack stack;

	public MinecraftItemStackRender(ItemStack stack)
	{
		this(Dim2D.flush(), stack);
	}
	
	public MinecraftItemStackRender(Rect dim, ItemStack stack)
	{
		super(dim.rebuild().area(20, 20).flush());
		
		this.stack = stack;
	}
	
	@Override
	public void draw(Graphics2D graphics, InputProvider input)
	{
		super.draw(graphics, input);
		
		if (this.stack != null)
		{
			GL11.glPushMatrix();

			RenderHelper.enableGUIStandardItemLighting();
			
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_LIGHTING);

			this.renderItemAndEffectIntoGUI(this.stack, this.dim().x() + 2.0D, this.dim().y() + 2.0D);

			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glPopMatrix();
		}
	}
	
	public ItemStack getItemStack()
	{
		return this.stack;
	}
	
    public void renderItemIntoGUI(ItemStack stack, double x, double y)
    {
        IBakedModel ibakedmodel = MC.getRenderItem().getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        MC.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        MC.renderEngine.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GUI);
        MC.getRenderItem().renderItem(stack, ibakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        MC.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        MC.renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(double xPosition, double yPosition, boolean isGui3d)
    {
        GlStateManager.translate(xPosition, yPosition, 150.0F);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, -1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        if (isGui3d)
        {
            GlStateManager.scale(40.0F, 40.0F, 40.0F);
            GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableLighting();
        }
        else
        {
            GlStateManager.scale(64.0F, 64.0F, 64.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
        }
    }

    public void renderItemAndEffectIntoGUI(final ItemStack stack, double xPosition, double yPosition)
    {
        if (stack != null)
        {
            try
            {
                this.renderItemIntoGUI(stack, xPosition, yPosition);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addCrashSectionCallable("Item Type", new Callable()
                {
                    private static final String __OBFID = "CL_00001004";
                    public String call()
                    {
                        return String.valueOf(stack.getItem());
                    }
                });
                crashreportcategory.addCrashSectionCallable("Item Aux", new Callable()
                {
                    private static final String __OBFID = "CL_00001005";
                    public String call()
                    {
                        return String.valueOf(stack.getMetadata());
                    }
                });
                crashreportcategory.addCrashSectionCallable("Item NBT", new Callable()
                {
                    private static final String __OBFID = "CL_00001006";
                    public String call()
                    {
                        return String.valueOf(stack.getTagCompound());
                    }
                });
                crashreportcategory.addCrashSectionCallable("Item Foil", new Callable()
                {
                    private static final String __OBFID = "CL_00001007";
                    public String call()
                    {
                        return String.valueOf(stack.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

}
