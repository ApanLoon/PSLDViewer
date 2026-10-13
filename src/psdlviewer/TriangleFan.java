package psdlviewer;
import psdlviewer.*;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleFanArray;
import javax.media.j3d.GeometryArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

public class TriangleFan
extends Shape3D
{
    /** Create a triangle fan.
     *
     */
    public TriangleFan (VertexList v,
			VertexRefList r,
			Appearance a)
    throws Exception
    {
	build (v, r, null, null, null, null, a);
    }



    /** Create a textured triangle fan.
     *
     */
    public TriangleFan (VertexList    v,
			VertexRefList r,
			TextureStore  ts,
			TextureMapRef tr,
			Appearance    a)
    throws Exception
    {
	build (v, r, null, null, ts, tr, a);
    }



    /** Create a triangle fan with a given y-value.
     *
     */
    public TriangleFan (VertexList    v,
			VertexRefList r,
			HeightList    h,
			HeightRef     height,
			Appearance    a)
    throws Exception
    {
	build (v, r, h, height, null, null, a);
    }

    /** Create a textured triangle fan with a given y-value.
     *
     */
    public TriangleFan (VertexList    v,
			VertexRefList r,
			HeightList    h,
			HeightRef     height,
			TextureStore  ts,
			TextureMapRef tr,
			Appearance a)
    throws Exception
    {
	build (v, r, h, height, ts, tr, a);
    }




    /** Create a triangle fan with a given y-value.
     *
     */
    protected void build (VertexList    v,
			  VertexRefList r,
			  HeightList    h,
			  HeightRef     height,
			  TextureStore  ts,
			  TextureMapRef tr,
			  Appearance a)
    throws Exception
    {
	setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	int nVertices = r.size();

	if (nVertices < 3)
	{
	    throw new Exception ("A triangle fan needs at least three vertices");
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
	}
	setAppearance (a);





	float y = 0f;
	if (height != null)
	{
	    y = h.heightAt (height.getIndex()).getHeight();
	}


	// Create geometry:
	//
	GeometryInfo gi = new GeometryInfo (GeometryInfo.POLYGON_ARRAY);
	gi.setTextureCoordinateParams (1, 2);

	Point3f[] coordinates   = new Point3f[nVertices];
	float[]   texCoords     = new float[nVertices * 2];
	int[]     stripCounts   = {nVertices};
	int[]     contourCounts = {1};

	int j = 0;
	for (int i = 0; i < nVertices; i++)
	{
	    VertexRef vr = r.referenceAt (i);
	    coordinates[i] = v.vertexAt (vr.getIndex()).toPoint3f();
	    if (height != null)
	    {
		coordinates[i].y = y;
	    }
	    // TODO: The scale factor must depend on texture resolution.
	    float s = (coordinates[0].x - coordinates[i].x) / 10f;
	    float t = (coordinates[0].z - coordinates[i].z) / 10f;
	    texCoords[j++] = s;
	    texCoords[j++] = t;
	}
	gi.setCoordinates   (coordinates);
	gi.setTextureCoordinates (0, texCoords);
	gi.setStripCounts   (stripCounts);
	gi.setContourCounts (contourCounts);

	Stripifier st = new Stripifier();
	st.stripify (gi);


	GeometryArray ga = gi.getGeometryArray();
	ga.setCapability (GeometryArray.ALLOW_COLOR_READ);
	ga.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
	ga.setCapability (GeometryArray.ALLOW_COUNT_READ);
	ga.setCapability (GeometryArray.ALLOW_FORMAT_READ);
	ga.setCapability (GeometryArray.ALLOW_NORMAL_READ);
	ga.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
	ga.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	setGeometry (ga);
    }


}
