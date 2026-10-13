package psdlviewer;
import psdlviewer.*;

/** 
 *  
 *
 */

import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Enumeration;

import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Path
{

    public static final int TYPE_POINTS    = 0;
    public static final int TYPE_LINES     = 1;
    public static final int TYPE_LINESTRIP = 2;


    protected String name;
    protected int unknown1;
    protected int type;
    protected int spacing;
    protected int unknown3;
    protected PathPointList points = new PathPointList();

    public Path()
    {
    }




    public String getName()
    {
	return name;
    }
    public void setName (String s)
    {
	name = s;
    }

    public int getUnknown1()
    {
	return unknown1;
    }
    public void setUnknown1 (int i)
    {
	unknown1 = i;
    }

    public int getType()
    {
	return type;
    }
    public void setType (int i)
    {
	type = i;
    }

    public float getSpacing()
    {
	return spacing / 4f;
    }
    public void setSpacing (float f)
    {
	spacing = (int)((f * 4) + 0.5);
    }

    public void setSpacing (int i)
    {
	spacing = i;
    }

    public int getUnknown3()
    {
	return unknown3;
    }
    public void setUnknown3 (int i)
    {
	unknown3 = i;
    }

    public PathPointList getPoints()
    {
	return points;
    }
    public void setPoints (PathPointList l)
    {
	points = l;
    }


    public String toString()
    {
	return getName();
    }

}
