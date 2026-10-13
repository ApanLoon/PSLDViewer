package psdlviewer;
import psdlviewer.*;

public class BlockRefList
extends java.util.Vector
{

    public BlockRef referenceAt (int i)
    {
	return (BlockRef)super.elementAt (i);
    }
}
