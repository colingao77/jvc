package com.ddm.video.process;

import java.io.IOException;
import java.io.InputStream;

public interface OutputConsumer
{
	public void consumeOutput(InputStream pInputStream) throws IOException;
}
