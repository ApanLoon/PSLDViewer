package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of booleans.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import javax.swing.BoxLayout;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JMenuItem;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;

import java.awt.Component;
import java.awt.Dimension;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class BoolListView
extends ListView
{

    public BoolListView (String name)
    {
	super (name, "-", null);
	initialize();
    }

    public BoolListView (Element root)
    {
	super ("", "-", null);
	initialize();
	setOptions (root);
    }




    protected void initialize()
    {
	menu.getComponent (1).setEnabled (false);
	menu.getComponent (2).setEnabled (false);


//	jTable.setDefaultEditor (Height.class, new HeightCellEditor());
	model.setEditable (true);
    }




    /** Sets the options for this view reading elements in the given
     *  DOM element.
     *
     */
/*
    public void setOptions (Element root)
    {
	setName (root.getAttribute ("NAME"));
    }
*/

    /** @return a DOM element containing the options for this view.
     *
     */
/*
    public Element getOptions (Document doc)
    {
	Element root = doc.createElement ("HEIGHT_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }
*/






    // ActionListener methods:
    //
    //
/*
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Adjust...".equals (cmd))
	{
	    adjustSelectedHeights();
	}
	else
	{
	    super.actionPerformed (e);
	}
    }
*/











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

	if (jTable.getRowCount() == 0)
	{
	    //	    removeBlockSelectionListener ((BlockView)dataView);
	    setDataView (null);
	}

    }


    /** Adds a row to this table.
     *
     *  Override this method in subclasses.
     *
     */
    protected void addRow (int mode)
    {
	// TODO: All modes adds last, should add a height relocation for BEFORE
        //       and AFTER modes.

	model.getList().add (new Height (0));
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

