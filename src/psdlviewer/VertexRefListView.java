package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of vertice references.
 *
 */

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DropTargetDropEvent;

public class VertexRefListView
extends ListView
{
    public VertexRefListView (String name)
    {
	super (name, "Vertex", null);
	jTable.setDefaultEditor (VertexRef.class,
				 new VertexRefCellEditor());
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
	// TODO: All modes adds last, should take care of of BEFORE
        //       and AFTER modes.

	model.getList().add (new VertexRef ((char)0));
	model.fireTableDataChanged();
    }











    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	Object[] selection = getSelectedValues();
	Config.getVertexList().vertexRefsSelected (selection);
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

    /** Handles VertexRefs dropped onto this component.
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
	    model.getList().add (index++, o[i]);
	}
	model.fireTableDataChanged();
    }

}

