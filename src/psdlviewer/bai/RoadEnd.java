package psdlviewer.bai;
import psdlviewer.*;

class RoadEnd
{
    protected long     intersection;
    protected int      unknown0;
    protected long     unknown1;
    protected long     unknown2;
    protected Vertex3D origin;
    protected Vertex3D axis;

    public void setIntersection (long id)
    {
	intersection = id;
    }
    public long getIntersection()
    {
	return intersection;
    }

    public int getUnknown0()
    {
	return unknown0;
    }
    public void setUnknown0 (int u)
    {
	unknown0 = u;
    }

    public long getUnknown1()
    {
	return unknown1;
    }
    public void setUnknown1 (long u)
    {
	unknown1 = u;
    }

    public long getUnknown2()
    {
	return unknown2;
    }
    public void setUnknown2 (long u)
    {
	unknown2 = u;
    }

    public Vertex3D getOrigin()
    {
	return origin;
    }
    public void setOrigin (Vertex3D v)
    {
	origin = v;
    }

    public Vertex3D getAxis()
    {
	return axis;
    }
    public void setAxis (Vertex3D v)
    {
	axis = v;
    }
}
