package psdlviewer;
import psdlviewer.*;

/** Base class for my image memory loaders.
 *
 */


import java.lang.ClassLoader;
import java.awt.Image;
import javax.swing.ImageIcon;


public class TextureLoader
{
    protected static Image notFound = null;
    protected static Image unknown  = null;


    public String getExtension()
    {
	return null;
    }

    public Image loadImage (String url)
    {
	return null;
    }

    public static Image getNotFound()
    {
	if (notFound == null)
	{
	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    notFound = new ImageIcon (
		      cl.getResource ("icons/ImageNotFound.png")).getImage();
	}
	return notFound;
    }


    public static Image getUnknown()
    {
	if (unknown == null)
	{
	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    unknown = new ImageIcon (
		      cl.getResource ("icons/ImageUnknown.png")).getImage();
	}
	return unknown;
    }

}
