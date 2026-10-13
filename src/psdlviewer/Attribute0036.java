package psdlviewer;
import psdlviewer.*;

/** Triangle fan, six triangles.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0036
extends Attribute0030
{
    public Attribute0036()
    {
	super();
	id = 0x0036;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 8);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 8)
	    throw new PSDLWriteException ("Attribute 0x0036 must have eight vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 6 triangles";
    }
}
