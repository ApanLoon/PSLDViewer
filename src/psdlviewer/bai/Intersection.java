package psdlviewer.bai;
import psdlviewer.*;

import java.util.Vector;

public class Intersection
{
    protected int      id;
    protected BlockRef block;
    protected Vertex3D center;
    protected Vector   routes = new Vector();

    public int getID()
    {
	return id;
    }
    public void setID (int i)
    {
	id = i;
    }

    public BlockRef getBlock()
    {
	return block;
    }
    public void setBlock (BlockRef br)
    {
	block = br;
    }

    public Vertex3D getCenter()
    {
	return center;
    }
    public void setCenter (Vertex3D v)
    {
	center = v;
    }

    public void addRoute (long l)
    {
	routes.add (new Long (l));
    }
}
