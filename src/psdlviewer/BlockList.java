package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

/** List of city blocks. This class provide selection notification mechanisms
 *  for use by various views as well as a plain list of blocks.
 *
 */
public class BlockList
extends Vector
{
    protected Vector blockListChangedListeners = new Vector();
    protected Vector blockSelectionListeners = new Vector();

    public Block blockAt (int i)
    {
	return (Block)super.elementAt (i);
    }


    /** Moves a block from one index to another.
     *
     */
    public void move (int from,
		      int to,
		      boolean fireEvent)
    {
	if (fireEvent)
	{
	    fireBlockMoved (from, to);
	}
        add (to, (Block)remove (from, false), false);
    }


    public void add (int   index,
		     Block insertedBlock,
		     boolean fireEvent)
    {
	super.add (index, insertedBlock);
	// Add one from block references above or to the inserted block:
	for (int b = 0; b < size(); b++)
	{
	    Block block = blockAt (b);
	    PerimeterList pl = block.getPerimeter();
	    for (int v = 0; v < pl.size(); v++)
	    {
		PerimeterVertex pv = pl.perimeterAt (v);
		int connectingBlock = (int)pv.getConnectingBlock();
		if (   connectingBlock != 0xffff
		    && connectingBlock >= index)
		{
		    pv.setConnectingBlock ((char)(connectingBlock + 1));
		}
	    }
	}

	if (fireEvent)
	{
	    fireBlockAdded (index);
	}
    }

    public Object remove (int index,
			  boolean fireEvent)
    {
	if (fireEvent)
	{
	    fireBlockRemoved (index);
	}

	// Subtract one from block references above or to the removed block:
	for (int b = 0; b < size(); b++)
	{
	    Block block = blockAt (b);
	    PerimeterList pl = block.getPerimeter();
	    for (int v = 0; v < pl.size(); v++)
	    {
		PerimeterVertex pv = pl.perimeterAt (v);
		int connectingBlock = (int)pv.getConnectingBlock();
		if (   connectingBlock != 0xffff
		    && connectingBlock >= index)
		{
		    pv.setConnectingBlock ((char)(connectingBlock - 1));
		    if (connectingBlock == index)
		    {
			pv.setConnectingBlock ((char)0xffff);
		    }
		}
	    }
	}
	Block removedBlock = (Block)super.remove (index);
	return removedBlock;
    }




    // BlockListChanged methods:
    //
    //

    public void addBlockListChangedListener (BlockListChangedListener l)
    {
	blockListChangedListeners.add (l);
    }

    public void removeBlockListChangedListener (BlockListChangedListener l)
    {
	blockListChangedListeners.remove (l);
    }


    protected void fireBlockMoved (int from,
				   int to)
    {
	for (int i = 0; i < blockListChangedListeners.size(); i++)
	{
	    BlockListChangedListener l = (BlockListChangedListener)
		                      blockListChangedListeners.elementAt (i);
	    l.blockMoved (from, to, this);
	}
    }

    protected void fireBlockAdded (int index)
    {
	for (int i = 0; i < blockListChangedListeners.size(); i++)
	{
	    BlockListChangedListener l = (BlockListChangedListener)
		                      blockListChangedListeners.elementAt (i);
	    l.blockAdded (index, this);
	}
    }

    protected void fireBlockRemoved (int index)
    {
	for (int i = 0; i < blockListChangedListeners.size(); i++)
	{
	    BlockListChangedListener l = (BlockListChangedListener)
		                      blockListChangedListeners.elementAt (i);
	    l.blockRemoved (index, this);
	}
    }



    // BlockSelection methods:
    //
    //

    public void addBlockSelectionListener (BlockSelectionListener l)
    {
	blockSelectionListeners.add (l);
    }

    public void removeBlockSelectionListener (BlockSelectionListener l)
    {
	blockSelectionListeners.remove (l);
    }


    protected void fireBlockSelection (Object[] o)
    {
	for (int i = 0; i < blockSelectionListeners.size(); i++)
	{
	    BlockSelectionListener l = (BlockSelectionListener)
		                        blockSelectionListeners.elementAt (i);
	    l.blocksSelected (o);
	}
    }


    public void blocksSelected (Object[] o)
    {
	fireBlockSelection (o);
    }
}
