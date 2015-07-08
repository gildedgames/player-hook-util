package com.gildedgames.util.ui.util.transform;

import java.util.List;

import com.gildedgames.util.ui.common.Gui;
import com.gildedgames.util.ui.data.Dim2D;

public class GuiPositionerGrid implements GuiPositioner
{
	private final int paddingX, paddingY;

	public GuiPositionerGrid(int paddingX, int paddingY)
	{
		this.paddingX = paddingX;
		this.paddingY = paddingY;
	}

	@Override
	public List<Gui> positionList(List<Gui> guis, Dim2D listDimensions)
	{
		int currentX = 0;
		int currentY = 0;

		for (Gui view : guis)
		{
			view.modDim().pos(currentX, currentY).center(false).compile();

			currentX += view.getDim().width() + this.paddingX;
			if (currentX > listDimensions.width())
			{
				int maxHeight = view.getDim().height();
				for (Gui view1 : guis)
				{
					if (view1.getDim().y() == currentY)
					{
						maxHeight = Math.max(view1.getDim().height(), maxHeight);
					}
				}
				currentY += maxHeight + this.paddingY;
				currentX = 0;
			}
		}

		return guis;
	}
}