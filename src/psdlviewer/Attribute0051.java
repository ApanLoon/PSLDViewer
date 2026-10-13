package psdlviewer;
import psdlviewer.*;

/** BlockAttribute defining a texture. (Id + 0x100)
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Attribute0051
extends TextureAttribute
{
    public Attribute0051()
    {
	super();
	id = 0x0051;
    }

    public void setLists (VertexList  vertexList,
			  HeightList  heightList,
			  TextureList textureList)
    {
	list = textureList;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	if (list == null)
	{
	    list = Config.getTextureList();
	}
	ref = new TextureMapRef (
             (UShort.parseUShort (in).getCharValue() - 1 + 0x100),
	     list);
	return 1;
    }

    public void write (OutputStream out)
    throws IOException
    {
	writeID (out);
	UShort.write ((char)(ref.getIndex() - 0x100 + 1), out);
    }


}
