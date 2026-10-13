package psdlviewer;
import psdlviewer.*;
/** Cell renderer for pathsets in the PathsetListView table.
 *
 */

import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

import javax.swing.event.CellEditorListener;

public class PathsetCellRenderer
extends JPanel
implements TableCellRenderer, TableCellEditor
{
    protected JTextField name   = new JTextField();
    protected HexField hexField = new HexField();


    // Information about the currently edited value:
    protected Pathset currentPathset;





    public PathsetCellRenderer()
    {
	// Build typeComponent:
	//
	GridBagLayout l = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	setLayout (l);

	c.anchor  = GridBagConstraints.NORTHWEST;
	c.insets  = new Insets (0, 0, 0, 0);
	c.weighty = 1;
	c.fill = GridBagConstraints.HORIZONTAL;

	c.weightx   = 0.5;
	name.setToolTipText ("Name");
	add (name, c);

	hexField.setToolTipText ("Unknown0");
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.weightx   = 0.5;
	add (hexField, c);
    }






    /** Sets up information in, and returns the component to use for
     *  rendering/editing of a block cell.
     *
     */
    protected Component getComponent (JTable table,
				      Object value,
				      boolean isSelected,
				      boolean hasFocus,
				      int row,
				      int column)
    {
	Component c = null;
	Color background = isSelected ? table.getSelectionBackground()
	                              : table.getBackground();

	Pathset b = (Pathset)value;



	name.setText (b.getName());
	hexField.setValue (b.getUnknown0());

	setBackground (background);
	return this;
    }





    // TableCellRenderer method:
    //
    //
    public Component getTableCellRendererComponent (JTable table,
						    Object value,
						    boolean isSelected,
						    boolean hasFocus,
						    int row,
						    int column)
    {
	return getComponent (table, value, isSelected, hasFocus, row, column);
    }




    // TableCellEditor method:
    //
    //
    public Component getTableCellEditorComponent(JTable table,
						 Object value,
						 boolean isSelected,
						 int row,
						 int column)
    {
	currentPathset = (Pathset)value;
	return getComponent (table, value, true, true, row, column);
    }



    // CellEditor methods:
    //
    //
    public Object getCellEditorValue()
    {
	System.err.println ("getCellEditorValue");
	return currentPathset;
    }



    public boolean isCellEditable(EventObject anEvent)
    {
	return true;
    }
    public boolean shouldSelectCell(EventObject anEvent)
    {
	return true;
    }
    public boolean stopCellEditing()
    {

	currentPathset.setName (name.getText());
	currentPathset.setUnknown0 (hexField.getValue());

	return true;
    }
    public void cancelCellEditing()
    {
	System.err.println ("Cancel");
    }
    public void addCellEditorListener(CellEditorListener l)
    {
    }
    public void removeCellEditorListener(CellEditorListener l)
    {
    }

}

