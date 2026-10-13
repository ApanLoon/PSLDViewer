package psdlviewer;
import psdlviewer.*;

/** Shape of a single decal.
 *  
 *
 */

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.GeometryArray;

import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Texture2D;


public class Shape_Decal
extends BranchGroup
{

    public Shape_Decal (PathPointList p,
			Texture2D     texture)
    {
	setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	int[] stripVertCounts = {p.size()};
	TriangleStripArray tsa = new TriangleStripArray (
				 p.size(),
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 stripVertCounts);
	Point3f point;
	int index = 0;
	for (int i = 0; i < p.size() / 2; i++)
	{
	    point = p.pathPointAt (index).getPoint().toPoint3f();
	    point.y += 0.01;
	    tsa.setCoordinate        (   index, point);
	    tsa.setTextureCoordinate (0, index++, new TexCoord2f (0f, (float)i));

	    point = p.pathPointAt (index).getPoint().toPoint3f();
	    point.y += 0.01;
	    tsa.setCoordinate        (   index, point);
	    tsa.setTextureCoordinate (0, index++, new TexCoord2f (1f, (float)i));
	}


	Shape3D shape = new Shape3D (tsa);
	shape.setAppearance (getAppearance (texture));
	addChild (shape);
    }








    protected Appearance getAppearance (Texture2D texture)
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

	if (texture != null)
	{
	    a.setTexture (texture);
	}
	return a;
    }

}
