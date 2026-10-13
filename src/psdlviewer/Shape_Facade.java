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


/** Facde rectangle.
 *
 */

public class Shape_Facade
extends Shape3D
{
    VertexListAttribute attribute;
    TextureAttribute textureAttribute;
    TextureMapRef    baseTexture;

    // Standard facade
    //
    public Shape_Facade (Attribute005e a,
			 TextureAttribute textureAttribute)
    {
	initialize (a, textureAttribute);

	Point3f v[] = new Point3f[4];
	v[0] = getPoint (0);
	v[1] = getPoint (1);
	v[2] = new Point3f (v[0]);
	v[3] = new Point3f (v[1]);

	float y0 = getHeight (a.bottom);
	float y1 = getHeight (a.top);
	v[0].y = y0;
	v[1].y = y0;
	v[2].y = y1;
	v[3].y = y1;

	createFacade (y0,
		      y1,
		      v,
		      a.xRepeat,
		      a.yRepeat);
    }

    // Ground facade, auto repeat
    //
    public Shape_Facade (Attribute001c    a,
			 TextureAttribute textureAttribute)
    {
	initialize (a, textureAttribute);

	Point3f v[] = new Point3f[4];
	v[0] = getPoint (0);
	v[1] = getPoint (1);
	v[2] = new Point3f (v[0]);
	v[3] = new Point3f (v[1]);

	float y0 = v[0].y;
	float y1 = getHeight (a.top);
	v[2].y = y1;
	v[3].y = y1;

	createFacade (y0,
		      y1,
		      v,
		      getHeight (a.textureScale));
    }




    protected void initialize (VertexListAttribute a,
			       TextureAttribute textureAttribute_)
    {
	attribute = a;
	textureAttribute = textureAttribute_;
	baseTexture      = textureAttribute.ref;

	setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	setCapability (Shape3D.ALLOW_GEOMETRY_READ);
    }




    protected void createFacade (float bottom,
				 float top,
				 Point3f p[],
				 float xRepeat,
				 float yRepeat)
    {
	int[] stripVertCounts = {4};
	TriangleStripArray tsa = new TriangleStripArray (4,
					  TriangleStripArray.COORDINATES
					| TriangleStripArray.TEXTURE_COORDINATE_2,
					stripVertCounts);

	tsa.setCapability (GeometryArray.ALLOW_COLOR_READ);
	tsa.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
	tsa.setCapability (GeometryArray.ALLOW_COUNT_READ);
	tsa.setCapability (GeometryArray.ALLOW_FORMAT_READ);
	tsa.setCapability (GeometryArray.ALLOW_NORMAL_READ);
	tsa.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
	tsa.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	TexCoord2f t[] = new TexCoord2f[4];
	t[0] = new TexCoord2f (0f,      0f);
	t[1] = new TexCoord2f (xRepeat, 0f);
	t[2] = new TexCoord2f (0f,      yRepeat);
	t[3] = new TexCoord2f (xRepeat, yRepeat);
	tsa.setCoordinate        (   0, p[0]);
	tsa.setTextureCoordinate (0, 0, t[0]);
	tsa.setCoordinate        (   1, p[1]);
	tsa.setTextureCoordinate (0, 1, t[1]);
	tsa.setCoordinate        (   2, p[2]);
	tsa.setTextureCoordinate (0, 2, t[2]);
	tsa.setCoordinate        (   3, p[3]);
	tsa.setTextureCoordinate (0, 3, t[3]);

	setGeometry (tsa);
	setAppearance (getAppearance (baseTexture));
    }





    /** 
     *
     */
    protected void createFacade (float bottom,
				 float top,
				 Point3f p[],
				 float textureScale)
    {
	int[] stripVertCounts = {4};
	TriangleStripArray tsa = new TriangleStripArray (4,
					  TriangleStripArray.COORDINATES
					| TriangleStripArray.TEXTURE_COORDINATE_2,
					stripVertCounts);

	tsa.setCapability (GeometryArray.ALLOW_COLOR_READ);
	tsa.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
	tsa.setCapability (GeometryArray.ALLOW_COUNT_READ);
	tsa.setCapability (GeometryArray.ALLOW_FORMAT_READ);
	tsa.setCapability (GeometryArray.ALLOW_NORMAL_READ);
	tsa.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
	tsa.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	TexCoord2f t[] = new TexCoord2f[4];

	float width   = p[0].distance (p[1]);
	float lHeight = p[0].distance (p[2]);
	float rHeight = p[1].distance (p[3]);
	float xRepeat = (int)(width * textureScale + 0.5);
	t[0] = new TexCoord2f (0f,      0f);
	t[1] = new TexCoord2f (xRepeat, 0f);
	t[2] = new TexCoord2f (0f,      (int)(rHeight / width * xRepeat + 0.5));
	t[3] = new TexCoord2f (xRepeat, (int)(rHeight / width * xRepeat + 0.5));

	tsa.setCoordinate        (   0, p[0]);
	tsa.setTextureCoordinate (0, 0, t[0]);
	tsa.setCoordinate        (   1, p[1]);
	tsa.setTextureCoordinate (0, 1, t[1]);
	tsa.setCoordinate        (   2, p[2]);
	tsa.setTextureCoordinate (0, 2, t[2]);
	tsa.setCoordinate        (   3, p[3]);
	tsa.setTextureCoordinate (0, 3, t[3]);


	setGeometry (tsa);
	setAppearance (getAppearance (baseTexture));
    }








    protected Point3f getPoint (int i)
    {
	VertexRef vr = attribute.vertexrefs.referenceAt (i);
	return Config.getVertexList().getVertex (vr).toPoint3f();
    }


    protected float getHeight (HeightRef r)
    {
	return Config.getHeightList().heightAt (r.getIndex()).getHeight();
    }



    protected Appearance getAppearance (TextureMapRef tr)
    {
	Appearance a = new Appearance();
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

	if (tr != null && tr.getIndex() != (char)-1)
	{
	    a.setTexture (Config.getTerrain().textureStore.getTexture (tr));
	}
	return a;
    }
}
