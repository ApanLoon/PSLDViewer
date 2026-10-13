package psdlviewer;
import psdlviewer.*;

/** Base class for all block attributes and representation of unknown
 *  attribute types.
 *
 */

import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Enumeration;

import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class BlockAttribute
{
    // Road strip attributes:
    public static final char ATTRIBUTE_0000    = 0x0000;
    public static final char ATTRIBUTE_0002    = 0x0002;
    public static final char ATTRIBUTE_0003    = 0x0003;
    public static final char ATTRIBUTE_0004    = 0x0004;
    public static final char ATTRIBUTE_0005    = 0x0005;
    public static final char ATTRIBUTE_0006    = 0x0006;
    public static final char ATTRIBUTE_0007    = 0x0007;

    public static final char ATTRIBUTE_0008    = 0x0008;
    public static final char ATTRIBUTE_000a    = 0x000a;
    public static final char ATTRIBUTE_000b    = 0x000b;
    public static final char ATTRIBUTE_000c    = 0x000c;
    public static final char ATTRIBUTE_000d    = 0x000d;
    public static final char ATTRIBUTE_000e    = 0x000e;
    public static final char ATTRIBUTE_000f    = 0x000f;

    public static final char ATTRIBUTE_0010    = 0x0010;
    public static final char ATTRIBUTE_0012    = 0x0012;
    public static final char ATTRIBUTE_0013    = 0x0013;
    public static final char ATTRIBUTE_0014    = 0x0014;
    public static final char ATTRIBUTE_0015    = 0x0015;
    public static final char ATTRIBUTE_0016    = 0x0016;
    public static final char ATTRIBUTE_0017    = 0x0017;

    //Crosswalk rectangle:
    public static final char ATTRIBUTE_0024    = 0x0024;

    //Road triangle fans:
    public static final char ATTRIBUTE_0028    = 0x0028;
    public static final char ATTRIBUTE_0029    = 0x0029;
    public static final char ATTRIBUTE_002a    = 0x002a;
    public static final char ATTRIBUTE_002b    = 0x002b;
    public static final char ATTRIBUTE_002c    = 0x002c;
    public static final char ATTRIBUTE_002d    = 0x002d;
    public static final char ATTRIBUTE_002e    = 0x002e;
    public static final char ATTRIBUTE_002f    = 0x002f;

    // Triangle strip attributes:
    public static final char ATTRIBUTE_0030    = 0x0030;
    public static final char ATTRIBUTE_0031    = 0x0031;
    public static final char ATTRIBUTE_0032    = 0x0032;
    public static final char ATTRIBUTE_0033    = 0x0033;
    public static final char ATTRIBUTE_0034    = 0x0034;
    public static final char ATTRIBUTE_0035    = 0x0035;
    public static final char ATTRIBUTE_0036    = 0x0036;
    public static final char ATTRIBUTE_0037    = 0x0037;

    // Divided road strip attributes:
    public static final char ATTRIBUTE_0040    = 0x0040;
    public static final char ATTRIBUTE_0042    = 0x0042;
    public static final char ATTRIBUTE_0043    = 0x0043;
    public static final char ATTRIBUTE_0044    = 0x0044;
    public static final char ATTRIBUTE_0045    = 0x0045;
    public static final char ATTRIBUTE_0046    = 0x0046;
    public static final char ATTRIBUTE_0047    = 0x0047;


    public static final char ATTRIBUTE_0048    = 0x0048;
    // Railing/Tunnel:
    public static final char ATTRIBUTE_004b    = 0x004b;


    public static final char TEXTURE_ATTRIBUTE = 0x0050;
    public static final char ATTRIBUTE_0051    = 0x0051;

    // Facade Attributes:
    public static final char ATTRIBUTE_001c    = 0x001c;
    public static final char ATTRIBUTE_003c    = 0x003c;
    public static final char ATTRIBUTE_005e    = 0x005e;

    // Roof attributes:
    public static final char ATTRIBUTE_0060    = 0x0060;
    public static final char ATTRIBUTE_0062    = 0x0062;
    public static final char ATTRIBUTE_0063    = 0x0063;
    public static final char ATTRIBUTE_0064    = 0x0064;
    public static final char ATTRIBUTE_0065    = 0x0065;
    public static final char ATTRIBUTE_0066    = 0x0066;
    public static final char ATTRIBUTE_0067    = 0x0067;






    
    protected static Hashtable attributeTypes = null;


    protected char   id;
    protected boolean lastAttribute;
    public Vector data = new Vector();



    protected static void init()
    {
	attributeTypes = new Hashtable();

	// Road sections:
	attributeTypes.put (new Character (ATTRIBUTE_0000),
			    Attribute0000.class);
	attributeTypes.put (new Character (ATTRIBUTE_0002),
			    Attribute0002.class);
	attributeTypes.put (new Character (ATTRIBUTE_0003),
			    Attribute0003.class);
	attributeTypes.put (new Character (ATTRIBUTE_0004),
			    Attribute0004.class);
	attributeTypes.put (new Character (ATTRIBUTE_0005),
			    Attribute0005.class);
	attributeTypes.put (new Character (ATTRIBUTE_0006),
			    Attribute0006.class);
	attributeTypes.put (new Character (ATTRIBUTE_0007),
			    Attribute0007.class);

	attributeTypes.put (new Character (ATTRIBUTE_0008),
			    Attribute0008.class);
	attributeTypes.put (new Character (ATTRIBUTE_000a),
			    Attribute000a.class);
	attributeTypes.put (new Character (ATTRIBUTE_000b),
			    Attribute000b.class);
	attributeTypes.put (new Character (ATTRIBUTE_000c),
			    Attribute000c.class);
	attributeTypes.put (new Character (ATTRIBUTE_000d),
			    Attribute000d.class);
	attributeTypes.put (new Character (ATTRIBUTE_000e),
			    Attribute000e.class);
	attributeTypes.put (new Character (ATTRIBUTE_000f),
			    Attribute000f.class);

	// Road sections (No sidewalks):
	attributeTypes.put (new Character (ATTRIBUTE_0010),
			    Attribute0010.class);
	attributeTypes.put (new Character (ATTRIBUTE_0012),
			    Attribute0012.class);
	attributeTypes.put (new Character (ATTRIBUTE_0013),
			    Attribute0013.class);
	attributeTypes.put (new Character (ATTRIBUTE_0014),
			    Attribute0014.class);
	attributeTypes.put (new Character (ATTRIBUTE_0015),
			    Attribute0015.class);
	attributeTypes.put (new Character (ATTRIBUTE_0016),
			    Attribute0016.class);
	attributeTypes.put (new Character (ATTRIBUTE_0017),
			    Attribute0017.class);


	attributeTypes.put (new Character (ATTRIBUTE_001c),
			    Attribute001c.class);
	attributeTypes.put (new Character (ATTRIBUTE_0024),
			    Attribute0024.class);

	attributeTypes.put (new Character (ATTRIBUTE_0028),
			    Attribute0028.class);
	attributeTypes.put (new Character (ATTRIBUTE_0029),
			    Attribute0029.class);
	attributeTypes.put (new Character (ATTRIBUTE_002a),
			    Attribute002a.class);
	attributeTypes.put (new Character (ATTRIBUTE_002b),
			    Attribute002b.class);
	attributeTypes.put (new Character (ATTRIBUTE_002c),
			    Attribute002c.class);
	attributeTypes.put (new Character (ATTRIBUTE_002d),
			    Attribute002d.class);
	attributeTypes.put (new Character (ATTRIBUTE_002e),
			    Attribute002e.class);
	attributeTypes.put (new Character (ATTRIBUTE_002f),
			    Attribute002f.class);

	attributeTypes.put (new Character (ATTRIBUTE_0030),
			    Attribute0030.class);
	attributeTypes.put (new Character (ATTRIBUTE_0031),
			    Attribute0031.class);
	attributeTypes.put (new Character (ATTRIBUTE_0032),
			    Attribute0032.class);
	attributeTypes.put (new Character (ATTRIBUTE_0033),
			    Attribute0033.class);
	attributeTypes.put (new Character (ATTRIBUTE_0034),
			    Attribute0034.class);
	attributeTypes.put (new Character (ATTRIBUTE_0035),
			    Attribute0035.class);
	attributeTypes.put (new Character (ATTRIBUTE_0036),
			    Attribute0036.class);
	attributeTypes.put (new Character (ATTRIBUTE_0037),
			    Attribute0037.class);

	attributeTypes.put (new Character (ATTRIBUTE_003c),
			    Attribute003c.class);

	// Divided roads:
	attributeTypes.put (new Character (ATTRIBUTE_0040),
			    Attribute0040.class);
	attributeTypes.put (new Character (ATTRIBUTE_0042),
			    Attribute0042.class);
	attributeTypes.put (new Character (ATTRIBUTE_0043),
			    Attribute0043.class);
	attributeTypes.put (new Character (ATTRIBUTE_0044),
			    Attribute0044.class);
	attributeTypes.put (new Character (ATTRIBUTE_0045),
			    Attribute0045.class);
	attributeTypes.put (new Character (ATTRIBUTE_0046),
			    Attribute0046.class);
	attributeTypes.put (new Character (ATTRIBUTE_0047),
			    Attribute0047.class);

	attributeTypes.put (new Character (ATTRIBUTE_0048),
			    Attribute0048.class);
	attributeTypes.put (new Character (ATTRIBUTE_004b),
			    Attribute004b.class);

	attributeTypes.put (new Character (TEXTURE_ATTRIBUTE),
			    TextureAttribute.class);
	attributeTypes.put (new Character (ATTRIBUTE_0051),
 			    Attribute0051.class);
	attributeTypes.put (new Character (ATTRIBUTE_005e),
 			    Attribute005e.class);

	attributeTypes.put (new Character (ATTRIBUTE_0060),
 			    Attribute0060.class);
	attributeTypes.put (new Character (ATTRIBUTE_0062),
 			    Attribute0062.class);
	attributeTypes.put (new Character (ATTRIBUTE_0063),
 			    Attribute0063.class);
	attributeTypes.put (new Character (ATTRIBUTE_0064),
 			    Attribute0064.class);
	attributeTypes.put (new Character (ATTRIBUTE_0065),
 			    Attribute0065.class);
	attributeTypes.put (new Character (ATTRIBUTE_0066),
 			    Attribute0066.class);
	attributeTypes.put (new Character (ATTRIBUTE_0067),
 			    Attribute0067.class);
    }

    public BlockAttribute()
    {
	if (attributeTypes == null)
	{
	    init();
	}
    }

    /** Return a string listing all known block attributes by number and name.
     *
     * TODO: How to get this numerically sorted?
     *
     */
    public static String getAttributeNames()
    {
	if (attributeTypes == null)
	{
	    init();
	}

	ArrayList list = new ArrayList();
	for (Enumeration t = attributeTypes.elements(); t.hasMoreElements();)
	{
	    try
	    {
		Class c = (Class)t.nextElement();
		BlockAttribute a = (BlockAttribute)c.newInstance();
		list.add (a.toString());
	    }
	    catch (IllegalAccessException e)
	    {
		System.err.println ("BlockAttribute.getAttributeName: " + e);
	    }
	    catch (InstantiationException e)
	    {
		System.err.println ("BlockAttribute.getAttributeName: " + e);
	    }
	}

	Collections.sort (list);
	String s = "";
	for (int i = 0; i < list.size(); i++)
	{
	    s += list.get (i) + "\n";
	}
	return s;
    }


    public static BlockAttribute getInstance (char        id,
					      VertexList  vertexList,
					      HeightList  heightList,
					      TextureList textureList)
    {
	if (attributeTypes == null)
	{
	    init();
	}



	try
	{
	    boolean lastAttribute = ((id & 0x80) != 0);
	    id = (char)(id & 0x7f);

	    Class c = (Class)attributeTypes.get (new Character (id));
	    if (c == null)
	    {
		c = BlockAttribute.class;
	    }

	    BlockAttribute a = (BlockAttribute)c.newInstance();
	    a.setLast (lastAttribute);
	    a.id = id;
	    a.setLists (vertexList, heightList, textureList);
	    return a;
	}
	catch (IllegalAccessException e)
	{
	    System.err.println ("BlockAttribute.getInstance: " + e);
	}
	catch (InstantiationException e)
	{
	    System.err.println ("BlockAttribute.getInstance: " + e);
	}
	return null;
    }


    public void setLists (VertexList  vertexList,
			  HeightList  heightList,
			  TextureList textureList)
    {
    }




    public String toHexString()
    {
	String reply = "";

	for (int i = 0; i < data.size(); i++)
	{
	    String s = ((UShort)data.elementAt (i)).toHexString();
	    reply += s;

	    if (   ((i + 1) % 8) == 0
		||  (i + 1)      == data.size())
	    {
		reply += "\n";
	    }
	    else
	    {
		reply += ", ";
	    }
	}
	return reply;
    }





    public long read (InputStream in, long maxLength)
    throws IOException
    {
	for (int i = 0; i < maxLength; i++)
	{
	    data.add (UShort.parseUShort (in));
	}
	return maxLength;
    }

    public void write (OutputStream out)
    throws IOException, PSDLWriteException
    {
	writeID (out);
	for (int i = 0; i < data.size(); i++)
	{
	    UShort u = (UShort)data.elementAt (i);
	    u.write (out);
	}
    }

    protected void writeID (OutputStream out)
    throws IOException
    {
	UShort.write ((char)(id + (isLast() ? 0x80 : 0x00)), out);
    }

    /** Adds offsets to table references in this attribute.
     *
     *  Override this in subclasses to correctly add to all references.
     *
     */
    public void addOffsets (int vertexOffset,
			    int heightOffset,
			    int textureOffset,
			    int blockOffset)
    {
    }


    public String toString()
    {
	return UShort.toHexString (id) + "?";
    }



    public boolean isLast()
    {
	return lastAttribute;
    }
    public void setLast (boolean b)
    {
	lastAttribute = b;
    }

    public char getID()
    {
	return id;
    }

    public long getLength()
    {
	return 1 + data.size();
    }



    public Vector getData()
    {
	return data;
    }
}
