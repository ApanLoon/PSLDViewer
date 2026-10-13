package psdlviewer;
import psdlviewer.*;

import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import com.sun.j3d.utils.image.TextureLoader;

import java.util.Hashtable;
import java.util.Vector;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class TextureStore
extends Hashtable
{
    public void flush()
    {
	clear();
    }

    public Texture2D getTexture (TextureMapRef tr)
    {
	TextureMap tm = tr.getTextureMap();
	if (tm == null)
	{
	    return null;
	}
	Texture2D t = (Texture2D)get (tm.getShortName());
	if (t == null)
	{
	    Image image = tm.getImage();
	    if (image == null)
	    {
		return null;
	    }

	    t = createTexture2D (image);
	    put (tm.getShortName(), t);
	}
	return t;
    }




    public Texture2D getTexture (String name, Vector path)
    {
	Texture2D t = (Texture2D)get (name);
	if (t == null)
	{
	    TextureMap tm = new TextureMap (name, path);

	    Image image = tm.getImage();
	    if (image == null)
	    {
		return null;
	    }
	    t = createTexture2D (image);
	    put (name, t);
	}
	return t;
    }














    protected Texture2D createTexture2D (Image image)
    {
	ImageComponent2D ic = getScaledImage (image);
	ic.setCapability (ImageComponent2D.ALLOW_FORMAT_READ);
	ic.setCapability (ImageComponent2D.ALLOW_IMAGE_READ);
	ic.setCapability (ImageComponent2D.ALLOW_SIZE_READ);


	Texture2D t = new Texture2D (Texture.BASE_LEVEL,
				     Texture.RGBA,
				     ic.getWidth(),
				     ic.getHeight());
	t.setCapability (Texture2D.ALLOW_BOUNDARY_COLOR_READ);
	t.setCapability (Texture2D.ALLOW_BOUNDARY_MODE_READ);
	t.setCapability (Texture2D.ALLOW_ENABLE_READ);
	t.setCapability (Texture2D.ALLOW_FILTER_READ);
	t.setCapability (Texture2D.ALLOW_FORMAT_READ);
	t.setCapability (Texture2D.ALLOW_IMAGE_READ);
	t.setCapability (Texture2D.ALLOW_MIPMAP_MODE_READ);
	t.setCapability (Texture2D.ALLOW_SIZE_READ);
	t.setImage (0, ic);
	return t;
    }



    protected ImageComponent2D getScaledImage (Image image)
    {
	TextureLoader tl = new TextureLoader (image, null);
	ImageComponent2D ic = tl.getImage();
	BufferedImage im = ic.getImage();
	int w = im.getWidth();
	int h = im.getHeight();
	int newW = power (w);
	int newH = power (h);
	if (w != newW || h != newH)
	{
	    AffineTransformOp op = new AffineTransformOp (
					 AffineTransform.getScaleInstance (
					      (double)newW / w,
					      (double)newH / h), null); 

	    BufferedImage newImage = op.filter(im, null); 
	    ic = new ImageComponent2D (ic.getFormat(), newImage);
	}
	return ic;
    }


    protected int power(int n)
    {
	int i = 1;
	while (i < n)
	{
	    i = i << 1;
	}
	return i;
    }
}
