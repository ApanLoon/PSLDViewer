package psdlviewer;
import psdlviewer.*;

/** Triangle fan, three triangles.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0033
extends Attribute0030
{
    public Attribute0033()
    {
	super();
	id = 0x0033;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 5);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 5)
	    throw new PSDLWriteException ("Attribute 0x0033 must have five vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 3 triangles";
    }
}
