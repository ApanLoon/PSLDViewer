package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of city blocks and their INST components.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;

import javax.swing.BoxLayout;

import javax.swing.event.ListSelectionEvent;

import java.awt.Component;
import java.awt.Dimension;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class INSTBlockListView
extends ListView
{
    protected Vector blockSelectionListeners = new Vector();

    public INSTBlockListView (String name)
    {
	super (name, "-", new INSTComponentListView ("Components"));
	initialize();
    }

    public INSTBlockListView (Element root)
    {
	super ("INST", "-", new INSTComponentListView ("Components"));
	initialize();

	setOptions (root);
    }


    protected void initialize()
    {
	menu.getComponent (0).setEnabled (false);
	menu.getComponent (1).setEnabled (false);
	menu.getComponent (2).setEnabled (false);
	menu.getComponent (3).setEnabled (false);

	model.setEditable (false);

	setDividerLocation (0.25);
    }





    public void setList (Vector v)
    {
	super.setList (v);
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
	Element root = doc.createElement ("INST_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }








    public void addBlockSelectionListener (BlockSelectionListener l)
    {
	blockSelectionListeners.add (l);
    }

    public void removeBlockSelectionListener (BlockSelectionListener l)
    {
	blockSelectionListeners.remove (l);
    }





    protected void refreshDataView (Block b)
    {
	INSTList l = b.getINSTList();
	if (l != null)
	{
	    int i = model.getList().indexOf (b);
	    ((INSTComponentListView)dataView).setList (i, l);
	}
    }







    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	Object[] selection = getSelectedValues();
	if (   selection != null
	    && selection.length != 0)
	{
	    refreshDataView ((Block)selection[0]);
	}

	// Old method of listener invocation:
	//
	for (int i = 0; i < blockSelectionListeners.size(); i++)
	{
	    BlockSelectionListener l;
	    l = (BlockSelectionListener)blockSelectionListeners.elementAt (i);
	    l.blocksSelected (selection);
	}

	// Notify the block list that selection has changed:
	//
	BlockList l = (BlockList)getList();
	l.blocksSelected (selection);

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

