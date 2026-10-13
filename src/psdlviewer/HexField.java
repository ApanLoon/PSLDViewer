package psdlviewer;
import psdlviewer.*;

/** An input field that only accepts an hexadecimal integer.
 *
 */

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import java.lang.NumberFormatException;

public class HexField
extends JTextField
{
    /** @return the value of this HexField.
     *
     */
    public int getValue()
    {
	String s = getText();
	if (s.length() == 0)
	{
	    return 0;
	}
	else
	{
	    return Integer.parseInt (s, 16);
	}
    }

    /** Sets the value of this HexField.
     *
     */
    public void setValue (int v)
    {
	setText (Integer.toHexString (v));
    }


    protected Document createDefaultModel()
    {
	return new HexNumberDocument();
    }



    protected class HexNumberDocument
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
		try
		{
		    String s = "" + source[i];
		    int digit = Integer.parseInt (s, 16);
		    result[j++] = source[i];		    
		}
		catch (NumberFormatException e)
		{
		    Toolkit.getDefaultToolkit().beep();
		}
	    }    
	    super.insertString (offs, new String (result, 0, j), a);
	}
    }


}
