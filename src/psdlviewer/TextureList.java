package psdlviewer;
import psdlviewer.*;

public class TextureList
extends java.util.Vector
{

    public TextureMap textureAt (int i)
    {
	return (TextureMap)super.elementAt (i);
    }


    /** Flushes the cached images.
     *
     */
    public void flush()
    {
	for (int i = 0; i < size(); i++)
	{
	    TextureMap tm = textureAt (i);
	    tm.flush();
	}
    }
}
