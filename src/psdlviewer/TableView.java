package psdlviewer;
import psdlviewer.*;

/** Base class for components displaying a table of objects. The table can
 *  have several linked tables with equal number of rows. If a row is added
 *  in any of the tables, a row is added in the same place in all tables.
 *  selected item.
 *
 */

import java.util.Vector;
import java.lang.ArrayIndexOutOfBoundsException;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;

import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.Panel;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class TableView
extends JPanel
implements ListSelectionListener, ActionListener, MouseListener
//	   SimpleDNDSourceTarget
{

    protected static final int ADD_LAST   = 0;
    protected static final int ADD_BEFORE = 1;
    protected static final int ADD_AFTER  = 2;


    protected String viewName;

    protected JLabel title;
    protected MyTableModel model             = new MyTableModel();
    protected ExtendedJTable jTable          = new ExtendedJTable (model);
    protected JScrollPane    tableScrollPane;

    protected JPopupMenu     menu = new JPopupMenu();

//    protected DNDComponent dndComponent = new DNDComponent (this,
//							    jTable,
//							    DNDComponent.COPY);








    public TableView (String name)
    {
	super();
	initialize (name);
    }



    protected void initialize (String name)
    {
	viewName = name;

	title = new JLabel (viewName);
	title.setAlignmentX (0f);
	title.setAlignmentY (0f);

	jTable.setBorder (BorderFactory.createBevelBorder (BevelBorder.LOWERED));
//	jTable.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);

	jTable.getSelectionModel().addListSelectionListener (this);

	setLayout (new BorderLayout());
	add (title, BorderLayout.NORTH);
	tableScrollPane = new JScrollPane (jTable);
	add (tableScrollPane);

	createPopupMenu();
	jTable.addMouseListener (this);

	revalidate();
    }






    protected void createPopupMenu()
    {
	JMenuItem item;

	item = new JMenuItem ("Add last");
	item.addActionListener (this);
	menu.add (item);

	item = new JMenuItem ("Add before");
	item.addActionListener (this);
	menu.add (item);

	item = new JMenuItem ("Add after");
	item.addActionListener (this);
	menu.add (item);

	item = new JMenuItem ("Remove");
	item.addActionListener (this);
	menu.add (item);
    }






    public void setName (String name)
    {
	viewName = name;
	title.setText (viewName);
    }

    public String getName()
    {
	return viewName;
    }




    public void dataChanged()
    {
	MyTableModel model = (MyTableModel)jTable.getModel();
	model.fireTableRowsUpdated (0, jTable.getRowCount());
    }

    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;
/* TODO:
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
*/
    }




    public void addColumn (String heading,
			   Vector v)
    {
	MyTableModel model = (MyTableModel)jTable.getModel();
	model.addColumn (heading, v);
    }

    public void removeColumn (int i)
    {
	MyTableModel model = (MyTableModel)jTable.getModel();
	model.removeColumn (i);
    }



    public Vector getColumnData (int i)
    {
	MyTableModel model = (MyTableModel)jTable.getModel();
	return model.getColumnData (i);
    }
    public void setColumnData (int i, Vector v)
    {
	MyTableModel model = (MyTableModel)jTable.getModel();
	model.setColumnData (i, v);
    }


    public int getColumnCount()
    {
	return jTable.getColumnCount();
    }

    public int getRowCount()
    {
	return jTable.getRowCount();
    }

    public void setDefaultEditor (Class c, TableCellEditor e)
    {
	jTable.setDefaultEditor (c, e);
    }





    public void clearSelection()
    {
	jTable.clearSelection();
    }


    /*
    public void select (Object[] l)
    {
	int firstIndex = -1;
	jTable.clearSelection();

	Vector v = model.getList();
	for (int i = 0; i < l.length; i++)
	{
	    int row = v.indexOf (l[i]);
	    jTable.addRowSelectionInterval (row, row);

	    if (i == 0)
	    {
		firstIndex = row;
	    }
	}

	// Scroll to first selected row:
	if (firstIndex != -1 && tableScrollPane != null)
	{
	    tableScrollPane.getViewport().setViewPosition (
	       jTable.getCellRect (firstIndex, 0, true).getLocation());
	}
    }
    */





    public int[] getSelectedIndices()
    {
	return jTable.getSelectedRows();
    }




    /** @return an array containing the values of column 1 in all selected
     *          rows.
     *
     */
