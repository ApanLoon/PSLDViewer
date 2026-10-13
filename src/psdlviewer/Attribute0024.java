package psdlviewer;
import psdlviewer.*;

/** Crosswalk rectangle.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0024
extends VertexListAttribute
{
    public Attribute0024()
    {
	super();
	id = 0x0024;
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
        return UShort.toHexString (id) + " crosswalk rectangle";
    }
}
