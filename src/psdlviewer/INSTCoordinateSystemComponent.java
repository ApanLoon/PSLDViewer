package psdlviewer;
import psdlviewer.*;

/** Coordinate system component, using a complete coordinate system
 *  transformation for scale and orientation
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class INSTCoordinateSystemComponent
extends INSTComponent
{
    protected Vertex3D xAxis;
    protected Vertex3D yAxis;
    protected Vertex3D zAxis;
    protected Vertex3D origo;

    public INSTCoordinateSystemComponent (int blockID_,
				int unknown_,
				String pkgName_,
				Vertex3D xAxis_,
				Vertex3D yAxis_,
				Vertex3D zAxis_,
				Vertex3D origo_)
    {
	super (blockID_, unknown_, pkgName_);
	xAxis = xAxis_;
	yAxis = yAxis_;
	zAxis = zAxis_;
	origo = origo_;
    }


    public INSTCoordinateSystemComponent()
    {
	origo = new Vertex3D (0, 0, 0);
	xAxis = new Vertex3D (1, 0, 0);
	yAxis = new Vertex3D (0, 1, 0);
	zAxis = new Vertex3D (0, 0, 1);
    }


    public void read (InputStream in)
    throws IOException
    {
	float x = CFloat.parseCFloat (in);
	float y = CFloat.parseCFloat (in);
	float z = CFloat.parseCFloat (in);
	xAxis = new Vertex3D (x, y, z);

	x = CFloat.parseCFloat (in);
	y = CFloat.parseCFloat (in);
	z = CFloat.parseCFloat (in);
	yAxis = new Vertex3D (x, y, z);

	x = CFloat.parseCFloat (in);
	y = CFloat.parseCFloat (in);
	z = CFloat.parseCFloat (in);
	zAxis = new Vertex3D (x, y, z);

	x = CFloat.parseCFloat (in);
	y = CFloat.parseCFloat (in);
	z = CFloat.parseCFloat (in);
	origo = new Vertex3D (x, y, z);
    }


    public void write (OutputStream out)
    throws IOException
    {
	super.write (out);
	CFloat.write (xAxis.x, out);
	CFloat.write (xAxis.y, out);
	CFloat.write (xAxis.z, out);
	CFloat.write (yAxis.x, out);
	CFloat.write (yAxis.y, out);
	CFloat.write (yAxis.z, out);
	CFloat.write (zAxis.x, out);
	CFloat.write (zAxis.y, out);
	CFloat.write (zAxis.z, out);
	CFloat.write (origo.x, out);
	CFloat.write (origo.y, out);
	CFloat.write (origo.z, out);
    }


    public void setXAxis (Vertex3D v)
    {
	xAxis = v;
    }

    public Vertex3D getXAxis()
    {
	return xAxis;
    }

    public void setYAxis (Vertex3D v)
    {
	yAxis = v;
    }

    public Vertex3D getYAxis()
    {
	return yAxis;
    }

    public void setZAxis (Vertex3D v)
    {
	zAxis = v;
    }

    public Vertex3D getZAxis()
    {
	return zAxis;
    }

    public void setOrigo (Vertex3D v)
    {
	origo = v;
    }

    public Vertex3D getOrigo()
    {
	return origo;
    }

    public int getType()
    {
	return COORDINATESYSTEM;
    }

/*
    public String toString()
    {
	String s = "INSTSimpleComponent\n";
	s += "{\n";
	s += "    BlockID:  0x" + Integer.toHexString (blockID) + "\n";
	s += "    Unknown:  0x" + Integer.toHexString (unknown) + "\n";
	s += "    PKG Name: " + pkgName + "\n";
	s += "    X Axis:   " + xAxis + "\n";
	s += "    Y Axis:   " + yAxis + "\n";
	s += "    Z Axis:   " + zAxis + "\n";
	s += "    Origo:    " + origo + "\n";
	s += "}\n";
	return s;
    }
*/
}
