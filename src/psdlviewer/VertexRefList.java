package psdlviewer;
import psdlviewer.*;

public class VertexRefList
extends java.util.Vector
{

    public VertexRef referenceAt (int i)
    {
	return (VertexRef)super.elementAt (i);
    }
}
