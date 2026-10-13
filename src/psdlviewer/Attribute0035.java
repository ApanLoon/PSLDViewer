package psdlviewer;
import psdlviewer.*;

/** Triangle fan, five triangles.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0035
extends Attribute0030
{
    public Attribute0035()
    {
	super();
	id = 0x0035;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 7);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 7)
	    throw new PSDLWriteException ("Attribute 0x0035 must have seven vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Triangle fan, 5 triangles";
    }
}
