package com.ddm.video.process;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogConsumer implements OutputConsumer
{
	private static Log log = LogFactory.getLog(LogConsumer.class);

	private String name = null;

	public LogConsumer(String name)
	{
		this.name = name;
		if(this.name == null)
		{
			this.name = "";
		}
	}

	@Override
	public void consumeOutput(InputStream pInputStream) throws IOException
	{
		InputStreamReader isr = new InputStreamReader(pInputStream);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		while((line = br.readLine()) != null)
		{
			log.info("[" + name + "]: " + line);
		}
	}

}
