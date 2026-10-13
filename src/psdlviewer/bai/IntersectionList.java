package psdlviewer.bai;
import psdlviewer.*;

public class IntersectionList
extends java.util.Vector
{

    public Intersection intersectionAt (int i)
    {
	return (Intersection)super.elementAt (i);
    }
}
