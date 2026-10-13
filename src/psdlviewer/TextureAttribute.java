package psdlviewer;
import psdlviewer.*;

/** BlockAttribute defining a texture.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class TextureAttribute
extends BlockAttribute
{
    public TextureMapRef ref;

    protected TextureList list;

    public TextureAttribute()
    {
	super();
	id = 0x0050;
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
             (UShort.parseUShort (in).getCharValue() - 1),
	     list);
	return 1;
    }

    public void write (OutputStream out)
    throws IOException
    {
	UShort.write (TEXTURE_ATTRIBUTE, out);
	ref.write (out);
    }



    /** Adds offsets to table references in this attribute.
     *
     *  Adds the texture offset to the texture references.
     *
     *  Override this in subclasses to correctly add to all references.
     *
     */
    public void addOffsets (int vertexOffset,
			    int heightOffset,
			    int textureOffset,
			    int blockOffset)
    {
	ref.setIndex ((char)(textureOffset + ref.getIndex()));
    }



    public String toString()
    {
	String reply = "";
	reply += UShort.toHexString (id) + " texture: " + ref;
	return reply;
    }


    public long getLength()
    {
	return 1 + 1;
    }


}
