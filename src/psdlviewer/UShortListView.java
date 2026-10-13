package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of UShorts.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DropTargetDropEvent;

public class UShortListView
extends ListView
{
    public UShortListView (String name)
    {
	super (name, "Data", null);

	jTable.setDefaultEditor (UShort.class, new UShortCellEditor());
	model.setEditable (true);
    }


    /** Removes all selected rows from this table.
     *
     */
    protected void removeSelectedRows()
    {
	int[] indices = jTable.getSelectedRows();
	for (int i = indices.length - 1; i >= 0; i--)
	{
	    model.getList().remove (indices[i]);
	}
	model.fireTableDataChanged();
    }


    /** Adds a row to this table.
     *
     *  Override this method in subclasses.
     *
     */
    protected void addRow (int mode)
    {
	// TODO: All modes adds last, should take care of BEFORE
        //       and AFTER modes.

	model.getList().add (new UShort ((char)0));
	model.fireTableDataChanged();
    }











    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
/*
	if (e.getValueIsAdjusting())
	    return;

	Object[] selection = getSelectedValues();

	for (int i = 0; i < vertexRefSelectionListeners.size(); i++)
	{
	    UShortSelectionListener l;
	    l = (UShortSelectionListener)vertexRefSelectionListeners.elementAt (i);
	    l.vertexRefsSelected (selection);
	}
*/
    }





    // SimpleDNDSourceTarget methods:
    //
    //

    /** @return the object that is dragged.
     *
     */
    public Object drag()
    {
	return null;
    }

    /** Handles UShorts dropped onto this component.
     *
     */
    public void drop (VertexRef[]         o,
		      DropTargetDropEvent event)
    {
	int index = jTable.rowAtPoint (event.getLocation());
	if (index == -1)
	{
	    index = jTable.getRowCount();
	}
	for (int i = 0; i < o.length; i++)
	{
	    model.getList().add (index++, new UShort (o[i].getIndex()));
	}
	model.fireTableDataChanged();
    }

}

