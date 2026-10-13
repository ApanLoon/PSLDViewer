package psdlviewer;
import psdlviewer.*;

/** ANSI C float
 *
 *  IEEE 754
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class CFloat
{
    public static float parseCFloat (InputStream in)
    throws IOException
    {
	byte[] buf = new byte[4];
	in.read (buf, 0, 4);

	// Stupid stunt to avoid the signed byte:
	//
	//
	long bits = 0;
	int shift = 24;
	for (int i = 3; i >= 0; i--)
	{
	    int msb = buf[i] & 0x80;
	    int v = (int)(buf[i] & 0x7f) | msb;
	    bits += v << shift;
	    shift -= 8;
	}
	return Float.intBitsToFloat ((int)bits);
    }


    public static void write (float f, OutputStream out)
    throws IOException
    {
	long l = Float.floatToRawIntBits (f);
	out.write ((int)((l >>>  0) & 0xff));
	out.write ((int)((l >>>  8) & 0xff));
	out.write ((int)((l >>> 16) & 0xff));
	out.write ((int)((l >>> 24) & 0xff));
    }

}
