package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute002e
extends Attribute0028
{
    public Attribute002e()
    {
	super();
	id = 0x002e;
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
	    throw new PSDLWriteException ("Attribute 0x002e must have eight vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Road triangle fan, 8 vertices";
    }
}
