package psdlviewer;
import psdlviewer.*;

/** Base class for INST components
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.lang.Cloneable;

public abstract class INSTComponent
implements Cloneable
{
    public static final int COORDINATESYSTEM = 0x00;
    public static final int SIMPLE           = 0x80;


    protected int    blockID;
    protected int    unknown;
    protected String pkgName;



    public static INSTComponent getInstance (InputStream in)
    throws IOException
    {
	int blockID = UShort.parseUShort (in).getCharValue() - 1;
	int unknown = UShort.parseUShort (in).getCharValue();

	String pkgName = "";
	int pkgNameLen = in.read();
	int type = pkgNameLen & 0x80;
	pkgNameLen &= 0x7f;

	if (pkgNameLen != 0)
	{
	    byte[] name = new byte[pkgNameLen - 1];
	    in.read (name, 0, pkgNameLen - 1);
	    int dummy = in.read(); // Skip terminating '\0'.
	    pkgName = new String (name);
	}

	INSTComponent c = getInstance (type);
	c.setBlockID (blockID);
	c.setUnknown (unknown);
	c.setPKGName (pkgName);
	c.read (in);
	return c;
    }


    public static INSTComponent getInstance (int type)
    {
	INSTComponent c = null;

	switch (type)
	{
	case SIMPLE:
	    c = new INSTSimpleComponent();
	    break;
	case COORDINATESYSTEM:
	    c = new INSTCoordinateSystemComponent();
	    break;
	}
	return c;
    }



    protected INSTComponent()
    {
    }

    protected INSTComponent (int blockID_,
			     int unknown_,
			     String pkgName_)
    {
	blockID = blockID_;
	unknown = unknown_;
	pkgName = pkgName_;
    }





    public Object clone()
    {
	Object o = null;
	try
	{
	    o = super.clone();
	}
	catch (Exception e)
	{
	}
	return o;
    }





    public void write (OutputStream out)
    throws IOException
    {
	UShort.write ((char)(blockID + 1), out);
	UShort.write ((char)unknown, out);

	byte[] buf = pkgName.getBytes();
	out.write (buf.length + 1 | getType());
	out.write (buf);
	out.write (0x00);
    }



    public void setBlockID (int i)
    {
	blockID = i;
    }
    public void setUnknown (int i)
    {
	unknown = i;
    }
    public void setPKGName (String s)
    {
	pkgName = s;
    }

    public int getBlockID()
    {
	return blockID;
    }
    public int getUnknown()
    {
	return unknown;
    }
    public String getPKGName()
    {
	return pkgName;
    }


    public abstract int getType();
    public abstract void read (InputStream in) throws IOException;



    public String toString()
    {
	return Integer.toHexString (blockID);
    }




    protected static void debug (String s)
    {
	System.out.print (s);
    }

}



