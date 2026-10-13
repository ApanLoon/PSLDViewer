package psdlviewer.bai;
import psdlviewer.*;

/** Component displaying a list of routes.
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


public class RouteListView
extends ListView
{
    public RouteListView (String name)
    {
	super (name, "-", new RouteView());
	initialize();
    }

    public RouteListView (Element root)
    {
	super ("", "-", null);
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

	setDataView (new RouteView());

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
	Element root = doc.createElement ("ROUTE_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }








    protected void refreshDataView (Route b)
    {
	if (b != null)
	{
	    ((RouteView)dataView).setRoute (b);
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
	    refreshDataView ((Route)selection[0]);
	}

	// Notify the route list that selection has changed:
	//
	RouteList l = (RouteList)getList();
	l.routesSelected (selection);
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

