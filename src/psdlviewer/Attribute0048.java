package psdlviewer;
import psdlviewer.*;

/** 
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.util.Vector;


public class Attribute0048
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
    public char      nWalls;
    public Vector    walls = new Vector();

    public Attribute0048()
    {
	super();
	id = 0x0048;
    }


    public long read (InputStream in, long maxLength)
    throws IOException
    {
	long ushortsRead = 0;
	char nShorts = UShort.parseUShort (in).getCharValue();
	ushortsRead++;
//	data.add (new UShort (nShorts));

	flags = UShort.parseUShort (in).getCharValue();
	ushortsRead++;
//	data.add (new UShort (flags));

	leftHeight = UShort.parseUShort (in).getCharValue();
	ushortsRead++;
//	data.add (new UShort (leftHeight));

	rightHeight = UShort.parseUShort (in).getCharValue();
	ushortsRead++;
//	data.add (new UShort (rightHeight));

	nWalls = UShort.parseUShort (in).getCharValue();
	ushortsRead++;
//	data.add (new UShort (nWalls));

//	Config.debug ("a48: \n");
	for (long i = ushortsRead; i < nShorts + 1; i++)
	{
	    char data = UShort.parseUShort (in).getCharValue();
//	    Config.debug ("  " + Integer.toHexString (data) + " ");

	    ushortsRead++;
//	    super.data.add (new UShort (data));

	    for (int j = 0; j < 16; j++)
	    {
		walls.add (new Boolean (((data >> j) & 1) == 1));
//		Config.debug ("" + ((data >> j) & 1));
	    }
//	    Config.debug ("\n");
	}
	return ushortsRead;
    }


    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	writeID (out);
	UShort.write ((char)(4 + Math.ceil (walls.size() / 16)), out);
	UShort.write (flags, out);
	UShort.write (leftHeight, out);
	UShort.write (rightHeight, out);
	UShort.write (nWalls, out);

	char data = 0;
	int bit = 0;
//	Config.debug ("a48:\n");
	for (int i = 0; i < walls.size(); i++)
	{
	    if (((Boolean)walls.elementAt (i)).booleanValue())
	    {
//		Config.debug ("1");
		data |= 1 << bit;
	    }
//	    else
//		Config.debug ("0");

	    if (bit == 15)
	    {
//		Config.debug (" " + Integer.toHexString(data) + "\n");
		UShort.write (data, out);
		data = 0;
		bit = 0;
	    }
	    else
	    {
		bit++;
	    }
	}
	if (bit != 0)
	{
	    UShort.write (data, out);
	}
    }

    public long getLength()
    {
	return   1 // id
	       + 1 // len
 	       + 1 // flags
 	       + 1 // leftheight
 	       + 1 // rightheight
 	       + 1 // nWalls
	       + (long)Math.ceil (walls.size() / 16);
    }


    public String toString()
    {
	return UShort.toHexString (id) + " junction tunnel/railing";
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
