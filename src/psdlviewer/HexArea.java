package psdlviewer;
import psdlviewer.*;

/** Subclass of JTextArea for viewing and editing a hex dump of bytes.
 *
 */

import java.awt.Font;
import javax.swing.JTextArea;


public class HexArea
extends JTextArea
{
    protected byte[] data;
    protected Font defaultFont = null;

    public HexArea ()
    {
	setDefaultFont();
	setEditable (false);
    }

    public HexArea (byte[] d)
    {
	setDefaultFont();
	setData (d);
	setEditable (false);
    }



    public void setData (byte[] d)
    {
	data = d;
	setText (hexDump (data));
    }




    protected String hexDump (byte[] d)
    {
	String s = "";
	if (d == null)
	    return s;
 
	String row = "";

	int rowStartIndex = 0;
	int i;
	for (i = 0; i < d.length; i++)
	{
	    row += getPaddedHexString (((int)d[i]) & 0xff, 2) + " ";

	    if ((i + 1) % 16 == 0)
	    {
		row = getPaddedHexString (rowStartIndex, 8)
		      + ": "
		      + row
		      + getAscii (d, i - 15, 16)
		      + "\n";
		s += row;
		row = "";
		rowStartIndex = i + 1;
	    }
	    else if ((i + 1) % 8 == 0)
	    {
		row += " ";
	    }
	}

	int n = i - rowStartIndex;
	if (n > 0)
	{
	    while (row.length() < 16 * 3 + 1)
	    {
		row += ' ';
	    }
	    row = getPaddedHexString (rowStartIndex, 8)
		  + ": "
		  + row
		  + getAscii (d, i - n, n)
		  + "\n";
		s += row;
	}
	return s;
    }

    protected String getPaddedHexString (int i, int n)
    {
	String s = Integer.toHexString (i);
	while (s.length() < n)
	{
	    s = '0' + s;
	}
	return s;
    }


    protected String getAscii (byte[] buf,
			       int offset,
			       int len)
    {
	String ascii = new String (buf, offset, len);
	char[] ch = ascii.toCharArray();

	String s = "";
	int j = 0;
	for (int i = offset; i < offset + len; i++)
	{
	    int c = ((int)buf[i]) & 0xff;
	    if (c < 32 || c > 127)
	    {
		ch[j] = '.';
	    }
	    j++;
	}
	return new String (ch);
    }



    public void setDefaultFont()
    {
	if (defaultFont == null)
	{
	    int    size  = 12;
	    int    style = Font.PLAIN;
	    String name  = "Courier";
	    defaultFont = new Font (name, style, size);
	}
	setFont (defaultFont);
    }



}
