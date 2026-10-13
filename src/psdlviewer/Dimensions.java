package psdlviewer;
import psdlviewer.*;

public class Dimensions
{
    // Bounding box:
    public Vertex3D min;
    public Vertex3D max;

    // Bounding sphere:
    public Vertex3D center;
    public float    radius;



    public Dimensions()
    {
	min = new Vertex3D (-1000, -1000, -1000);
	max = new Vertex3D (1000, 1000, 1000);
	center = new Vertex3D (0, 0, 0);
	radius = 0;
    }

    public Dimensions (Vertex3D min_, 
		       Vertex3D max_, 
		       Vertex3D center_, 
		       float    radius_)
    {
	min = min_;
	max = max_;
	center = center_;
	radius = radius_;
    }



    public void setMin (Vertex3D v)
    {
	min = v;
    }
    public void setMax (Vertex3D v)
    {
	max = v;
    }
    public void setCenter (Vertex3D v)
    {
	center = v;
    }
    public void setRadius (float f)
    {
	radius = f;
    }

    public Vertex3D getMin()
    {
	return min;
    }
    public Vertex3D getMax()
    {
	return max;
    }
    public Vertex3D getCenter()
    {
	return center;
    }
    public float getRadius()
    {
	return radius;
    }


}
