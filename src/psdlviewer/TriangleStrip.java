package psdlviewer;
import psdlviewer.*;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.GeometryArray;

import javax.vecmath.Point3f;
import javax.vecmath.Color3f;
import javax.vecmath.TexCoord2f;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;

/** Triangle strip, stand alone or part of a list of road sections.
 *
 * Stand alone:
 *
 *         1 +------+ 0
 *          /      /
 *         /      /
 *      3 +------+ 2
 *       /      /
 *      /      /
 *   5 +------+ 4
 *
 *
 *
 */

public class TriangleStrip
extends Shape3D
{
    public static final int STANDALONE  = 0;

    protected VertexList vertices;
    protected VertexRefList references;








    /** Build a triangle strip geometry.
     *
     * @param vertices_ Reference to the vertex store.
     * @param references_ List of the vertex references to use for this
     *                    triangle strip.
     * @param sectionLength The number of vertex references that make up one 
     *                      segment. 2 for a standalone strip, 4 for a 
     *                      regular road and 6 for a divided road.
     * @param left If true, build the left sidewalk otherwise the right. Note 
     *             that this affect the orientation of stand alone strips.
     *             In MM2 these should be "right sided".
     * 
     * @param ts Reference to the texture store.
     * @param tr The texture to use.
     * @param swapUV Swaps the texture cordinate axis.
     *
     * @param a The appearance to us if not texture mapped.
     *
     */
    public TriangleStrip (
			  VertexList    vertices_,
			  VertexRefList references_,
			  TextureStore  ts,
			  TextureMapRef tr,
			  boolean       swapUV,
			  Appearance    a)
    throws Exception
    {
	setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	int sectionLength = 2;

	vertices = vertices_;
	references = references_;

	// Variables for geometry:
	Point3f outer;
	Point3f inner;
	int     offset      = 0;  // How far in to start.
	int     innerOffset = 0;  // How far from start is the inner vertex.
	int     outerOffset = 0;  // How far from start is the outer vertex.

	TriangleStripArray tsa;


	// Variables for texture mapping:
	float      scale     = 1.0f;    // Tile is 1.0 meter wide.
	TexCoord2f t0        = null;
	TexCoord2f t1        = null;
	Point3f    lastInner = null;

	sectionLength = 2;
	offset        = 0;
	innerOffset = 1;
	outerOffset = 0;


	int nVertices = references.size();
	int nCrossSections = nVertices / sectionLength;

	if (nCrossSections < 2)
	{
	    throw new Exception ("A triangle strip requires at least two cross sections");
	}

	// Additional variables for geometry:
	int[] stripVertCounts = {nCrossSections * 2};





	if (tr != null)
	{
	    a = new Appearance();
	    a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
	    a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
	    a.setCapability (Appearance.ALLOW_MATERIAL_READ);
	    a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
	    a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
	    a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
	    a.setCapability (Appearance.ALLOW_TEXGEN_READ);
	    a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
	    a.setCapability (Appearance.ALLOW_TEXTURE_READ);
	    a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
	    a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	    // Create polygon attributes:
	    //
	    PolygonAttributes pAttr = new PolygonAttributes();
	    pAttr.setPolygonMode (PolygonAttributes.POLYGON_FILL);
	    pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
	    a.setPolygonAttributes (pAttr);

	    Material m = new Material();
	    m.setCapability (Material.ALLOW_COMPONENT_READ);

	    m.setDiffuseColor (new Color3f (1.0f, 1.0f, 1.0f));
	    m.setAmbientColor (new Color3f (1.0f, 1.0f, 1.0f));
	    a.setMaterial (m);
	    ColoringAttributes cAttr = new ColoringAttributes();
	    cAttr.setColor (new Color3f (1.0f, 1.0f, 1.0f));
	    a.setColoringAttributes (cAttr);
	    a.setTexture (ts.getTexture (tr));

	    if (swapUV)
	    {
		t0 = new TexCoord2f (0f, 1f);
		t1 = new TexCoord2f (1f, 1f);
	    }
	    else
	    {
		t0 = new TexCoord2f (1f, 0f);
		t1 = new TexCoord2f (1f, 1f);
	    }
	    lastInner = getPoint (offset + innerOffset);

	    tsa = new TriangleStripArray (
				nCrossSections * 2,
				  TriangleStripArray.COORDINATES
				| TriangleStripArray.TEXTURE_COORDINATE_2,
				stripVertCounts);
	}
	else
	{
	    tsa = new TriangleStripArray (
				nCrossSections * 2,
				TriangleStripArray.COORDINATES,
				stripVertCounts);
	}

		tsa.setCapability (GeometryArray.ALLOW_COLOR_READ);
		tsa.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
		tsa.setCapability (GeometryArray.ALLOW_COUNT_READ);
		tsa.setCapability (GeometryArray.ALLOW_FORMAT_READ);
		tsa.setCapability (GeometryArray.ALLOW_NORMAL_READ);
		tsa.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
		tsa.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	int h = 0;
	for (int i = offset; i < nVertices; i += sectionLength)
	{
	    outer = getPoint (i + outerOffset);

	    if (innerOffset == -1)
	    {
		Point3f p0 = getPoint (i);
		Point3f p1 = getPoint (i + 1);
		inner = new Point3f();
		inner.x = (p0.x + p1.x) / 2f;
		inner.y = (p0.y + p1.y) / 2f;
		inner.z = (p0.z + p1.z) / 2f;
	    }
	    else
	    {
		inner = getPoint (i + innerOffset);
	    }

	    if (tr != null)
	    {
		float d = Math.abs (inner.distance (lastInner));
		if (swapUV)
		{
		    t0.y -= d / scale;
		    t1.y = t0.y;
		}
		else
		{
		    t0.x -= d / scale;
		    t1.x = t0.x;
		}
		lastInner = inner;

		tsa.setTextureCoordinate (0, h,     t0);
		tsa.setTextureCoordinate (0, h + 1, t1);
	    }

	    tsa.setCoordinate (h,     inner);
	    tsa.setCoordinate (h + 1, outer);

	    h += 2;
	}

	setGeometry (tsa);
	setAppearance (a);
    }






    protected Point3f getPoint (int i)
    {
	VertexRef vr = references.referenceAt (i);
	return vertices.vertexAt (vr.getIndex()).toPoint3f();
    }
}
