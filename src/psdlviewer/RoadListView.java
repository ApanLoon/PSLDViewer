package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of roads.
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class RoadListView
extends ListView
{

    protected Vector roadSelectionListeners = new Vector();


    public RoadListView (String name)
    {
	super (name, "-", new RoadView());
	initialize();
    }


    public RoadListView (Element root)
    {
	super ("", "-", new RoadView());
	initialize();

	setOptions (root);
    }



    protected void initialize()
    {
	RoadList l = new RoadList();
	super.setList (l);
	setList (l);
    }





    /** Sets the options for this view reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	setName (root.getAttribute ("NAME"));
    }

    /** @return a DOM element containing the options for this view.
     *
     */
    public Element getOptions (Document doc)
    {
	Element root = doc.createElement ("ROAD_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }



    public void addRoadSelectionListener (RoadSelectionListener l)
    {
	roadSelectionListeners.add (l);
    }

    public void removeRoadSelectionListener (RoadSelectionListener l)
    {
	roadSelectionListeners.remove (l);
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
	int index = 0;
	RoadList l = (RoadList) model.getList();
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

	l.add (index, new Road()); // Inserts before index
	model.fireTableDataChanged();
    }



    public void setList (RoadList v)
    {
	System.err.println ("setList: " + model.getList().getClass());
	RoadList old = (RoadList)model.getList();
	if (old != null)
	{
	    // TODO: Should this really be removed?
	    removeRoadSelectionListener (old);

	    old.removeRoadSelectionListener ((RoadView)getDataView());
	}
	super.setList (v);

	v.addRoadSelectionListener ((RoadView)getDataView());
    }





    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

/*
	if (getDataView() == null)
	{
	    RoadView v = new RoadView();
	    setDataView (v);
	    addRoadSelectionListener (v);
	}
*/

	Object[] selection = getSelectedValues();

	// Notify the list that selection has changed:
	//
	RoadList l = (RoadList)getList();
	l.roadsSelected (selection);
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
