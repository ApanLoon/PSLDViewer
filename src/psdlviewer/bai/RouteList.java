package psdlviewer.bai;
import psdlviewer.*;

import java.util.Vector;

public class RouteList
extends java.util.Vector
{
    protected Vector routeSelectionListeners = new Vector();

    public Route routeAt (int i)
    {
	return (Route)super.elementAt (i);
    }


    // RouteSelection methods:
    //
    //

    public void addRouteSelectionListener (RouteSelectionListener l)
    {
	routeSelectionListeners.add (l);
    }

    public void removeRouteSelectionListener (RouteSelectionListener l)
    {
	routeSelectionListeners.remove (l);
    }


    protected void fireRouteSelection (Object[] o)
    {
	for (int i = 0; i < routeSelectionListeners.size(); i++)
	{
	    RouteSelectionListener l = (RouteSelectionListener)
		                        routeSelectionListeners.elementAt (i);
	    l.routesSelected (o);
	}
    }


    public void routesSelected (Object[] o)
    {
	fireRouteSelection (o);
    }

}
