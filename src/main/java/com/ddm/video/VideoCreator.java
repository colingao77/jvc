package com.ddm.video;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ddm.video.process.ProcessExecutor;

public class VideoCreator
{
	private static Log log = LogFactory.getLog(ProcessExecutor.class);
	
	private int srcFPS = -1;
	private String srcImageDir = null;
	private String srcImageNames = null;
	private String dstVideoName = null;
	private int width = -1;
	private int height = -1;
	private String srcAudio = null;
	
	public VideoCreator()
	{
		reset();
	}
	
	private void reset()
	{
		srcFPS = 25;
		srcImageDir = null;
		srcImageNames = "frame%05d.png";
		width = 480;
		height = 320;
		srcAudio = null;
	}
	
	public VideoCreator srcFPS(int srcFPS)
	{
		this.srcFPS = srcFPS;
		return this;
	}
	public VideoCreator srcImageDir(String srcImageDir)
	{
		this.srcImageDir = srcImageDir;
		return this;
	}
	public VideoCreator srcImageNames(String srcImageNames)
	{
		this.srcImageNames = srcImageNames;
		return this;
	}
	public VideoCreator dstVideoName(String dstVideoName)
	{
		this.dstVideoName = dstVideoName;
		return this;
	}
	public VideoCreator width(int width)
	{
		this.width = width;
		return this;
	}
	public VideoCreator height(int height)
	{
		this.height = height;
		return this;
	}
	public VideoCreator srcAudio(String srcAudio)
	{
		this.srcAudio = srcAudio;
		return this;
	}
	
	public int create()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ffmpeg -y -f image2 ");
		sb.append("-r ").append(srcFPS);
		sb.append(" -i ");
		
		if(srcImageDir != null)
		{
			sb.append(srcImageDir).append(File.separator);
		}
		sb.append(srcImageNames);
		
		//audio?
		if(srcAudio != null)
		{
			sb.append(" -i ").append(srcAudio);
			sb.append(" -shortest -acodec libfaac -ab 128k -ar 44100 -ac 2 ");
		}
		
		sb.append(" -vcodec libx264 -b:v 1200k -s ");
		sb.append(width).append("x").append(height);
		sb.append(" -r 25 ");
		sb.append(dstVideoName);
		
		int exitVal = 0;
		try
		{
			exitVal = ProcessExecutor.execute(sb.toString());
		}
		catch(Exception e)
		{
			log.error("Create Video Failed", e);
			
			exitVal = 49297;
		}

		return exitVal;
	}
}
