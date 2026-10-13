package psdlviewer;
import psdlviewer.*;

/** Base class for components displaying a list and a representation of the
 *  selected item.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JOptionPane;
import javax.swing.JViewport;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.Panel;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public abstract class ListView
extends JSplitPane
implements ListSelectionListener, ActionListener, MouseListener,
	   SimpleDNDSourceTarget, PropertyChangeListener
{

    protected static final int ADD_LAST   = 0;
    protected static final int ADD_BEFORE = 1;
    protected static final int ADD_AFTER  = 2;


    /** Location of the divider, used when changing data view.
     *  -1 means that there is no data view.
     */
    protected int dividerLocation = -1;


    protected Vector      list;

    protected String viewName;

    protected JPanel      listPanel = new JPanel();
    protected JLabel title;
    protected ListTableModel model           = new ListTableModel();
    protected ExtendedJTable jTable          = new ExtendedJTable (model);
    protected JScrollPane    tableScrollPane;

    protected JPopupMenu     menu = new JPopupMenu();

    protected DataView    dataView = null;




    protected DNDComponent dndComponent = new DNDComponent (this,
							    jTable,
							    DNDComponent.COPY);








    public ListView (String     name,
		     String     columnHeader,
		     DataView   dataView_)
    {
	super();
	initialize (name, columnHeader, dataView_);
    }



    protected void initialize (String name,
			       String columnHeader,
			       DataView dataView_)
    {
	viewName = name;

	title = new JLabel (viewName);
	title.setAlignmentX (0f);
	title.setAlignmentY (0f);

	jTable.setBorder (BorderFactory.createBevelBorder (BevelBorder.LOWERED));
	jTable.getColumnModel().getColumn (0).setHeaderValue ("Index");
	jTable.getColumnModel().getColumn (1).setHeaderValue (columnHeader);
	jTable.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);

	jTable.getSelectionModel().addListSelectionListener (this);

	listPanel.setLayout (new BorderLayout());
	listPanel.add (title, BorderLayout.NORTH);
	tableScrollPane = new JScrollPane (jTable);
	listPanel.add (tableScrollPane);

	setLeftComponent (listPanel);
	setDataView (dataView_);
	setDividerSize (2);

	setDividerLocation (0.5);
	setPreferredListSize (getDividerLocation());
	dividerLocation = getDividerLocation();

	createPopupMenu();
	jTable.addMouseListener (this);

	addPropertyChangeListener (this);

	revalidate();
    }




    protected void setPreferredListSize (int width)
    {
/* TODO: These lines causes the growing bug:
	Dimension d = new Dimension (width,
				     listPanel.getPreferredSize().height);
	listPanel.setPreferredSize (d);
*/
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




    protected DataView getDataView()
    {
	return dataView;
    }

    protected void setDataView (DataView dv)
    {
	dataView = dv;
	if (dataView != null)
	{
	    setRightComponent (dataView.getComponent());

	    if (dividerLocation == -1)
	    {
		setDividerLocation (0.5);
		dividerLocation = getDividerLocation();
	    }
	    else
	    {
		setDividerLocation (dividerLocation);
	    }
	}
	else
	{
	    setRightComponent (null);
	    setDividerLocation (1.0);
	    dividerLocation = -1;
	}
	revalidate();
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
	model.fireTableRowsUpdated (0, jTable.getRowCount());
    }




    public void setList (Vector v)
    {
	ListTableModel model = (ListTableModel)jTable.getModel();
	model.setList (v);
    }



    public Vector getList()
    {
	return model.getList();
    }







    public void clearSelection()
    {
	jTable.clearSelection();
    }


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






    public int[] getSelectedIndices()
    {
	return jTable.getSelectedRows();
    }




    /** @return an array containing the values of column 1 in all selected
     *          rows.
     *
     */
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



    protected class ListTableModel
    extends AbstractTableModel
    {
	protected boolean isEditable = false;

	protected Vector values = new Vector();


	public ListTableModel()
	{
	    super();
	}


	public void setEditable (boolean editable)
	{
	    isEditable = editable;
	}

	public void setList (Vector v)
	{
	    values = v;
	    fireTableDataChanged();
	}


	public Vector getList()
	{
	    return values;
	}


	public int getColumnCount()
	{
	    return 2;
	}

	public int getRowCount()
	{
	    return values == null ? 0 : values.size();
	}

	public Object getValueAt (int row,
				  int col)
	{
	    Object ret = null;

	    if (values != null)
	    {
		switch (col)
		{
		case 0:
		    ret = new String (Integer.toHexString (row));
		    break;
		    
		case 1:
		    ret = values.elementAt (row);
		    break;
		}
	    }
	    return ret;
	}


	public void setValueAt (Object v,
				int    row,
				int    col)
	{
	    if (col == 1 && values != null)
	    {
		values.setElementAt (v, row);
		fireTableCellUpdated (row, col);
	    }
	}



	public boolean isCellEditable (int row,
				       int col)
        {
	    return isEditable && (col == 1);
	}


	public Class getColumnClass (int columnIndex)
	{
	    if (columnIndex == 0)
	    {
		return String.class;
	    }
	    else
	    {
		if (values != null && values.size() >= 1)
		{
		    return values.elementAt (0).getClass();
		}
		else
		{
		    return null;
		}
	    }
	}

    }









    /** Sets the options for this view reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	System.err.println ("ListView.setOptions: Top level method called.");
    }

    /** @return a DOM element containing the options for this view.
     *
     */
    public Element getOptions (Document doc)
    {
	System.err.println ("ListView.getOptions: Top level method called.");
	return null;
    }



    // PropertyChangeListener method:
    //
    //
    public void propertyChange (PropertyChangeEvent evt)
    {
	if (JSplitPane.DIVIDER_LOCATION_PROPERTY.equals (evt.getPropertyName()))
	{
	    Integer width = (Integer)evt.getNewValue();
	    setPreferredListSize (width.intValue());
	    dividerLocation = width.intValue();
	}
    }


}
