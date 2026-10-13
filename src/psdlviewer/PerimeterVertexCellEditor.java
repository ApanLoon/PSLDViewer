package psdlviewer;
import psdlviewer.*;

/** Cell editor for perimeter vertices.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

import java.util.StringTokenizer;

public class PerimeterVertexCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public PerimeterVertexCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	char vertex;
	char block;
	StringTokenizer t = new StringTokenizer (textField.getText(), ",");
	String s = t.nextToken().trim();
	vertex = (char)Integer.parseInt (s, 16);
	s = t.nextToken().trim();
	if ("-".equals (s.trim()))
	{
	    block = (char)-1;
	}
	else
	{
	    block = (char)Integer.parseInt (s, 16);
	}
	return new PerimeterVertex (vertex, block);
    }
}
