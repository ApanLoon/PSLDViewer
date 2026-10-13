package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class Attribute004b
extends BlockAttribute
{

    public static final int LEFT_BARRIER               = 0x0001;
    public static final int RIGHT_BARRIER              = 0x0002;
    public static final int WALL                       = 0x0004;
    public static final int FLAT_CEILING               = 0x0008;
    public static final int CLOSE_START_LEFT_BARRIER   = 0x0010;
    public static final int CLOSE_END_LEFT_BARRIER     = 0x0020;
    public static final int CLOSE_START_RIGHT_BARRIER  = 0x0040;
    public static final int CLOSE_END_RIGHT_BARRIER    = 0x0080;
    public static final int SUBDIVIDED_CEILING         = 0x0100;
    public static final int OFFSET_START_LEFT_BARRIER  = 0x0200;
    public static final int OFFSET_END_LEFT_BARRIER    = 0x0400;
    public static final int OFFSET_START_RIGHT_BARRIER = 0x0800;
    public static final int OFFSET_END_RIGHT_BARRIER   = 0x1000;
    public static final int SUBDIVIDED_INNER_WALLS     = 0x2000;
    public static final int THICK_BARRIERS             = 0x4000;
    public static final int UNKNOWN                    = 0x8000;


    public char      flags;
    public char      leftHeight;   // (ushort)(height * 256)
    public char      rightHeight;   // (ushort)(height * 256)



    public Attribute004b()
    {
	super();
	id = 0x004b;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	flags       = UShort.parseUShort (in).getCharValue();
	leftHeight  = UShort.parseUShort (in).getCharValue();
	rightHeight = UShort.parseUShort (in).getCharValue();
	return 3;
    }



    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	writeID (out);
	UShort.write (flags, out);
	UShort.write (leftHeight, out);
	UShort.write (rightHeight, out);
    }


    public long getLength()
    {
	return 1 + 1 + 1 + 1;
    }



    public String toString()
    {
	return UShort.toHexString (id) + " tunnel/railing";
    }



    public int getFlags()
    {
	return flags;
    }
    public void setFlags (int f)
    {
	flags = (char)f;
    }
    public boolean getFlag (int f)
    {
	return (flags & f) != 0;
    }
    public void setFlag (int f, boolean v)
    {
	if (v)
	{
	    flags |= f;
	}
	else
	{
	    flags &= ~f;
	}
    }

    public float getLeftHeight()
    {
	return leftHeight / 256;
    }
    public void setLeftHeight (float h)
    {
	leftHeight = (char)(h * 256 + 0.5);
    }
    public float getRightHeight()
    {
	return rightHeight / 256;
    }
    public void setRightHeight (float h)
    {
	rightHeight = (char)(h * 256 + 0.5);
    }
}
