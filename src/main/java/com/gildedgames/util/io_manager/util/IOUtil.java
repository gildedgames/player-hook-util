package com.gildedgames.util.io_manager.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gildedgames.util.io_manager.IOCore;
import com.gildedgames.util.io_manager.factory.IOBridge;
import com.gildedgames.util.io_manager.factory.IOFactory;
import com.gildedgames.util.io_manager.io.IO;

public class IOUtil
{
	
	public static <I, O> void setIOList(String key, List<? extends IO<I, O>> list, IOFactory<I, O> factory, O output)
	{
		IOBridge outputBridge = factory.createOutputBridge(output);
		
		outputBridge.setInteger(key + "listSize", list.size());
		
		for (int count = 0; count < list.size(); count++)
		{
			IO<I, O> obj = list.get(count);
			
			IOCore.io().set(key + "IO" + count, output, factory, obj);
		}
	}
	
	public static <I, O> List<? extends IO<I, O>> getIOList(String key, IOFactory<I, O> factory, I input)
	{
		IOBridge inputBridge = factory.createInputBridge(input);
		
		int listSize = inputBridge.getInteger(key + "listSize");
		
		List<IO<I, O>> list = new ArrayList<IO<I, O>>(listSize);
		
		for (int count = 0; count < listSize; count++)
		{
			IO<I, O> obj = IOCore.io().get(key + "IO" + count, input, factory);
			
			list.add(obj);
		}
		
		return list;
	}

	public static <I, O> void setIOMap(String key, Map<? extends IO<I, O>, ? extends IO<I, O>> map, IOFactory<I, O> factory, O output)
	{
		IOUtil.setIOList(key + "keys", new ArrayList<IO<I, O>>(map.keySet()), factory, output);
		IOUtil.setIOList(key + "values", new ArrayList<IO<I, O>>(map.values()), factory, output);
	}
	
	public static <I, O> Map<? extends IO<I, O>, ? extends IO<I, O>> getIOMap(String key, IOFactory<I, O> factory, I input)
	{
		Map<IO<I, O>, IO<I, O>> map = new HashMap<IO<I, O>, IO<I, O>>();
		
		Iterator<? extends IO<I, O>> keys = IOUtil.getIOList(key + "keys", factory, input).iterator();
		Iterator<? extends IO<I, O>> values = IOUtil.getIOList(key + "values", factory, input).iterator();
		
		while (keys.hasNext() && values.hasNext())
		{
		    map.put(keys.next(), values.next());
		}
		
		return map;
	}

	public static File[] getFoldersInDirectory(File directory)
	{
		if (!directory.exists())
		{
			directory.mkdirs();
		}

		final File[] files = directory.listFiles();
		final ArrayList<File> fileList = new ArrayList<File>();

		if (files != null)
		{
			for (final File file : files)
			{
				if (file.isDirectory())
				{
					fileList.add(file);
				}
			}
		}

		return fileList.toArray(new File[fileList.size()]);
	}

	public static List<File> getFilesWithExtension(File directory, List<String> extensions)
	{
		final List<File> files = new ArrayList<File>();
		for (final String string : extensions)
		{
			final File[] fileArray = getFilesWithExtension(directory, string);

			if (fileArray != null)
			{
				files.addAll(Arrays.asList(fileArray));
			}
		}
		return files;
	}

	public static File[] getFilesWithExtension(File directory, String extension)
	{
		if (!directory.exists())
		{
			directory.mkdirs();
		}

		return directory.listFiles(new FilenameFilterExtension(extension));
	}

	private static class FilenameFilterExtension implements FilenameFilter
	{

		public String extension;

		public FilenameFilterExtension(String extension)
		{
			super();
			this.extension = extension;
		}

		@Override
		public boolean accept(File dir, String name)
		{
			return name.endsWith("." + this.extension);
		}

	}
	
}