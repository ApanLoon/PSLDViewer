package psdlviewer;
import psdlviewer.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Component displaying a list of INST components.
 *
 */

import java.util.Vector;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;

import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.event.ListSelectionEvent;

public class INSTComponentListView
extends ListView
implements DataView
{

    public INSTComponentListView (String name)
    {
	super (name, "Block", new INSTComponentView());

	// Add duplicate function to pop up menu:
	JMenuItem item;

	item = new JMenuItem ("Duplicate");
	item.addActionListener (this);
	menu.add (item);
    }


    public INSTComponentListView (Element root)
    {
	super ("INST", "Block", new INSTComponentView());
	initialize();

	setOptions (root);
    }


    protected void initialize()
    {
	jTable.setDefaultEditor (INSTComponent.class, new INSTCellEditor (this));
	model.setEditable (true);

	setDividerLocation (0.25);
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
	Object[] types = {"Simple", "Coordinate system"};
	String s = (String)JOptionPane.showInputDialog (this,
						   "Select component type",
						   "Add INST component",
						   JOptionPane.PLAIN_MESSAGE,
						   null,
						   types,
						   "Simple");

	if ((s != null) && (s.length() > 0))
	{
	    INSTComponent c = null;
	    if ("Simple".equals (s))
	    {
		c = new INSTSimpleComponent();
	    }
	    else if ("Coordinate system".equals (s))
	    {
		c = new INSTCoordinateSystemComponent();
	    }

	    INSTList l = (INSTList)model.getList();
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

	    l.add (index, c); // Inserts before index
	    model.fireTableDataChanged();
	}


/*
	HexInputDialog dialog = new HexInputDialog (this,
						  "Add block attribute",
						  "Enter block attribute ID:");
	dialog.show();
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
*/
    }




    public void setList (INSTList v)
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




    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	Object[] selection = getSelectedValues();
	if (selection.length > 0)
	{
	    INSTComponentView dv = (INSTComponentView)dataView;
	    dv.setINSTComponent ((INSTComponent)selection[0]);

//	    setDividerLocation (0.5);
	}
	else
	{
//	    setDividerLocation (1.0);
	}
	revalidate();
    }







    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Duplicate".equals (cmd))
	{
	    duplicateSelected();
	}
	else
	{
	    super.actionPerformed (e);
	}
    }



    protected void duplicateSelected()
    {
	int[] indices = jTable.getSelectedRows();
	for (int i = 0; i < indices.length; i++)
	{
	    INSTList l = (INSTList)model.getList();
	    INSTComponent c = l.componentAt (indices[i]);
	    l.add (c.clone());
	}
	model.fireTableDataChanged();
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



    // DataView methods:
    //
    //
    public Component getComponent()
    {
	return this;
    }

}