/*
    protected Object[] getSelectedValues()
    {
	Vector selection = new Vector();

	int[] indices = jTable.getSelectedRows();
	for (int i = 0; i < indices.length; i++)
	{
	    selection.add (model.getValueAt (indices[i], 1));
	}
	return selection.toArray();
    }
*/






    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Add last".equals (cmd))
	{
	    addRow (ADD_LAST);
	}
	else if ("Add before".equals (cmd))
	{
	    addRow (ADD_BEFORE);
	}
	else if ("Add after".equals (cmd))
	{
	    addRow (ADD_AFTER);
	}
	else if ("Remove".equals (cmd))
	{
	    removeSelectedRows();
	}
    }


    /** Removes all selected rows from this table.
     *
     *  Override this method in subclasses.
     *
     */
    protected void removeSelectedRows()
    {
	JOptionPane.showMessageDialog (this,
				       "Can't remove from this list.",
				       "Error",
				       JOptionPane.ERROR_MESSAGE);
    }


    /** Adds a row to this table.
     *
     *  Override this method in subclasses.
     *
     * @param mode defines where a row should be added. ADD_LAST, ADD_BEFORE
     *             or ADD_AFTER.
     *
     */
    protected void addRow (int mode)
    {
	JOptionPane.showMessageDialog (this,
				       "Can't add to this list.",
				       "Error",
				       JOptionPane.ERROR_MESSAGE);
    }





    // MouseListener methods:
    //
    //
    public void mouseClicked (MouseEvent e)
    {
    }

    public void mousePressed (MouseEvent e)
    {
        maybeShowPopup (e);
    }

    public void mouseReleased (MouseEvent e)
    {
        maybeShowPopup (e);
    }

    public void mouseEntered (MouseEvent e)
    {
    }

    public void mouseExited (MouseEvent e)
    {
    }

    private void maybeShowPopup (MouseEvent e)
    {
        if (e.isPopupTrigger())
	{
            menu.show (e.getComponent(),
		       e.getX(),
		       e.getY());
        }
    }



















    // Data model:
    //
    //



    protected class MyTableModel
    extends AbstractTableModel
    {
	protected boolean isEditable = false;

	protected Vector columns  = new Vector();
	protected Vector headings = new Vector();


	public MyTableModel()
	{
	    super();
	}


	public void setEditable (boolean editable)
	{
	    isEditable = editable;
	}



	public int getColumnCount()
	{
	    return columns.size() + 1;
	}

	public int getRowCount()
	{
	    
	    return getColumnCount() == 1
		? 0
		: ((Vector)columns.elementAt (0)).size();
	}



	public Object getValueAt (int row,
				  int col)
	throws ArrayIndexOutOfBoundsException
	{
	    Object ret = null;

	    if (col >= getColumnCount())
	    {
		throw new ArrayIndexOutOfBoundsException ("Column: "
							  + col
							  + " >= "
							  + getColumnCount());
	    }
	    if (row >= getRowCount())
	    {
		throw new ArrayIndexOutOfBoundsException ("Row: "
							  + row
							  + " >= "
							  + getRowCount());
	    }


	    if (col == 0)
	    {
		ret = new String (Integer.toHexString (row));
	    }
	    else
	    {
		ret = ((Vector)columns.elementAt (col - 1)).elementAt (row);
	    }
	    return ret;
	}

	public void setValueAt (Object v,
				int    row,
				int    col)
	{
	    if (   col >= 1
		&& col < getColumnCount())
	    {
		((Vector)columns.elementAt (col - 1)).setElementAt (v, row);
		fireTableCellUpdated (row, col - 1);
	    }
	}

	public boolean isCellEditable (int row,
				       int col)
        {
	    return isEditable && (col >= 1);
	}


	public Class getColumnClass (int columnIndex)
	{
	    Class ret = null;
	    if (columnIndex == 0)
		ret = Integer.class;
	    else
	    {
		Object o = getValueAt (0, columnIndex);
		ret = o == null ? null : o.getClass();
	    }
	    return ret;
	}

	public String getColumnName (int columnIndex)
	{
	    if (columnIndex == 0)
		return "Index";
	    else
		return (String)headings.elementAt (columnIndex - 1);
	}




	public void addColumn (String heading,
			       Vector v)
	{
	    columns.add (v);
	    headings.add (heading);
	    fireTableStructureChanged();
	    fireTableDataChanged();
	}

	public void removeColumn (int i)
	{
	    if (i < 1 || i > getColumnCount())
		return;

	    columns.removeElementAt (i - 1);
	    headings.removeElementAt (i - 1);
	    fireTableStructureChanged();
	    fireTableDataChanged();
	}


	public Vector getColumnData (int i)
	{
	    if (i == 0)
	    {
		return null;
	    }
	    else
	    {
		return (Vector)columns.elementAt (i - 1);
	    }
	}

	public void setColumnData (int i, Vector v)
	{
	    if (i > 0 && i < getColumnCount())
	    {
		columns.setElementAt (v, i - 1);
		fireTableDataChanged();
	    }
	}





    }












    /** Sets the options for this view reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	System.err.println ("TableView.setOptions: Top level method called.");
    }

    /** @return a DOM element containing the options for this view.
     *
     */
    public Element getOptions (Document doc)
    {
	System.err.println ("TableView.getOptions: Top level method called.");
	return null;
    }



}
