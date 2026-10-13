package psdlviewer;
import psdlviewer.*;

/** Cell editor for INST components.
 *
 */

import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JTable;
//import javax.swing.TableCellEditor;
//import javax.swing.CellEditorListener;
import javax.swing.DefaultCellEditor;

public class INSTCellEditor
//implements TableCellEditor, CellEditorListener
    extends DefaultCellEditor
{
    protected INSTComponent c;
    protected ListView listView;

    protected HexField textField;

    public INSTCellEditor (ListView lv)
    {
	super (new HexField());
	listView = lv;
	textField = (HexField)getComponent();
    }

    public Object getCellEditorValue()
    {
	c.setBlockID (textField.getValue());
	return c;
    }

    public Component getTableCellEditorComponent(JTable table,
                                             Object value,
                                             boolean isSelected,
                                             int row,
                                             int column)
    {
	c = (INSTComponent)value;
	textField.setValue (c.getBlockID());
	return textField;
    }
}
