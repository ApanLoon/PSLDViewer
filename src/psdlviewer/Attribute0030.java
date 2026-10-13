package psdlviewer;
import psdlviewer.*;

/** Attribute that renders a triangle fan.
 *
 *  Note that this attribute consists of a list of vertex references followed
 *  by two more vertex references. These indicate the two closing vertices of
 *  the fan and are not included in the counter preceeding the list of vertices.
 *
 *  For internal storage the last two vertices are kept in the same list as the
 *  other vertices.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0030
extends VertexListAttribute
{
    public Attribute0030()
    {
	super();
	id = 0x0030;
    }

    public long read (InputStream in, long maxLength)
    throws IOException
    {
	char len = UShort.parseUShort (in).getCharValue();
	readVertexRefs (in, len + 2);
	return vertexrefs.size() + 1;
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() < 3)
	    throw new PSDLWriteException ("Attribute 0x0030 requires at least three vertices.");

	writeID (out);

	UShort.write ((char)(vertexrefs.size() - 2), out);
	writeVertexRefs (out);
    }


    public String toHexString()
    {
	return "";
    }


    public long getLength()
    {
	// If the attribute is a subclass, the length of a VertexListAttribute
	// is correct:
	if (id != 0x0030)
	    return super.getLength();

	// Otherwise return the length of this attribute:
	if (vertexrefs.size() < 3)
	{
	    return 0;
	}
	else
	{
	    return   1               // id
		   + 1               // len
	           + vertexrefs.size();
	}
    }



    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan";
    }
}
