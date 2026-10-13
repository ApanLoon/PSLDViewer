package psdlviewer;
import psdlviewer.*;

/** Cell editor for block references.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

import java.util.StringTokenizer;

public class BlockRefCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public BlockRefCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	char block;
	block = (char)Integer.parseInt (textField.getText(), 16);
	return new BlockRef (block);
    }
}
