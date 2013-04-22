package com.ddm.video;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.ddm.video.util.FileUtils;

public class ImageCreator
{
	private static Log log = LogFactory.getLog(ImageCreator.class);

	private static ConvertCmd convert = new ConvertCmd();
	private static CompositeCmd composite = new CompositeCmd();

	// App-level property
	private String tmpDir = null;

	// convert: source images
	private String srcImage = null;

	private int rotate = 0;
	private int scale = 100;

	// composite: with background image
	private String bkgImage = null;

	private int offX = 0;
	private int offY = 0;

	private int srcDissolvePercent = 100;
	private int dstDissolvePercent = 100;

	// result image
	private String dstImage = null;

	public ImageCreator()
	{
		reset();
	}

	public void reset()
	{
		tmpDir = "." + File.separator + "tmp";

		srcImage = null;
		rotate = 0;
		scale = 100;

		bkgImage = null;
		offX = 0;
		offY = 0;
		srcDissolvePercent = 100;
		dstDissolvePercent = 100;

		dstImage = null;
	}

	public ImageCreator tmpDir(String tmpDir)
	{
		this.tmpDir = tmpDir;
		return this;
	}

	public ImageCreator srcImage(String srcImage)
	{
		this.srcImage = srcImage;
		return this;
	}

	public ImageCreator bkgImage(String bkgImage)
	{
		this.bkgImage = bkgImage;
		return this;
	}

	public ImageCreator dstImage(String dstImage)
	{
		this.dstImage = dstImage;
		return this;
	}

	public ImageCreator rotate(int rotate)
	{
		this.rotate = rotate;
		return this;
	}

	public ImageCreator scale(int scale)
	{
		this.scale = scale;
		return this;
	}

	public ImageCreator offset(int x, int y)
	{
		this.offX = x;
		this.offY = y;
		return this;
	}

	public ImageCreator alpha(int src, int dst)
	{
		this.srcDissolvePercent = src;
		this.dstDissolvePercent = dst;
		return this;
	}

	public int create() throws Exception
	{
		if(srcImage == null || bkgImage == null || dstImage == null)
		{
			throw new IllegalArgumentException("srcImage=" + srcImage + ", bkgImage=" + bkgImage
				+ ", dstImage=" + bkgImage);
		}

		String processedImage = null;
		try
		{
			processedImage = convert();
			composite(processedImage);
			if(log.isDebugEnabled())
			{
				log.debug("Create Image [" + dstImage + "] Done.");
			}
		}
		finally
		{
			// clean up
//			FileUtils.cleanDirectory(new File(tmpDir), new FileUtils.PrefixFilter("tmp"));
		}

		return 0;
	}

	private String convert() throws IOException, InterruptedException, IM4JavaException
	{
		IMOperation op = new IMOperation();
		if(rotate != 0)
		{
			op.rotate(new Double(rotate));
			op.background("transparent");
		}
		if(scale != 100)
		{
			op.addRawArgs("-scale", String.format("%d%c", scale, '%'));
		}
		if(op.getCmdArgs().isEmpty())
		{
			return srcImage;
		}

		String tmpFile = getTmpFile();
		op.addImage(srcImage, tmpFile);

		if(log.isDebugEnabled())
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			convert.createScript(pw, op, new Properties());
			log.debug("[convert]: " + sw.toString());
		}

		convert.run(op);

		return tmpFile;
	}

	private void composite(String inputImage) throws IOException, InterruptedException,
		IM4JavaException
	{
		IMOperation op = new IMOperation();

		if(offX != 0 && offY != 0)
		{
			op.geometry(null, null, offX, offY);
		}
		op.type("TrueColorMatte");
		op.depth(8);
		if(srcDissolvePercent != 100 || dstDissolvePercent != 100)
		{
			op.addRawArgs("-dissolve",
				String.format("%dx%d", srcDissolvePercent, dstDissolvePercent));
		}

		op.addImage(inputImage, bkgImage, dstImage);

		if(log.isDebugEnabled())
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			composite.createScript(pw, op, new Properties());
			log.debug("[composite]: " + sw.toString());
		}

		composite.run(op);
	}

	/**
	 * Create a temporary file.
	 */

	private String getTmpFile() throws IOException
	{
		return tmpDir + File.separator + "tmp1.png";
	}
}
