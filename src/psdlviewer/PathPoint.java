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


public class PathPoint
{
    protected int unknown2;
    protected Vertex3D point;


    public PathPoint()
    {
    }




    public int getUnknown2()
    {
	return unknown2;
    }
    public void setUnknown2 (int i)
    {
	unknown2 = i;
    }

    public Vertex3D getPoint()
    {
	return point;
    }
    public void setPoint (Vertex3D p)
    {
	point = p;
    }


    public String toString()
    {
	return unknown2 + ", " + point;
    }

}
