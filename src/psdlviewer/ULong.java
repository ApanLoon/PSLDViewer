package psdlviewer;
import psdlviewer.*;

/** Unsigned 32 bit integer.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class ULong
{
    public static long parseULong (InputStream in)
    throws IOException
    {
	byte[] buf = new byte[4];
	in.read (buf, 0, 4);

	// Stupid stunt to avoid the signed byte:
	//
	//
	long u = 0;
	int shift = 24;
	for (int i = 3; i >= 0; i--)
	{
	    int msb = buf[i] & 0x80;
	    int v = (int)(buf[i] & 0x7f) | msb;
	    u += v << shift;
	    shift -= 8;
	}
	return u;
    }



    public static void write (long l, OutputStream out)
    throws IOException
    {
	out.write ((int)((l >>>  0) & 0xff));
	out.write ((int)((l >>>  8) & 0xff));
	out.write ((int)((l >>> 16) & 0xff));
	out.write ((int)((l >>> 24) & 0xff));
    }
}
