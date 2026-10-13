package psdlviewer;
import psdlviewer.*;

/** A DNDComponent object enables any(?) component to be used for drag and
 *  drop.
 *
 */

import java.io.IOException;

import java.awt.Component;

import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetEvent;
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

import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;


public class DNDComponent
implements DragGestureListener, DragSourceListener, DropTargetListener
{
    public static final int COPY         = DnDConstants.ACTION_COPY;
    public static final int COPY_OR_MOVE = DnDConstants.ACTION_COPY_OR_MOVE;
    public static final int LINK         = DnDConstants.ACTION_LINK;
    public static final int MOVE         = DnDConstants.ACTION_MOVE;
    public static final int NONE         = DnDConstants.ACTION_NONE;
    public static final int REFERENCE    = DnDConstants.ACTION_REFERENCE;




    /** The SimpleDNDSourceTarget.
     *
     */
    protected SimpleDNDSourceTarget dndSourceTarget;

    /** The component to drag from or drop onto.
     *
     */
    protected Component component;


    /** DragSource object.
     *
     */
    protected DragSource dragSource;

    /** DropTarget object.
     *
     */
    protected DropTarget dropTarget;





    /** Constructs the drag and drop objects needed.
     *
     */
    public DNDComponent (SimpleDNDSourceTarget st,
			 Component             c,
			 int                   actions)
    {
	dndSourceTarget = st;
	component       = c;


	dragSource = new DragSource();
	dragSource.createDefaultDragGestureRecognizer (component,
						       actions,
						       this);

	dropTarget = new DropTarget (component, this);
    }









    // DragGestureListener methods:
    //
    //

    /** Called when a drag gesture has been initiated.
     * 
     */
    public void dragGestureRecognized (DragGestureEvent event)
    {
	Object o = dndSourceTarget.drag();
	if (o == null)
	    return;

	SimpleTransferable t = new SimpleTransferable (o);
	if (t == null)
	    return;


	dragSource.startDrag (event,
			      DragSource.DefaultMoveDrop,
			      t,
			      this);
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













    // DropTargetListener methods:
    //
    //

    /** Called when user is dragging over the DropSite.
     * 
     */
    public void dragEnter (DropTargetDragEvent event)
    {
	// debug messages for diagnostics 
//	System.out.println ("dragEnter");
	event.acceptDrag (DnDConstants.ACTION_LINK);
    }

    /** Called when user exit the DropSite without dropping.
     *
     */
    public void dragExit (DropTargetEvent event)
    {
//	System.out.println ("dragExit");
    }

    /** Called when a drag operation is going on.
     * 
     */
    public void dragOver (DropTargetDragEvent event)
    {
//	System.out.println ("dragOver");
    }

    /** Called when a drop has occurred.
     * 
     */
    public void drop (DropTargetDropEvent event)
    {
	try
	{
	    Transferable t = event.getTransferable();
	    DataFlavor[] df = t.getTransferDataFlavors();
	    Object data = t.getTransferData (df[0]);

	    // TODO: Accept correct action.

	    Class c = dndSourceTarget.getClass();
	    Class[] argTypes = new Class[2];
	    argTypes[0] = data.getClass();
	    argTypes[1] = DropTargetDropEvent.class;
	    Method m = c.getMethod ("drop", argTypes);
	    Object[] args = new Object[2];
	    args[0] = data;
	    args[1] = event;

	    event.acceptDrop (DnDConstants.ACTION_LINK);

	    m.invoke (dndSourceTarget, args);
	    
	    event.getDropTargetContext().dropComplete (true);
	} 
	catch (IllegalAccessException e)
	{
	}
	catch (InvocationTargetException e)
	{
	}
	catch (NoSuchMethodException e)
	{
	    event.rejectDrop();
	}
	catch (IOException e)
	{
	    System.err.println ("DNDComponent.dropTargetDropEvent: " + e);
	    e.printStackTrace (System.err);
	    event.rejectDrop();
	}
	catch (UnsupportedFlavorException e)
	{
	    event.rejectDrop();
	}
    }

    /** Called when user modifies the current drop gesture.
     * 
     */
    public void dropActionChanged (DropTargetDragEvent event)
    {
    }







}
