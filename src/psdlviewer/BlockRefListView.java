package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of block references.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import javax.swing.BoxLayout;

import javax.swing.event.ListSelectionEvent;

import java.awt.Component;
import java.awt.Dimension;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class BlockRefListView
extends ListView
{

    public BlockRefListView (String name)
    {
	super (name, "Block", null);
	initialize();
    }


    protected void initialize()
    {
	jTable.setDefaultEditor (BlockRef.class, new BlockRefCellEditor());
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
	int index = 0;
	BlockRefList l = (BlockRefList) model.getList();
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

	l.add (index, new BlockRef((char)0)); // Inserts before index
	model.fireTableDataChanged();
    }








    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

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

    /** Handles the object dropped onto this component.
     *
     *  Use methods with different signatures to handle dropped objects of
     *  different types.
     *
     */
    public void drop (Object o)
    {
    }

}

