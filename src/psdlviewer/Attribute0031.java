package psdlviewer;
import psdlviewer.*;

/** Triangle fan, single triangle
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0031
extends Attribute0030
{
    public Attribute0031()
    {
	super();
	id = 0x0031;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 3);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 3)
	    throw new PSDLWriteException ("Attribute 0x0031 requires three vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 1 triangle";
    }
}
