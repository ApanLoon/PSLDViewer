package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

/** List of pathpoints. This class provide selection notification mechanisms
 *  for use by various views as well as a plain list of pathpoints.
 *
 */
public class PathPointList
extends Vector
{
    protected Vector pathPointSelectionListeners = new Vector();

    public PathPoint pathPointAt (int i)
    {
	return (PathPoint)super.elementAt (i);
    }



    // PathPointSelection methods:
    //
    //

    public void addPathPointSelectionListener (PathPointSelectionListener l)
    {
	pathPointSelectionListeners.add (l);
    }

    public void removePathPointSelectionListener (PathPointSelectionListener l)
    {
	pathPointSelectionListeners.remove (l);
    }


    protected void firePathPointSelection (Object[] o)
    {
	for (int i = 0; i < pathPointSelectionListeners.size(); i++)
	{
	    PathPointSelectionListener l = (PathPointSelectionListener)
		                        pathPointSelectionListeners.elementAt (i);
	    l.pathPointsSelected (o);
	}
    }


    public void pathPointsSelected (Object[] o)
    {
	firePathPointSelection (o);
    }
}
