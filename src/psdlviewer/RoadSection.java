package psdlviewer;
import psdlviewer.*;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.GeometryArray;

import javax.vecmath.Point3f;
import javax.vecmath.Color3f;

import javax.media.j3d.Appearance;

/** Triangle strip of one road section.
 *
 *
 *       3------+                 +------0
 *      /      /|________________/|     /
 *     /      / /2              /1     /
 *    /      / /               /      /
 *   7------+ /               +------4
 *          |/________________|/
 *          6                 5
 *
 */

public class RoadSection
extends Shape3D
{
    protected static final float SIDEWALK_HEIGHT = 0.15f;

    protected VertexList vertices;
    protected VertexRefList references;

    public RoadSection (VertexList    v,
			VertexRefList r,
			int           firstVertex,
			Appearance    a)
    throws Exception
    {
	setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	vertices = v;
	references = r;

	int nVertices = r.size()- firstVertex;

	if (nVertices < 8)
	{
	    throw new Exception ("A road section requires eight vertices");
	}


	// Create geometry:
	//

	Color3f color1 = new Color3f();
	a.getColoringAttributes().getColor (color1);
	Color3f color2 = new Color3f (color1);
	color2.x -= 0.1f;
	color2.y -= 0.1f;
	color2.z -= 0.1f;


	Point3f p;
	int[] stripVertCounts = {12};
	TriangleStripArray tsa = new TriangleStripArray (12,
					  TriangleStripArray.COORDINATES
					| TriangleStripArray.COLOR_3,
					stripVertCounts);
		tsa.setCapability (GeometryArray.ALLOW_COLOR_READ);
		tsa.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
		tsa.setCapability (GeometryArray.ALLOW_COUNT_READ);
		tsa.setCapability (GeometryArray.ALLOW_FORMAT_READ);
		tsa.setCapability (GeometryArray.ALLOW_NORMAL_READ);
		tsa.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
		tsa.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	tsa.setCoordinate (0, getPoint (3 + firstVertex));
	tsa.setCoordinate (1, getPoint (7 + firstVertex));

	p = getPoint (2 + firstVertex);
	tsa.setCoordinate (4, p);
	p.y += SIDEWALK_HEIGHT;
	tsa.setCoordinate (2, p);

	p = getPoint (6 + firstVertex);
	tsa.setCoordinate (5, p);
	p.y += SIDEWALK_HEIGHT;
	tsa.setCoordinate (3, p);

	p = getPoint (1 + firstVertex);
	tsa.setCoordinate (6, p);
	p.y += SIDEWALK_HEIGHT;
	tsa.setCoordinate (8, p);

	p = getPoint (5 + firstVertex);
	tsa.setCoordinate (7, p);
	p.y += SIDEWALK_HEIGHT;
	tsa.setCoordinate (9, p);

	tsa.setCoordinate (10, getPoint (0 + firstVertex));
	tsa.setCoordinate (11, getPoint (4 + firstVertex));

	tsa.setColor (0, color1);
	tsa.setColor (1, color1);
	tsa.setColor (2, color1);
	tsa.setColor (3, color1);
	tsa.setColor (4, color2);
	tsa.setColor (5, color2);
	tsa.setColor (6, color2);
	tsa.setColor (7, color2);
	tsa.setColor (8, color1);
	tsa.setColor (9, color1);
	tsa.setColor (10, color1);
	tsa.setColor (11, color1);


	setGeometry (tsa);
    }

    protected Point3f getPoint (int i)
    {
	VertexRef vr = references.referenceAt (i);
	return vertices.vertexAt (vr.getIndex()).toPoint3f();
    }
}
