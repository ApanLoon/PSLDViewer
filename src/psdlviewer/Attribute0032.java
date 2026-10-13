package psdlviewer;
import psdlviewer.*;

/** Triangle fan, two triangles.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0032
extends Attribute0030
{
    public Attribute0032()
    {
	super();
	id = 0x0032;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 4);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 4)
	    throw new PSDLWriteException ("Attribute 0x0032 must have four vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 2 triangles";
    }
}
