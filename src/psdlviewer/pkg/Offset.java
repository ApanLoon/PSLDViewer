package psdlviewer.pkg;

import psdlviewer.CFloat;

import javax.vecmath.Point3f;
import java.io.InputStream;

public class Offset
{
    Point3f offset;
    
    public Offset (InputStream in)
    throws Exception
    {
	float x = CFloat.parseCFloat (in);
	float y = CFloat.parseCFloat (in);
	float z = CFloat.parseCFloat (in);
	offset = new Point3f (x, y, z);
    }
}
