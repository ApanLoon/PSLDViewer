package psdlviewer;
import psdlviewer.*;

/** Sidewalk piece of a crossing
 *
 * TODO: Not sure if this is a sidewalk strip, format appears to be
 *       a, b, v0, v1. Don't know what it means... :(
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute000a
extends Attribute0008
{
    public Attribute000a()
    {
	super();
        nSections = 2;
	id = 0x000a;
    }
}


/** Old definition:
public class Attribute000a
extends VertexListAttribute
{
    public Attribute000a()
    {
	super();
	id = 0x000a;
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
        return UShort.toHexString (id) + " Sidewalk strip, 2 vertex pairs";
    }
}
*/
