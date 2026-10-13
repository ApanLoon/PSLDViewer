package psdlviewer;
import psdlviewer.*;

import java.util.Vector;

import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.Component;

import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureEvent;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DragSource;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class DNDJTable
extends JTable
implements DropTargetListener, DragSourceListener, DragGestureListener
{
    DND source;

    TableModel model;


    // Drag'n drop attributes:
    //
    //
    /** Enables this component to be a dropTarget
     *
     */
    DropTarget dropTarget = null;

    /** Enables this component to be a Drag Source
     *
     */
    DragSource dragSource = null;






    public DNDJTable (TableModel m, DND source_)
    {
	super (m);
	model = m;
	source = source_;

	// Initialize drag'n drop:
	//
	//
	dropTarget = new DropTarget (this, this);
	dragSource = new DragSource();
	dragSource.createDefaultDragGestureRecognizer (this,
						      DnDConstants.ACTION_LINK,
						      this);
    }













    // DropTargetListener methods:
    //
    //
    /**
     * is invoked when you are dragging over the DropSite
     * 
     */
    public void dragEnter (DropTargetDragEvent event)
    {
	// debug messages for diagnostics 
//	System.out.println ("dragEnter");
	event.acceptDrag (DnDConstants.ACTION_LINK);
    }

    /**
     * is invoked when you are exit the DropSite without dropping
     *
     */
    public void dragExit (DropTargetEvent event)
    {
//	System.out.println ("dragExit");
    }

    /**
     * is invoked when a drag operation is going on
     * 
     */
    public void dragOver (DropTargetDragEvent event)
    {
//	System.out.println ("dragOver");
    }

    /**
     * a drop has occurred
     * 
     */
    public void drop (DropTargetDropEvent event)
    {
	try
	{
	    DataFlavor df = TransferableVector.getDataFlavor();
	    Transferable transferable = event.getTransferable();
                   
	    DNDTransferable data = (DNDTransferable)transferable.
		                                       getTransferData (df);
	    event.acceptDrop (DnDConstants.ACTION_LINK);
	    DND dragSource = (DND)((DropTarget)event.getSource()).getComponent();
	    dragSource.drop (event);

/*
	    if (source == this)
	    {
		System.err.println ("Can't rearrange tables yet.");
		// TODO: Handle move within same table.
	    }
	    else
	    {
		ListView lv = (ListView)source.getParent();
		System.err.println (lv);
		int index = rowAtPoint (event.getLocation());
		Vector v = source.getSelectedValues();
		for (int i = 0; i < v.size(); i++)
		{
		    
//		    model.getList().add (v.elementAt (i), index++);
		}
//		model.fireTableDataChanged();
	    }
		
*/
	    event.getDropTargetContext().dropComplete (true);
	} 
	catch (IOException e)
	{
	    System.err.println ("DNDJTable.dropTargetDropEvent: " + e);
	    e.printStackTrace (System.err);
	    event.rejectDrop();
	}
	catch (UnsupportedFlavorException e)
	{
	    event.rejectDrop();
	}
    }

    /**
     * is invoked if the use modifies the current drop gesture
     * 
     */
    public void dropActionChanged (DropTargetDragEvent event)
    {
    }



    // DragGestureListener methods:
    //
    //
    /**
     * a drag gesture has been initiated
     * 
     */
    public void dragGestureRecognized (DragGestureEvent event)
    {
	Transferable t = source.startDrag();
	if (t != null)
	{
	    dragSource.startDrag (event,
				  DragSource.DefaultMoveDrop,
				  t,
				  this);
	}
    }


    // DragSourceListener methods:
    //
    //
    /**
     * this message goes to DragSourceListener, informing it that the dragging 
     * has ended
     * 
     */
    public void dragDropEnd (DragSourceDropEvent event)
    {   
	if (event.getDropSuccess())
	{
//	    System.err.println ("dragDropEnd");
//	    removeElement();
	}
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging 
     * has entered the DropSite
     * 
     */
    public void dragEnter (DragSourceDragEvent event)
    {
//	System.out.println ("dragEnter");
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging 
     * has exited the DropSite
     * 
     */
    public void dragExit (DragSourceEvent event)
    {
//	System.out.println ("dragExit");
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging is currently 
     * ocurring over the DropSite
     * 
     */
    public void dragOver (DragSourceDragEvent event)
    {
//	System.out.println ("dragExit");
    }

    /**
     * is invoked when the user changes the dropAction
     * 
     */
    public void dropActionChanged (DragSourceDragEvent event)
    {
//	System.out.println ("dropActionChanged"); 
    }


}

