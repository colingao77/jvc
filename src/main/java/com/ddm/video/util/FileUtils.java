package com.ddm.video.util;

import java.io.File;
import java.io.FileFilter;

public class FileUtils
{
	public static void cleanDirectory(File directory, FileFilter filter)
	{
		if(!directory.exists() || !directory.isDirectory())
		{
			return;
		}
		File[] files = directory.listFiles(filter);
		if(files == null || files.length == 0)
		{
			return;
		}
		for(File file : files)
		{
			file.delete();
		}
	}
	
	public static class PrefixFilter implements FileFilter
	{
		private String prefix = null;
		public PrefixFilter(String prefix)
		{
			this.prefix = prefix;
		}
		public boolean accept(File pathname)
		{
			if(prefix == null || pathname.getName().startsWith(prefix))
			{
				return true;
			}
			return false;
		}
	}
}
