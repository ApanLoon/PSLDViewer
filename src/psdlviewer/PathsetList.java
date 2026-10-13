package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

/** List of pathsets. This class provide selection notification mechanisms
 *  for use by various views as well as a plain list of pathsets.
 *
 */
public class PathsetList
extends Vector
{
    protected Vector pathsetSelectionListeners = new Vector();

    public Pathset pathsetAt (int i)
    {
	return (Pathset)super.elementAt (i);
    }



    // PathsetSelection methods:
    //
    //

    public void addPathsetSelectionListener (PathsetSelectionListener l)
    {
	pathsetSelectionListeners.add (l);
    }

    public void removePathsetSelectionListener (PathsetSelectionListener l)
    {
	pathsetSelectionListeners.remove (l);
    }


    protected void firePathsetSelection (Object[] o)
    {
	for (int i = 0; i < pathsetSelectionListeners.size(); i++)
	{
	    PathsetSelectionListener l = (PathsetSelectionListener)
		                        pathsetSelectionListeners.elementAt (i);
	    l.pathsetsSelected (o);
	}
    }


    public void pathsetsSelected (Object[] o)
    {
	firePathsetSelection (o);
    }
}
