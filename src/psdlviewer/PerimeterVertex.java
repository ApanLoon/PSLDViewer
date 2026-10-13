package psdlviewer;
import psdlviewer.*;

/** Reference to a vertex and connecting block, defining the perimeter of a
 *  block.
 *
 */

public class PerimeterVertex
extends VertexRef
{
    char  connectingBlock;

    public PerimeterVertex (char index_,
			    char block)
    {
	super (index_);
	connectingBlock = block;
    }

    public PerimeterVertex (UShort index_,
			    UShort block)
    {
	super (index_);
	connectingBlock = block.getCharValue();
    }


    public String toString()
    {
	return UShort.toHexString (index)
	       + ", " + ((connectingBlock == (char)-1)
			 ? "-"
			 : UShort.toHexString (connectingBlock));
    }

    public char getConnectingBlock()
    {
	return connectingBlock;
    }

    public void setConnectingBlock (char i)
    {
	connectingBlock = i;
    }
}
