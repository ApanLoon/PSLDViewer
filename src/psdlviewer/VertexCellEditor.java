package psdlviewer;
import psdlviewer.*;

/** Cell editor for vertices.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

import java.util.StringTokenizer;

public class VertexCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public VertexCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	return new Vertex3D (textField.getText());
    }
}
