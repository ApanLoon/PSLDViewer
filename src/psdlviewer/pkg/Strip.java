package psdlviewer.pkg;

import psdlviewer.Config;
import psdlviewer.CFloat;
import psdlviewer.UShort;
import psdlviewer.ULong;


import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.TexCoord2f;

import java.io.InputStream;

public class Strip
{
    long         nVerticesPerFace;
    int          nVertices;

    Point3f[]    coordinate;
    Vector3f[]   normal;
    int[]        unknown;
    TexCoord2f[] texCoord;

    int          nIndices;
    int[]        index;

    Section section;

    public Strip (Section  section_,
		  InputStream in)
    throws Exception
    {
	section = section_;


	if (section.unknown == 0x0100)
	{
	    nVerticesPerFace = UShort.parseUShort (in).getCharValue();
	    nVertices        = UShort.parseUShort (in).getCharValue();
	}
	else
	{
	    nVerticesPerFace = ULong.parseULong (in);
	    nVertices        = (int)ULong.parseULong (in);
	}

//	Config.debug ("nVerticesPerFace = " + nVerticesPerFace + "\n");
//	Config.debug ("nVertices        = " + nVertices + "\n");

	coordinate = new Point3f[nVertices];
	normal     = new Vector3f[nVertices];
	unknown    = new int[nVertices];
	texCoord   = new TexCoord2f[nVertices];

	float x;
	float y;
	float z;
	for (int v = 0; v < nVertices; v++)
	{
	    if ((section.model.flags & Model.FLAG_COORDINATE) != 0)
	    {
		x = CFloat.parseCFloat (in);
		y = CFloat.parseCFloat (in);
		z = CFloat.parseCFloat (in);
		coordinate[v] = new Point3f (x, y, z);

		// Check for bounding box:
		if (x < section.model.pkg.getMinX())
		{
		    section.model.pkg.setMinX (x);
		}
		if (x > section.model.pkg.getMaxX())
		{
		    section.model.pkg.setMaxX (x);
		}
		if (y < section.model.pkg.getMinY())
		{
		    section.model.pkg.setMinY (y);
		}
		if (y > section.model.pkg.getMaxY())
		{
		    section.model.pkg.setMaxY (y);
		}
		if (z < section.model.pkg.getMinZ())
		{
		    section.model.pkg.setMinZ (z);
		}
		if (z > section.model.pkg.getMaxZ())
		{
		    section.model.pkg.setMaxZ (z);
		}

	    }

	    if ((section.model.flags & Model.FLAG_NORMAL) != 0)
	    {
		if (section.unknown == 0x100)
		{
		    float xAngle = (float)((in.read() / 256f) * 2 * Math.PI);
		    float yAngle = (float)((in.read() / 256f) * 2 * Math.PI);
		    float zAngle = (float)((in.read() / 256f) * 2 * Math.PI);
		    normal[v] = new Vector3f();
		    normal[v].x = (float)(1f / Math.sin (yAngle));
		    normal[v].y = (float)(1f / Math.cos (yAngle));
		    normal[v].z = (float)(1f / Math.sin (zAngle));
		}
		else
		{
		    x = CFloat.parseCFloat (in);
		    y = CFloat.parseCFloat (in);
		    z = CFloat.parseCFloat (in);
		    normal[v] = new Vector3f (x, y, z);
		}
	    }

	    if ((section.model.flags & Model.FLAG_NGON) != 0)
	    {
		unknown[v] = (int)ULong.parseULong (in);
	    }

	    if ((section.model.flags & Model.FLAG_TEXCOORD) != 0)
	    {
		if (section.unknown == 0x100)
		{
		    x = UShort.parseUShort (in).getCharValue() / 128f;
		    y = UShort.parseUShort (in).getCharValue() / 128f;
		}
		else
		{
		    x = CFloat.parseCFloat (in);
		    y = CFloat.parseCFloat (in);
		}
		texCoord[v] = new TexCoord2f (x, y);
	    }
	}

	if (section.unknown == 0x0100)
	{
	    nIndices = UShort.parseUShort (in).getCharValue();
	}
	else
	{
	    nIndices = (int)ULong.parseULong (in);
	}
	index = new int[nIndices];

	for (int i = 0; i < nIndices; i++)
	{
	    index[i] = (int)UShort.parseUShort (in).getCharValue();
	}
    }
}
