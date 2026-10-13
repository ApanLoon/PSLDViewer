package psdlviewer;
import psdlviewer.*;

/** A texture map image.
 *
 */

import java.io.File;
import java.io.Serializable;

import javax.swing.ImageIcon;

import java.awt.Image;

import java.util.Vector;

public class TextureMap
implements Serializable
{
    protected static Vector loaders = new Vector();

    protected File  file   = null;
    protected int   loader = -1;
    protected Image image  = null;

    protected String name;


    public TextureMap()
    {
	if (loaders.size() == 0)
	{
	    init();
	}
    }

    public TextureMap (String name_,
		       Vector path)
    {
	if (loaders.size() == 0)
	{
	    init();
	}

	name = name_;

	boolean done = false;
	int i = 0;
	while (i < loaders.size() && !done)
	{
	    TextureLoader l = (TextureLoader) loaders.elementAt (i);

	    int p = 0;
	    while (!done && p < path.size())
	    {
		String fileName =   path.elementAt (p).toString() + '/'
		                  + name + "." + l.getExtension();
		File f = new File (fileName);
		if (f.exists())
		{
		    file   = f;
		    loader = i;
		    done   = true;
		}
		else // Try to load first frame of animation
		{
		    fileName =   path.elementAt (p).toString() + '/'
			       + name + "-0001." + l.getExtension();
		    f = new File (fileName);
		    if (f.exists())
		    {
			file   = f;
			loader = i;
			done   = true;
		    }
		}
		p++;
	    }
	    i++;
	}
    }




    protected void init()
    {
	loaders.add (new TEX());
	loaders.add (new GIF());
	loaders.add (new JPG());
	loaders.add (new TGA());
    }





    /** Flushes the cached image.
     *
     *  The next call to getImage() will reload the image from disk.
     *
     */
    public void flush()
    {
	image = null;
    }



    public Image getImage()
    {
	if (image == null)
	{
	    if (file == null || !file.exists())
	    {
		image = TextureLoader.getNotFound();
	    }
	    else if (loader == -1)
	    {
		image = TextureLoader.getUnknown();
	    }
	    else
	    {
		TextureLoader l = (TextureLoader)loaders.elementAt (loader);
		image = l.loadImage (file.getAbsolutePath());
	    }
	}
	return image;
    }


    public String getShortName()
    {
	return name;
    }

    public String getName()
    {
	return name;
    }


    public String toString()
    {
	return getName();
    }
}

