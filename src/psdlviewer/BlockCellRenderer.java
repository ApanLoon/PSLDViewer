package psdlviewer;
import psdlviewer.*;
/** Cell renderer for blocks in the BlockListView table.
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
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

import javax.swing.event.CellEditorListener;

public class BlockCellRenderer
extends JPanel
implements TableCellRenderer, TableCellEditor
{
    protected JCheckBox[] bit   = new JCheckBox[8];
    protected HexField hexField = new HexField();


    // Information about the currently edited value:
    protected Block currentBlock;





    public BlockCellRenderer()
    {
	// Build typeComponent:
	//
	GridBagLayout l = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	setLayout (l);

	c.anchor  = GridBagConstraints.NORTHWEST;
	c.insets  = new Insets (0, 0, 0, 0);
	c.weighty = 1;

	for (int i = 7; i >= 0; i--)
	{
	    bit[i] = new JCheckBox();
	    bit[i].setMargin (new Insets (0, 0, 0, 0));
/*
	    if (i == 0)
	    {
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx   = 1;
	    }
*/
	    add (bit[i], c);
	}

	hexField.setToolTipText ("Prop rule");
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx   = 1;
	add (hexField, c);
    }



    public String getToolTipText (MouseEvent event)
    {
	// TODO: Why does not this work?
//	JComponent c = (JComponent)getComponentAt (event.getPoint());

	JComponent c = null;
	int i = 0;
	boolean found = false;
	while (i < getComponentCount() && ! found)
	{
	    c = (JComponent)getComponent (i);
	    if (c.getBounds(null).contains (event.getPoint()))
	    {
		found = true;
	    }
	    else
	    {
		i++;
	    }
	}
	if (found)
	{
	    return c.getToolTipText();
	}
	else
	{
	    return getToolTipText();
	}
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

	Block b = (Block)value;



	setToolTipText ("Type 0x" + Integer.toHexString (b.getType()));
	int type = 1;
	for (int i = 0; i < 8; i++)
	{
	    bit[i].setBackground (background);
	    bit[i].setSelected (b.isType (type));
	    bit[i].setToolTipText (Block.TYPE_NAME[i]);
	    type = type << 1;
	}
	hexField.setValue (b.getUnknown());

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
	currentBlock = (Block)value;
	return getComponent (table, value, true, true, row, column);
    }



    // CellEditor methods:
    //
    //
    public Object getCellEditorValue()
    {
	System.err.println ("getCellEditorValue");
	return currentBlock;
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

	int type = 0;
	int typeBits = 1;
	for (int i = 0; i < 8; i++)
	{
	    if (bit[i].isSelected())
	    {
		type |= typeBits;
	    }
	    typeBits = typeBits << 1;
	}
	currentBlock.setType (type);

	currentBlock.setUnknown (hexField.getValue());

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

