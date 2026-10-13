package psdlviewer;
import psdlviewer.*;

/** Shape of a road section, renders flat, normal and divided roads with or
 *  without railings or tunnels.
 *
 */

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.GeometryArray;

import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;


public class Shape_Roadsection
extends BranchGroup
{
    RoadAttribute roadAttribute;
    int           nSections;
    int           nVertsPerSection;
    TextureMapRef baseTexture;


    class Offset
    {
	public boolean order;
	public int sidewalkOuter;
	public int roadOuter;
	public int roadInner;
    };

    protected Offset left  = new Offset();
    protected Offset right = new Offset();


    /** Flat road
     *
     */
    public Shape_Roadsection (Attribute0010    roadAttribute_,
			      TextureAttribute textureAttribute,
			      Attribute004b    tunnelAttribute)
    {
	nVertsPerSection = 2;

	right.roadOuter      = 1;
	right.roadInner      = 0;

	initialize (roadAttribute_,
		    textureAttribute,
		    tunnelAttribute);

	addChild (getDrivingLane (right));
    }

    /** Standard road
     *
     */
    public Shape_Roadsection (Attribute0000    roadAttribute_,
			      TextureAttribute textureAttribute,
			      Attribute004b    tunnelAttribute)
    {
	nVertsPerSection = 4;

	left.sidewalkOuter  = 0;
	left.roadOuter      = 1;
	left.roadInner      = -2;

	right.sidewalkOuter  = 3;
	right.roadOuter      = 2;
	right.roadInner      = -1;

	initialize (roadAttribute_,
		    textureAttribute,
		    tunnelAttribute);


	addChild (getSidewalk    (left));
	addChild (getDrivingLane (left));
	addChild (getDrivingLane (right));
	addChild (getSidewalk    (right));
    }

    /** Divided road
     *
     */
    public Shape_Roadsection (Attribute0040    roadAttribute_,
			      TextureAttribute textureAttribute,
			      Attribute004b    tunnelAttribute)
    {
	nVertsPerSection = 6;

	left.sidewalkOuter  = 0;
	left.roadOuter      = 1;
	left.roadInner      = 2;

	right.sidewalkOuter  = 5;
	right.roadOuter      = 4;
	right.roadInner      = 3;


	initialize (roadAttribute_,
		    textureAttribute,
		    tunnelAttribute);

	addChild (getSidewalk    (left));
	addChild (getDrivingLane (left));
        addChild (getDivider     (left, right));
	addChild (getDrivingLane (right));
	addChild (getSidewalk    (right));
    }


    protected void initialize (RoadAttribute   roadAttribute_,
			       TextureAttribute textureAttribute,
			       Attribute004b    tunnelAttribute)
    {
	setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	roadAttribute    = roadAttribute_;
	nSections        = roadAttribute.nSections();
	baseTexture      = textureAttribute.ref;

	left.order  = false;
	right.order = true;
    }





    protected Shape3D getSidewalk (Offset o)
    {
	if (   roadAttribute.vertexrefs.referenceAt (o.roadOuter).getIndex()
	    == roadAttribute.vertexrefs.referenceAt (o.sidewalkOuter).getIndex())
	{
	    return null;
	}

	int[] stripVertCounts = {nSections * 2, nSections * 2};
	TriangleStripArray tsa = new TriangleStripArray (
				 nSections * 2 * 2,
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


	int offset = 0;

	Point3f lastOuterVertex = null;
	TexCoord2f innerTexCoord = new TexCoord2f (1f, 0f);
	TexCoord2f outerTexCoord = new TexCoord2f (1f, 1f);

	int h = 0;                  // Offset for horizontal strip
	int v = nSections * 2;      // Offset for vertical strip
	for (int i = 0; i < nSections; i++)
	{
	    Point3f roadVertex  = getPoint (offset + o.roadOuter);
	    Point3f innerVertex = getPoint (offset + o.roadOuter);
	    Point3f outerVertex = getPoint (offset + o.sidewalkOuter);

	    innerVertex.y += 0.15;



	    if (lastOuterVertex != null)
	    {
		float tDistance = outerVertex.distance (innerVertex);
		float sDistance = outerVertex.distance (lastOuterVertex);
		innerTexCoord.x += (float)(sDistance / tDistance);
		outerTexCoord.x = innerTexCoord.x;
	    }

	    if (o.order)
	    {

	        tsa.setCoordinate        (   h,     innerVertex);       // Horizontal strip
		tsa.setTextureCoordinate (0, h,     innerTexCoord);     // Horizontal strip
	        tsa.setCoordinate        (   h + 1, outerVertex);   // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1, outerTexCoord); // Horizontal strip
		tsa.setCoordinate        (   v,     roadVertex);        // Vertical strip
		tsa.setTextureCoordinate (0, v,     innerTexCoord);     // Vertical strip
		tsa.setCoordinate        (   v + 1, innerVertex);   // Vertical strip
		tsa.setTextureCoordinate (0, v + 1, innerTexCoord); // Vertical strip

	    }
	    else
	    {
	        tsa.setCoordinate        (   h + 1, innerVertex);   // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1, innerTexCoord); // Horizontal strip
	        tsa.setCoordinate        (   h,     outerVertex);   // Horizontal strip
		tsa.setTextureCoordinate (0, h,     outerTexCoord); // Horizontal strip
		tsa.setCoordinate        (   v + 1, roadVertex);    // Vertical strip
		tsa.setTextureCoordinate (0, v + 1, innerTexCoord); // Vertical strip
		tsa.setCoordinate        (   v,     innerVertex);   // Vertical strip
		tsa.setTextureCoordinate (0, v,     innerTexCoord); // Vertical strip
	    }

	    lastOuterVertex = outerVertex;
	    v += 2;
	    h += 2;
	    offset += nVertsPerSection;
	}

	Shape3D shape = new Shape3D (tsa);
	shape.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	shape.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	shape.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	shape.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	TextureList tList  = Config.getTextureList();
	int         tIndex = baseTexture.getIndex();
	TextureMapRef tr = null;
	if (tList.size() > tIndex + 1)
	{
	    tr = new TextureMapRef (tIndex + 1, tList);
	}

	shape.setAppearance (getAppearance (tr));
	return shape;
    }




    protected Shape3D getDrivingLane (Offset o)
    {
	int[] stripVertCounts = {nSections * 2};
	TriangleStripArray tsa = new TriangleStripArray (
				 nSections * 2,
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


	int offset = 0;

	Point3f lastOuterVertex = null;
	TexCoord2f innerTexCoord = new TexCoord2f (1f, 0f);
	TexCoord2f outerTexCoord = new TexCoord2f (1f, 1f);

	int h = 0;                  // Offset for strip
	for (int i = 0; i < nSections; i++)
	{
	    Point3f outerVertex = getPoint (offset + o.roadOuter);
	    Point3f innerVertex;
	    if (o.roadInner < 0)
	    {
		innerVertex = getPoint (offset + -o.roadInner);
		innerVertex.x = (outerVertex.x + innerVertex.x) / 2f;
		innerVertex.y = (outerVertex.y + innerVertex.y) / 2f;
		innerVertex.z = (outerVertex.z + innerVertex.z) / 2f;
	    }
	    else
	    {
		innerVertex = getPoint (offset + o.roadInner);
	    }

	    if (lastOuterVertex != null)
	    {
		float tDistance = outerVertex.distance (innerVertex);
		float sDistance = outerVertex.distance (lastOuterVertex);
		innerTexCoord.x += (float)(sDistance / tDistance);
		outerTexCoord.x = innerTexCoord.x;
	    }

	    if (o.order)
	    {
		tsa.setCoordinate (h,     innerVertex);        // Horizontal strip
		tsa.setCoordinate (h + 1, outerVertex);        // Horizontal strip
		tsa.setTextureCoordinate (0, h,     innerTexCoord);   // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1, outerTexCoord);   // Horizontal strip
	    }
	    else
	    {
		tsa.setCoordinate (h + 1,     innerVertex);        // Horizontal strip
		tsa.setCoordinate (h, outerVertex);        // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1,     innerTexCoord);   // Horizontal strip
		tsa.setTextureCoordinate (0, h, outerTexCoord);   // Horizontal strip
	    }

	    lastOuterVertex = outerVertex;
	    h += 2;
	    offset += nVertsPerSection;
	}

	Shape3D shape = new Shape3D (tsa);

	shape.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	shape.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	shape.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	shape.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	shape.setAppearance (getAppearance (baseTexture));
	return shape;
    }





    protected BranchGroup getDivider (Offset left, Offset right)
    {
	int[] sidesVCounts = {nSections * 2, nSections * 2};
	int[] sidesHCounts = {nSections * 2, nSections * 2};
	int[] topCounts = {nSections * 2};
	TriangleStripArray sidesVtsa;
	TriangleStripArray sidesHtsa;
	TriangleStripArray toptsa;
	QuadArray          endsqa = null;

	int offset = 0;

	Point3f lastP0;
	TexCoord2f t1;
	TexCoord2f t0;

	int h = 0;                  // Offset for left strip
	int v = nSections * 2;      // Offset for right strip
	int endsindex = 0;

	float tDistance;
	float sDistance;

	Vector3f v0 = new Vector3f();
	float factor;
	float dx;
	float dz;

	Point3f p[] = new Point3f[6]; 

	Shape3D shape;


	BranchGroup root = new BranchGroup();
	root.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	Attribute0040 a = (Attribute0040)roadAttribute;

	TextureList tList  = Config.getTextureList();
	int         tIndex = a.getTextureMap().getIndex();
	TextureMapRef texture[] = new TextureMapRef[4];
	for (int i = 0; i < 4; i++)
	{
	    if (tList.size() > tIndex + i)
	    {
		texture[i] = new TextureMapRef (tIndex + i, tList);
	    }
	}



	if (   a.getType() == Attribute0040.TYPE_FLATELEVATED
	    || a.getType() == Attribute0040.TYPE_WEDGE)
	{
	    int nVertices = (a.getFlag (Attribute0040.CLOSE_START) ? 4 : 0)
		          + (a.getFlag (Attribute0040.CLOSE_END)   ? 4 : 0);

	    if (nVertices != 0)
	    {
		endsqa = new QuadArray (
		                 nVertices,
				   QuadArray.COORDINATES
				 | QuadArray.TEXTURE_COORDINATE_2);
		
	    }
	}




	switch (a.getType())
	{
	case Attribute0040.TYPE_FLAT:
	    toptsa = new TriangleStripArray (
				 nSections * 2,
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 topCounts);

	    offset = 0;

	    lastP0 = null;
	    t1 = new TexCoord2f (1f, a.getHeight());
	    t0 = new TexCoord2f (1f, 0f);

	    h = 0;                  // Offset for strip
	    for (int i = 0; i < nSections; i++)
	    {
		p[0] = getPoint (offset + left.roadInner);
		p[1] = getPoint (offset + right.roadInner);

		// Calculate texture coordinates:
		if (lastP0 != null)
		{
		    tDistance = p[0].distance (p[1]);
		    sDistance = p[0].distance (lastP0);
		    t1.x += (float)(sDistance / tDistance * a.getHeight());;
		    t0.x = t1.x;
		}

		toptsa.setCoordinate (h,     p[0]);
		toptsa.setCoordinate (h + 1, p[1]);
		toptsa.setTextureCoordinate (0, h,     t0);
		toptsa.setTextureCoordinate (0, h + 1, t1);



		lastP0 = p[0];
		h += 2;
		offset += nVertsPerSection;
	    }

	    shape = new Shape3D (toptsa);
	    shape.setAppearance (getAppearance (texture[1]));
	    root.addChild (shape);


	    break;

	case Attribute0040.TYPE_FLATELEVATED:
	    sidesVtsa = new TriangleStripArray (
		                 nSections * 2 * 2,
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 sidesVCounts);
	    sidesHtsa = new TriangleStripArray (
		                 nSections * 2 * 2,
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 sidesHCounts);
	    toptsa = new TriangleStripArray (
		                 nSections * 2,
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 topCounts);

	    lastP0 = null;
	    t1 = new TexCoord2f (1f, 1f);
	    t0 = new TexCoord2f (1f, 0f);

	    h = 0;                  // Offset for left strip
	    v = nSections * 2;      // Offset for right strip
	    for (int i = 0; i < nSections; i++)
	    {
		p[0] = getPoint (offset + left.roadInner);
		p[5] = getPoint (offset + right.roadInner);

		v0.sub (p[5], p[0]);
		factor = a.getHeight() / v0.length();
		dx = v0.x * factor;
		dz = v0.z * factor;

		p[1] = new Point3f (p[0]);
		p[1].y += a.getHeight();
		p[2] = new Point3f (p[1]);
		p[2].x += dx;
		p[2].z += dz;
		p[4] = new Point3f (p[5]);
		p[4].y += a.getHeight();
		p[3] = new Point3f (p[4]);
		p[3].x -= dx;
		p[3].z -= dz;

		// Calculate texture coordinates:
		if (lastP0 != null)
		{
		    tDistance = p[0].distance (p[1]);
		    sDistance = p[0].distance (lastP0);
		    t1.x += (float)(sDistance / tDistance * a.getHeight());;
		    t0.x = t1.x;
		}

		sidesVtsa.setCoordinate (h,     p[0]);
		sidesVtsa.setCoordinate (h + 1, p[1]);
		sidesVtsa.setTextureCoordinate (0, h,     t0);
		sidesVtsa.setTextureCoordinate (0, h + 1, t1);
		sidesVtsa.setCoordinate (v,     p[4]);
		sidesVtsa.setCoordinate (v + 1, p[5]);
		sidesVtsa.setTextureCoordinate (0, v,     t1);
		sidesVtsa.setTextureCoordinate (0, v + 1, t0);

		sidesHtsa.setCoordinate (h,     p[1]);
		sidesHtsa.setCoordinate (h + 1, p[2]);
		sidesHtsa.setTextureCoordinate (0, h,     t0);
		sidesHtsa.setTextureCoordinate (0, h + 1, t1);
		sidesHtsa.setCoordinate (v,     p[3]);
		sidesHtsa.setCoordinate (v + 1, p[4]);
		sidesHtsa.setTextureCoordinate (0, v,     t1);
		sidesHtsa.setTextureCoordinate (0, v + 1, t0);

		toptsa.setCoordinate (h,     p[2]);
		toptsa.setCoordinate (h + 1, p[3]);
		toptsa.setTextureCoordinate (0, h,     t0);
		toptsa.setTextureCoordinate (0, h + 1, t1);

		if (i == 0 && a.getFlag (Attribute0040.CLOSE_START))
		{
		    endsqa.setCoordinate        (   endsindex, p[0]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[5]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[4]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 1f));
		    endsqa.setCoordinate        (   endsindex, p[1]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 1f));
		}
		if (i == nSections - 1 && a.getFlag (Attribute0040.CLOSE_END))
		{
		    endsqa.setCoordinate        (   endsindex, p[5]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[0]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[1]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 1f));
		    endsqa.setCoordinate        (   endsindex, p[4]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 1f));
		}


		lastP0 = p[0];
		h += 2;
		v += 2;
		offset += nVertsPerSection;
	    }

	    shape = new Shape3D (sidesVtsa);
	    shape.setAppearance (getAppearance (texture[0]));
	    root.addChild (shape);

	    shape = new Shape3D (sidesHtsa);
	    shape.setAppearance (getAppearance (texture[1]));
	    root.addChild (shape);

	    shape = new Shape3D (toptsa);
	    shape.setAppearance (getAppearance (texture[2]));
	    root.addChild (shape);

	    shape = new Shape3D (endsqa);
	    shape.setAppearance (getAppearance (texture[3]));
	    root.addChild (shape);

	    break;

	case Attribute0040.TYPE_WEDGE:
	    sidesVtsa = new TriangleStripArray (
		                 nSections * 2 * 2,
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 sidesVCounts);
	    toptsa = new TriangleStripArray (
		                 nSections * 2,
				   TriangleStripArray.COORDINATES
				 | TriangleStripArray.TEXTURE_COORDINATE_2,
				 topCounts);

	    lastP0 = null;
	    t1 = new TexCoord2f (1f, 1f);
	    t0 = new TexCoord2f (1f, 0f);

	    h = 0;                  // Offset for left strip
	    v = nSections * 2;      // Offset for right strip
	    for (int i = 0; i < nSections; i++)
	    {
		p[0] = getPoint (offset + left.roadInner);
		p[5] = getPoint (offset + right.roadInner);

		v0.sub (p[5], p[0]);
		factor = 0.5f / v0.length();
		dx = v0.x * factor;
		dz = v0.z * factor;

		p[1] = new Point3f (p[0]);
		p[1].y += 1f;
		p[2] = new Point3f (p[1]);
		p[2].x += dx;
		p[2].z += dz;
		p[4] = new Point3f (p[5]);
		p[4].y += 1f;
		p[3] = new Point3f (p[4]);
		p[3].x -= dx;
		p[3].z -= dz;

		// Calculate texture coordinates:
		if (lastP0 != null)
		{
		    tDistance = p[0].distance (p[1]);
		    sDistance = p[0].distance (lastP0);
		    t1.x += (float)(sDistance / tDistance * a.getHeight());;
		    t0.x = t1.x;
		}

		sidesVtsa.setCoordinate (h,     p[0]);
		sidesVtsa.setCoordinate (h + 1, p[2]);
		sidesVtsa.setTextureCoordinate (0, h,     t0);
		sidesVtsa.setTextureCoordinate (0, h + 1, t1);
		sidesVtsa.setCoordinate (v,     p[3]);
		sidesVtsa.setCoordinate (v + 1, p[5]);
		sidesVtsa.setTextureCoordinate (0, v,     t1);
		sidesVtsa.setTextureCoordinate (0, v + 1, t0);

		toptsa.setCoordinate (h,     p[2]);
		toptsa.setCoordinate (h + 1, p[3]);
		toptsa.setTextureCoordinate (0, h,     t0);
		toptsa.setTextureCoordinate (0, h + 1, t1);


		if (i == 0 && a.getFlag (Attribute0040.CLOSE_START))
		{
		    endsqa.setCoordinate        (   endsindex, p[0]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[5]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[3]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 1f));
		    endsqa.setCoordinate        (   endsindex, p[2]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 1f));
		}
		if (i == nSections - 1 && a.getFlag (Attribute0040.CLOSE_END))
		{
		    endsqa.setCoordinate        (   endsindex, p[5]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[0]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 0f));
		    endsqa.setCoordinate        (   endsindex, p[2]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (1f, 1f));
		    endsqa.setCoordinate        (   endsindex, p[3]);
		    endsqa.setTextureCoordinate (0, endsindex++, new TexCoord2f (0f, 1f));
		}


		lastP0 = p[0];
		h += 2;
		v += 2;
		offset += nVertsPerSection;
	    }

	    shape = new Shape3D (sidesVtsa);
	    shape.setAppearance (getAppearance (texture[1]));
	    root.addChild (shape);

	    shape = new Shape3D (toptsa);
	    shape.setAppearance (getAppearance (texture[2]));
	    root.addChild (shape);

	    shape = new Shape3D (endsqa);
	    shape.setAppearance (getAppearance (texture[3]));
	    root.addChild (shape);

	    break;
	}



	/*
	if (   roadAttribute.vertexrefs.referenceAt (o.roadOuter).getIndex()
	    == roadAttribute.vertexrefs.referenceAt (o.sidewalkOuter).getIndex())
	{
	    return null;
	}

	int[] stripVertCounts = {nSections * 2, nSections * 2};
	TriangleStripArray tsa = new TriangleStripArray (
				 nSections * 2 * 2,
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


	int offset = 0;

	Point3f lastOuterVertex = null;
	TexCoord2f innerTexCoord = new TexCoord2f (1f, 0f);
	TexCoord2f outerTexCoord = new TexCoord2f (1f, 1f);

	int h = 0;                  // Offset for horizontal strip
	int v = nSections * 2;      // Offset for vertical strip
	for (int i = 0; i < nSections; i++)
	{
	    Point3f roadVertex  = getPoint (offset + o.roadOuter);
	    Point3f innerVertex = getPoint (offset + o.roadOuter);
	    Point3f outerVertex = getPoint (offset + o.sidewalkOuter);

	    innerVertex.y += 0.15;



	    if (lastOuterVertex != null)
	    {
		float tDistance = outerVertex.distance (innerVertex);
		float sDistance = outerVertex.distance (lastOuterVertex);
		innerTexCoord.x += (float)(sDistance / tDistance);
		outerTexCoord.x = innerTexCoord.x;
	    }

	    if (o.order)
	    {

	        tsa.setCoordinate        (   h,     innerVertex);       // Horizontal strip
		tsa.setTextureCoordinate (0, h,     innerTexCoord);     // Horizontal strip
	        tsa.setCoordinate        (   h + 1, outerVertex);   // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1, outerTexCoord); // Horizontal strip
		tsa.setCoordinate        (   v,     roadVertex);        // Vertical strip
		tsa.setTextureCoordinate (0, v,     innerTexCoord);     // Vertical strip
		tsa.setCoordinate        (   v + 1, innerVertex);   // Vertical strip
		tsa.setTextureCoordinate (0, v + 1, innerTexCoord); // Vertical strip

	    }
	    else
	    {
	        tsa.setCoordinate        (   h + 1, innerVertex);   // Horizontal strip
		tsa.setTextureCoordinate (0, h + 1, innerTexCoord); // Horizontal strip
	        tsa.setCoordinate        (   h,     outerVertex);   // Horizontal strip
		tsa.setTextureCoordinate (0, h,     outerTexCoord); // Horizontal strip
		tsa.setCoordinate        (   v + 1, roadVertex);    // Vertical strip
		tsa.setTextureCoordinate (0, v + 1, innerTexCoord); // Vertical strip
		tsa.setCoordinate        (   v,     innerVertex);   // Vertical strip
		tsa.setTextureCoordinate (0, v,     innerTexCoord); // Vertical strip
	    }

	    lastOuterVertex = outerVertex;
	    v += 2;
	    h += 2;
	    offset += nVertsPerSection;
	}

	Shape3D shape = new Shape3D (tsa);
	shape.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
	shape.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
	shape.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
	shape.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	TextureList tList  = Config.getTextureList();
	int         tIndex = baseTexture.getIndex();
	TextureMapRef tr = null;
	if (tList.size() > tIndex + 1)
	{
	    tr = new TextureMapRef (tIndex + 1, tList);
	}

	shape.setAppearance (getAppearance (tr));
	return shape;
	*/
	return root;
    }











    protected Point3f getPoint (int i)
    {
	VertexRef vr = roadAttribute.vertexrefs.referenceAt (i);
	return Config.getVertexList().getVertex (vr).toPoint3f();
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
