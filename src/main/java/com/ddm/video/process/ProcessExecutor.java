package com.ddm.video.process;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessExecutor
{
	private static Log log = LogFactory.getLog(ProcessExecutor.class);

	public static int execute(String cmd, OutputConsumer output, OutputConsumer error) throws IOException,
		InterruptedException
	{
		log.info(String.format("start cmd: [%s], output=%s, error=%s.", cmd, output, error));

		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(cmd);

		// stderr stream
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), error);
		// stdout stream
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), output);

		// kick them off
		errorGobbler.start();
		outputGobbler.start();

		// wait stream threads to die
		errorGobbler.join();
		outputGobbler.join();

		int exitVal = proc.waitFor();

		log.info(String.format("end cmd: [%s], exitVal=%d.", cmd, exitVal));

		return exitVal;
	}

	public static int execute(String cmd) throws IOException, InterruptedException
	{
		OutputConsumer stdout = new LogConsumer("out");
		OutputConsumer stderr = new LogConsumer("err");
		return execute(cmd, stdout, stderr);
	}
}
