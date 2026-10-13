package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

public class RoadList
extends java.util.Vector
implements RoadSelectionListener
{
    protected Vector roadSelectionListeners = new Vector();

    public Road roadAt (int i)
    {
	return (Road)super.elementAt (i);
    }

    public String toString()
    {
	String s = "";
/*
	for (int i = 0; i < size(); i++)
	{
	    s += roadAt(i).toString();
	}
*/
	return s;
    }




    // RoadSelection methods:
    //
    //

    public void addRoadSelectionListener (RoadSelectionListener l)
    {
	roadSelectionListeners.add (l);
    }

    public void removeRoadSelectionListener (RoadSelectionListener l)
    {
	roadSelectionListeners.remove (l);
    }


    protected void fireRoadSelection (Object[] o)
    {
	for (int i = 0; i < roadSelectionListeners.size(); i++)
	{
	    RoadSelectionListener l = (RoadSelectionListener)
		                        roadSelectionListeners.elementAt (i);
	    l.roadsSelected (o);
	}
    }


    public void roadsSelected (Object[] o)
    {
	fireRoadSelection (o);
    }

}
