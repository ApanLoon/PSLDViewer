package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

/** List of pathsets. This class provide selection notification mechanisms
 *  for use by various views as well as a plain list of pathsets.
 *
 */
public class PathList
extends Vector
{
    protected Vector pathSelectionListeners = new Vector();

    public Path pathAt (int i)
    {
	return (Path)super.elementAt (i);
    }



    // PathSelection methods:
    //
    //

    public void addPathSelectionListener (PathSelectionListener l)
    {
	pathSelectionListeners.add (l);
    }

    public void removePathSelectionListener (PathSelectionListener l)
    {
	pathSelectionListeners.remove (l);
    }


    protected void firePathSelection (Object[] o)
    {
	for (int i = 0; i < pathSelectionListeners.size(); i++)
	{
	    PathSelectionListener l = (PathSelectionListener)
		                        pathSelectionListeners.elementAt (i);
	    l.pathsSelected (o);
	}
    }


    public void pathsSelected (Object[] o)
    {
	firePathSelection (o);
    }
}
