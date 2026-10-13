package psdlviewer;
import psdlviewer.*;

/** Reference to a texture map.
 *
 */

import java.util.Vector;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;

public class TextureMapRef
implements Serializable
{
    TextureList list;
    int         index;


    public TextureMapRef (char index_)
    {
	index             = index_;
    }

    public TextureMapRef (int         index_,
			  TextureList list_)
    {
	index = index_;
	list = list_;
    }


    public String toString()
    {
	return Integer.toHexString (index);
    }



    public int getIndex()
    {
	return index;
    }

    public void setIndex (int i)
    {
	index = i;
    }

    public TextureList getList()
    {
	return list;
    }
    public void setList (TextureList v)
    {
	list = v;
    }


    public void write (OutputStream out)
    throws IOException
    {
	UShort.write ((char)(index + 1), out);
    }


    public TextureMap getTextureMap()
    {
	return list.textureAt (index);
    }

}
