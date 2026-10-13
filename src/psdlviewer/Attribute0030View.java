package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single 0x0030 block attribute.
 *
 */

public class Attribute0030View
extends BlockAttributeView
{
    Attribute0030 attribute;

    protected VertexRefListView vertices = new VertexRefListView ("Vertices");

    public Attribute0030View (BlockAttribute a_)
    {
	super (a_, false);
	attribute = (Attribute0030)a_;

	vertices.setList (attribute.getData());
	panel.add (vertices, fillC);
    }

}

