package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute000b
extends Attribute0008
{
    public Attribute000b()
    {
	super();
        nSections = 3;
	id = 0x000b;
    }

    /*
    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 6);
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 6)
	    throw new PSDLWriteException ("Attribute 0x000b must have six vertices.");
	writeID (out);
	writeVertexRefs (out);
    }


    public String toString()
    {
        return UShort.toHexString (id) + " Sidewalk strip, 3 vertex pairs";
    }
    */
}
