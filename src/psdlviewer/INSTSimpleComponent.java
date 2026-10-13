package psdlviewer;
import psdlviewer.*;

/** Simple component, using only delta x and delta z for scale and orientation
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class INSTSimpleComponent
extends INSTComponent
{
    protected float    deltaX = 1;
    protected float    deltaZ = 0;
    protected Vertex3D origo;

    public INSTSimpleComponent (int blockID_,
				int unknown_,
				String pkgName_,
				float deltaX_,
				float deltaZ_,
				Vertex3D origo_)
    {
	super (blockID_, unknown_, pkgName_);
	deltaX = deltaX_;
	deltaZ = deltaZ_;
	origo = origo_;
    }


    public INSTSimpleComponent()
    {
	origo = new Vertex3D (0, 0, 0);
    }


    public void read (InputStream in)
    throws IOException
    {
	deltaX = CFloat.parseCFloat (in);
	deltaZ = -CFloat.parseCFloat (in);

	float x = CFloat.parseCFloat (in);
	float y = CFloat.parseCFloat (in);
	float z = CFloat.parseCFloat (in);
	origo = new Vertex3D (x, y, z);
    }

    public void write (OutputStream out)
    throws IOException
    {
	super.write (out);
	CFloat.write (deltaX, out);
	CFloat.write (-deltaZ, out);
	CFloat.write (origo.x, out);
	CFloat.write (origo.y, out);
	CFloat.write (origo.z, out);
    }


    public void setOrigo (Vertex3D v)
    {
	origo = v;
    }





    public float getDeltaX()
    {
	return deltaX;
    }

    public float getDeltaZ()
    {
	return deltaZ;
    }




    public void setOrientationScale (float scale, float angle)
    {
	deltaX = (float)Math.cos (angle) * scale;
	deltaZ = (float)Math.sin (angle) * scale;
    }



    public float getScale()
    {
	return (float)Math.sqrt (deltaX * deltaX + deltaZ * deltaZ);
    }


    public float getAngle()
    {
	float angle = 0;
	if (deltaX == 0 && deltaZ != 0)
	{
	    angle = (float)((Math.PI / 2) * (deltaZ / Math.abs (deltaZ)));
	}
	else if (deltaX != 0 && deltaZ == 0)
	{
	    angle = (float)(deltaX < 0 ? Math.PI : 0);
	}
	else
	{
	    angle = (float)Math.atan (deltaZ / deltaX);
	    if (deltaX < 0)
	    {
		angle += (float)Math.PI;
	    }
	}
	return angle;
    }


    public Vertex3D getOrigo()
    {
	return origo;
    }


    public int getType()
    {
	return SIMPLE;
    }


/*
    public String toString()
    {
	String s = "INSTSimpleComponent\n";
	s += "{\n";
	s += "    BlockID:  0x" + Integer.toHexString (blockID) + "\n";
	s += "    Unknown:  0x" + Integer.toHexString (unknown) + "\n";
	s += "    PKG Name: "   + pkgName + "\n";
	s += "    DeltaX:   "   + deltaX + "\n";
	s += "    DeltaZ:   "   + deltaZ + "\n";
	s += "    Origo:    "   + origo + "\n";
	s += "}\n";
	return s;
    }
*/
}
