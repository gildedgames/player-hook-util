package com.gildedgames.util.ui.util.events.slots;

import com.gildedgames.util.ui.common.GuiFrame;
import com.gildedgames.util.ui.event.GuiEvent;
import com.gildedgames.util.ui.graphics.Graphics2D;
import com.gildedgames.util.ui.input.ButtonState;
import com.gildedgames.util.ui.input.InputProvider;
import com.gildedgames.util.ui.input.MouseInputPool;
import com.gildedgames.util.ui.util.GuiCanvas;
import com.gildedgames.util.ui.util.events.DragBehavior;
import com.gildedgames.util.ui.util.factory.Factory;
import com.google.common.base.Function;

public class SlotStackFactory extends GuiEvent
{
	
	private Factory<? extends GuiFrame> iconFactory;
	
	private Function<GuiFrame, Object> dataFactory;

	public SlotStackFactory(Factory<? extends GuiFrame> iconFactory, Function<GuiFrame, Object> dataFactory)
	{
		this.iconFactory = iconFactory;
		this.dataFactory = dataFactory;
	}
	
	@Override
	public void draw(Graphics2D graphics, InputProvider input)
	{
		super.draw(graphics, input);
	}
	
	@Override
	public void onMouseInput(MouseInputPool pool, InputProvider input)
	{
		if (this.isActive(pool, input) && input.isHovered(this.getGui().dim()) && pool.has(ButtonState.PRESS))
		{
			GuiCanvas canvas = GuiCanvas.fetch("dragCanvas", false);

			if (canvas != null)
			{
				GuiFrame icon = this.iconFactory.create();
				
				SlotStack stack = new SlotStack(icon, this.dataFactory.apply(icon));
				
				stack.events().set("dragBehavior", new DragBehavior(), stack);
				
				stack.dim().mod().pos(input.getMouseX(), input.getMouseY()).flush();

				if (canvas.get("draggedObject") != null)
				{
					if (this.shouldRemoveDragged(stack))
					{
						canvas.remove("draggedObject");
					}
						
					return;
				}
				
				canvas.set("draggedObject", stack);
				
				this.onCreateDraggedState();
			}
		}
		
		super.onMouseInput(pool, input);
	}
	
	public boolean isActive(MouseInputPool pool, InputProvider input)
	{
		return true;
	}
	
	public boolean shouldRemoveDragged(SlotStack createdStack)
	{
		return true;
	}
	
	public void onCreateDraggedState()
	{
		
	}

	@Override
	public void initEvent()
	{
		
	}

}
