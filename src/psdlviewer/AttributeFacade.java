package psdlviewer;
import psdlviewer.*;

/** Base class for facade attributes
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class AttributeFacade
extends VertexListAttribute
{
    public HeightRef h0;
    public HeightRef h1;
    public char      xRepeat = (char)1;
    public char      yRepeat = (char)1;

    public AttributeFacade()
    {
	super();
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	h0      = new HeightRef (UShort.parseUShort (in));
	h1      = new HeightRef (UShort.parseUShort (in));
	readVertexRefs (in, 2);
	return vertexrefs.size() + 2;
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Facade (no repeat)";
    }


    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 2)
	    throw new PSDLWriteException ("Attribute "
					  + toHexString()
					  + " requires two vertices");
	writeID (out);
	h0.write (out);
	h1.write (out);
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
	h0.setIndex ((char)(heightOffset + h0.getIndex()));
	h1.setIndex ((char)(heightOffset + h1.getIndex()));
    }



    public long getLength()
    {
	return   1     // id
	       + 1     // h0
 	       + 1     // h1
	       + vertexrefs.size();
    }

}
