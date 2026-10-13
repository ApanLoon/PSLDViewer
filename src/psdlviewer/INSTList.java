package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

/** List of INST components. This class provide selection notification mechanisms
 *  for use by various views as well as a plain list of components.
 *
 */
public class INSTList
extends Vector
implements BlockListChangedListener
{
    protected Vector selectionListeners = new Vector();

    public INSTComponent componentAt (int i)
    {
	return (INSTComponent)super.elementAt (i);
    }



    // ComponentSelection methods:
    //
    //

    public void addSelectionListener (INSTComponentSelectionListener l)
    {
	selectionListeners.add (l);
    }

    public void removeSelectionListener (INSTComponentSelectionListener l)
    {
	selectionListeners.remove (l);
    }


    protected void fireSelection (Object[] o)
    {
	for (int i = 0; i < selectionListeners.size(); i++)
	{
	    INSTComponentSelectionListener l = (INSTComponentSelectionListener)
		                        selectionListeners.elementAt (i);
	    l.componentsSelected (o);
	}
    }


    public void componentsSelected (Object[] o)
    {
	fireSelection (o);
    }




    /** BlockListChanged methods:
     *
     */
    public void blockMoved (int from,
			    int to,
			    BlockList list)
    {
	for (int i = 0; i < size(); i++)
	{
	    INSTComponent c = componentAt (i);
	    int blockID = c.getBlockID();

	    if (to < from)
	    {
		if (blockID >= to && blockID < from)
		{
		    c.setBlockID (blockID + 1);
		}
	    }
	    else
	    {
		if (blockID > from && blockID <= to)
		{
		    c.setBlockID (blockID - 1);
		}
	    }
	}
    }

    public void blockAdded (int index,
			    BlockList list)
    {
	for (int i = 0; i < size(); i++)
	{
	    INSTComponent c = componentAt (i);
	    int blockID = c.getBlockID();

	    if (blockID >= index)
	    {
		c.setBlockID (blockID + 1);
	    }
	}
    }

    public void blockRemoved (int index,
			      BlockList list)
    {
	for (int i = 0; i < size(); i++)
	{
	    INSTComponent c = componentAt (i);
	    int blockID = c.getBlockID();

	    if (blockID == index)
	    {
		remove (i);
	    }
	    else if (blockID > index)
	    {
		c.setBlockID (blockID - 1);
	    }
	}
    }



}
