package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of paths.
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

public class PathListView
extends ListView
{
    protected Vector pathSelectionListeners = new Vector();

//    protected Vector dataViews = new Vector();


    public PathListView (String name)
    {
	super (name, "Path", null);
//	super (name, "Path", new PathView());



	menu.getComponent (0).setEnabled (false);
	menu.getComponent (1).setEnabled (false);
	menu.getComponent (2).setEnabled (false);
	menu.getComponent (3).setEnabled (false);

	model.setEditable (false);
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
//	    dataViews.remove (indices[i]);
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
	PathList l = (PathList) model.getList();
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
	Path p = new Path();
	l.add (index, p); // Inserts before index

//	DataView v = new PathView (p);
//	dataViews.add (index, v);

	model.fireTableDataChanged();
    }



    public void setList (PathList v)
    {
	super.setList (v);
	setDataView (null);
/*
	super.setList (v);
	dataViews.clear();
	PathList aList = (PathList)model.getList();
	for (int i = 0; i < aList.size(); i++)
	{
	    Path a = aList.pathAt (i);
	    DataView dv = new PathView (a);
	    dataViews.add (dv);
	}
*/
    }





    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	if (getDataView() == null)
	{
	    PathView bv = new PathView();
	    setDataView (bv);
//	    setDividerLocation (0.5);

	    addPathSelectionListener ((PathView)dataView);
	}


	Object[] selection = getSelectedValues();

	// Old method of listener invocation:
	//
	for (int i = 0; i < pathSelectionListeners.size(); i++)
	{
	    PathSelectionListener l;
	    l = (PathSelectionListener)pathSelectionListeners.elementAt (i);
	    l.pathsSelected (selection);
	}


	// Notify the pathset list that selection has changed:
	//
	PathList l = (PathList)getList();
	l.pathsSelected (selection);

/*
	int i = jTable.getSelectedRow();
	if (i != -1)
	{
	    setDataView ((DataView)dataViews.elementAt (i));
//	    setDividerLocation (0.5);
	}
	else
	{
	    setDataView (null);
//	    setDividerLocation (1.0);
	}
	revalidate();
*/
    }





    public void addPathSelectionListener (PathSelectionListener l)
    {
	pathSelectionListeners.add (l);
    }

    public void removePathSelectionListener (PathSelectionListener l)
    {
	pathSelectionListeners.remove (l);
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
