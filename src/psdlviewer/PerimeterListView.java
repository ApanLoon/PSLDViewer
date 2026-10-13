package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of perimeter vertices.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import javax.swing.BoxLayout;

import javax.swing.event.ListSelectionEvent;

import java.awt.Component;
import java.awt.Dimension;

import java.awt.dnd.DropTargetDropEvent;

public class PerimeterListView
extends ListView
{
    public PerimeterListView (String name)
    {
	super (name, "Vertex, block", null);
	jTable.setDefaultEditor (PerimeterVertex.class,
				 new PerimeterVertexCellEditor());
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
     * @param mode defines where a row should be added. ADD_LAST, ADD_BEFORE
     *             or ADD_AFTER.
     *
     */
    protected void addRow (int mode)
    {
	PerimeterVertex v = new PerimeterVertex ((char)0, (char)-1);
	PerimeterList l = (PerimeterList) model.getList();
	int index = 0;

	switch (mode)
	{
	case ADD_LAST:
	    index = l.size();
	    break;

	case ADD_BEFORE:
	    index = jTable.getSelectedRow();
	    if (index == -1)
	    {
		index = 0;
	    }
	    break;

	case ADD_AFTER:
	    int[] sel = jTable.getSelectedRows();
	    if (sel.length == 0)
	    {
		index = 0;
	    }
	    else
	    {
		index = sel[sel.length - 1] + 1;
	    }
	    break;
	}

	l.add (index, v); // Inserts before index
//	model.getList().add (new PerimeterVertex ((char)0, (char)-1));
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
	    VertexRef vr = o[i];
	    model.getList().add (index++,
				 new PerimeterVertex (vr.getIndex(),
						      (char)-1));
	}
	model.fireTableDataChanged();
    }

}

