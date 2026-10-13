package psdlviewer;
import psdlviewer.*;
import psdlviewer.bai.*;
import psdlviewer.pkg.*;

/** The Terrain class represents the complete terrain of a PSDL file.
 *
 */

import javax.media.j3d.VirtualUniverse;
import javax.media.j3d.Locale;


import javax.media.j3d.Node;

import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.Bounds;
import javax.media.j3d.BoundingSphere;
import javax.vecmath.Point3d;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;

import javax.media.j3d.Shape3D;
import javax.media.j3d.LineArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.GeometryArray;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Texture2D;


import java.awt.Font;
import java.awt.font.TextAttribute;
import javax.media.j3d.Font3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.OrientedShape3D;

import com.sun.j3d.utils.geometry.Sphere;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix4f;

import javax.swing.JOptionPane;

import java.util.Vector;
import java.awt.Color;
import javax.media.j3d.LineStripArray;

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.FileNotFoundException;


public class Terrain
extends VirtualUniverse
implements VertexSelectionListener,
	   BlockSelectionListener,
	   RouteSelectionListener
{

    /** Drawing mode used for rendering city blocks.
     *
     */
    protected int drawingMode = DRAWING_FILL;

    /** Lighting model used in this view.
     *
     */
    protected int lightingMode = LIGHTING_UNLIT;

    /** Colour selection method used in this view.
     *
     */
    protected int colouringMode  = COLOURING_RANDOM;



    /** If true this view displays the city block index next to each block.
     *
     */
    protected boolean showBlockIndices = false;

    /** If true all block attributes are rendered, if false only the perimeter
     *  of each block is rendered.
     */
    protected boolean renderBlockAttributes = true;

    /** If true this view displays block bounding perimeter.
     *
     */
    protected boolean showBlockBound = false;



    /** If true this view displays city bounding sphere.
     *
     */
    protected boolean showBound = false;



    /** If true this view displays INST objects.
     *
     */
    protected boolean showINST = false;

    /** If true this view displays Pathset objects.
     *
     */
    protected boolean showPathset = true;

    /** If true this view displays BAI paths.
     *
     */
    protected boolean showBAI = false;






    protected VertexList  vertices;
    protected HeightList  heights;
    protected TextureList textures;
    protected BlockList   blocks;

    protected ProgressWindow monitor;

    public    Locale       locale;
    public    BranchGroup  psdl        = new BranchGroup();
    protected BranchGroup  inst        = new BranchGroup();

    protected BranchGroup  bound;

    protected BranchGroup  pathsetGroup;
    protected BranchGroup  bai        = new BranchGroup();


    protected BranchGroup  blockLabels = new BranchGroup();
    protected BranchGroup  markers;
    protected BranchGroup  blockBounds;
    protected BranchGroup  routeMarkers;

    protected BranchGroup  level;
    protected BranchGroup  edges;

    protected BranchGroup  view        = new BranchGroup();

    public TextureStore textureStore = new TextureStore();
    public PKGStore     pkgStore     = new PKGStore();

    public Font3D font;


    public Terrain()
    {
	super();

	// Set up the locale:
	locale = new Locale (this);

	psdl.setCapability (BranchGroup.ALLOW_DETACH); 
//	psdl.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	locale.addBranchGraph (psdl);

	inst.setCapability (BranchGroup.ALLOW_DETACH); 
//	inst.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	locale.addBranchGraph (inst);

	bai.setCapability (BranchGroup.ALLOW_DETACH); 
//	bai.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	locale.addBranchGraph (bai);

	blockLabels.setCapability (BranchGroup.ALLOW_DETACH); 
//	blockLabels.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	locale.addBranchGraph (blockLabels);

//	view.setCapability (BranchGroup.ALLOW_CHILDREN_READ);
	view.setCapability (BranchGroup.ALLOW_CHILDREN_WRITE);
	view.setCapability (BranchGroup.ALLOW_CHILDREN_EXTEND); 
	locale.addBranchGraph (view);
    }



    protected void initialize (VertexList     vertices_,
			       HeightList     heights_,
			       TextureList    textures_,
			       BlockList      blocks_,
			       ProgressWindow monitor_)
    {
	vertices = vertices_;
	heights  = heights_;
	textures = textures_;
	blocks   = blocks_;
	monitor  = monitor_;
    }

    /* Removes the old terrain and renders a new one.
     *
     * @param v A vector holding Vertex3D objects that make up the PSDL terrain
     * @param h A vector holding Height objects used to render the blocks
     * @param t A vector holding TextureMap objects used to render the blocks
     * @param b A vector holding Block object defining the terrain
     */
    public void createTerrain (VertexList     vertices_,
			       HeightList     heights_,
			       TextureList    textures_,
			       BlockList      blocks_,
			       ProgressWindow monitor_)
    {
	initialize (vertices_, heights_, textures_, blocks_, monitor_);
	renderPSDL();
	renderINST();
	renderBAI();
	renderPathsets();

	renderBlockLabels();

	renderBoundingSphere();

/*
	if (content != null)
	{
	    locale.removeBranchGraph (content);
	}
	content = createScene();
	locale.addBranchGraph (content);
*/
    }




    protected void renderBoundingSphere()
    {
	if (bound != null)
	{
	    locale.removeBranchGraph (bound);
	}

	if (!showBound)
	{
	    return;
	}

	bound = new BranchGroup();
	bound.setCapability (BranchGroup.ALLOW_DETACH); 

	Dimensions d = Config.dimensions;

	Transform3D tr = new Transform3D();
	tr.setTranslation (new Vector3d (d.getCenter().toPoint3f()));
	TransformGroup t = new TransformGroup (tr);


	Appearance a = new Appearance();

	PolygonAttributes pAttr = new PolygonAttributes();
	pAttr.setPolygonMode (PolygonAttributes.POLYGON_LINE);
	pAttr.setCullFace    (PolygonAttributes.CULL_NONE);
	a.setPolygonAttributes (pAttr);

	Material m = new Material();
	m.setDiffuseColor (new Color3f (1.0f, 1.0f, 1.0f));
	m.setAmbientColor (new Color3f (1.0f, 1.0f, 1.0f));
	a.setMaterial (m);

	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (0.3f, 0.7f, 1.0f));
	a.setColoringAttributes (cAttr);

	Sphere s = new Sphere (d.getRadius(), Sphere.GENERATE_NORMALS_INWARD, a);
	s.setAppearance (a);
	bound.addChild (t);
	t.addChild (s);
	bound.compile();

	locale.addBranchGraph (bound);
    }



    /** Renders the PSDL
     *
     */
    public void renderPSDL (VertexList     vertices_,
			    HeightList     heights_,
			    TextureList    textures_,
			    BlockList      blocks_,
			    ProgressWindow monitor_)
    {
	initialize (vertices_, heights_, textures_, blocks_, monitor_);
	renderPSDL();
    }

    protected void renderPSDL()
    {
	locale.removeBranchGraph (psdl);
	psdl = new BranchGroup();
	psdl.setCapability (BranchGroup.ALLOW_DETACH); 
//	psdl.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	if (blocks == null)
	{
	    return;
	}

	if (monitor != null)
	{
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());
	    monitor.setNote ("Rendering PSDL");
	}

	for (int i = 0; i < blocks.size(); i++)
	{
	    if (monitor != null)
	    {
		monitor.setProgress (i);
	    }
	    try
	    {
		Block block = blocks.blockAt (i);

		// Create block geometry:
		//
		psdl.addChild (createBlockObject (block));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (Config.mainWindow,
					       e + " at block " + Integer.toHexString (i));

		System.err.println ("Terrain.renderPSDL: " + e);
		e.printStackTrace (System.err);

	    }
	}
	psdl.compile();
	locale.addBranchGraph (psdl);
    }





    /** Renders block labels
     *
     */
    public void renderBlockLabels (VertexList     vertices_,
				   HeightList     heights_,
				   TextureList    textures_,
				   BlockList      blocks_,
				   ProgressWindow monitor_)

    {
	initialize (vertices_, heights_, textures_, blocks_, monitor_);
	renderBlockLabels();
    }

    protected void renderBlockLabels()
    {
	locale.removeBranchGraph (blockLabels);
	blockLabels = new BranchGroup();
	blockLabels.setCapability (BranchGroup.ALLOW_DETACH); 
//	blockLabels.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	if (!showBlockIndices || blocks == null)
	{
	    return;
	}

	if (monitor != null)
	{
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());
	    monitor.setNote ("Rendering block labels");
	}

	for (int i = 0; i < blocks.size(); i++)
	{
	    if (monitor != null)
	    {
		monitor.setProgress (i);
	    }
	    try
	    {
		Block block = blocks.blockAt (i);

		// Create block label:
		//
		blockLabels.addChild (createBlockIDLabel (block, i));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (Config.mainWindow,
					       e + " at block " + Integer.toHexString (i));
/*
		System.err.println ("Terrain.renderPSDL: " + e);
		e.printStackTrace (System.err);
*/
	    }
	}
	blockLabels.compile();
	locale.addBranchGraph (blockLabels);
    }

    /** Renders INST
     *
     */
    public void renderINST (VertexList     vertices_,
			    HeightList     heights_,
			    TextureList    textures_,
			    BlockList      blocks_,
			    ProgressWindow monitor_)
    {
	initialize (vertices_, heights_, textures_, blocks_, monitor_);
	renderINST();
    }

    protected void renderINST()
    {
	locale.removeBranchGraph (inst);
	inst = new BranchGroup();
	inst.setCapability (BranchGroup.ALLOW_DETACH); 
//	inst.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	INSTFile instFile = Config.instFile;

	if (!showINST || instFile == null)
	{
	    return;
	}

	INSTList instList = Config.instFile.getComponents();

	if (monitor != null)
	{
	    monitor.setMinimum (0);
	    monitor.setMaximum (instList.size());
	    monitor.setNote ("Rendering INST");
	}

	for (int i = 0; i < instList.size(); i++)
	{
	    if (monitor != null)
	    {
		monitor.setProgress (i);
	    }
	    try
	    {
		INSTComponent c = instList.componentAt (i);

		// Create INST geometry:
		//
		inst.addChild (createINST (c));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (Config.mainWindow,
					       e + " at INST component " + Integer.toHexString (i));
/*
		System.err.println ("Terrain.renderINST: " + e + " at INST component " + Integer.toHexString (i));
		e.printStackTrace (System.err);
*/
	    }
	}
	inst.compile();
	locale.addBranchGraph (inst);
    }




    protected void renderBAI()
    {
	locale.removeBranchGraph (bai);
	bai = new BranchGroup();
	bai.setCapability (BranchGroup.ALLOW_DETACH); 
//	bai.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	BAIFile baiFile = Config.baiFile;

	if (!showBAI || baiFile == null)
	{
	    return;
	}

	RouteList routes = baiFile.getRoutes();

	if (monitor != null)
	{
	    monitor.setMinimum (0);
	    monitor.setMaximum (routes.size());
	    monitor.setNote ("Rendering BAI");
	}

	for (int r = 0; r < routes.size(); r++)
	{
	    if (monitor != null)
	    {
		monitor.setProgress (r);
	    }
	    try
	    {
		Route route = routes.routeAt (r);

		// Create BAI component:
		//
		bai.addChild (createBAI (route));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (Config.mainWindow,
					       e + " at route " + Integer.toHexString (r));
/*
		System.err.println ("Terrain.renderBAI: " + e + " at route " + Integer.toHexString (r));
		e.printStackTrace (System.err);
*/
	    }
	}
	bai.compile();
	locale.addBranchGraph (bai);
    }







    /** Renders Pathsets
     *
     */
    public void renderPathsets (VertexList     vertices_,
				HeightList     heights_,
				TextureList    textures_,
				BlockList      blocks_,
				ProgressWindow monitor_)
    {
	initialize (vertices_, heights_, textures_, blocks_, monitor_);
	renderPathsets();
    }

    protected void renderPathsets()
    {
	if (pathsetGroup != null)
	{
	    locale.removeBranchGraph (pathsetGroup);
	}
	pathsetGroup = new BranchGroup();
	pathsetGroup.setCapability (BranchGroup.ALLOW_DETACH); 
//	pathsetGroup.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	if (!showPathset || Config.pathsetList.size() == 0)
	{
	    return;
	}

	for (int pset = 0; pset < Config.pathsetList.size(); pset++)
	{
	    Pathset pathset = Config.pathsetList.pathsetAt (pset);
	    BranchGroup bg = new BranchGroup();
	    bg.setCapability (BranchGroup.ALLOW_DETACH); 
//	    bg.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	    if (monitor != null)
	    {
		monitor.setMinimum (0);
		monitor.setMaximum (pathset.getPaths().size());
		monitor.setNote ("Rendering Pathset");
	    }
	    /*
		if (monitor != null)
		{
		    monitor.setProgress (pth);
		}
	    */
	    try
	    {
		bg.addChild (createPathObject (pathset));
	    }
	    catch (Exception e)
	    {
		String msg = e + " in pathset " + pathset.getName();

		JOptionPane.showMessageDialog (Config.mainWindow,
					       msg);

		System.err.println ("Terrain.renderPathsets: " + msg);
		e.printStackTrace (System.err);
	    }
	    pathsetGroup.addChild (bg);
	}
	pathsetGroup.compile();
	locale.addBranchGraph (pathsetGroup);
    }







    protected void renderLevel (VertexList l,
				float height)
    {
	if (level != null)
	{
	    locale.removeBranchGraph (level);
	}
	level = new BranchGroup();
	level.setCapability (BranchGroup.ALLOW_DETACH); 
	level.setCapability (BranchGroup.ALLOW_CHILDREN_EXTEND); 
	level.setCapability (BranchGroup.ALLOW_CHILDREN_WRITE); 
	level.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	float minX = Float.MAX_VALUE;
	float minZ = Float.MAX_VALUE;
	float maxX = Float.MIN_VALUE;
	float maxZ = Float.MIN_VALUE;
	for (int i = 0; i < l.size(); i++)
	{
	    Vertex3D v = l.vertexAt (i);
	    if (v.x < minX)
		minX = v.x;
	    else if (v.x > maxX)
		maxX = v.x;
	    if (v.z < minZ)
		minZ = v.z;
	    else if (v.z > maxZ)
		maxZ = v.z;
	}

	QuadArray qa = new QuadArray (4, QuadArray.COORDINATES);

//	qa.setCapability (GeometryArray.ALLOW_COLOR_READ);
//	qa.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
//	qa.setCapability (GeometryArray.ALLOW_COUNT_READ);
//	qa.setCapability (GeometryArray.ALLOW_FORMAT_READ);
//	qa.setCapability (GeometryArray.ALLOW_NORMAL_READ);
//	qa.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
//	qa.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	qa.setCoordinate (0, new Point3f (minX, height, minZ));
	qa.setCoordinate (1, new Point3f (minX, height, maxZ));
	qa.setCoordinate (2, new Point3f (maxX, height, maxZ));
	qa.setCoordinate (3, new Point3f (maxX, height, minZ));

	Shape3D s = new Shape3D (qa);
//	s.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
//	s.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
//	s.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
//	s.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	Appearance a = new Appearance();
//	a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_MATERIAL_READ);
//	a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXGEN_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//	a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	// Create polygon attributes:
	//
	PolygonAttributes pAttr = new PolygonAttributes();
	pAttr.setPolygonMode (PolygonAttributes.POLYGON_FILL);
	pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
	a.setPolygonAttributes (pAttr);

	Material m = new Material();
//	m.setCapability (Material.ALLOW_COMPONENT_READ);

	m.setDiffuseColor (new Color3f (1.0f, 1.0f, 1.0f));
	m.setAmbientColor (new Color3f (1.0f, 1.0f, 1.0f));
	a.setMaterial (m);
	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (0.3f, 0.7f, 1.0f));
	a.setColoringAttributes (cAttr);
	s.setAppearance (a);

	level.addChild (s);
	level.compile();
	locale.addBranchGraph (level);
    }


    public void renderEdges (LineArray la)
    {
	if (edges != null)
	{
	    level.removeChild (edges);
	}

	edges = new BranchGroup();
	edges.setCapability (BranchGroup.ALLOW_DETACH); 
//	edges.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	Shape3D s = new Shape3D (la);
//	s.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
//	s.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
//	s.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
//	s.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	Appearance a = new Appearance();
//	a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_MATERIAL_READ);
//	a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXGEN_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//	a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	// Create line attributes:
	//
	LineAttributes lAttr = new LineAttributes();
	lAttr.setLineWidth (5.0f);
	lAttr.setLineAntialiasingEnable(true);
	a.setLineAttributes (lAttr);

	// Create polygon attributes:
	//
	PolygonAttributes pAttr = new PolygonAttributes();
	pAttr.setPolygonMode (PolygonAttributes.POLYGON_LINE);
	pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
	a.setPolygonAttributes (pAttr);

	Material m = new Material();
//	m.setCapability (Material.ALLOW_COMPONENT_READ);

	m.setDiffuseColor (new Color3f (1.0f, 1.0f, 1.0f));
	m.setAmbientColor (new Color3f (1.0f, 1.0f, 1.0f));
	a.setMaterial (m);
	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (1.0f, 0.0f, 0.0f));
	a.setColoringAttributes (cAttr);
	s.setAppearance (a);



	edges.addChild (s);
	level.addChild (edges);
    }







    protected BranchGroup createBlockObject (Block b)
    throws Exception
    {
	BranchGroup root = new BranchGroup();
	root.setCapability (BranchGroup.ALLOW_DETACH); 
//	root.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 
	boolean rendered = false;

	// Create default appearance:
	//
	Appearance a = new Appearance();
//	a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_MATERIAL_READ);
//	a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXGEN_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//	a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	// Create default polygon attributes:
	//
	PolygonAttributes pAttr = new PolygonAttributes();
	switch (drawingMode)
	{
	case DRAWING_LINE:
	    pAttr.setPolygonMode (PolygonAttributes.POLYGON_LINE);
	    break;
	case DRAWING_FILL:
	    pAttr.setPolygonMode (PolygonAttributes.POLYGON_FILL);
	    break;
	}

	pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
	a.setPolygonAttributes (pAttr);

	float red   = 1.0f;
	float green = 1.0f;
	float blue  = 1.0f;
	switch (colouringMode)
	{
	case COLOURING_GREY:
	    red   = ((float)1 / blocks.size()) / 2f + 0.5f;
	    green = red;
	    blue  = red;
	    break;
	case COLOURING_RANDOM:
	    red   = (float)Math.random() * 0.7f + 0.3f;
	    green = (float)Math.random() * 0.7f + 0.3f;
	    blue  = (float)Math.random() * 0.7f + 0.3f;
	    break;
	case COLOURING_TEXTURE_INDEX:
	    break;
	}

	Material m = new Material();
//	m.setCapability (Material.ALLOW_COMPONENT_READ);

	m.setDiffuseColor (new Color3f (red, green, blue));
	m.setAmbientColor (new Color3f (red, green, blue));
	a.setMaterial (m);
	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (red, green, blue));
	a.setColoringAttributes (cAttr);

	if (renderBlockAttributes)
	{
	    // Build geometry of all block attributes:
	    //
	    //
	    int j;
	    TriangleFan   fan;

	    SidewalkStrip sidewalkStrip;
	    TriangleStrip triangleStrip;

	    VertexListAttribute vlAttribute;

	    TextureAttribute textureAttribute = null;
	    TextureMapRef tex0 = null;
	    TextureMapRef tex1 = null;
	    TextureMapRef tex2 = null;

	    Attribute004b tunnelAttribute = null;


	    BlockAttributeList attr = b.getAttributes();
	    for (int i = 0; i < attr.size(); i++)
	    {
		boolean invertHeight = false;
		    
		BlockAttribute ba = attr.attributeAt (i);
		switch (ba.getID())
		{

	 	case 0x000a:
		    vlAttribute = (VertexListAttribute)ba;

		    VertexRefList l = vlAttribute.getVertexRefs();
		    if (   l.referenceAt (0).getIndex()
			!= l.referenceAt (1).getIndex())
		    {
			sidewalkStrip = new SidewalkStrip (
						SidewalkStrip.STANDALONE,
						false, // Right oriented
						vertices,
						vlAttribute.getVertexRefs(), 
						textureStore,
						tex1,
						a);
			root.addChild (sidewalkStrip);
			rendered = true;
		    }
		    else
		    {

			Point3f p[] = new Point3f[3];
			if (l.referenceAt (0).getIndex() == (char)0)
			{
			    p[2] = vertices.getVertex (l.referenceAt (2)).toPoint3f();
			    p[1] = vertices.getVertex (l.referenceAt (3)).toPoint3f();
			    p[0] = new Point3f (p[2]);
			    p[0].y += 0.15f;
			}
			else
			{
			    p[0] = vertices.getVertex (l.referenceAt (2)).toPoint3f();
			    p[1] = vertices.getVertex (l.referenceAt (3)).toPoint3f();
			    p[2] = new Point3f (p[0]);
			    p[2].y += 0.15f;
			}
			root.addChild (new Shape_Triangle (p, tex1));
			rendered = true;
		    }
		    break;

		case 0x0008:
		case 0x000b:
		case 0x000c:
		case 0x000d:
		case 0x000e:
		case 0x000f:
		    vlAttribute = (VertexListAttribute)ba;
		    sidewalkStrip = new SidewalkStrip (
						SidewalkStrip.STANDALONE,
						false, // Right oriented
						vertices,
						vlAttribute.getVertexRefs(), 
						textureStore,
						tex1,
						a);
		    root.addChild (sidewalkStrip);
		    rendered = true;
		    break;

		case 0x0024:
		    vlAttribute = (VertexListAttribute)ba;
		    triangleStrip = new TriangleStrip (
					      vertices,
					      vlAttribute.getVertexRefs(), 
					      textureStore,
					      tex2,
					      true,
					      a);
		    root.addChild (triangleStrip);
		    rendered = true;
		    break;

		case 0x0028:
		case 0x0029:
		case 0x002a:
		case 0x002b:
		case 0x002c:
		case 0x002d:
		case 0x002e:
		case 0x002f:
		    vlAttribute = (VertexListAttribute)ba;

		    if (   colouringMode == COLOURING_TEXTURE_MAP
			&& tex0 != null)
		    {
			fan = new TriangleFan (vertices,
					       vlAttribute.getVertexRefs(),
					       textureStore,
					       tex0,
					       a);
		    }
		    else
		    {
			fan = new TriangleFan (vertices,
					       vlAttribute.getVertexRefs(),
					       a);
		    }
		    root.addChild (fan);
		    rendered = true;
		    break;

		case 0x0030:
		case 0x0031:
		case 0x0032:
		case 0x0033:
		case 0x0034:
		case 0x0035:
		case 0x0036:
		case 0x0037:
		    Attribute0030 a30 = (Attribute0030)ba;

		    if (   colouringMode == COLOURING_TEXTURE_MAP
			&& tex0 != null)
		    {
			fan = new TriangleFan (vertices,
					       a30.getVertexRefs(),
					       textureStore,
					       tex0,
					       a);
		    }
		    else
		    {
			fan = new TriangleFan (vertices,
					       a30.getVertexRefs(), a);
		    }
		    root.addChild (fan);
		    rendered = true;
		    break;


		case 0x0050:
		case 0x0051:
		    textureAttribute = (TextureAttribute)ba;

		    tex0 = textureAttribute.ref;

		    // TODO: This should be handled by each object itself:
		    TextureList tList  = Config.getTextureList();
		    int         tIndex = tex0.getIndex();

		    // TODO: A bit fishy, this is:
		    tex0.setList (tList);

		    if (tIndex == 0xffff)
		    {
			tex0 = null;
			tex1 = null;
			tex2 = null;
		    }
		    else
		    {
			if (tList.size() > tIndex + 1)
			{
			    tex1 = new TextureMapRef (tIndex + 1, tList);
			}
			else
			{
			    tex1 = null;
			}

			if (tList.size() > tIndex + 2)
			{
			    tex2 = new TextureMapRef (tIndex + 2, tList);
			}
			else
			{
			    tex2 = null;
			}
		    }
		    break;



		case 0x003c:
		    break;


		case 0x001c: // Ground facade, no repeat
		    root.addChild (new Shape_Facade ((Attribute001c)ba,
						     textureAttribute));
		    rendered = true;
		    break;


		case 0x005e: // Facade
		    root.addChild (new Shape_Facade ((Attribute005e)ba,
						     textureAttribute));
		    rendered = true;
		    break;

		case 0x0060:
		case 0x0062:
		case 0x0063:
		case 0x0064:
		case 0x0065:
		case 0x0066:
		case 0x0067:
		    Attribute0060 a60 = (Attribute0060)ba;
		    if (   colouringMode == COLOURING_TEXTURE_MAP
			&& tex0 != null)
		    {
			fan = new TriangleFan (vertices,
					       a60.getVertexRefs(),
					       heights,
					       a60.height,
					       textureStore,
					       tex0,
					       a);
		    }
		    else
		    {
			fan = new TriangleFan (vertices,
					       a60.getVertexRefs(),
					       heights,
					       a60.height,
					       a);
		    }
		    root.addChild (fan);
		    rendered = true;
		    break;




		case 0x0000: // Road strips:
		case 0x0002:
		case 0x0003:
		case 0x0004:
		case 0x0005:
		case 0x0006:
		case 0x0007:
		    root.addChild (new Shape_Roadsection ((Attribute0000)ba,
							  textureAttribute,
							  tunnelAttribute));
		    rendered = true;
		    break;

		case 0x0010:
		case 0x0012:
		case 0x0013:
		case 0x0014:
		case 0x0015:
		case 0x0016:
		case 0x0017:
		    root.addChild (new Shape_Roadsection ((Attribute0010)ba,
							  textureAttribute,
							  tunnelAttribute));
		    rendered = true;
		    break;

		case 0x0040:	// Divided roads:
		case 0x0042:
		case 0x0043:
		case 0x0044:
		case 0x0045:
		case 0x0046:
		case 0x0047:
		    root.addChild (new Shape_Roadsection ((Attribute0040)ba,
							  textureAttribute,
							  tunnelAttribute));
		    rendered = true;
		    break;



		default:
		    System.out.println ("Attribute \"" + ba + "\" not rendered.");
		    break;
		}
	    }
	}
	else //if (!rendered)
	{
	    TriangleFan fan = new TriangleFan (vertices,
	                                       b.getPerimeter(),
					       a);
	    root.addChild (fan);
	 }

	return root;
    }



    protected BranchGroup createINST (INSTComponent c)
    throws Exception
    {
	BranchGroup root = new BranchGroup();
	root.setCapability (BranchGroup.ALLOW_DETACH); 
//	root.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	TransformGroup g = new TransformGroup();
	Transform3D    t  = new Transform3D();

	Node pkg = pkgStore.getPKGNode (c.getPKGName(),
					PKG.LOD_H,
					c.getUnknown() & 0x7f,
					Config.geometryPath,
					textureStore,
					Config.texturePath);

	if (pkg == null)
	{
	    return (null);
	}

	switch (c.getType())
	{
	case INSTComponent.SIMPLE:
	    INSTSimpleComponent sc = (INSTSimpleComponent)c;

	    float scale = sc.getScale();
	    float angle = sc.getAngle();

	    t.setEuler (new Vector3d (0d, angle, 0d));
	    t.setScale (new Vector3d (scale, 1d, scale));
	    t.setTranslation (new Vector3f (sc.getOrigo().toPoint3f()));
	    break;

	case INSTComponent.COORDINATESYSTEM:
	    INSTCoordinateSystemComponent cc = (INSTCoordinateSystemComponent)c;
	    Vertex3D xAxis = cc.getXAxis();
	    Vertex3D yAxis = cc.getYAxis();
	    Vertex3D zAxis = cc.getZAxis();
	    Vertex3D origo = cc.getOrigo();
	    Matrix4f matrix = new Matrix4f (xAxis.x, yAxis.x, zAxis.x, origo.x,
					    xAxis.y, yAxis.y, zAxis.y, origo.y,
					    xAxis.z, yAxis.z, zAxis.z, origo.z,
					    0f,      0f,      0f,      1f);
	    t.set (matrix);
	    break;
	}

	g.setTransform (t);



	Appearance a = new Appearance();
//	a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_MATERIAL_READ);
//	a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXGEN_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//	a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	PolygonAttributes pAttr = new PolygonAttributes();
	switch (drawingMode)
	{
	case DRAWING_LINE:
	    pAttr.setPolygonMode (PolygonAttributes.POLYGON_LINE);
	    break;
	case DRAWING_FILL:
	    pAttr.setPolygonMode (PolygonAttributes.POLYGON_FILL);
	    break;
	}

	pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
	a.setPolygonAttributes (pAttr);

	float red   = 1.0f;
	float green = 1.0f;
	float blue  = 1.0f;
	switch (colouringMode)
	{
	case COLOURING_GREY:
	    red   = ((float)1 / blocks.size()) / 2f + 0.5f;
	    green = red;
	    blue  = red;
	    break;
	case COLOURING_RANDOM:
	    red   = (float)Math.random() * 0.7f + 0.3f;
	    green = (float)Math.random() * 0.7f + 0.3f;
	    blue  = (float)Math.random() * 0.7f + 0.3f;
	    break;
	case COLOURING_TEXTURE_INDEX:
	    break;
	}

	red   = (float)Math.random() * 0.7f + 0.3f;
	green = (float)Math.random() * 0.7f + 0.3f;
	blue  = (float)Math.random() * 0.7f + 0.3f;
		
	Material m = new Material();
//	m.setCapability (Material.ALLOW_COMPONENT_READ);

	m.setDiffuseColor (new Color3f (red, green, blue));
	m.setAmbientColor (new Color3f (red, green, blue));
	a.setMaterial (m);
	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (red, green, blue));
	a.setColoringAttributes (cAttr);

	g.addChild (pkg);
	root.addChild (g);

	return root;
    }





    protected BranchGroup createBAI (Route route)
    {
	//0f                // Red
	//(float)(1f / 5f)  // Yellow
	//(float)(1f / 3f)  // Green
	//(float)(2f / 3f)  // Blue

	BranchGroup center = new BranchGroup();
	center.setCapability (BranchGroup.ALLOW_DETACH); 
//	center.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 


	Vector centralLine = route.getCentralLine();
	int nSections = centralLine.size();
	center.addChild (generateCurve (0f,
					LineAttributes.PATTERN_DASH,
					centralLine,
					0,
					nSections));

	RoadData rd = route.getRight();
	renderRoadData (rd, center, (float)(2f / 3f), nSections);

	rd = route.getLeft();
	renderRoadData (rd, center, (float)(1f / 3f), nSections);

	return center;
    }


    protected void renderRoadData (RoadData    rd,
				   BranchGroup group,
				   float       laneHUE,
				   int         nSections)
    {
	Vector lanes = rd.getLaneVertices();
	Vector points;
	for (int l = 0; l < rd.getNLanes(); l++)
	{
	    group.addChild (generateCurve (laneHUE,
					   LineAttributes.PATTERN_SOLID,
					   lanes,
					   l * nSections,
					   (l + 1) * nSections));
	}

	points = rd.getSidewalkCenter();
	group.addChild (generateCurve ((float)(1f / 5f),
				       LineAttributes.PATTERN_SOLID,
				       points,
				       0,
				       nSections));
	points = rd.getSidewalkInner();
	group.addChild (generateCurve ((float)(1f / 5f),
				       LineAttributes.PATTERN_SOLID,
				       points,
				       0,
				       nSections));
	points = rd.getSidewalkOuter();
	group.addChild (generateCurve ((float)(1f / 5f),
				       LineAttributes.PATTERN_SOLID,
				       points,
				       0,
				       nSections));

	points = rd.getTramVertices();
	for (int tram = 0; tram < rd.getNTrams(); tram++)
	{
	    group.addChild (generateCurve ((float)(4f / 5f),
					   LineAttributes.PATTERN_SOLID,
					   points,
					   tram * nSections,
					   (tram + 1) * nSections));
	}

	points = rd.getTrainVertices();
	for (int train = 0; train < rd.getNTrains(); train++)
	{
	    group.addChild (generateCurve ((float)(1f / 9f),
					   LineAttributes.PATTERN_SOLID,
					   points,
					   train * nSections,
					   (train + 1) * nSections));
	}
    }



    protected Shape3D generateCurve (float  hue,
				     int    pattern,
				     Vector points,
				     int    first,
				     int    last)
    {
	int nPoints = last - first;
	int[] stripCounts = {nPoints};
	LineStripArray la = new LineStripArray (nPoints,
						  LineStripArray.COORDINATES
						| LineStripArray.COLOR_3,
						stripCounts);

	float factor = 1f / nPoints; 
	for (int s = 0; s < nPoints; s++)
	{
	    la.setCoordinate (s,
		    ((Vertex3D)points.elementAt (s + first)).toPoint3f());
	    Color c = Color.getHSBColor (hue, (s * factor), 1f);
	    la.setColor (s, new Color3f (c));
	}

	Shape3D s = new Shape3D (la);
	Appearance a = new Appearance();

	// Create line attributes:
	//
	LineAttributes lAttr = new LineAttributes();
	lAttr.setLinePattern (pattern);
	lAttr.setLineWidth (2.5f);
	lAttr.setLineAntialiasingEnable(true);
	a.setLineAttributes (lAttr);

	// Create polygon attributes:
	//
	PolygonAttributes pAttr = new PolygonAttributes();
	pAttr.setPolygonMode (PolygonAttributes.POLYGON_LINE);
	pAttr.setCullFace    (PolygonAttributes.CULL_BACK);
	a.setPolygonAttributes (pAttr);

	Material m = new Material();
//	m.setCapability (Material.ALLOW_COMPONENT_READ);

	m.setDiffuseColor (new Color3f (1.0f, 1.0f, 1.0f));
	m.setAmbientColor (new Color3f (1.0f, 1.0f, 1.0f));
	a.setMaterial (m);
	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (1.0f, 0.0f, 0.0f));
	a.setColoringAttributes (cAttr);
	s.setAppearance (a);
	return s;
    }





    protected BranchGroup createPathObject (Pathset pathset)
    throws Exception
    {
	BranchGroup root = new BranchGroup();
	root.setCapability (BranchGroup.ALLOW_DETACH); 
//	root.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	PathList l = pathset.getPaths();
	if (l == null)
	    return null;


	for (int pth = 0; pth < l.size(); pth++)
	{
	    try
	    {
		Path path = l.pathAt (pth);
		for (int pt = 0; pt < path.getPoints().size() - 1; pt++)
		{
		    Point3f p0 = path.getPoints().pathPointAt (pt).getPoint().toPoint3f();
		    Point3f p1 = path.getPoints().pathPointAt (pt + 1).getPoint().toPoint3f();

		    Vector3f direction = new Vector3f();
		    direction.sub (p1, p0);
		    float distance = direction.length();
		    direction.normalize();

		    double angle = 0.0;

		    if (direction.x < 0 && direction.z < 0)
		    {
			angle = -Math.atan (  -direction.z
					    / -direction.x);
		    }
		    else if (direction.x < 0 && direction.z >= 0)
		    {
			angle = Math.atan (   direction.z
					   / -direction.x)
			    + Math.PI;
		    }
		    else if (direction.x >= 0 && direction.z < 0)
		    {
			angle = -Math.atan (  -direction.z
					    /  direction.x);

		    }

		    int nProps = 1;
		    int step = 2;

		    switch (path.getType())
		    {
		    case Path.TYPE_POINTS:
			nProps = 1;
			step = 2;
			break;

		    case Path.TYPE_LINES:
//			nProps = (int)(distance / path.getSpacing());
			nProps = 1;
			step = 2;
			break;

		    case Path.TYPE_LINESTRIP:
			nProps = (int)(distance / path.getSpacing());
			step = 1;
			break;

		    default:
			throw new Exception ("Unknown path type");
		    }

		    Vector3f origin = new Vector3f (p0);
		    for (int prp = 0; prp < nProps; prp++) // += step)
		    {
			try
			{
			    PKG pkg = pkgStore.getPKG (path.getName(),
						       Config.geometryPath);
			    Node pkgNode = pkgStore.getPKGNode (path.getName(),
						    PKG.LOD_H,
						    0,       // Paintjob
						    Config.geometryPath,
						    textureStore,
						    Config.texturePath);

			    float height = bogusFindHeight (pkg);
		    
			    TransformGroup g = new TransformGroup();
			    Transform3D    t = new Transform3D();

			    t.setEuler (new Vector3d (0d, angle, 0d));
			
			    origin.y += height / 2f;
			    t.setTranslation (origin);

			    g.setTransform (t);
			    g.addChild (pkgNode);
			    root.addChild (g);

			    if (   path.getName().equals ("sp_transbayexit_f")
				|| path.getName().equals ("sp_embarcexit_f")
				|| path.getName().equals ("sp_civcentrexit_f")
				|| pth == 0x2d
				|| pth == 0x4c)
			    {
				System.err.println (path.getName());
				System.err.println (p0 + "->" + p1);
				System.err.println ("Direction: " + direction);
				System.err.println ("Angle: " + 180 * angle / Math.PI + ", path: " + Integer.toHexString (pth));
				System.err.println ("Height: " + height + ", origo: " + origin);
				System.err.println ("nProps: " + nProps + ", step: " + step);
			    }

			    origin.scaleAdd (path.getSpacing() * (prp + 1), direction, p0);
			}
			catch (FileNotFoundException e)
			{
			    // TODO: Check for decal.
			    Texture2D tex = textureStore.getTexture (path.getName(), Config.texturePath);
			    if (tex != null)
			    {
				PathPointList pl = path.getPoints();
				root.addChild (new Shape_Decal (pl, tex));
			    }
			}

		    }
		}
	    }
	    catch (Exception e)
	    {
		String msg = e + " in pathset " + pathset.getName() + ", path " + Integer.toHexString (pth) + ".";

		JOptionPane.showMessageDialog (Config.mainWindow,
					       msg);

		System.err.println ("Terrain.renderPathsets: " + msg);
		e.printStackTrace (System.err);
	    }
	}
	return root;
    }

    protected float bogusFindHeight (PKG pkg)
    {
	return (float)(Math.abs (pkg.getMaxY() - pkg.getMinY()));

	/*
	Point3d p = new Point3d();
	Bounds bounds = pkg.getBounds();
	BoundingSphere bsphere = (BoundingSphere)bounds;
	bsphere.getCenter (p);
	return (float)(p.y + bsphere.getRadius());
	*/
    }





    protected Node createBlockIDLabel (Block block, int index)
    {
	Point3f midPoint = new Point3f();
	PerimeterList perimeter = block.getPerimeter();
	if (perimeter == null)
	{
	    return null;
	}

	int n = perimeter.size();
	for (int i = 0; i < n; i++)
	{
	    PerimeterVertex pv = perimeter.perimeterAt (i);
	    Vertex3D v = vertices.vertexAt (pv.getIndex());
	    midPoint.x += v.x;
	    midPoint.y += v.y;
	    midPoint.z += v.z;
	}
	midPoint.x /= n;
	midPoint.y /= n;
	midPoint.z /= n;
	Vertex3D v = new Vertex3D (midPoint.x,
				   midPoint.y + 2.0f,
				   midPoint.z);

	TransformGroup group = new TransformGroup();
	group.setCapability (BranchGroup.ALLOW_DETACH); 
	Transform3D t = new Transform3D();
	t.set (new Vector3f (v.toPoint3f()));
	group.setTransform (t);

	TransformGroup tgr = new TransformGroup();

	tgr.setCapability (TransformGroup.ALLOW_TRANSFORM_WRITE);
	group.addChild (tgr);

	OrientedShape3D shape = new OrientedShape3D ();
//	shape.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
//	shape.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
//	shape.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
//	shape.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	shape.setAlignmentMode (OrientedShape3D.ROTATE_ABOUT_POINT);
	shape.setRotationPoint (0f, 0f, 0f);
	shape.setGeometry (new Text3D (font, Integer.toHexString (index)));

	Appearance a = new Appearance();
//	a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_MATERIAL_READ);
//	a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXGEN_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_READ);
//	a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//	a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	ColoringAttributes cAttr = new ColoringAttributes();
	cAttr.setColor (new Color3f (1.0f, 0.0f, 0.0f));
	a.setColoringAttributes (cAttr);
	shape.setAppearance (a);
	group.addChild (shape);
	return group;
    }


