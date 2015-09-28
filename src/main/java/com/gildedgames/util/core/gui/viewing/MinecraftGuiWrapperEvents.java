package com.gildedgames.util.core.gui.viewing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.gildedgames.util.core.UtilCore;
import com.gildedgames.util.ui.UiCore;
import com.gildedgames.util.ui.UiServices.Overlay;
import com.gildedgames.util.ui.UiServices.RenderOrder;
import com.gildedgames.util.ui.common.GuiFrame;
import com.gildedgames.util.ui.common.GuiViewer;
import com.gildedgames.util.ui.data.TickInfo;
import com.gildedgames.util.ui.input.ButtonState;
import com.gildedgames.util.ui.input.InputProvider;
import com.gildedgames.util.ui.input.KeyboardInput;
import com.gildedgames.util.ui.input.KeyboardInputPool;
import com.gildedgames.util.ui.input.MouseButton;
import com.gildedgames.util.ui.input.MouseInput;
import com.gildedgames.util.ui.input.MouseInputPool;
import com.gildedgames.util.ui.input.MouseMotion;

public class MinecraftGuiWrapperEvents implements TickInfo
{

	protected Minecraft mc = Minecraft.getMinecraft();
	
	protected int ticks;
	
	private double width, height, scaleFactor, touchValue, eventButton;
	
	private long lastMouseEvent;
	
	private boolean worldStarted;
	
    public void handleInput()
    {
        if (Mouse.isCreated())
        {
        	for (Overlay overlay : UiCore.locate().overlays())
    		{
    			GuiFrame frame = overlay.getFrame();
    			GuiViewer viewer = overlay.getViewer();
				
				this.handleMouseInput(frame, viewer);
			}

        }

        if (Keyboard.isCreated())
        {
        	for (Overlay overlay : UiCore.locate().overlays())
    		{
    			GuiFrame frame = overlay.getFrame();
    			GuiViewer viewer = overlay.getViewer();

				this.handleKeyboardInput(frame, viewer);
			}
        }
    }
	
    public void handleMouseInput(GuiFrame frame, GuiViewer viewer)
    {
    	double i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        double j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int button = Mouse.getEventButton();

        if (Mouse.getEventButtonState())
        {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0)
            {
                return;
            }

            this.eventButton = button;
            this.lastMouseEvent = Minecraft.getSystemTime();
            
            MouseInputPool pool = new MouseInputPool(new MouseInput(MouseButton.fromIndex(button), ButtonState.PRESSED), new MouseInput(MouseButton.fromIndex(button), ButtonState.PRESSED));

    		frame.onMouseInput(pool, viewer.getInputProvider());
        }
        else if (button != -1)
        {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0)
            {
                return;
            }

            this.eventButton = -1;
            
            MouseInputPool pool = new MouseInputPool(new MouseInput(MouseButton.fromIndex(button), ButtonState.RELEASED));

    		frame.onMouseInput(pool, viewer.getInputProvider());
        }
        else if (this.eventButton != -1 && this.lastMouseEvent > 0L)
        {
            long l = Minecraft.getSystemTime() - this.lastMouseEvent;
            
            MouseInputPool pool = new MouseInputPool(new MouseInput(MouseButton.fromIndex(button), ButtonState.PRESSED, MouseMotion.MOVING));

    		frame.onMouseInput(pool, viewer.getInputProvider());
        }
        
        int scrollDifference = Mouse.getDWheel();

		if (scrollDifference != 0)
		{
			frame.onMouseScroll(scrollDifference, viewer.getInputProvider());
		}
    }

    /**
     * Handles keyboard input.
     */
    public void handleKeyboardInput(GuiFrame frame, GuiViewer viewer)
    {
        if (Keyboard.getEventKeyState())
        {
        	KeyboardInputPool pool = new KeyboardInputPool(new KeyboardInput(Keyboard.getEventKey(), ButtonState.PRESSED));

    		frame.onKeyboardInput(pool, viewer.getInputProvider());
        }
    }

	@SubscribeEvent
	public void tickStartClient(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			this.ticks++;
			
			if (this.mc.currentScreen instanceof MinecraftGuiWrapper)
			{
				MinecraftGuiWrapper viewer = (MinecraftGuiWrapper)this.mc.currentScreen;
				
				viewer.tick(this);
			}

			for (Overlay overlay : UiCore.locate().overlays())
			{
				GuiFrame frame = overlay.getFrame();
				GuiViewer viewer = overlay.getViewer();
				RenderOrder renderOrder = overlay.getRenderOrder();
				
				InputProvider input = viewer.getInputProvider();
				
				if (this.worldStarted || this.width != input.getScreenWidth() || this.height != input.getScreenHeight() || this.scaleFactor != input.getScaleFactor())
				{
					viewer.getInputProvider().refreshResolution();
					
					this.width = input.getScreenWidth();
					this.height = input.getScreenHeight();
					this.scaleFactor = input.getScaleFactor();
					
					frame.onResolutionChange(input);
				}
				
				frame.tick(viewer.getTickInfo(), viewer.getInputProvider());
			}
			
			this.handleInput();
		}
	}
	
	private void renderOverlays(RenderOrder desiredOrder)
	{
		if (!this.worldStarted)
		{
			this.worldStarted = true;
			
			UiCore.locate().createRegisteredOverlays();
		}
		
		for (Overlay overlay : UiCore.locate().overlays())
		{
			GuiFrame frame = overlay.getFrame();
			GuiViewer viewer = overlay.getViewer();
			RenderOrder renderOrder = overlay.getRenderOrder();
			
			if (renderOrder == desiredOrder)
			{
				viewer.getInputProvider().refreshResolution();
				
				frame.draw(viewer.getGraphics(), viewer.getInputProvider());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpen(GuiOpenEvent event)
	{
		if (event.gui instanceof GuiMainMenu)
        {
			this.worldStarted = false;
			
			UiCore.locate().destroyRegisteredOverlays();
        }
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderGameOverlayPre(RenderGameOverlayEvent.Pre event)
	{
		this.renderOverlays(RenderOrder.PRE);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderGameOverlay(RenderGameOverlayEvent event)
	{
		this.renderOverlays(RenderOrder.NORMAL);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderGameOverlayPost(RenderGameOverlayEvent.Post event)
	{
		this.renderOverlays(RenderOrder.POST);
	}

	@Override
	public int getTotalTicks()
	{
		return this.ticks;
	}

}