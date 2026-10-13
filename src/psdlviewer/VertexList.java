package psdlviewer;
import psdlviewer.*;

/** List of vertices. This class provide selection notification mechanisms for
 *  use by various views as well as alain list of vertices.
 *
 */

import java.util.Vector;

public class VertexList
extends Vector
{
    protected Vector vertexSelectionListeners = new Vector();
    protected Vector vertexRefSelectionListeners = new Vector();


    public Vertex3D vertexAt (int i)
    {
	return (Vertex3D)super.elementAt (i);
    }

    public Vertex3D getVertex (VertexRef ref)
    {
	return (Vertex3D)super.elementAt ((int)ref.getIndex());
    }

    // VertexSelection methods:
    //
    //

    public void addVertexSelectionListener (VertexSelectionListener l)
    {
	vertexSelectionListeners.add (l);
    }

    public void removeVertexSelectionListener (VertexSelectionListener l)
    {
	vertexSelectionListeners.remove (l);
    }


    protected void fireVertexSelection (Object[] o)
    {
	for (int i = 0; i < vertexSelectionListeners.size(); i++)
	{
	    VertexSelectionListener l = (VertexSelectionListener)
		                        vertexSelectionListeners.elementAt (i);
	    l.verticesSelected (o);
	}
    }


    public void verticesSelected (Object[] o)
    {
	fireVertexSelection (o);
    }





    // VertexRefSelection methods:
    //
    //

    public void addVertexRefSelectionListener (VertexRefSelectionListener l)
    {
	vertexRefSelectionListeners.add (l);
    }

    public void removeVertexRefSelectionListener (VertexRefSelectionListener l)
    {
	vertexRefSelectionListeners.remove (l);
    }


    protected void fireVertexRefSelection (Object[] o)
    {
	for (int i = 0; i < vertexRefSelectionListeners.size(); i++)
	{
	    VertexRefSelectionListener l = (VertexRefSelectionListener)
		                        vertexRefSelectionListeners.elementAt (i);
	    l.vertexRefsSelected (o);
	}
    }


    public void vertexRefsSelected (Object[] o)
    {
	fireVertexRefSelection (o);

	// TODO: Convert refs to vertices and
	//fireVertexSelecion (v);
    }

}

