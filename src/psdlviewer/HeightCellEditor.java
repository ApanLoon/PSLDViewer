package psdlviewer;
import psdlviewer.*;

/** Cell editor for heights.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

public class HeightCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public HeightCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	String s = textField.getText().trim();
	s = s.replaceAll ("[^0-9.]*", "");
	return new Height (Float.parseFloat (s));
    }
}
