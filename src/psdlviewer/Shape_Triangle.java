package psdlviewer;
import psdlviewer.*;

/** Shape of a single triangle.
 *  
 *
 */

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.GeometryArray;

import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;


public class Shape_Triangle
extends BranchGroup
{
    public Shape_Triangle (Point3f       p[],
			   TextureMapRef textureMap)
    {
	setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	TriangleArray ta = new TriangleArray (3,
				   TriangleArray.COORDINATES
				 | TriangleArray.TEXTURE_COORDINATE_2);

	ta.setCoordinate        (   0, p[0]);
	ta.setTextureCoordinate (0, 0, new TexCoord2f (0f, 0f));
	ta.setCoordinate        (   1, p[1]);
	ta.setTextureCoordinate (0, 1, new TexCoord2f (1f, 0f));
	ta.setCoordinate        (   2, p[2]);
	ta.setTextureCoordinate (0, 2, new TexCoord2f (1f, 1f));


	Shape3D shape = new Shape3D (ta);
	shape.setAppearance (getAppearance (textureMap));
	addChild (shape);
    }








    protected Appearance getAppearance (TextureMapRef tr)
    {
	Appearance a = new Appearance();

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
