package psdlviewer;
import psdlviewer.*;

/** Road strip
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute0000
extends RoadAttribute
{
    public Attribute0000()
    {
	super();
	id = 0x0000;
    }


    public int nSections()
    {
	return vertexrefs.size() / 4;
    }




    public long read (InputStream in, long maxLength)
    throws IOException
    {
	long ushortsRead = 0;
	if (id == 0x0000)
	{
	    nSections = UShort.parseUShort (in).getCharValue();
	    ushortsRead++;
	}
	readVertexRefs (in, nSections * 4);
	ushortsRead += vertexrefs.size();
	return ushortsRead;
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	if (id == 0x0000)
	{
	    if (vertexrefs.size() < 2 * 4)
		throw new PSDLWriteException ("Attribute 0x0000 requires at least 8 vertices");

	    if (vertexrefs.size() % 4 != 0)
		throw new PSDLWriteException ("Attribute 0x0000 requires a multiple of 4 vertices");
	}
	else
	{
	if (vertexrefs.size() != nSections * 4)
	    throw new PSDLWriteException ("Attribute 0x" + Integer.toHexString (id)+ " requires " + nSections * 4 + " vertices");
	}
	writeID (out);

	if (id == 0x0000)
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

	if (id == 0x0000)
	{
	    if (   (vertexrefs.size() < 2 * 4)
		|| (vertexrefs.size() % 4 != 0))
	    {
		return 0;
	    }

	    return   1               // id
		   + 1               // len
	           + vertexrefs.size();
	}
	else
	{
	    if (vertexrefs.size() != nSections * 4)
	    {
		return 0;
	    }

	    return   1               // id
	           + vertexrefs.size();
	}
    }

    public String toString()
    {
	String s = UShort.toHexString (id) + " road strip";
	if (id != 0x0000)
	{
	    s += " (" + (int)nSections + " cross sections)";
	}
	return s;
    }


}
