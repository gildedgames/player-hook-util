package com.gildedgames.util.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gildedgames.util.core.ObjectFilter;
import com.gildedgames.util.ui.common.Gui;
import com.gildedgames.util.ui.common.GuiFrame;
import com.gildedgames.util.ui.data.Dim2D;
import com.gildedgames.util.ui.data.Dim2D.ModifierType;
import com.gildedgames.util.ui.data.Pos2D;
import com.gildedgames.util.ui.graphics.Graphics2D;
import com.gildedgames.util.ui.input.InputProvider;
import com.gildedgames.util.ui.util.factory.ContentFactory;
import com.gildedgames.util.ui.util.transform.GuiPositioner;
import com.gildedgames.util.ui.util.transform.GuiSorter;
import com.google.common.collect.ImmutableMap;

public class GuiCollection extends GuiFrame
{

	protected GuiPositioner positioner;

	protected GuiSorter sorter;

	protected List<ContentFactory> contentProviders = new ArrayList<ContentFactory>();

	public GuiCollection(GuiPositioner positioner, ContentFactory... contentProviders)
	{
		this(Pos2D.flush(), 0, positioner, contentProviders);
	}

	public GuiCollection(Pos2D pos, int width, GuiPositioner positioner, ContentFactory... contentProviders)
	{
		super(Dim2D.build().pos(pos).width(width).flush());

		this.positioner = positioner;

		this.contentProviders.addAll(Arrays.<ContentFactory> asList(contentProviders));
	}

	public void setPositioner(GuiPositioner positioner)
	{
		this.positioner = positioner;
	}

	public GuiPositioner getPositioner()
	{
		return this.positioner;
	}

	public void setSorter(GuiSorter sorter)
	{
		this.sorter = sorter;
	}

	public GuiSorter getSorter()
	{
		return this.sorter;
	}

	private void clearAndProvideContent()
	{
		this.events().clear(Gui.class);

		for (ContentFactory contentProvider : this.contentProviders)
		{
			if (contentProvider != null)
			{
				this.events().setAll(contentProvider.provideContent(ImmutableMap.copyOf(this.events().map()), this.getDim()));
			}
		}
	}

	private void positionContent(List<Gui> views)
	{
		this.positioner.positionList(views, this.getDim());

		float totalContentHeight = 0;

		for (Gui gui : views)
		{
			if (gui != null)
			{
				//gui.modDim().addModifier(this, ModifierType.POS).flush();

				totalContentHeight = Math.max(totalContentHeight, gui.getDim().y() + gui.getDim().height());
			}
		}

		this.modDim().height(totalContentHeight).flush();
	}

	private void sortContent()
	{
		List<Gui> filteredViews = ObjectFilter.getTypesFrom(this.events().elements(), Gui.class);
		List<Gui> sortedViews = this.sorter != null ? this.sorter.sortList(filteredViews) : filteredViews;

		for (Gui view : filteredViews)
		{
			view.setVisible(false);
		}

		for (Gui view : sortedViews)
		{
			view.setVisible(true);
		}
	}

	private void sortAndPositionContent()
	{
		this.positionContent(this.getSortedViews());
		this.sortContent();
	}

	private List<Gui> getSortedViews()
	{
		List<Gui> filteredViews = ObjectFilter.getTypesFrom(this.events().elements(), Gui.class);
		List<Gui> sortedViews = this.sorter != null ? this.sorter.sortList(filteredViews) : filteredViews;

		return sortedViews;
	}

	public void refresh()
	{
		this.clearAndProvideContent();
		this.sortAndPositionContent();
	}

	@Override
	public void initContent(InputProvider input)
	{
		this.refresh();

		//this.getDimensions().set(this.getListeners().getCombinedDimensions());
		
		super.initContent(input);
	}

	@Override
	public void draw(Graphics2D graphics, InputProvider input)
	{
		super.draw(graphics, input);
	}

}
