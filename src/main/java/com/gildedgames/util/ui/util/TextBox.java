package com.gildedgames.util.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gildedgames.util.ui.common.GuiFrame;
import com.gildedgames.util.ui.data.Dim2D;
import com.gildedgames.util.ui.data.Pos2D;
import com.gildedgames.util.ui.input.InputProvider;

public class TextBox extends GuiFrame
{

	private Text[] text;

	private boolean centerFormat;

	public TextBox(Dim2D dim, boolean centerFormat, Text... text)
	{
		super(dim);
		
		this.text = text;
		this.centerFormat = centerFormat;
	}
	
	public void setText(Text... text)
	{
		this.text = text;
	}

	@Override
	public void initContent(InputProvider input)
	{
		int i = 0;
		int textHeight = 0;

		double halfWidth = this.getDim().width() / 2;
		
		for (Text t : this.text)
		{
			if (t.text == null || t.text.isEmpty())
			{
				continue;
			}
			
			final String[] strings = t.text.split("/n");

			final List<String> stringList = new ArrayList<String>(strings.length);
			Collections.addAll(stringList, strings);

			for (final String string : stringList)
			{
				final List<String> newStrings = t.font.splitStringsIntoArea(string, (int) (this.getDim().width() / t.scale));

				for (final String s : newStrings)
				{
					TextElement textElement;
					
					if (this.centerFormat)
					{
						textElement = new TextElement(new Text(s, t.drawingData.getColor(), t.scale, t.font), Pos2D.flush(halfWidth, textHeight), true);
					}
					else
					{
						textElement = new TextElement(new Text(s, t.drawingData.getColor(), t.scale, t.font), Pos2D.flush(0, textHeight), false);
					}
					
					this.content().set(String.valueOf(i), textElement);

					textHeight += 1.1f * t.scaledHeight();
					i++;
				}
			}
		}

		this.modDim().height(textHeight).flush();
		
		super.initContent(input);
	}

}
