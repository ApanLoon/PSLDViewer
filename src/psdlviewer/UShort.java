package psdlviewer;
import psdlviewer.*;

/** Unsigned 16 bit integer.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class UShort
{
    protected char value;



    public UShort (char v)
    {
	value = v;
    }


    public void write (OutputStream out)
    throws IOException
    {
	write (value, out);
    }


    public String toHexString()
    {
	return toHexString (value);
    }


    public char getCharValue()
    {
	return value;
    }


    public void add (int i)
    {
	value += i;
    }

    public String toString()
    {
	return toHexString();
    }


    // Static methods:
    //
    //


    public static UShort parseUShort (InputStream in)
    throws IOException
    {
	byte[] buf = new byte[2];
	in.read (buf, 0, 2);

	// Stupid stunt to avoid the signed byte:
	//
	//
	char u = 0;
	int shift = 8;
	for (int i = 1; i >= 0; i--)
	{
	    int msb = buf[i] & 0x80;
	    int v = (int)(buf[i] & 0x7f) | msb;
	    u += v << shift;
	    shift -= 8;
	}
	return new UShort (u);
    }




    public static void write (char c, OutputStream out)
    throws IOException
    {
	out.write ((int)((c >>>  0) & 0xff));
	out.write ((int)((c >>>  8) & 0xff));
    }



    public static String toHexString (char c)
    {
	String s = "";
	s += Integer.toHexString ((int)c);
	while (s.length() < 4)
	{
	    s = "0" + s;
	}
	return s;
    }
}
