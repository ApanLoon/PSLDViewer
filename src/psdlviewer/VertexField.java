package psdlviewer;
import psdlviewer.*;

/** An input field that only accepts a vertex.
 *
 */

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import java.lang.NumberFormatException;

public class VertexField
extends JTextField
{
    /** @return the value of this VertexField.
     *
     */
    public Vertex3D getValue()
    {
	String s = getText();
	if (s.length() == 0)
	{
	    return new Vertex3D (0, 0, 0);
	}
	else
	{
	    return new Vertex3D (s);
	}
    }

    /** Sets the value of this VertexField.
     *
     */
    public void setValue (Vertex3D v)
    {
	setText (v.toString());
    }


    protected Document createDefaultModel()
    {
	return new VertexDocument();
    }



    protected class VertexDocument
    extends PlainDocument
    {
	public void insertString (int          offs,
				  String       str,
				  AttributeSet a)
	throws BadLocationException
	{
	    char[] source = str.toCharArray();
	    char[] result = new char[source.length];
	    int j = 0;

	    for (int i = 0; i < result.length; i++)
	    {
		if (   Character.isDigit (source[i])
		    || '-' == source[i]
		    || '.' == source[i]
		    || ',' == source[i]
	            || Character.isWhitespace (source[i]))
		{
		    result[j++] = source[i];
		}
		else
		{
		    Toolkit.getDefaultToolkit().beep();
		}
	    }    
	    super.insertString (offs, new String (result, 0, j), a);
	}
    }


}
