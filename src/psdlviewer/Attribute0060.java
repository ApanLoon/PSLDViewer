package psdlviewer;
import psdlviewer.*;

/** Attribute that renders a roof triangle fan.
 *
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0060
extends VertexListAttribute
{
    char nVertices;
    public HeightRef height;

    public Attribute0060()
    {
	super();
	id = 0x0060;
    }

    public int nVertices()
    {
	return vertexrefs.size();
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	long ushortsRead = 0;
	if (id == 0x0060)
	{
	    nVertices = (char)(UShort.parseUShort (in).getCharValue() + 1);
	    ushortsRead++;
	}
	height = new HeightRef (UShort.parseUShort (in));
	ushortsRead++;

	readVertexRefs (in, nVertices);
	ushortsRead += vertexrefs.size();
	return ushortsRead;
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (id != 0x0060 && vertexrefs.size() != nVertices)
	{
	    throw new PSDLWriteException ("Attribute 0x" + Integer.toHexString (id)+ " requires " + nVertices + " vertices");
	}
	writeID (out);

	if (id == 0x0060)
	{
	    UShort.write ((char)(nVertices() - 1), out);
	}

	height.write (out);
	writeVertexRefs (out);




/* Hmm... -1 ?!?
	writeID (out);
	UShort.write ((char)(vertexrefs.size() - 1), out);
	height.write (out);
	writeVertexRefs (out);
*/
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
	height.setIndex ((char)(heightOffset + height.getIndex()));
    }


    public String toHexString()
    {
	return "";
    }


    public long getLength()
    {
	// If the attribute is a subclass, the length of a VertexListAttribute
	// is correct + the height index:
	if (id != 0x0060)
	    return super.getLength() + 1;

	// Otherwise return the length of this attribute:
	if (vertexrefs.size() < 3)
	{
	    return 0;
	}
	else
	{
	    return   1               // id
		   + 1               // len
		   + 1               // height
	           + vertexrefs.size();
	}
    }



    public String toString()
    {
	String s = UShort.toHexString (id) + " roof triangle fan";
	if (id != 0x0060)
	{
	    s += " (" + (int)nVertices + " vertices)";
	}
	return s;
    }
}
