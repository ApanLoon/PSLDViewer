package psdlviewer;
import psdlviewer.*;
/** Road definition
 *
 */

import java.util.Vector;


public class Road
{
    protected UShort unknown0;
    protected UShort unknown1;

    protected Vector forwardLanes = new Vector();
    protected Vector backwardLanes = new Vector();

    protected UShort unknown2;

    protected VertexRefList start = new VertexRefList();
    protected VertexRefList end = new VertexRefList();

    protected BlockRefList blocks = new BlockRefList();



    public Road()
    {
    }


    public void setU0 (UShort u0)
    {
	unknown0 = u0;
    }
    public UShort getU0()
    {
	return unknown0;
    }

    public void setU1 (UShort u1)
    {
	unknown1 = u1;
    }
    public UShort getU1()
    {
	return unknown1;
    }

    public void addForwardLane (Float f)
    {
	forwardLanes.add (f);
    }
    public int getNFLanes()
    {
	return forwardLanes.size();
    }
    public float getForwardLane (int i)
    {
	return ((Float)forwardLanes.elementAt (i)).floatValue();
    }
    public Vector getForwardLanes()
    {
	return forwardLanes;
    }


    public void addBackwardLane (Float f)
    {
	backwardLanes.add (f);
    }
    public int getNBLanes()
    {
	return backwardLanes.size();
    }
    public float getBackwardLane (int i)
    {
	return ((Float)backwardLanes.elementAt (i)).floatValue();
    }
    public Vector getBackwardLanes()
    {
	return backwardLanes;
    }

    public void setU2 (UShort u2)
    {
	unknown2 = u2;
    }
    public UShort getU2()
    {
	return unknown2;
    }


    public void addStartVertex (VertexRef v)
    {
	start.add (v);
    }
    public VertexRef getStartVertex (int i)
    {
	return start.referenceAt (i);
    }
    public VertexRefList getStartVertices()
    {
	return start;
    }

    public void addEndVertex (VertexRef v)
    {
	end.add (v);
    }
    public VertexRef getEndVertex (int i)
    {
	return end.referenceAt (i);
    }
    public VertexRefList getEndVertices()
    {
	return end;
    }


    public void addBlockRef (BlockRef b)
    {
	blocks.add (b);
    }
    public int getNBlocks()
    {
	return blocks.size();
    }
    public BlockRef getBlockRef (int i)
    {
	return blocks.referenceAt (i);
    }
    public BlockRefList getBlockRefs()
    {
	return blocks;
    }


    public String toString()
    {
	String s = "";
/*
	s += "Road\n";
	s += "{\n";
	s += "    unknown0 = " + unknown0 + "\n";
	s += "    unknown1 = " + unknown1 + "\n";
	s += "    forward lanes = [";
	for (int i = 0; i < forwardLanes.size(); i++)
	{
	    s += getForwardLane (i) + ", ";
	}
	s += "]\n";
	s += "    backward lanes = [";
	for (int i = 0; i < backwardLanes.size(); i++)
	{
	    s += getBackwardLane (i) + ", ";
	}
	s += "]\n";
	s += "    unknown2 = " + unknown2 + "\n";

	s += "    start vertices = [";
	for (int i = 0; i < 4; i++)
	{
	    s += getStartVertex (i) + ", ";
	}
	s += "]\n";

	s += "    end vertices = [";
	for (int i = 0; i < 4; i++)
	{
	    s += getEndVertex (i) + ", ";
	}
	s += "]\n";

	s += "    blocks = [";
	for (int i = 0; i < blocks.size(); i++)
	{
	    s += getBlockRef (i) + ", ";
	}
	s += "]\n";
	s += "}\n";
*/
	return s;
    }
}

