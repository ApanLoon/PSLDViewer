package psdlviewer;
import psdlviewer.*;

/** Triangle fan, seven triangles.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0037
extends Attribute0030
{
    public Attribute0037()
    {
	super();
	id = 0x0037;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 9);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 9)
	    throw new PSDLWriteException ("Attribute 0x0037 must have nine vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 7 triangles";
    }
}
