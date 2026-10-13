package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute002d
extends Attribute0028
{
    public Attribute002d()
    {
	super();
	id = 0x002d;
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
	    throw new PSDLWriteException ("Attribute 0x002d must have seven vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Road triangle fan, 7 vertices";
    }
}
