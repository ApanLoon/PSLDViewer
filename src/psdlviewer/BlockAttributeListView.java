package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of block attributes.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.BoxLayout;

import javax.swing.event.ListSelectionEvent;

public class BlockAttributeListView
extends ListView
{

    protected Vector dataViews = new Vector();


    public BlockAttributeListView (String name)
    {
	super (name, "Attribute", null);
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
	    dataViews.remove (indices[i]);
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
	HexInputDialog dialog = new HexInputDialog (this,
						  "Add block attribute",
						  "Enter block attribute ID:");
	dialog.setVisible(true);
	if (dialog.accepted())
	{
	    BlockAttribute a = BlockAttribute.getInstance 
		                                  ((char)dialog.getValue(),
						   Config.getVertexList(),
						   Config.getHeightList(),
						   Config.getTextureList());
	    BlockAttributeList l = (BlockAttributeList) model.getList();
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

	    l.add (index, a); // Inserts before index

	    DataView v = BlockAttributeView.getInstance (a);
	    dataViews.add (index, v);

	    model.fireTableDataChanged();
	}
    }



    public void setList (BlockAttributeList v)
    {
	super.setList (v);

	dataViews.clear();
	BlockAttributeList aList = (BlockAttributeList)model.getList();
	for (int i = 0; i < aList.size(); i++)
	{
	    BlockAttribute a = aList.attributeAt (i);
	    DataView dv = BlockAttributeView.getInstance (a);
	    dataViews.add (dv);
	}
    }





    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	int i = jTable.getSelectedRow();
	if (i != -1)
	{
	    boolean moveDivider = false;
	    if (getDataView() == null)
	    {
		moveDivider = true;
	    }
	    setDataView ((DataView)dataViews.elementAt (i));
	    if (moveDivider)
	    {
		setDividerLocation (0.5);
	    }
	}
	else
	{
	    setDataView (null);
	    setDividerLocation (1.0);
	}
	revalidate();
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
