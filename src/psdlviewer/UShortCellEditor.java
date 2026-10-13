package psdlviewer;
import psdlviewer.*;

/** Cell editor for ushorts.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

public class UShortCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public UShortCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	char c = (char)Integer.parseInt (textField.getText(), 16);
	return new UShort (c);
    }
}
