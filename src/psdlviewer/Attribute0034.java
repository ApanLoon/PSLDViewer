package psdlviewer;
import psdlviewer.*;

/** Triangle fan, four triangles.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0034
extends Attribute0030
{
    public Attribute0034()
    {
	super();
	id = 0x0034;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 6);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 6)
	    throw new PSDLWriteException ("Attribute 0x0034 must have six vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 4 triangles";
    }
}