/*
    }
*/


    public void addCamera (Camera c)
    {
//	System.err.println ("Add: " + c);
	c.setCapability (BranchGroup.ALLOW_DETACH); 
	view.addChild (c);
    }

    public void removeCamera (Camera c)
    {
//	System.err.println ("Remove: " + c + " " + c.getCapability (BranchGroup.ALLOW_DETACH));
//	view.removeChild (c);
    }














    /** Clears all markers:
     *
     */
    public void clearMarkers()
    {
	if (markers != null)
	{
	    locale.removeBranchGraph (markers);
	    markers = null;
	}
    }


    // VertexSelectionListener methods:
    //
    //
    public void verticesSelected (Object[] p)
    {

	clearMarkers();
	markers = new BranchGroup();
	markers.setCapability (BranchGroup.ALLOW_DETACH);
//	markers.setCapability (BranchGroup.ALLOW_CHILDREN_READ); 

	LineArray la = new LineArray (6, LineArray.COORDINATES);

//	la.setCapability (GeometryArray.ALLOW_COLOR_READ);
//	la.setCapability (GeometryArray.ALLOW_COORDINATE_READ);
//	la.setCapability (GeometryArray.ALLOW_COUNT_READ);
//	la.setCapability (GeometryArray.ALLOW_FORMAT_READ);
//	la.setCapability (GeometryArray.ALLOW_NORMAL_READ);
//	la.setCapability (GeometryArray.ALLOW_REF_DATA_READ);
//	la.setCapability (GeometryArray.ALLOW_TEXCOORD_READ);

	la.setCoordinate (0, new Point3f (-1.0f, 0f, 0f));
	la.setCoordinate (1, new Point3f ( 1.0f, 0f, 0f));

	la.setCoordinate (2, new Point3f (0f, -1.0f, 0f));
	la.setCoordinate (3, new Point3f (0f,  1.0f, 0f));

	la.setCoordinate (4, new Point3f (0f, 0f, -1.0f));
	la.setCoordinate (5, new Point3f (0f, 0f,  1.0f));

	for (int i = 0; i < p.length; i++)
	{
	    TransformGroup tg = new TransformGroup();
	    Transform3D    t  = new Transform3D();
	    t.set (new Vector3f (((Vertex3D)p[i]).toPoint3f()));
	    tg.setTransform (t);

	    Shape3D shape = new Shape3D (la);
//	    shape.setCapability (Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
//	    shape.setCapability (Shape3D.ALLOW_APPEARANCE_READ);
//	    shape.setCapability (Shape3D.ALLOW_COLLISION_BOUNDS_READ);
//	    shape.setCapability (Shape3D.ALLOW_GEOMETRY_READ);

	    Appearance a = new Appearance();
//	    a.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//	    a.setCapability (Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//	    a.setCapability (Appearance.ALLOW_MATERIAL_READ);
//	    a.setCapability (Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//	    a.setCapability (Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//	    a.setCapability (Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//	    a.setCapability (Appearance.ALLOW_TEXGEN_READ);
//	    a.setCapability (Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//	    a.setCapability (Appearance.ALLOW_TEXTURE_READ);
//	    a.setCapability (Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//	    a.setCapability (Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	    ColoringAttributes cAttr = new ColoringAttributes();
	    cAttr.setColor (new Color3f (1.0f, 0f, 0f));
	    a.setColoringAttributes (cAttr);

	    shape.setAppearance (a);

	    tg.addChild (shape);
	    markers.addChild (tg);
	}
	locale.addBranchGraph (markers);
    }







    /* Methods that handle rendering of block bound perimeters:
     *
     */


    /** Clears all block bounds:
     *
     */
    public void clearBlockBounds()
    {
	if (blockBounds != null)
	{
	    locale.removeBranchGraph (blockBounds);
	    blockBounds = null;
	}
    }

    // BlockSelectionListener methods:
    //
    //
    public void blocksSelected (Object[] p)
    {
	if (!showBlockBound)
	{
	    return;
	}

	clearBlockBounds();
	blockBounds = new BranchGroup();
	blockBounds.setCapability (BranchGroup.ALLOW_DETACH);


	for (int b = 0; b < p.length; b++)
	{
	    PerimeterList l = ((Block)p[b]).getPerimeter();
	    Vector points = new Vector();
	    int index = -1;
	    for (int point = 0; point < l.size(); point++)
	    {
		PerimeterVertex pv = l.perimeterAt (point);
		int i = (int)pv.getIndex();
		if (i != index)
		{
		    points.add (vertices.vertexAt (i));
		    index = i;
		}
	    }

	    int nSections = points.size();
	    blockBounds.addChild (generateCurve (2f / 3f,
				      LineAttributes.PATTERN_SOLID,
				      points,
				      0,
				      nSections));
	}

/* TODO: Draw lines between each perimeter vertex.
	LineArray la = new LineArray (6, LineArray.COORDINATES);

	la.setCoordinate (0, new Point3f (-1.0f, 0f, 0f));
	la.setCoordinate (1, new Point3f ( 1.0f, 0f, 0f));

	la.setCoordinate (2, new Point3f (0f, -1.0f, 0f));
	la.setCoordinate (3, new Point3f (0f,  1.0f, 0f));

	la.setCoordinate (4, new Point3f (0f, 0f, -1.0f));
	la.setCoordinate (5, new Point3f (0f, 0f,  1.0f));

	for (int i = 0; i < p.length; i++)
	{
	    TransformGroup tg = new TransformGroup();
	    Transform3D    t  = new Transform3D();
	    t.set (new Vector3f (((Vertex3D)p[i]).toPoint3f()));
	    tg.setTransform (t);

	    Shape3D shape = new Shape3D (la);
	    Appearance a = new Appearance();
	    ColoringAttributes cAttr = new ColoringAttributes();
	    cAttr.setColor (new Color3f (1.0f, 0f, 0f));
	    a.setColoringAttributes (cAttr);

	    shape.setAppearance (a);

	    tg.addChild (shape);
	    markers.addChild (tg);
	}
*/
	locale.addBranchGraph (blockBounds);
    }







    /** Clears all route markers:
     *
     */
    public void clearRouteMarkers()
    {
	if (routeMarkers != null)
	{
	    locale.removeBranchGraph (routeMarkers);
	    routeMarkers = null;
	}
    }

    // RouteSelectionListener methods:
    //
    //
    public void routesSelected (Object[] p)
    {
	if (!showBAI || Config.baiFile == null)
	{
	    return;
	}

	clearRouteMarkers();
	routeMarkers = new BranchGroup();
	routeMarkers.setCapability (BranchGroup.ALLOW_DETACH);


	Vector centralLine;
	Vector xOrientation;
	Vector yOrientation;
	Vector zOrientation;
	Vector wOrientation;

	for (int b = 0; b < p.length; b++)
	{
	    Route route = (Route)p[b];
	    centralLine = route.getCentralLine();
	    xOrientation = route.getXOrientation();
	    yOrientation = route.getYOrientation();
	    zOrientation = route.getZOrientation();
	    wOrientation = route.getWOrientation();

	    for (int r = 0; r < centralLine.size(); r++)
	    {
		Point3f origo = ((Vertex3D)centralLine.elementAt (r)).toPoint3f();
		Point3f e0    = ((Vertex3D)xOrientation.elementAt (r)).toPoint3f();
		Point3f e1    = ((Vertex3D)yOrientation.elementAt (r)).toPoint3f();
		Point3f e2    = ((Vertex3D)zOrientation.elementAt (r)).toPoint3f();
		Point3f v     = ((Vertex3D)wOrientation.elementAt (r)).toPoint3f();

		e0.x += origo.x;
		e0.y += origo.y;
		e0.z += origo.z;
		e1.x += origo.x;
		e1.y += origo.y;
		e1.z += origo.z;
		e2.x += origo.x;
		e2.y += origo.y;
		e2.z += origo.z;
		v.x  += origo.x;
		v.y  += origo.y;
		v.z  += origo.z;

		LineArray la = new LineArray (8,   LineArray.COORDINATES
					         | LineArray.COLOR_3);

		la.setColor (0, new Color3f (1f, 0f, 0f));
		la.setColor (1, new Color3f (1f, 0f, 0f));
		la.setCoordinate (0, origo);
		la.setCoordinate (1, e0);

		la.setColor (2, new Color3f (0f, 1f, 0f));
		la.setColor (3, new Color3f (0f, 1f, 0f));
		la.setCoordinate (2, origo);
		la.setCoordinate (3, e1);

		la.setColor (4, new Color3f (0f, 0f, 1f));
		la.setColor (5, new Color3f (0f, 0f, 1f));
		la.setCoordinate (4, origo);
		la.setCoordinate (5, e2);

		la.setColor (6, new Color3f (1f, 0f, 1f));
		la.setColor (7, new Color3f (1f, 0f, 1f));
		la.setCoordinate (6, origo);
		la.setCoordinate (7, v);

		Shape3D shape = new Shape3D (la);

		Appearance a = new Appearance();

		// Create line attributes:
		//
		LineAttributes lAttr = new LineAttributes();
		lAttr.setLineWidth (3f);
		lAttr.setLineAntialiasingEnable(true);
		a.setLineAttributes (lAttr);

		shape.setAppearance (a);

		routeMarkers.addChild (shape);
	    }
	}
	locale.addBranchGraph (routeMarkers);
    }














    /** Recreates the scenegraph and displays the new one.
     *
     */
    public void redraw()
    {
	createTerrain (vertices, heights, textures, blocks, null);
    }





    /** @return the current drawing mode of this view.
     *
     */
    public int getDrawingMode()
    {
	return drawingMode;
    }

    /** Sets the drawing mode to use in this view. The changes are not displayed
     *  immediately, a call to "redraw" must be made for the changes to appear.
     *
     */
    public void setDrawingMode (int mode)
    {
	drawingMode = mode;
    }


    /** @return the current lighting mode of this view.
     *
     */
    public int getLightingMode()
    {
	return lightingMode;
    }

    /** Sets the lighting mode to use in this view. The changes are not displayed
     *  immediately, a call to "redraw" must be made for the changes to appear.
     *
     */
    public void setLightingMode (int mode)
    {
	lightingMode = mode;
    }


    /** @return the current colouring mode of this view.
     *
     */
    public int getColouringMode()
    {
	return colouringMode;
    }

    /** Sets the colouring mode to use in this view. The changes are not displayed
     *  immediately, a call to "redraw" must be made for the changes to appear.
     *
     */
    public void setColouringMode (int mode)
    {
	colouringMode = mode;
    }



    /** @return true if this view displays city block indices.
     *
     */
    public boolean getShowBlockIndices()
    {
	return showBlockIndices;
    }


    /** Controls whether this view displays city block indices or not.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setShowBlockIndices (boolean b)
    {
	showBlockIndices = b;
    }



    /** @return true if this view renders city block attributes.
     *
     */
    public boolean getRenderBlockAttributes()
    {
	return renderBlockAttributes;
    }


    /** Controls whether this view renders city block attributess or perimeters
     *  only.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setRenderBlockAttributes (boolean b)
    {
	renderBlockAttributes = b;
    }

    /** @return true if this view displays block bounds.
     *
     */
    public boolean getShowBlockBound()
    {
	return showBlockBound;
    }


    /** Controls whether this view displays block bounds or not.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setShowBlockBound (boolean b)
    {
	showBlockBound = b;
    }




    /** @return true if this view displays city bounds.
     *
     */
    public boolean getShowBound()
    {
	return showBound;
    }


    /** Controls whether this view displays city bounds or not.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setShowBound (boolean b)
    {
	showBound = b;
    }






    /** @return true if this view renders INST objects.
     *
     */
    public boolean getShowINST()
    {
	return showINST;
    }


    /** Controls whether this view renders INST objects.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setShowINST (boolean b)
    {
	showINST = b;
    }


    /** @return true if this view displays pathset props.
     *
     */
    public boolean getShowPathset()
    {
	return showPathset;
    }


    /** Controls whether this view displays pathset props or not.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setShowPathset (boolean b)
    {
	showPathset = b;
    }

    /** @return true if this view displays BAI rails.
     *
     */
    public boolean getShowBAI()
    {
	return showBAI;
    }


    /** Controls whether this view displays city BAI rails or not.
     *
     *  The changes are not displayed immediately, a call to "redraw" must be
     *  made for the changes to appear.
     *
     */
    public void setShowBAI (boolean b)
    {
	showBAI = b;
    }





    public Font3D getFont()
    {
	return font;
    }

    public void setFont (String family, String size)
    {
	Hashtable attrs = new Hashtable();
	String s;

	family = family.trim();
	if (family != null && family.length() != 0)
	{
	    attrs.put (TextAttribute.FAMILY, family);
	}

	size = size.trim();
	if (size != null && size.length() != 0)
	{
	    attrs.put (TextAttribute.SIZE, new Float (size));
	}

	font = new Font3D (Font.getFont (attrs), null);
    }







    /** Sets the options for this terrain reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	NodeList l;
	Element e;

	l = root.getElementsByTagName ("DRAWING_MODE");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    setDrawingMode (getDrawingModeByName (e.getFirstChild().getNodeValue()));
	}

	l = root.getElementsByTagName ("COLOURING_MODE");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    setColouringMode (getColouringModeByName (e.getFirstChild().getNodeValue()));
	}

	l = root.getElementsByTagName ("LIGHTING_MODE");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    setLightingMode (getLightingModeByName (e.getFirstChild().getNodeValue()));
	}





	l = root.getElementsByTagName ("SHOW_BLOCK_INDICES");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setShowBlockIndices ("Yes".equals ((String)n.getNodeValue()));
	    }
	}

	l = root.getElementsByTagName ("RENDER_BLOCK_ATTRIBUTES");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setRenderBlockAttributes ("Yes".equals ((String)n.getNodeValue()));
	    }
	}

	l = root.getElementsByTagName ("SHOW_BLOCK_BOUNDS");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setShowBlockBound ("Yes".equals ((String)n.getNodeValue()));
	    }
	}



	l = root.getElementsByTagName ("SHOW_CITY_BOUNDS");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setShowBound ("Yes".equals ((String)n.getNodeValue()));
	    }
	}





	l = root.getElementsByTagName ("RENDER_INST");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setShowINST ("Yes".equals ((String)n.getNodeValue()));
	    }
	}

	l = root.getElementsByTagName ("SHOW_PATHSET");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setShowPathset ("Yes".equals ((String)n.getNodeValue()));
	    }
	}

	l = root.getElementsByTagName ("SHOW_BAI");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    if (e.hasChildNodes())
	    {
		org.w3c.dom.Node n = e.getFirstChild();
		setShowBAI ("Yes".equals ((String)n.getNodeValue()));
	    }
	}

	l = root.getElementsByTagName ("FONT3D");
	if (l.getLength() != 0)
	{
	    e = (Element)l.item (0);
	    setFont (e.getAttribute ("FAMILY"), e.getAttribute ("SIZE"));
	}

    }




    /** @return a DOM element containing the options for this terrain view.
     *
     */
    public Element getOptions (Document doc)
    {
	Element root = doc.createElement ("TERRAIN");
	Element e;

	e = doc.createElement ("DRAWING_MODE");
	e.appendChild (doc.createTextNode (getDrawingName (drawingMode)));
	root.appendChild (e);

	e = doc.createElement ("COLOURING_MODE");
	e.appendChild (doc.createTextNode (getColouringName (colouringMode)));
	root.appendChild (e);

	e = doc.createElement ("LIGHTING_MODE");
	e.appendChild (doc.createTextNode (getLightingName (lightingMode)));
	root.appendChild (e);




	e = doc.createElement ("SHOW_BLOCK_INDICES");
	e.appendChild (doc.createTextNode (showBlockIndices ? "Yes" : "No"));
	root.appendChild (e);

	e = doc.createElement ("RENDER_BLOCK_ATTRIBUTES");
	e.appendChild (doc.createTextNode (renderBlockAttributes ? "Yes" : "No"));
	root.appendChild (e);

	e = doc.createElement ("SHOW_BLOCK_BOUNDS");
	e.appendChild (doc.createTextNode (showBlockBound ? "Yes" : "No"));
	root.appendChild (e);




	e = doc.createElement ("SHOW_CITY_BOUNDS");
	e.appendChild (doc.createTextNode (showBound ? "Yes" : "No"));
	root.appendChild (e);




	e = doc.createElement ("RENDER_INST");
	e.appendChild (doc.createTextNode (showINST ? "Yes" : "No"));
	root.appendChild (e);

	e = doc.createElement ("SHOW_PATHSET");
	e.appendChild (doc.createTextNode (showPathset ? "Yes" : "No"));
	root.appendChild (e);

	e = doc.createElement ("SHOW_BAI");
	e.appendChild (doc.createTextNode (showBAI ? "Yes" : "No"));
	root.appendChild (e);


	e = doc.createElement ("FONT3D");
	e.setAttribute ("FAMILY", font.getFont().getFamily());
	e.setAttribute ("SIZE",   "" + font.getFont().getSize2D());
	root.appendChild (e);


	return root;
    }









    // Constants:
    //
    //

    /** Drawing mode - draw each city block as triangles.
     *
     */
    public static final int DRAWING_LINE   = 0;

    /** Drawing mode - draw each city block as a filled polygon.
     *
     */
    public static final int DRAWING_FILL   = 1;




    /** Lighting mode - draw all city blocks without any lighting effects.
     *
     */
    public static final int LIGHTING_UNLIT   = 0;

    /** Lighting mode - draw all city blocks using a camera headlight.
     *
     *  NOT implemented!
     *
     */
    public static final int LIGHTING_LIT     = 1;




    /** Colour mode - selects colour by the city block index, the index modulo
     *                255 is used as the intensity in a grey scale.
     *
     */
    public static final int COLOURING_GREY          = 0;

    /** Colour mode - selects a random colour for each city block.
     *
     */
    public static final int COLOURING_RANDOM        = 1;

    /** Colour mode - selects the colour corresponding to the texture used in
     *                each city block. 
     *  NOT implemented!
     *
     */
    public static final int COLOURING_TEXTURE_INDEX = 2;

    /** Colour mode - uses the texture of the city block to render an
     *                in-game-like view.
     *
     *  NOT implemented!
     *
     */
    public static final int COLOURING_TEXTURE_MAP   = 3;





    /** Array of strings for each drawing mode.
     *
     */
    public static final String[] drawingNames =
    {
	"Lines",
	"Fill"
    };

    /** Array of strings for each lighting mode.
     *
     */
    public static final String[] lightingNames =
    {
	"Unlit",
	"Lit"
    };

    /** Array of strings for each colouring mode.
     *
     */
    public static final String[] colouringNames =
    {
	"Grey indexed",
	"Random",
	"Texture indexed",
	"Texture map"
    };






    // Static methods handling the constants and the names of the constants:
    //
    //


    /** @return the index of the given string in the given array of strings or
     *          -1 if the name does not appear in the array.
     *
     */
    protected static int getIndexByName (String name, String[] a)
    {
	int i = 0;
	while (i < a.length
	       && !a[i].equals (name))
	{
	    i++;
	}
	if (i < a.length)
	    return i;
	else
	    return -1;
    }



    /** @return the name of the given drawing mode.
     *
     */
    public static String getDrawingName (int mode)
    {
	return drawingNames[mode];
    }

    /** @return the drawing mode by the given name.
     *
     */
    public static int getDrawingModeByName (String name)
    {
	int i = getIndexByName (name, drawingNames);
	return (i == -1 ? DRAWING_FILL : i);
    }







    /** @return the name of the given lighting mode.
     *
     */
    public static String getLightingName (int mode)
    {
	return lightingNames[mode];
    }

    /** @return the lighting mode by the given name.
     *
     */
    public static int getLightingModeByName (String name)
    {
	int i = getIndexByName (name, lightingNames);
	return (i == -1 ? LIGHTING_UNLIT : i);
    }








    /** @return the name of the given colouring mode.
     *
     */
    public static String getColouringName (int mode)
    {
	return colouringNames[mode];
    }

    /** @return the colouring mode by the given name.
     *
     */
    public static int getColouringModeByName (String name)
    {
	int i = getIndexByName (name, colouringNames);
	return (i == -1 ? COLOURING_RANDOM : i);
    }




}
