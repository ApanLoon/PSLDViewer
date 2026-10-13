package psdlviewer;
import psdlviewer.*;

/** Facade bound
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute003c
extends VertexListAttribute
{

    public HeightRef top;
    public int       shade;

    public Attribute003c()
    {
	super();
	id = 0x003c;
    }

    public long read (InputStream in, long maxLength)
    throws IOException
    {
	shade   = UShort.parseUShort (in).getCharValue();
	top     = new HeightRef (UShort.parseUShort (in));
	readVertexRefs (in, 2);
	return vertexrefs.size() + 2;
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Facade bound";
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 2)
	    throw new PSDLWriteException ("Attribute "
					  + toHexString()
					  + " requires two vertices");
	writeID (out);
	UShort.write ((char)shade, out);
	top.write (out);
	writeVertexRefs (out);

    }


    /** Adds offsets to table references in this attribute.
     *
     *  Adds the height offset to the height reference.
     *
     *  Override this in subclasses to correctly add to all references.
     *
     */
    public void addOffsets (int vertexOffset,
			    int heightOffset,
			    int textureOffset,
			    int blockOffset)
    {
	super.addOffsets (vertexOffset,
			  heightOffset,
			  textureOffset,
			  blockOffset);
	top.setIndex ((char)(heightOffset + top.getIndex()));
    }



    public long getLength()
    {
	return   1     // id
	       + 1     // shade
 	       + 1     // h1
	       + vertexrefs.size();
    }
}
