package psdlviewer;
import psdlviewer.*;

/** Class for my TEX image memory loader.
 *
 */

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;

import java.awt.color.ColorSpace;

import javax.swing.JLabel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.lang.Exception;

public class TEX
extends TextureLoader
{
    public String getExtension()
    {
	return "tex";
    }


    public static final int P8       = 1;
    public static final int PA8      = 14;
    public static final int P4_MC    = 15;
    public static final int PA4_MC   = 16;
    public static final int RGB888   = 17;
    public static final int RGBA8888 = 18;




    protected int    width;
    protected int    height;
    protected int    type;
    protected int    mips;
    protected int    unknown;
    protected int    bits;


    protected ColorModel cModel;






    public Image loadImage (String url)
    {
	Image image = null;
	try
	{
	    FileInputStream in = new FileInputStream (url);
	    readHeader (in);

	    boolean readImages = false;
	    boolean isIndexed  = false;
	    int indexSize      = 0;
	    boolean hasAlpha   = false;

	    switch (type)
	    {
	    case P8:
		readPalette (true, 256, in);
		readImages = true;
		isIndexed = true;
		indexSize = 8;
		hasAlpha = true;
		break;
	    case PA8:
		readPalette (true, 256, in);
		readImages = true;
		isIndexed = true;
		indexSize = 8;
		hasAlpha = true;
		break;
	    case P4_MC:
		readPalette (true, 16, in);
		readImages = true;
		isIndexed = true;
		indexSize = 4;
		hasAlpha = true;
		width /= 2;
		height /= 2;
		break;
	    case PA4_MC:
		readPalette (true, 16, in);
		readImages = true;
		isIndexed = true;
		indexSize = 4;
		hasAlpha = true;
		width /= 2;
		height /= 2;
		break;
	    case RGB888:
		isIndexed = false;
		readImages = true;
		hasAlpha = false;
		break;
	    case RGBA8888:
		isIndexed = false;
		readImages = true;
		hasAlpha = true;
		break;

	    default:
		readImages = false;
		image = getUnknown();
	    }
	    if (readImages)
	    {
		image = readImage (1, isIndexed, indexSize, hasAlpha, in);
	    }
	    in.close();
	}
	catch (IOException e)
	{
	    System.err.println ("Couldn't read file. (" + url +")");
	}

	return image;
    }



    protected void readHeader (FileInputStream in)
    throws IOException
    {
	width   = (int)(UShort.parseUShort (in).getCharValue());
	height  = (int)(UShort.parseUShort (in).getCharValue());
	type    = (int)(UShort.parseUShort (in).getCharValue());
	mips    = (int)(UShort.parseUShort (in).getCharValue());
//    mips |= 1 << LOD_VH;
	unknown = (int)(UShort.parseUShort (in).getCharValue());
	bits    = (int)ULong.parseULong (in);
    }



    /** Reads a palette from the given file and creates a color model for this
     *  texture.
     *
     *  @param hasAlpha If true, the palette contains an alpha channel
     *  @param nEntries Number of color entries to read into the palette
     *  @param in The stream to read from
     *
     */
    protected void readPalette (boolean         hasAlpha,
				int             nEntries,
				FileInputStream in)
    throws IOException
    {
	int nComponents = hasAlpha ? 4 : 3;
	byte[] buf = new byte[nComponents * nEntries];
	in.read (buf, 0, nComponents * nEntries);

	// Extract colour components to separate arrays:
	byte[] red   = new byte[nEntries];
	byte[] green = new byte[nEntries];
	byte[] blue  = new byte[nEntries];
	byte[] alpha = new byte[nEntries];
	for (int i = 0, j = 0;
	     i < nComponents * nEntries;
	     i += nComponents, j++)
	{
	    blue[j]  = buf[i];
	    green[j] = buf[i + 1];
	    red[j]   = buf[i + 2];
	    if (hasAlpha)
	    {
		alpha[j] = buf[i + 3];
	    }
	}

	// Create colour model:
	if (hasAlpha)
	{
	    cModel = new IndexColorModel (8,
					  nEntries,
					  red,
					  green,
					  blue,
					  alpha);
	}
	else
	{
	    cModel = new IndexColorModel (8,
					  nEntries,
					  red,
					  green,
					  blue);
	}
    }







    protected Image readImage (int             scale,
			       boolean         isIndexed,
			       int             indexSize,
			       boolean         hasAlpha,
			       FileInputStream in)
    throws IOException
    {
	if (isIndexed)
	{
	    return readIndexedImage (scale, indexSize, in);
	}
	else
	{
	    return readImage (scale, hasAlpha, in);
	}
    }




    /** Reads an indexed image and creates an Image object.
     *
     */
    protected Image readIndexedImage (int scale, 
				      int indexSize,
				      FileInputStream in)
    throws IOException
    {
	int w = width / scale;
	int h = height / scale;

	byte[] pix = new byte[w * h];
	for (int y = 0; y < h; y++)
	{
	    for (int x = 0; x < w; x++)
	    {
		int i = in.read();
		int mask = (1 << indexSize) - 1;
		for (int startBit = 0; startBit < 8; startBit += indexSize)
		{
		    int index = (i & mask) >> startBit;
		    pix[((h - y - 1) * w) + x] = (byte)(index);
		    mask = mask << indexSize;
		}
	    }
	}
	MemoryImageSource s = new MemoryImageSource (w,
						     h,
						     cModel,
						     pix,
						     0,
						     w);
	JLabel l = new JLabel();
	return l.createImage (s);
    }



    /** Reads a non-indexed image and creates an Image object.
     *
     */
    protected Image readImage (int             scale,
			       boolean         hasAlpha,
			       FileInputStream in)
    throws IOException
    {
	int w = width / scale;
	int h = height / scale;

	int[] pix = new int[w * h];
	for (int y = 0; y < h; y++)
	{
	    for (int x = 0; x < w; x++)
	    {
		int red   = in.read();
		int green = in.read();
		int blue  = in.read();
		int alpha = (hasAlpha ? in.read() : 255);

		pix[((h - y - 1) * w) + x] =   (alpha << 24)
		                             + (red   << 16)
		                             + (green <<  8)
		                             + (blue  <<  0);
	    }
	}
	MemoryImageSource s = new MemoryImageSource (w,
						     h,
						     pix,
						     0,
						     w);
	JLabel l = new JLabel();
	return l.createImage (s);
    }



/*
	    int width  = UShort.parseUShort (in).getCharValue();
	    int height = UShort.parseUShort (in).getCharValue();
	    int type   = UShort.parseUShort (in).getCharValue();

	    switch (type)
	    {
	    case 0x01: //(P8)
	    case 0x0e: //(PA8)
		image = indexed (width, height, in);
		break;

	    case 0x11: //(RGB888)
		image = rgb (width, height, in);
		break;

	    case 0x12: //(RGBA8888)
		image = rgba (width, height, in);
		break;

	    default:
		System.err.println ("Unsupported texture format. Type = "
				    + type + " " + url);
		break;
	    }

	    return image;

	}
	catch (IOException e)
	{
	    System.err.println ("Couldn't read texture. (" + url +")");
	}
	return null;
    }






    /** TODO: s_tun_top.tex appears to be pure black, the binary isn't though!
     *
     *   /
    protected Image indexed (int width,
			     int height,
			     InputStream in)
    throws IOException
    {
	int nScales = 1;

	int a1 = UShort.parseUShort (in).getCharValue();
	int a2 = UShort.parseUShort (in).getCharValue();
	int a3 = UShort.parseUShort (in).getCharValue();
	int a4 = UShort.parseUShort (in).getCharValue();

	if (   a1 == 7
	    || a1 == 4)
	{
	    nScales = 1; // TODO:  2;
	}


	byte[] buf = new byte[256 * 4];
	in.read (buf, 0, 256 * 4);

	byte[] red   = new byte[256];
	byte[] green = new byte[256];
	byte[] blue  = new byte[256];
	byte[] alpha = new byte[256];
	for (int i = 0, j = 0; i < 256 * 4; i += 4, j++)
	{
	    blue[j]  = buf[i];
	    green[j] = buf[i + 1];
	    red[j]   = buf[i + 2];
	    alpha[j] = buf[i + 3];
	}

	IndexColorModel cmap = new IndexColorModel (8,
						    256,
						    red,
						    green,
						    blue,
						    alpha);

	int totalHeight = (int)(height * ( 1.0 + 1.0 - (1.0 / (1 << (nScales - 1)))));

	byte[] pix = new byte[width * totalHeight];
	for (int scale = 1; scale <= nScales; scale++)
	{
	    for (int y = 0; y < height / scale; y++)
	    {
		for (int x = 0; x < width; x++)
		{
		    int i = 0;
		    if (x < (width / scale))
		    {
			i = in.read();
		    }

		    if (i == -1)
			System.err.println ("Read too much!");

		    pix[((height - y - 1) * width) + x] = (byte)(i & 0xff);
		}
	    }
	}

	MemoryImageSource s = new MemoryImageSource (width,
						     totalHeight,
						     cmap,
						     pix,
						     0,
						     width);
	JLabel l = new JLabel();
	return l.createImage (s);
    }





    protected Image rgb (int width,
			 int height,
			 InputStream in)
    throws IOException
    {
	int a1 = UShort.parseUShort (in).getCharValue();  // nComponents?
	int a2 = UShort.parseUShort (in).getCharValue();  // nBytes per component?
	int a3 = UShort.parseUShort (in).getCharValue();  // 8006 flags?
	int a4 = UShort.parseUShort (in).getCharValue();  // 0000 flags?

	int[] pix = new int[width * height];
	int index = 0;
	for (int y = 0; y < height; y++)
	{
	    for (int x = 0; x < width; x++)
	    {
		int red   = in.read();
		int green = in.read();
		int blue  = in.read();
		int alpha = 255;

		pix[((height - y - 1) * width) + x] =   (alpha << 24)
		    + (red   << 16)
		    + (green <<  8)
		    + (blue  <<  0);
	    }
	}
	MemoryImageSource s = new MemoryImageSource (width,
						     height,
						     pix,
						     0,
						     width);




	JLabel l = new JLabel();
	return l.createImage (s);
    }





    protected Image rgba (int width,
			  int height,
			  InputStream in)
    throws IOException
    {
	int nScales = 1;

	int a1 = UShort.parseUShort (in).getCharValue();  // ?
	int a2 = UShort.parseUShort (in).getCharValue();  // ?
	int a3 = UShort.parseUShort (in).getCharValue();
	int a4 = UShort.parseUShort (in).getCharValue();  // ?


	if (a1 == 7)
	{
	    nScales = 2;
	}



	int totalHeight = (int)(height * ( 1.0 + 1.0 - (1.0 / (1 << (nScales - 1)))));

	int[] pix = new int[width * totalHeight];
	int index = 0;

	for (int scale = 1; scale <= nScales; scale++)
	{
	    for (int y = 0; y < height / scale; y++)
	    {
		for (int x = 0; x < width; x++)
		{
		    int red   = 0;
		    int green = 0;
		    int blue  = 0;
		    int alpha = 255;

		    if (x < (width / scale))
		    {
			red   = in.read();
			green = in.read();
			blue  = in.read();
			alpha = in.read();
		    }

		    pix[((height - y - 1) * width) + x] =   (alpha << 24)
			           + (red   << 16)
			           + (green <<  8)
			           + (blue  <<  0);
		}
	    }
	}

	MemoryImageSource s = new MemoryImageSource (width,
						     totalHeight,
						     pix,
						     0,
						     width);
	JLabel l = new JLabel();
	return l.createImage (s);
    }
*/

}
