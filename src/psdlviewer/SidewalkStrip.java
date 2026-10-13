package psdlviewer;
import psdlviewer.*;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.GeometryArray;

import javax.vecmath.TexCoord2f;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;


/** Sidewalk strip, stand alone or part of a list of road sections.
 *
 * Stand alone:
 *
 *             Right
 *         1 +------+
 *          /      /|
 *         /      / + 0
 *      3 +------+ /
 *       /      /|/
 *      /      / + 2
 *   5 +------+ /
 *            |/
 *            + 4
 *
 * Road section:
 *
 *            Right                     Left
 *         3 +------+                 +------+ 0
 *          /      /|                /|     /
 *         /      / +---------------/-+    /
 *        /      / /2              / /1   /
 *     7 +------+ /               +------+ 4
 *      /      /|/               /|/    /
 *     /      / +---------------/-+    /
 *    /      / /6              / /5   /
 * b +------+ /               +------+ 8
 *          |/                |/
 *          +-----------------+
 *          a                 9
 *
 *
 * Divided road section:
 *
 *             Right                     Left
 *          5 +------+        +---+        +------+ 0
 *           /      /|       /|  /|       /|     /
 *          /      / +------/-+-/-+------/-+    /
 *         /      / /4     / /3/ /2     / /1   /
 *      b +------+ /      +---+ /      +------+ 6
 *       /      /|/      /|/ /|/      /|/    /
 *      /      / +------/-+-/-+------/-+    /
 *     /      / /a     / /9/ /8     / /7   /
 * 11 +------+ /      +---+ /      +------+ c
 *           |/       |/  |/       |/
 *           +--------+---+--------+
 *          10     f  e        d
 *
 */

public class SidewalkStrip
extends Shape3D
{
    public static final int STANDALONE  = 0;
    public static final int ROAD        = 1;
    public static final int DIVIDEDROAD = 2;

    protected static final float SIDEWALK_HEIGHT = 0.15f;

    protected VertexList vertices;
    protected VertexRefList references;



    /** Create a sidewalk strip.
     *
     * @param type One of STANDALONE, ROAD and DIVIDEDROAD.
     * @param left Determines if this sidewalk should be left or right 
     *             oriented. Standalone sidewalks should be right oriented.
     */
    public SidewalkStrip (int           type,
			  boolean       left,
			  VertexList    vertices_,
			  VertexRefList references_,
			  TextureStore  ts,
			  TextureMapRef tr,
			  Appearance    a)
    throws Exception
    {
	setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	int sectionLength = 2;
	switch (type)
	{
	case STANDALONE:
	    sectionLength = 2;
	    break;

	case ROAD:
	    sectionLength = 4;
	    break;

	case DIVIDEDROAD:
	    sectionLength = 6;
	    break;
	}
	build (vertices_,
	       references_,
	       sectionLength,
	       left,
	       ts,
	       tr,
	       a);
    }








    /** Builds a sidewalk geometry.
     *
     * @param vertices_ Reference to the vertex store.
     * @param references_ List of the vertex references to use for this
     *                    sidewalk.
     * @param sectionLength The number of vertex references that make up one 
     *                      segment. 2 for a standalone sidewalk, 4 for a 
     *                      regular road and 6 for a divided road.
     * @param left If true, build the left sidewalk otherwise the right. Note 
     *             that this affect the orientation of  stand alone sidewalks.
     *             In MM2 these should be "right sided".
     * 
     * @param ts Reference to the texture store.
     * @param tr The texture to use.
     *
     * @param a The appearance to use if not texture mapped.
     *
     */
    protected void build (VertexList    vertices_,
			  VertexRefList references_,
			  int           sectionLength,
			  boolean       left,
			  TextureStore  ts,
			  TextureMapRef tr,
			  Appearance    a)
    throws Exception
    {
	vertices = vertices_;
	references = references_;

	int nVertices = references.size();
	int nCrossSections = nVertices / sectionLength;

	if (nCrossSections < 2)
	{
	    throw new Exception ("A sidewalk strip requires at least two cross sections");
	}

	// Variables for geometry:
	Point3f outer;
	Point3f inner;
	Point3f road;
	int     offset;       // How far in to start.
	int     roadOffset;   // How far from start is the road vertex.
	int     outerOffset;  // How far from start is the outer vertex.

	int[] stripVertCounts = {nCrossSections * 2, nCrossSections * 2};
	TriangleStripArray tsa;


	// Variables for texture mapping:
	float      scale    = 1.0f;    // Tile is 1.0f meter wide.
	TexCoord2f t0       = null;
	TexCoord2f t1       = null;
	Point3f    lastRoad = null;

	// These are used if we are not using textures:
	Color3f color1 = null;
	Color3f color2 = null;



	if (left)
	{
	    offset      = 0;
	    roadOffset  = 1;
	    outerOffset = 0;
	}
	else
	{
	    offset      = sectionLength - 2;
	    roadOffset  = 0;
	    outerOffset = 1;
	}


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

	    t0 = new TexCoord2f (1f, 0f);
	    t1 = new TexCoord2f (1f, 1f);

	    lastRoad = getPoint (offset + roadOffset);

	    tsa = new TriangleStripArray (
				nCrossSections * 2 * 2,
				  TriangleStripArray.COORDINATES
				| TriangleStripArray.TEXTURE_COORDINATE_2,
				stripVertCounts);
	}
	else
	{
	    color1 = new Color3f();
	    a.getColoringAttributes().getColor (color1);
	    color2 = new Color3f (color1);
	    color2.x -= 0.1f;
	    color2.y -= 0.1f;
	    color2.z -= 0.1f;

	    tsa = new TriangleStripArray (
				nCrossSections * 2 * 2,
				  TriangleStripArray.COORDINATES
				| TriangleStripArray.COLOR_3,
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
	int v = nCrossSections * 2;
	for (int i = offset; i < nVertices; i += sectionLength)
	{
	    outer = getPoint (i + outerOffset);
	    road  = getPoint (i + roadOffset);
	    inner = new Point3f (road);
	    inner.y += SIDEWALK_HEIGHT;

	    if (tr != null)
	    {
		float d = Math.abs (road.distance (lastRoad));
		t0.x -= d / scale;
		t1.x = t0.x;
		lastRoad = road;

		tsa.setTextureCoordinate (0, v,     t0);   // Vertical strip
		tsa.setTextureCoordinate (0, v + 1, t0);   // Vertical strip
		tsa.setTextureCoordinate (0, h,     t0);   // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1, t1);   // Horizontal strip
	    }
	    else
	    {
		tsa.setColor (v,     color2);   // Vertical strip
		tsa.setColor (v + 1, color2);   // Vertical strip
		tsa.setColor (h,     color1);   // Horizontal strip
		tsa.setColor (h + 1, color1);   // Horizontal strip
	    }

	    tsa.setCoordinate (v,     road);         // Vertical strip
	    tsa.setCoordinate (v + 1, inner);        // Vertical strip
	    tsa.setCoordinate (h,     inner);        // Horizontal strip
	    tsa.setCoordinate (h + 1, outer);        // Horizontal strip

	    v += 2;
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
