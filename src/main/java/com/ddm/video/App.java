package com.ddm.video;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * Hello world!
 * 
 */
public class App
{
	static
	{		
		PropertyConfigurator.configure(App.class.getResource("/commons-logging.properties"));
	}

	private static Log log = LogFactory.getLog(App.class);
	
	public void test1()
	{
		VideoCreator vc = new VideoCreator();
		
		vc.srcFPS(30).srcImageDir("E:\\repo\\im4jtest\\images\\test\\temp1");
		vc.srcImageNames("frame%04d.png").dstVideoName("temp\\output.mp4");
		vc.width(480).height(320);
		
		int exitVal = vc.create();
		
		log.info("exitVal=" + exitVal);
	}
	
	public void test2()
	{
		ImageCreator ic = new ImageCreator();
		String baseDir = "E:\\repo\\im4jtest\\images\\test";
		ic.tmpDir(baseDir);
		ic.srcImage(baseDir + File.separator + "rose2.jpg");
		ic.bkgImage(baseDir + File.separator + "bkg-green-gradient.jpg");
		ic.dstImage(baseDir + File.separator + "newtest" + File.separator + "frame.png");
		
		ic.scale(30).rotate(260).offset(400, 60).alpha(20, 100);
		
		try
		{
			ic.create();
		}
		catch(Exception e)
		{
			log.error("ImageCreator create failed", e);
		}
		
	}

	public static void main(String[] args)
	{
		log.info("Hello World!");
		
		App app = new App();
//		app.test1();
		app.test2();
	}
}
