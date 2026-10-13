package psdlviewer;
import psdlviewer.*;

import java.util.StringTokenizer;

/** Three dimensional vertex.
 *
 */

import javax.vecmath.Point3f;
import java.io.Serializable;

public class Vertex3D
implements Serializable
{
    public float x;
    public float y;
    public float z;

    protected Vertex3D()
    {
    }

    public Vertex3D (float x_,
		     float y_,
		     float z_)
    {
	x = x_;
	y = y_;
	z = z_;
    }


    public Vertex3D (String s)
    {
	StringTokenizer t = new StringTokenizer (s, ",");
	String token = t.nextToken();
	x = Float.parseFloat (token);
	token = t.nextToken();
	y = Float.parseFloat (token);
	token = t.nextToken();
	z = Float.parseFloat (token);
    }


    public String toString()
    {
	return "" + x + ", " + y + ", " + z;
    }



    public Point3f toPoint3f()
    {
	return new Point3f (x, y, z);
    }
}
