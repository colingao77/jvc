package com.ddm.video.process;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamGobbler extends Thread
{
	private static Log log = LogFactory.getLog(StreamGobbler.class);
	
	private InputStream is = null;
	private OutputConsumer consumer = null;
	
	public StreamGobbler(InputStream is, OutputConsumer consumer)
	{
		this.is = is;
		this.consumer = consumer;
	}

	public void run()
	{
		try
		{
			consumer.consumeOutput(is);
			is.close();
		}
		catch(IOException e)
		{
			log.error("consumeOutput error", e);
		}
	}
}
