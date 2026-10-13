package psdlviewer.bai;
import psdlviewer.*;

import java.util.Vector;

public class Route
{
    protected int          id;
    protected int          unknown0;
    protected BlockRefList blocks = new BlockRefList();
    protected float        unknown1;
    protected float        unknown2;
    protected RoadData     right;
    protected RoadData     left;
    protected Vector       centralDistance = new Vector();
    protected Vector       centralLine     = new Vector();
    protected Vector       xOrientation    = new Vector();
    protected Vector       yOrientation    = new Vector();
    protected Vector       zOrientation    = new Vector();
    protected Vector       wOrientation    = new Vector();
    protected RoadEnd      roadEnd;
    protected RoadEnd      roadStart;


    public int getID()
    {
	return id;
    }
    public void setID (int id_)
    {
	id = id_;
    }

    public int getUnknown0()
    {
	return unknown0;
    }
    public void setUnknown0 (int u)
    {
	unknown0 = u;
    }

    public BlockRefList getBlocks()
    {
	return blocks;
    }
    public void setBlocks (BlockRefList l)
    {
	blocks = l;
    }
    public void addBlockRef (BlockRef br)
    {
	blocks.add (br);
    }

    public float getUnknown1()
    {
	return unknown1;
    }
    public void setUnknown1 (float u)
    {
	unknown1 = u;
    }

    public float getUnknown2()
    {
	return unknown2;
    }
    public void setUnknown2 (float u)
    {
	unknown2 = u;
    }

    public RoadEnd getRoadEnd()
    {
	return roadEnd;
    }
    public void setRoadEnd (RoadEnd re)
    {
	roadEnd = re;
    }

    public RoadEnd getRoadStart()
    {
	return roadStart;
    }
    public void setRoadStart (RoadEnd re)
    {
	roadStart = re;
    }

    public Vector getCentralDistances()
    {
	return centralDistance;
    }
    public void addCentralDistance (float d)
    {
	centralDistance.add (new Float (d));
    }

    public Vector getCentralLine()
    {
	return centralLine;
    }
    public void addCentralVertex (Vertex3D v)
    {
	centralLine.add (v);
    }

    public Vector getXOrientation()
    {
	return xOrientation;
    }
    public void addXOrientationVertex (Vertex3D v)
    {
	xOrientation.add (v);
    }

    public Vector getYOrientation()
    {
	return yOrientation;
    }
    public void addYOrientationVertex (Vertex3D v)
    {
	yOrientation.add (v);
    }

    public Vector getZOrientation()
    {
	return zOrientation;
    }
    public void addZOrientationVertex (Vertex3D v)
    {
	zOrientation.add (v);
    }

    public Vector getWOrientation()
    {
	return wOrientation;
    }
    public void addWOrientationVertex (Vertex3D v)
    {
	wOrientation.add (v);
    }

    public RoadData getRight()
    {
	return right;
    }
    public void setRight (RoadData r)
    {
	right = r;
    }

    public RoadData getLeft()
    {
	return left;
    }
    public void setLeft (RoadData r)
    {
	left = r;
    }
}
