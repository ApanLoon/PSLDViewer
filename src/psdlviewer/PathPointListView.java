package psdlviewer;
import psdlviewer.*;

/** Comonent displaying a list of pathpoints.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JMenuItem;

import javax.swing.event.ListSelectionEvent;

import java.awt.Container;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PathPointListView
extends ListView
implements PathPointSelectionListener
{
    public PathPointListView (String name)
    {
	super (name, "Unknown2, X, Y, Z", null);
	initialize();
    }


    public PathPointListView (Element root)
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

	model.setEditable (true);

//	jTable.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
//	model.setEditable (true);
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
	Element root = doc.createElement ("VERTEX_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }




    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();
	/*
	if ("Translate...".equals (cmd))
	{
	    translateSelectedVertices();
	}
	else
	{
	    super.actionPerformed (e);
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

	Object[] selection = getSelectedValues();
	PathPointList l = (PathPointList)getList();
	l.pathPointsSelected (selection);
    }










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
     */
    protected void addRow (int mode)
    {
	// TODO: All modes adds last, should add a vertex relocation for BEFORE
        //       and AFTER modes.

	model.getList().add (new PathPoint());
	model.fireTableDataChanged();
    }




    public void pathPointsSelected (Object[] p)
    {
    }

}
