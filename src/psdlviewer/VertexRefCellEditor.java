package psdlviewer;
import psdlviewer.*;

/** Cell editor for vertex references.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

import java.util.StringTokenizer;

public class VertexRefCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public VertexRefCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	char vertex;
	String s = textField.getText().trim();
	vertex = (char)Integer.parseInt (s, 16);
	return new VertexRef (vertex);
    }
}
