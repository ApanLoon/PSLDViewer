package psdlviewer;
import psdlviewer.*;

/** City block.
 *
 */

public class Block
{
    public static final int TYPE_BIT0 = 1;
    public static final int TYPE_BIT1 = 2;
    public static final int TYPE_BIT2 = 4;
    public static final int TYPE_BIT3 = 8;
    public static final int TYPE_BIT4 = 16;
    public static final int TYPE_BIT5 = 32;
    public static final int TYPE_BIT6 = 64;
    public static final int TYPE_BIT7 = 128;

    public static final int TYPE_SUB       = 2;
    public static final int TYPE_PLAIN     = 4;
    public static final int TYPE_ROAD      = 8;
    public static final int TYPE_CROSSING  = 16;
    public static final int TYPE_INST      = 128;

    public static final String[] TYPE_NAME = 
    {
	"Bit 0: Unknown",
	"Bit 1: Subterrainean",
	"Bit 2: Plain",
	"Bit 3: Road",
	"Bit 4: Intersection",
	"Bit 5: Unknown",
	"Bit 6: Unknown",
	"Bit 7: INST?"
    };


    protected PerimeterList      perimeter  = new PerimeterList();
    protected BlockAttributeList attributes = new BlockAttributeList();
    protected int                type = TYPE_PLAIN;
    protected int                unknown = 0x00;

    protected INSTList           instList = new INSTList();


    public Block()
    {
    }



    public PerimeterList getPerimeter()
    {
	return perimeter;
    }

    public BlockAttributeList getAttributes()
    {
	return attributes;
    }


    

    public void addVertex (PerimeterVertex v)
    {
	perimeter.add (v);
    }

    public void addAttribute (BlockAttribute a)
    {
	attributes.add (a);
    }




    public void setType (int flag, boolean value)
    {
	if (value)
	{
	    type |= flag;
	}
	else
	{
	    type &= ~flag;
	}
    }
    public void setType (int type_)
    {
	type = type_;
    }
    public int getType()
    {
	return type;
    }
    public boolean isType (int type_)
    {
	return (type & type_) != 0;
    }

    public void setUnknown (int unknown_)
    {
	unknown = unknown_;
    }
    public int getUnknown()
    {
	return unknown;
    }

    public void setINSTList (INSTList l)
    {
	instList = l;
    }
    public INSTList getINSTList()
    {
	return instList;
    }


    public String toString()
    {
	return "";
    }



}
