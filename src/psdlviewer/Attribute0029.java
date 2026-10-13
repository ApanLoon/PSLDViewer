package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0029
extends Attribute0028
{
    public Attribute0029()
    {
	super();
	id = 0x0029;
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
	    throw new PSDLWriteException ("Attribute 0x0029 must have three vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Road triangle fan, 3 vertices";
    }
}
