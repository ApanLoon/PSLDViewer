package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute002b
extends Attribute0028
{
    public Attribute002b()
    {
	super();
	id = 0x002b;
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
	    throw new PSDLWriteException ("Attribute 0x002b must have five vertices.");
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Road triangle fan, 5 vertices";
    }
}
