package psdlviewer;
import psdlviewer.*;

/** Flat rectangle strip, n vertex pairs
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0010
extends RoadAttribute
{
    public Attribute0010()
    {
	super();
	id = 0x0010;
    }


    public int nSections()
    {
	return vertexrefs.size() / 2;
    }




    public long read (InputStream in, long maxLength)
    throws IOException
    {
	long ushortsRead = 0;
	if (id == 0x0010)
	{
	    nSections = UShort.parseUShort (in).getCharValue();
	    ushortsRead++;
	}
	readVertexRefs (in, nSections * 2);
	ushortsRead += vertexrefs.size();
	return ushortsRead;
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (id == 0x0010)
	{
	    if (vertexrefs.size() < 2 * 2)
		throw new PSDLWriteException ("Attribute 0x0010 requires at least 4 vertices");

	    if (vertexrefs.size() % 2 != 0)
		throw new PSDLWriteException ("Attribute 0x0010 requires a multiple of 2 vertices");
	}
	else
	{
	if (vertexrefs.size() != nSections * 2)
	    throw new PSDLWriteException ("Attribute 0x" + Integer.toHexString (id)+ " requires " + nSections * 2 + " vertices");
	}
	writeID (out);

	if (id == 0x0010)
	{
	    UShort.write ((char)nSections(), out);
	}

	writeVertexRefs (out);
    }




    public String toHexString()
    {
	return "";
    }

    public long getLength()
    {
	long size = 0;

	if (id == 0x0010)
	{
	    if (   (vertexrefs.size() < 2 * 2)
		|| (vertexrefs.size() % 2 != 0))
	    {
		return 0;
	    }

	    return   1               // id
		   + 1               // len
	           + vertexrefs.size();
	}
	else
	{
	    if (vertexrefs.size() != nSections * 2)
	    {
		return 0;
	    }

	    return   1               // id
	           + vertexrefs.size();
	}
    }

    public String toString()
    {
	String s = UShort.toHexString (id) + " rectangle strip";
	if (id != 0x0010)
	{
	    s += " (" + (int)nSections + " cross sections)";
	}
	return s;
    }
}
