package psdlviewer;
import psdlviewer.*;

/** Divided road strip
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0040
extends RoadAttribute
{
    public static final int TYPE_INVISIBLE             = 0x00;
    public static final int TYPE_FLAT                  = 0x01;
    public static final int TYPE_FLATELEVATED          = 0x02;
    public static final int TYPE_WEDGE                 = 0x03;



//    public static final int FLAT_DIVIDER               = 0x01;
//    public static final int ELEVATED_DIVIDER           = 0x02;
    public static final int UNKNOWN0                   = 0x04;
    public static final int UNKNOWN1                   = 0x08;
    public static final int UNKNOWN2                   = 0x10;
    public static final int UNKNOWN3                   = 0x20;
    public static final int CLOSE_START                = 0x40;
    public static final int CLOSE_END                  = 0x80;

    TextureMapRef tRef = new TextureMapRef ((char)0);
    int type;
    int flags;
    char height;

    public Attribute0040()
    {
	super();
	id = 0x0040;
    }


    public int nSections()
    {
	return vertexrefs.size() / 6;
    }

    

    public long read (InputStream in, long maxLength)
    throws IOException
    {
	long ushortsRead = 0;
	if (id == 0x0040)
	{
	    nSections = UShort.parseUShort (in).getCharValue();
	    ushortsRead++;
	}

	flags = in.read();
	type = flags & 0x3;
	flags &= ~0x3;

	tRef = new TextureMapRef (in.read() - 1,
				  Config.getTextureList());

	height = UShort.parseUShort (in).getCharValue();
	readVertexRefs (in, nSections * 6);
	ushortsRead += 2 + vertexrefs.size();
	return ushortsRead;
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (id == 0x0040)
	{
	    if (vertexrefs.size() < 2 * 6)
		throw new PSDLWriteException ("Attribute 0x0040 requires at least 12 vertices");

	    if (vertexrefs.size() % 6 != 0)
		throw new PSDLWriteException ("Attribute 0x0040 requires a multiple of 6 vertices");
	}
	else
	{
	if (vertexrefs.size() != nSections * 6)
	    throw new PSDLWriteException ("Attribute 0x" + Integer.toHexString (id)+ " requires " + nSections * 6 + " vertices");
	}
	writeID (out);

	if (id == 0x0040)
	{
	    UShort.write ((char)nSections(), out);
	}

	out.write (flags | type);
	out.write (tRef.getIndex() + 1);

	UShort.write ((char)height, out);

	writeVertexRefs (out);
    }

    public String toHexString()
    {
	return "";
    }

    public long getLength()
    {
	long size = 0;

	if (id == 0x0040)
	{
	    if (   (vertexrefs.size() < 2 * 6)
		|| (vertexrefs.size() % 6 != 0))
	    {
		return 0;
	    }

	    return   1               // id
		   + 1               // len
		   + 1               // tRef + flags
		   + 1               // height
	           + vertexrefs.size();
	}
	else
	{
	    if (vertexrefs.size() != nSections * 6)
	    {
		return 0;
	    }

	    return   1               // id
		   + 1               // tRef + flags
		   + 1               // height
	           + vertexrefs.size();
	}
    }

    public String toString()
    {
	String s = UShort.toHexString (id) + " divided road strip";
	if (id != 0x0040)
	{
	    s += " (" + (int)nSections + " cross sections)";
	}
	return s;
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
	tRef.setIndex ((char)(textureOffset + tRef.getIndex()));
    }





    public TextureMapRef getTextureMap()
    {
	return tRef;
    }

    public void setTextureMap (TextureMapRef t)
    {
	tRef = t;
    }




    public int getType()
    {
	return type;
    }
    public void setType (int f)
    {
	type = f;
    }


    public int getFlags()
    {
	return flags;
    }
    public void setFlags (int f)
    {
	flags = f;
    }
    public boolean getFlag (int f)
    {
	return (flags & f) != 0;
    }
    public void setFlag (int f, boolean v)
    {
	if (v)
	{
	    flags |= f;
	}
	else
	{
	    flags &= ~f;
	}
    }

    public float getHeight()
    {
	return height / 256f;
    }
    public void setHeight (float h)
    {
	height = (char)(h * 256 + 0.5);
    }

}
