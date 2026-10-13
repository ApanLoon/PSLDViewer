package psdlviewer;
import psdlviewer.*;

/** Default view of a vertex list attribute.
 *
 */

public class VertexListAttributeView
extends BlockAttributeView
{
    VertexListAttribute attribute;

    protected VertexRefListView vertices = new VertexRefListView ("Vertices");

    public VertexListAttributeView (BlockAttribute a_)
    {
	super (a_, false);
	attribute = (VertexListAttribute)a_;

	// Remove title:
	panel.remove (0);

	vertices.setList (attribute.getData());
	panel.add (vertices, fillC);
    }

}

