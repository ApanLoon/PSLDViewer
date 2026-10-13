package psdlviewer.bai;
import psdlviewer.*;

import java.util.Vector;

public class RoadData
{
    protected int    nLanes;
    protected int    nTrams;
    protected int    nTrains;
    protected int    unknown3;
    protected int    unknown4;
    protected Vector laneDistances  = new Vector();
    protected Vector distances      = new Vector();
    protected Vector unknown0       = new Vector();
    protected Vector laneVertices   = new Vector();
    protected Vector sidewalkCenter = new Vector();
    protected Vector tramVertices   = new Vector();
    protected Vector trainVertices  = new Vector();
    protected Vector sidewalkInner  = new Vector();
    protected Vector sidewalkOuter  = new Vector();

    public void setNLanes (int n)
    {
	nLanes = n;
    }
    public int getNLanes()
    {
	return nLanes;
    }

    public void setNTrams (int n)
    {
	nTrams = n;
    }
    public int getNTrams()
    {
	return nTrams;
    }

    public void setNTrains (int n)
    {
	nTrains = n;
    }
    public int getNTrains()
    {
	return nTrains;
    }

    public int getUnknown3()
    {
	return unknown3;
    }
    public void setUnknown3 (int u)
    {
	unknown3 = u;
    }

    public int getUnknown4()
    {
	return unknown4;
    }
    public void setUnknown4 (int u)
    {
	unknown4 = u;
    }

    public Vector getLaneDistances()
    {
	return laneDistances;
    }
    public void addLaneDistance (float d)
    {
	laneDistances.add (new Float (d));
    }

    public Vector getDistances()
    {
	return distances;
    }
    public void addDistance (float d)
    {
	distances.add (new Float (d));
    }

    public Vector getLaneVertices()
    {
	return laneVertices;
    }
    public void addLaneVertex (Vertex3D v)
    {
	laneVertices.add (v);
    }

    public Vector getSidewalkCenter()
    {
	return sidewalkCenter;
    }
    public void addSidewalkCenterVertex (Vertex3D v)
    {
	sidewalkCenter.add (v);
    }

    public Vector getTramVertices()
    {
	return tramVertices;
    }
    public void addTramVertex (Vertex3D v)
    {
	tramVertices.add (v);
    }

    public Vector getTrainVertices()
    {
	return trainVertices;
    }
    public void addTrainVertex (Vertex3D v)
    {
	trainVertices.add (v);
    }

    public Vector getSidewalkInner()
    {
	return sidewalkInner;
    }
    public void addSidewalkInnerVertex (Vertex3D v)
    {
	sidewalkInner.add (v);
    }

    public Vector getSidewalkOuter()
    {
	return sidewalkOuter;
    }
    public void addSidewalkOuterVertex (Vertex3D v)
    {
	sidewalkOuter.add (v);
    }

    public Vector getUnknown0()
    {
	return unknown0;
    }
    public void addUnknown0 (float d)
    {
	unknown0.add (new Float (d));
    }

}
