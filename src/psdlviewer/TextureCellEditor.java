package psdlviewer;
import psdlviewer.*;

/** Cell editor for texture maps.
 *
 */

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

public class TextureCellEditor
extends DefaultCellEditor
{
    protected JTextField textField;

    public TextureCellEditor()
    {
	super (new JTextField());
	textField = (JTextField)getComponent();
    }


    public Object getCellEditorValue()
    {
	return new TextureMap (textField.getText(), Config.texturePath);
    }
}
