package psdlviewer;
import psdlviewer.*;

public class PerimeterList
extends VertexRefList
{
    public PerimeterVertex perimeterAt (int i)
    {
	return (PerimeterVertex)super.elementAt (i);
    }
}
