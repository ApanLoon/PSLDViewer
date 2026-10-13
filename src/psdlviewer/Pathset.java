package psdlviewer;
import psdlviewer.*;

/** Pathset.
 *
 */

public class Pathset
{
    protected PathList      paths  = new PathList();
    protected String        name;
    protected int           unknown0 = 0x00;

    public Pathset()
    {
    }



    public PathList getPaths()
    {
	return paths;
    }


    

    public void addPath (Path p)
    {
	paths.add (p);
    }





    public void setName (String name_)
    {
	name = name_;
    }
    public String getName()
    {
	return name;
    }

    public void setUnknown0 (int unknown0_)
    {
	unknown0 = unknown0_;
    }
    public int getUnknown0()
    {
	return unknown0;
    }



    public String toString()
    {
	return "apa: " + name;
    }



}
