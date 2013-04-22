package com.ddm.video;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.im4java.core.Info;

import com.ddm.video.util.FileUtils;

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
	
	private String baseDir = "." + File.separator + "temp";
	private String srcImage = baseDir + File.separator + "rose.jpg";
	private String bkgImage = baseDir + File.separator + "background.jpg";
	private String dstImageDir = baseDir + File.separator + "tmp";
	
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
		
		ic.tmpDir(baseDir).srcImage(srcImage).bkgImage(bkgImage);
		ic.dstImage(dstImageDir + File.separator + "frame.png");
		ic.scale(30).rotate(0).offset(400, 60).alpha(30, 100);
		
		try
		{
			ic.create();
		}
		catch(Exception e)
		{
			log.error("ImageCreator create failed", e);
		}
	}
	
	public void test3()
	{
		try
		{
			Info srcImageInfo = new Info(srcImage);
			int srcW = srcImageInfo.getImageWidth(), srcH = srcImageInfo.getImageHeight();
			
			Info bkgImageInfo = new Info(bkgImage);
			int bkgW = bkgImageInfo.getImageWidth(), bkgH = bkgImageInfo.getImageHeight();
			
			int totalSeconds = 20, fps = 25, totalFrames = totalSeconds * fps;
			
			Updater offX = new Updater(10, 1, 10, (int)(bkgW - srcW * 0.3));
			Updater offY = new Updater(10, 1, 10, (int)(bkgH - srcH * 0.3));
			Updater rotation = new Updater(0, 1, -360, 360);
			Updater alpha = new Updater(100, -1, 0, 100);
			Updater scale = new Updater(30, 1, 20, 50);
	
			String dstImageName = null;
			for(int i = 0; i < totalFrames; i++)
			{
				ImageCreator ic = new ImageCreator();
				dstImageName = String.format("%sframe%05d.png", dstImageDir + File.separator, i);
				
				ic.tmpDir(dstImageDir).srcImage(srcImage).bkgImage(bkgImage).dstImage(dstImageName);
				ic.scale(scale.update()).rotate(rotation.update());
				ic.offset(offX.update(), offY.update()).alpha(alpha.update(), 100);
				
				ic.create();
			}
			
			VideoCreator vc = new VideoCreator();
			
			vc.srcImageDir(dstImageDir).srcImageNames("frame%05d.png");
			vc.dstVideoName(baseDir + File.separator + "output.mp4");
			vc.srcFPS(fps).width(bkgW).height(bkgH);
			vc.srcAudio(baseDir + File.separator + "Aitheme1.mp3");
			
			int exitVal = vc.create();
			
			log.info("exitVal=" + exitVal);
		}
		catch(Exception e)
		{
			log.error("test3 failed", e);
		}
		finally
		{
			// clean up
//			FileUtils.cleanDirectory(new File(dstImageDir), null);
		}
	}
	
	private static class Updater
	{
		private int val = 0;
		private int delta = 0;
		private int min = 0;
		private int max = 0;
		public Updater(int val, int delta, int min, int max)
		{
			this.val = val;
			this.delta = delta;
			this.min = min;
			this.max = max;
		}
		public int update()
		{
			val += delta;
			
			if(val < min)
			{
				val = min;
				delta *= -1;
			}
			if(val > max)
			{
				val = max;
				delta *= -1;
			}
			
			return val;
		}
	}
	
	public void clean()
	{
		
	}

	public static void main(String[] args)
	{
		log.info("Hello World!");
		
		App app = new App();
//		app.test1();
//		app.test2();
		app.test3();
	}
}
