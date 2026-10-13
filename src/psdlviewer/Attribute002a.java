package psdlviewer;
import psdlviewer.*;

/** Internal part of a road, for use in crossings. (rectangle definition)
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute002a
extends Attribute0028
{
    public Attribute002a()
    {
	super();
	id = 0x002a;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	return readVertexRefs (in, 4);
    }

    public void write (OutputStream out)
    throws IOException
    {
	writeID (out);
	writeVertexRefs (out);
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Road triangle fan, 4 vertices";
    }
}
