package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of city blocks.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JMenuItem;

import javax.swing.BoxLayout;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;

import java.awt.Component;
import java.awt.Dimension;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class BlockListView
extends ListView
{
    protected Vector blockSelectionListeners = new Vector();


    protected static BlockSearch blockSearch;




    public BlockListView (String name)
    {
	super (name, "Type", null);
	initialize();
    }

    public BlockListView (Element root)
    {
	super ("", "Type", null);
	initialize();

	setOptions (root);
    }


    protected void initialize()
    {
//	menu.getComponent (1).setEnabled (false);
//	menu.getComponent (2).setEnabled (false);


	blockSearch = new BlockSearch (this);

	JMenuItem item = new JMenuItem ("Find blocks...");
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_F);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_F,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Search for blocks");
	menu.add (item);



	jTable.getColumnModel().getColumn (1).setCellRenderer (new BlockCellRenderer());
	jTable.setDefaultEditor (Block.class, new BlockCellRenderer());
	model.setEditable (true);
    }





    public void setList (Vector v)
    {
	super.setList (v);
	setDataView (null);
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
	Element root = doc.createElement ("BLOCK_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }



    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Find blocks...".equals (cmd))
	{
	    blockSearch.setVisible (true);
	}
	else
	{
	    super.actionPerformed (e);
	}
    }





    public void addBlockSelectionListener (BlockSelectionListener l)
    {
	blockSelectionListeners.add (l);
    }

    public void removeBlockSelectionListener (BlockSelectionListener l)
    {
	blockSelectionListeners.remove (l);
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

	if (jTable.getRowCount() == 0)
	{
	    removeBlockSelectionListener ((BlockView)dataView);
	    setDataView (null);
	}

    }


    /** Adds a row to this table.
     *
     * @param mode defines where a row should be added. ADD_LAST, ADD_BEFORE
     *             or ADD_AFTER. When ADD_BEFORE or ADD_AFTER is used, the
     *             perimeters of all blocks are bumped to maintain the
     *             references.
     *
     * TODO: Add bumping of "roads" section.
     *
     */
    protected void addRow (int mode)
    {

	BlockList l = (BlockList)model.getList();
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
	l.add (index, new Block()); // Inserts before index
	model.fireTableDataChanged();
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
	    BlockView bv = new BlockView();
	    setDataView (bv);
//	    setDividerLocation (0.5);

	    addBlockSelectionListener ((BlockView)dataView);
	}

	Object[] selection = getSelectedValues();

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

