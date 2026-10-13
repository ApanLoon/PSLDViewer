package psdlviewer;
import psdlviewer.*;

/** Facade
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute005e
extends VertexListAttribute
{
    public HeightRef bottom;
    public HeightRef top;
    public char      xRepeat = (char)1;
    public char      yRepeat = (char)1;

    public Attribute005e()
    {
	super();
	id = 0x005e;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	bottom  = new HeightRef (UShort.parseUShort (in));
	top     = new HeightRef (UShort.parseUShort (in));
	xRepeat = UShort.parseUShort (in).getCharValue();
	yRepeat = UShort.parseUShort (in).getCharValue();
	readVertexRefs (in, 2);
	return vertexrefs.size() + 4;
    }

    public String toString()
    {
        return UShort.toHexString (id) + " Facade";
    }


    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (vertexrefs.size() != 2)
	    throw new PSDLWriteException ("Attribute 0x005e requires two vertices");
	writeID (out);
	bottom.write (out);
	top.write (out);
	UShort.write ((char)xRepeat, out);
	UShort.write ((char)yRepeat, out);
	writeVertexRefs (out);
    }

    public long getLength()
    {
	return   1     // id
	       + 1     // bottom
 	       + 1     // top
 	       + 1     // xRepeat
	       + 1     // yRepeat
	       + vertexrefs.size();
    }
}
