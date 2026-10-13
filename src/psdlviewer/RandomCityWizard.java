package psdlviewer;
import psdlviewer.*;

import javax.vecmath.Point3f;
import javax.vecmath.Color3f;
import javax.media.j3d.LineArray;
import java.awt.Color;

import java.lang.Thread;
import java.lang.InterruptedException;

import java.util.Vector;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.lang.ClassLoader;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RandomCityWizard
extends Wizard
{
    protected int   size          = 9; //129;
    protected float sideLength    = 500;
    protected float initialHeight = 0;
    protected float smoothness    = 0.001f;
    protected float heightScale   = 10;
    protected float waterHeight   = 0;


    protected Worker worker;
    protected Frame mainWindow;

    protected Vertex3D[][] vertices;

    
    protected static final int IDLE          = 0;
    protected static final int INIT          = 1;
    protected static final int SEED          = 2;
    protected static final int SMOOTH        = 3;
    protected static final int WATER_PREVIEW = 4;
    protected static final int WATER         = 5;
    protected static final int FLATTEN       = 6;
    protected static final int ROAD          = 7;
    protected static final int BUILDING      = 8;
    protected static final int EXIT   = 10000;


    public RandomCityWizard (Frame parent)
    {
	super (parent, "Random City");

	mainWindow = parent;
	worker = new Worker();

	addStep (new InitStep());
	addStep (new SeedStep());
	addStep (new SmoothStep());
	addStep (new WaterStep());
	addStep (new FlattenStep());
	addStep (new RoadStep());
	addStep (new BuildingStep());

	addNavigation();
	setSize();

	setCurrentStep (0);
    }


    protected void finnish()
    {
	System.err.println ("Finnish");
    }

    protected void close()
    {
	worker.setTask (EXIT);
    }














    class InitStep
    extends WizardStep
    {
	protected IntField   sizeField = new IntField();
	protected FloatField sideLengthField = new FloatField();
	protected FloatField initialHeightField = new FloatField();

	public InitStep()
	{
	    super ("Init", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Init.png")));



	    GridBagLayout gbl = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
	    content.setLayout (gbl);

	    sizeField.setValue (size);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = GridBagConstraints.RELATIVE;
	    c.gridheight = 1;
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    content.add (new JLabel ("Size ((2^n)+1): "), c);

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 0.0;
	    content.add (sizeField, c);


	    sideLengthField.setValue (sideLength);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = GridBagConstraints.RELATIVE;
	    c.gridheight = 1;
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    content.add (new JLabel ("Side length (m): "), c);

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 0.0;
	    content.add (sideLengthField, c);



	    initialHeightField.setValue (initialHeight);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = GridBagConstraints.RELATIVE;
	    c.gridheight = 1;
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    content.add (new JLabel ("Initial height (m): "), c);

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 0.0;
	    content.add (initialHeightField, c);
	}

	// WizardStep methods:
	//

	public void next()
	{
	    /* TODO: Nice feature:
	    if (parameters changed)
	    {
		if (already done)
		{
		    show warning;
		}
		if (!canceled)
		{
		    set state of worker task to work;
		    return next step;
		}
		else
		{
		    set state of worker task to idle;
		    return current step;
		}
	    }
	    */

	    sideLength    = sideLengthField.getValue();
	    size          = sizeField.getValue();
	    initialHeight = initialHeightField.getValue();

	    worker.setTask (INIT);
	}

	public void prev()
	{
	    System.err.println ("Init Prev");
	}	
    }

    class SeedStep
    extends WizardStep
    {
	public SeedStep()
	{
	    super ("Seed", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Seed.png")));
	}

	// WizardStep methods:
	//

	public void next()
	{
	    worker.setTask (SEED);
	}

	public void prev()
	{
	    System.err.println ("Seed Prev");
	}	
    }


    class SmoothStep
    extends WizardStep
    {
	protected FloatField smoothnessField = new FloatField();
	protected FloatField heightScaleField = new FloatField();

	public SmoothStep()
	{
	    super ("Smooth", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Smooth.png")));


	    GridBagLayout gbl = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
	    content.setLayout (gbl);

	    smoothnessField.setValue (smoothness);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = GridBagConstraints.RELATIVE;
	    c.gridheight = 1;
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    content.add (new JLabel ("Smoothness (0.0-1.0): "), c);

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 0.0;
	    content.add (smoothnessField, c);

	    heightScaleField.setValue (heightScale);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = GridBagConstraints.RELATIVE;
	    c.gridheight = 1;
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    content.add (new JLabel ("Height scale: "), c);

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 0.0;
	    content.add (heightScaleField, c);

	}

	// WizardStep methods:
	//

	public void next()
	{
	    smoothness  = smoothnessField.getValue();
	    heightScale = heightScaleField.getValue();

	    worker.setTask (SMOOTH);
	}

	public void prev()
	{
	    System.err.println ("Smooth Prev");
	}
    }


    class WaterStep
    extends WizardStep
    implements ActionListener
    {
	protected FloatField waterHeightField = new FloatField();

	public WaterStep()
	{
	    super ("Water", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Water.png")));


	    GridBagLayout gbl = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
	    content.setLayout (gbl);

	    waterHeightField.setValue (waterHeight);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = GridBagConstraints.RELATIVE;
	    c.gridheight = 1;
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    content.add (new JLabel ("Water height (m): "), c);

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 0.0;
	    content.add (waterHeightField, c);

	    JButton button = new JButton ("Preview");
	    button.setActionCommand ("preview");
	    button.addActionListener (this);
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = 1;
	    c.weightx = 1.0;
	    c.weighty = 1.0;
	    content.add (button, c);

	}



	// ActionListener methods:
	//
	//
	public void actionPerformed (ActionEvent e)
	{
	    waterHeight = waterHeightField.getValue();

	    String cmd = e.getActionCommand();
	    if ("preview".equals (cmd))
	    {
		worker.setTask (WATER_PREVIEW);
	    }
	}




	// WizardStep methods:
	//

	public void next()
	{
	    worker.setTask (WATER);
	}

	public void prev()
	{
	    System.err.println ("Water Prev");
	}	
    }

    class FlattenStep
    extends WizardStep
    {
	public FlattenStep()
	{
	    super ("Flatten", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Flatten.png")));
	}

	// WizardStep methods:
	//

	public void next()
	{
	    System.err.println ("Flatten Next");
	}

	public void prev()
	{
	    System.err.println ("Flatten Prev");
	}	
    }

    class RoadStep
    extends WizardStep
    {
	public RoadStep()
	{
	    super ("Road", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Roads.png")));
	}

	// WizardStep methods:
	//

	public void next()
	{
	    System.err.println ("Road Next");
	}

	public void prev()
	{
	    System.err.println ("Road Prev");
	}	
    }

    class BuildingStep
    extends WizardStep
    {
	public BuildingStep()
	{
	    super ("Building", null);

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    setIcon (new ImageIcon (cl.getResource ("icons/RandomCity_Buildings.png")));
	}

	// WizardStep methods:
	//

	public void next()
	{
	    System.err.println ("Building Next");
	}

	public void prev()
	{
	    System.err.println ("Building Prev");
	}	
    }



















    /** Worker thread that does the job.
     *
     */
    class Worker
    extends Thread
    {
	protected int state = IDLE;
	protected ProgressWindow monitor;

	public Worker()
	{
	    monitor = new ProgressWindow (mainWindow,
					  "Generating random city");
	    start();
	}


	public void setTask (int i)
	{
	    state = i;
	    interrupt();
	}


	public void run()
	{
	    while (state != EXIT)
	    {
		switch (state)
		{
		case INIT:
		    init();
		    state = IDLE;
		    break;

		case SEED:
		    state = IDLE;
		    break;

		case SMOOTH:
		    smooth();
		    state = IDLE;
		    break;

		case WATER_PREVIEW:
		    waterPreview();
		    state = IDLE;
		    break;

		case WATER:
		    water();
		    state = IDLE;
		    break;

		case FLATTEN:
		    state = IDLE;
		    break;

		case ROAD:
		    state = IDLE;
		    break;

		case BUILDING:
		    state = IDLE;
		    break;

		case IDLE:
		    try
		    {
			sleep (Long.MAX_VALUE);
		    }
		    catch (InterruptedException e)
		    {
		    }
		    break;
		}
	    }
	    monitor.close();
	}







	/** Allocates vertices, textures and generates blocks.
	 *
	 */
	protected void init()
	{
	    monitor.setNote ("Initializing surface");
	    monitor.setProgress (0);
	    monitor.setMinimum (0);
	    monitor.setMaximum (1);
	    monitor.open();

	    // Allocate vertices:
	    //
	    vertices = new Vertex3D[size][size];
		
	    float start = -(sideLength / 2);
	    float inc   = sideLength / size;

	    VertexList vl = new VertexList();
	    float zf = start;
	    for (int z = 0; z < size; z++)
	    {
		float xf = start;
		for (int x = 0; x < size; x++)
		{
		    vertices[x][z] = new Vertex3D (xf, initialHeight, zf);
		    vl.add (vertices[x][z]);
		    xf += inc;
		    //		monitor.setProgress (z * size + x);
		}
		zf += inc;
	    }
	    Config.setVertexList (vl);
	

	    // Build texture list:
	    //
	    TextureList tl = new TextureList();
	    tl.add (new TextureMap ("s_grass", Config.texturePath));
	    tl.add (new TextureMap ("s_grass2mud", Config.texturePath));
	    tl.add (new TextureMap ("s_rock_concrt", Config.texturePath));
	    Config.setTextureList (tl);

	    // Build blocks:
	    //
	    BlockList bl = new BlockList();
	    int i = 0;
	    for (int z = 1; z < size - 1; z += 2)
	    {
		for (int x = 1; x < size - 1; x += 2)
		{
		    Block b = new Block();
		    addPerimeter (x, z, size, b, i);
		    addGround (x, z, size, b, tl);
		    bl.add (b);
		    i++;
		}
	    }
	    Config.setBlockList (bl);

	    monitor.setProgress (0);

	    Terrain t = Config.getTerrain();
	    t.renderPSDL (Config.getVertexList(),
			  Config.getHeightList(),
			  Config.getTextureList(),
			  Config.getBlockList(),
			  monitor);
	    monitor.close();
	}






	/** Runs the diamond-square algorithm to smooth out the terrain.
	 *
	 */
	protected void smooth()
	{
	    // Calculate vertices:
	    //
		
	    /* Set up our roughness constants.
	       Random numbers are always generated in the range 0.0 to 1.0.
	       'scale' is multiplied by the randum number.
	       'ratio' is multiplied by 'scale' after each iteration
	       to effectively reduce the randum number range.
	    */
	    float ratio = (float)Math.pow (2, -smoothness);
	    float scale = heightScale * ratio;
		
	    int subSize = size - 1;
	    
	    int stride = subSize / 2;

	    monitor.setNote ("Generating surface");
	    monitor.setProgress (0);
	    monitor.setMinimum (0);
	    monitor.setMaximum ((int)Math.log (stride));
	    monitor.open();
	    int count = 0;

	    while (stride != 0)
	    {
		monitor.setProgress (count++);

		// Diamond steps:
		for (int z = stride; z < subSize; z += stride)
		{
		    for (int x = stride; x < subSize; x += stride)
		    {
			vertices[x][z].y =
			    (float)(scale * (Math.random() - 0.5f))
			    + avgSquareVals (z, x, stride, size, vertices);
			x += stride;
		    }
		    z += stride;
		}
		    

		// Square steps:
		boolean oddline = false;
		for (int z = 0; z < subSize; z += stride)
		{
		    oddline = !oddline;
		    for (int x = 0; x < subSize; x += stride)
		    {
			if ((oddline) && x == 0)
			    x += stride;

			/* z and x are setup. Call avgDiamondVals with the
			   current position. It will return the average of the
			   surrounding diamond data points. */
			vertices[x][z].y =
			    (float)(scale * (Math.random() - 0.5f))
			    + avgDiamondVals (z, x, stride, size, subSize, vertices);
			    
			/* To wrap edges seamlessly, copy edge values around
			   to other side of array */
			if (z == 0)
			{
			    vertices[x][subSize].y = vertices[x][z].y;
			}
			if (x == 0)
			{
			    vertices[subSize][z].y = vertices[x][z].y;
			}

			x += stride;
		    }
		}

		/* reduce random number range. */
		scale *= ratio;
		stride >>= 1;
	    }

	    Terrain t = Config.getTerrain();
	    t.renderPSDL (Config.getVertexList(),
			  Config.getHeightList(),
			  Config.getTextureList(),
			  Config.getBlockList(),
			  monitor);
	    monitor.close();

	}




	protected void waterPreview()
	{
	    Terrain t = Config.getTerrain();
	    t.renderLevel (Config.getVertexList(),
			   waterHeight);
	}




	class Edge
	{
	    public Vertex3D a;
	    public Vertex3D b;
	    public Edge (Vertex3D a_, Vertex3D b_)
	    {
		a = a_;
		b = b_;
	    }
	    public String toString()
	    {
		return "(" + a + ")-(" + b + ")";
	    }
	}

	class EdgeList
	extends Vector
	{
/*
	    public void add (Edge e)
	    {
		if (   round (e.a.x, 100) == round (e.b.x, 100)
		    && round (e.a.y, 100) == round (e.b.y, 100)
		    && round (e.a.z, 100) == round (e.b.z, 100))
		{
		    System.err.println ("Null edge: " + e);
		}
		else
		{
		    super.add (e);
		}
	    }
*/

	    public Edge edgeAt (int i)
	    {
		return (Edge)elementAt (i);
	    }

	    public Edge removeEdge (int i)
	    {
		return (Edge)remove (i);
	    }

	}


	protected EdgeList edges = new EdgeList();
	protected Vector polygons = new Vector();



	protected void water()
	{
	    edges.clear();
	    BlockList blocks = Config.getBlockList();
	    VertexList vertexList = Config.getVertexList();
	    for (int b = 0; b < blocks.size(); b++)
	    {
		Block block = blocks.blockAt (b);
		PerimeterList perimeter = block.getPerimeter();
		Vertex3D pivot = vertexList.vertexAt (perimeter.perimeterAt (0).getIndex());
		Vertex3D p1;
		Vertex3D p2 = vertexList.vertexAt (perimeter.perimeterAt (1).getIndex());
		int p = 2;
		while (p < perimeter.size())
		{
		    p1 = p2;
		    while (   p1.x == p2.x
			   && p1.y == p2.y
			   && p1.z == p2.z)
		    {
			p2 = vertexList.vertexAt (perimeter.perimeterAt (p++).getIndex());
		    }
		    intersectFace (pivot, p1, p2, waterHeight);
		}
	    }


	    // Check the edges of the world:
	    //
	    Vertex3D a_south = null;
	    Vertex3D a_east = null;
	    Vertex3D a_north = null;
	    Vertex3D a_west = null;

	    Vertex3D p0;
	    Vertex3D p1;
	    for (int i = 0; i < size - 1; i++)
	    {

		a_south = water_checkEdgeSegment (i,
						  vertices[i][size - 1],
						  vertices[i + 1][size - 1],
						  a_south);

		a_east = water_checkEdgeSegment (i,
						 vertices[size - 1][size - 1 - i],
						 vertices[size - 1][size - 1 - i - 1],
						 a_east);

		a_north = water_checkEdgeSegment (i,
						  vertices[size - 1 - i][0],
						  vertices[size - 1 - i - 1][0],
						  a_north);

		a_west = water_checkEdgeSegment (i,
						 vertices[0][i],
						 vertices[0][i + 1],
						 a_west);



	    }

	    int nEdges = edges.size();

	    water_groupEdges();


	    // TODO: Temporary render:

/* Single edge array:
	    LineArray la = new LineArray (edges.size() * 2, LineArray.COORDINATES | LineArray.COLOR_3);

	    for (int i = 0; i < edges.size(); i++)
	    {
		Edge e = (Edge)edges.elementAt (i);
		la.setCoordinate (i * 2,     new Point3f (e.a.x, e.a.y, e.a.z));
		la.setColor (i * 2, new Color3f (1.0f, 0.0f, 0.0f));
		la.setCoordinate (i * 2 + 1, new Point3f (e.b.x, e.b.y, e.b.z));	
		la.setColor (i * 2 + 1, new Color3f (1.0f, 1.0f, 1.0f));
	    }
	    Terrain t = Config.getTerrain();
	    t.renderEdges (la);
*/

/* set of polygons:
 */
	    LineArray la = new LineArray (nEdges * 2, LineArray.COORDINATES | LineArray.COLOR_3);

	    int index = 0;
	    for (int p = 0; p < polygons.size(); p++)
	    {
		EdgeList el = (EdgeList)polygons.elementAt (p);
		System.err.println ("Polygon " + p + ", " + el.size() + " edges");
		float factor = 2f / (3f * el.size()); 
		for (int e = 0; e < el.size(); e++)
		{
		    Edge edge = el.edgeAt (e);
		    la.setCoordinate (index, new Point3f (edge.a.x,
							  edge.a.y,
							  edge.a.z));
		    Color c = Color.getHSBColor ((float)p / polygons.size(), 1, (e * factor + 1f / 3f));
		    la.setColor (index, new Color3f (c));
		    index++;

		    la.setCoordinate (index, new Point3f (edge.b.x,
							      edge.b.y,
							      edge.b.z));	
		    c = Color.getHSBColor ((float)p / polygons.size(), 1, ((e + 1) * factor + 1f / 3f));
		    la.setColor (index, new Color3f (c));
		    index++;
		}
	    }
	    Terrain t = Config.getTerrain();
	    t.renderEdges (la);


	}


	protected Vertex3D water_checkEdgeSegment (int i,
						   Vertex3D p0,
						   Vertex3D p1,
						   Vertex3D a)
	{
	    if (   i == 0
		&& p0.y < waterHeight)
	    {
		a = p0;
	    }
	    if (   p0.y > waterHeight
		&& p1.y < waterHeight)
	    {
		a = intersectLine (p0, p1, waterHeight);
	    }
	    else if (   p0.y < waterHeight
		     && p1.y > waterHeight
		     && a != null)
	    {
		edges.add (new Edge (a, intersectLine (p0,
						       p1,
						       waterHeight)));
		a = null;
	    }
	    if (   i == size - 2
		&& p1.y < waterHeight
		&& a != null)
	    {
		edges.add (new Edge (a, p1));
		a = null;
	    }
	    return a;
	}



	protected void water_groupEdges()
	{
	    polygons.clear();
	    // Order edges into edge lists
	    //

	    while (edges.size() != 0)
	    {
		Edge c = edges.removeEdge (0);
		System.err.println ("Edge: " + c);
		EdgeList l = new EdgeList();
		l.add (c);
		polygons.add (l);

		boolean match = true;
		while (match)
		{
		    match = false;
		    for (int i = 0; i < edges.size(); i++)
		    {
			Edge x = edges.edgeAt (i);
			System.err.println (" " + i + " " + x);

			if (   round (x.a.x, 100) == round (c.b.x, 100)
			    && round (x.a.y, 100) == round (c.b.y, 100)
			    && round (x.a.z, 100) == round (c.b.z, 100))

/*
			if (   x.a.x == c.b.x
			    && x.a.y == c.b.y
			    && x.a.z == c.b.z)
*/
			{
			    edges.removeEdge (i);
			    l.add (x);
			    c = x;
			    match = true;
			    break;
			}
		    }
		}
	    }

	    System.err.println ("nPolygons: " + polygons.size());

	    /*
	    // Merge lists:
	    //
	    For all lists, X;
	    {
		e0 = X.edgeAt (0);
		p0 = e0.start;
		For all lists except X, Y;
		{
		    For all edges in Y, e1;
		    {
			p1 = e1.start;
			For all edges in all lists except Y and X, e2;
			{
			    if (p0, p1) intersects e2;
			    {
				Move to next Y;
			    }
			}
			if no intersections;
			{
			    Add list Y to list X;
			    Move to next Y;
			}
		    }
		}
	    }
	    */
	}


	protected float round (float f, int precision)
	{
	    return (float)((int)(f * precision + 0.5f) / precision);
	}

	protected void intersectFace (Vertex3D a, Vertex3D b, Vertex3D c, float h)
	{
	    Vertex3D p0 = intersectLine (a, b, h);
	    Vertex3D p1 = intersectLine (b, c, h);
	    Vertex3D p2 = intersectLine (c, a, h);

	    if (p0 == null && p1 != null && p2 != null)
	    {
		if (c.y < h)
		{
		    edges.add (new Edge (p1, p2));
		}
		else
		{
		    edges.add (new Edge (p2, p1));
		}
	    }
	    else if (p0 != null && p1 == null && p2 != null)
	    {
		if (a.y < h)
		{
		    edges.add (new Edge (p2, p0));
		}
		else
		{
		    edges.add (new Edge (p0, p2));
		}
	    }
	    else if (p0 != null && p1 != null && p2 == null)
	    {
		if (b.y < h)
		{
		    edges.add (new Edge (p0, p1));
		}
		else
		{
		    edges.add (new Edge (p1, p0));
		}
	    }
	}
	
	protected Vertex3D intersectLine (Vertex3D a, Vertex3D b, float h)
	{
	    float t = (h - a.y) / (b.y - a.y);
	    if (t > 0 && t < 1)
	    {
		float x = a.x + t * (b.x - a.x);
		float z = a.z + t * (b.z - a.z);
		return new Vertex3D (x, h, z);
	    }
	    else
	    {
		return null;
	    }
	}






	/** avgDiamondVals - Given the i,j location as the center of a diamond,
	 * average the data values at the four corners of the diamond and
	 * return it. "Stride" represents the distance from the diamond center
	 * to a diamond corner.
	 *
	 */
	protected float avgDiamondVals (int i,
					int j,
					int stride,
					int size,
					int subSize,
					Vertex3D[][] vertices)
	{
	    /* In this diagram, our input stride is 1, the i,j location is
	       indicated by "X", and the four value we want to average are
	       "*"s:
	       .   *   .
	   
	       *   X   *
	   
	       .   *   .
	    */
	
	    /* In order to support tiled surfaces which meet seamless at the
	       edges (that is, they "wrap"), We need to be careful how we
	       calculate averages when the i,j diamond center lies on an edge
	       of the array. The first four 'if' clauses handle these
	       cases. The final 'else' clause handles the general case (in
	       which i,j is not on an edge).
	    */
	    if (i == 0)
		return ((float) (  vertices[j - stride][i].y
				   + vertices[j + stride][i].y
				   + vertices[j][subSize - stride].y
				   + vertices[j][i + stride].y)
			* 0.25f);
	    else if (i == size-1)
		return ((float) (  vertices[j - stride][i].y
				   + vertices[j + stride][i].y
				   + vertices[j][i - stride].y
				   + vertices[j][0 + stride].y)
			* 0.25f);
	    else if (j == 0)
		return ((float) (  vertices[j][i - stride].y
				   + vertices[j][i + stride].y
				   + vertices[j + stride][i].y
				   + vertices[subSize - stride][i].y)
			* 0.25f);
	    else if (j == size - 1)
		return ((float) (  vertices[j][i - stride].y
				   + vertices[j][i + stride].y
				   + vertices[j - stride][i].y
				   + vertices[0 + stride][i].y)
			* 0.25f);
	    else
		return ((float) (  vertices[j][i - stride].y
				   + vertices[j][i + stride].y
				   + vertices[j - stride][i].y
				   + vertices[j + stride][i].y)
			* 0.25f);
	}




	/** avgSquareVals - Given the z,x location as the center of a square,
	 * average the data values at the four corners of the square and return
	 * it. "Stride" represents half the length of one side of the square.
	 *
	 */
	protected float avgSquareVals (int z,
				       int x,
				       int stride,
				       int size,
				       Vertex3D[][] vertices)
	{
	    /* In this diagram, our input stride is 1, the z,x location is
	       indicated by "*", and the four value we want to average are
	       "X"s:
	       X   .   X
	   
	       .   *   .
	   
	       X   .   X
	    */
	    return ((float)(  vertices[x - stride][z - stride].y
			      + vertices[x + stride][z - stride].y
			      + vertices[x - stride][z + stride].y
			      + vertices[x + stride][z + stride].y)
		    * 0.25f);
	}




	protected void addPerimeter (int x,
				     int z,
				     int size,
				     Block b,
				     int index)
	{
	    char noBlock    = (char)-1;
	    char eastBlock  = (x == size - 2) ? (char)-1 : (char)(index + 1);
	    char westBlock  = (x == 1) ? (char)-1 : (char)(index - 1);
	    char southBlock = (z == size - 2) ? (char)-1 : (char)(index + size / 2);
	    char northBlock = (z == 1) ? (char)-1 : (char)(index - size / 2);

	    char southEastBlock = (   z == size - 2
				      || x == size - 2)
		? (char)-1
		: (char)(index + size / 2 + 1);

	    char southWestBlock = (   z == size - 2
				      || x == 1)
		? (char)-1
		: (char)(index + size / 2 - 1);

	    char northEastBlock = (   z == 1
				      || x == size - 2)
		? (char)-1
		: (char)(index - size / 2 + 1);

	    char northWestBlock = (   z == 1
				      || x == 1)
		? (char)-1
		: (char)(index - size / 2 - 1);

	    // Center:
	    b.addVertex (new PerimeterVertex (coord2offset (x,     z,     size), noBlock));

	    // South west corner:
	    b.addVertex (new PerimeterVertex (coord2offset (x - 1, z + 1, size), westBlock));

	    // West:
	    b.addVertex (new PerimeterVertex (coord2offset (x - 1, z,     size), westBlock));

	    // North west corner:
	    if (westBlock != (char)-1 || (westBlock == (char)-1 && northBlock == (char)-1 && northWestBlock == (char)-1))
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x - 1, z - 1, size), westBlock));
	    }

	    if (northWestBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x - 1, z - 1, size), northWestBlock));
	    }

	    if (northBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x - 1, z - 1, size), northBlock));
	    }


	    // North:
	    b.addVertex (new PerimeterVertex (coord2offset (x,     z - 1, size), northBlock));


	    // North east corner:
	    if (northBlock != (char)-1 || (eastBlock == (char)-1 && northBlock == (char)-1 && northEastBlock == (char)-1))
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x + 1, z - 1, size), northBlock));
	    }
	    if (northEastBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x + 1, z - 1, size), northEastBlock));
	    }
	    if (eastBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x + 1, z - 1, size), eastBlock));
	    }

	    // East:
	    b.addVertex (new PerimeterVertex (coord2offset (x + 1, z,     size), eastBlock));


	    // South east corner:
	    if (eastBlock != (char)-1 || (eastBlock == (char)-1 && southBlock == (char)-1 && southEastBlock == (char)-1))
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x + 1, z + 1, size), eastBlock));
	    }
	    if (southEastBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x + 1, z + 1, size), southEastBlock));
	    }
	    if (southBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x + 1, z + 1, size), southBlock));
	    }

	    // South:
	    b.addVertex (new PerimeterVertex (coord2offset (x,     z + 1, size), southBlock));

	    // South west corner:
	    if (southBlock != (char)-1 || (southBlock == (char)-1 && southWestBlock == (char)-1))
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x - 1, z + 1, size), southBlock));
	    }
	    if (southWestBlock != (char)-1)
	    {
		b.addVertex (new PerimeterVertex (coord2offset (x - 1, z + 1, size), southWestBlock));
	    }

	    // Center:
	    b.addVertex (new PerimeterVertex (coord2offset (x,     z,     size), noBlock));
	}


	protected void addGround (int x,
				  int z,
				  int size,
				  Block b,
				  TextureList tl)
	{
	    TextureAttribute ta = new TextureAttribute ();
	    ta.ref = new TextureMapRef ((char)(Math.random() * 2 + 0.5), tl);
	    b.addAttribute (ta);

	    Attribute0030 ga = new Attribute0030();
	    VertexRefList l = ga.getVertexRefs();
	    l.add (new VertexRef (coord2offset (x,     z,     size)));
	    l.add (new VertexRef (coord2offset (x - 1, z + 1, size)));
	    l.add (new VertexRef (coord2offset (x,     z + 1, size)));
	    l.add (new VertexRef (coord2offset (x + 1, z + 1, size)));
	    l.add (new VertexRef (coord2offset (x + 1, z,     size)));
	    l.add (new VertexRef (coord2offset (x + 1, z - 1, size)));
	    l.add (new VertexRef (coord2offset (x,     z - 1, size)));
	    l.add (new VertexRef (coord2offset (x - 1, z - 1, size)));
	    l.add (new VertexRef (coord2offset (x - 1, z,     size)));
	    l.add (new VertexRef (coord2offset (x - 1, z + 1, size)));
	    l.add (new VertexRef (coord2offset (x,     z,     size)));
	    b.addAttribute (ga);
	}




	protected char coord2offset (int x, int z, int size)
	{
	    return (char)(z * size + x);
	}

    }








}
