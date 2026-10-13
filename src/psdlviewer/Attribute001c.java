package psdlviewer;
import psdlviewer.*;

/** Facade - No repeat
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute001c
extends VertexListAttribute
{
    public HeightRef top;
    public HeightRef textureScale;

    public Attribute001c()
    {
	super();
	id = 0x001c;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	top          = new HeightRef (UShort.parseUShort (in));
	textureScale = new HeightRef (UShort.parseUShort (in));
	readVertexRefs (in, 2);
	return vertexrefs.size() + 2;
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Sliver";
    }


    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 2)
	    throw new PSDLWriteException ("Attribute "
					  + toHexString()
					  + " requires two vertices");
	writeID (out);
	top.write (out);
	textureScale.write (out);
	writeVertexRefs (out);

    }



    /** Adds offsets to table references in this attribute.
     *
     *  Adds the height offset to the height references.
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
	textureScale.setIndex ((char)(heightOffset + textureScale.getIndex()));
    }



    public long getLength()
    {
	return   1     // id
 	       + 1     // top
	       + 1     // textureScale
	       + vertexrefs.size();
    }

}
