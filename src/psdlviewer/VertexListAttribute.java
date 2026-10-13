package psdlviewer;
import psdlviewer.*;

/** Base class for attributes that have nothing but a plain list of vertices as data.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class VertexListAttribute
extends BlockAttribute
{
    public VertexRefList vertexrefs = new VertexRefList();

    public VertexListAttribute()
    {
	super();
	data = vertexrefs;
    }


    public long readVertexRefs (InputStream in, long n)
    throws IOException
    {
	for (int i = 0; i < n; i++)
	{
	    vertexrefs.add (new VertexRef (UShort.parseUShort (in)));
	}
	return vertexrefs.size();
    }

    public void writeVertexRefs (OutputStream out)
    throws IOException
    {
	for (int i = 0; i < vertexrefs.size(); i++)
	{
	    vertexrefs.referenceAt (i).write (out);
	}
    }


    /** Adds offsets to table references in this attribute.
     *
     *  Adds the vertex offset to the vertex references.
     *
     *  Override this in subclasses to correctly add to all references.
     *
     */
    public void addOffsets (int vertexOffset,
			    int heightOffset,
			    int textureOffset,
			    int blockOffset)
    {
	for (int i = 0; i < vertexrefs.size(); i++)
	{
	    VertexRef r = vertexrefs.referenceAt (i);
	    r.setIndex ((char)(vertexOffset + r.getIndex()));
	}
    }


    public String toHexString()
    {
	return "";
    }


    public long getLength()
    {
	return   1               // id
	       + vertexrefs.size();
    }

    public VertexRefList getVertexRefs()
    {
	return vertexrefs;
    }

}
