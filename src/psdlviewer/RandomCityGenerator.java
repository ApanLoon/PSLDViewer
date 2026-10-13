package psdlviewer;
import psdlviewer.*;

/** Generates a random city
 *
 */

import java.lang.Thread;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


public class RandomCityGenerator
extends Thread
{
    ProgressWindow monitor;
    MainWindow mainWindow;

    protected static float sideLength  = 500;
    protected static int   size        = 9; //129;
    protected static float smoothness  = 0.001f;
    protected static float heightScale = 10;

    protected Vertex3D[][] vertices;


    public RandomCityGenerator (MainWindow mw)
    {
	mainWindow = mw;


	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	JPanel panel = new JPanel();
	panel.setLayout (gbl);

	FloatField sl = new FloatField();
	IntField   si = new IntField();
	FloatField sm = new FloatField();
	FloatField hs = new FloatField();

	sl.setValue (sideLength);
	si.setValue (size);
	sm.setValue (smoothness);
	hs.setValue (heightScale);
	
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	panel.add (new JLabel ("Side length (m): "), c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	panel.add (sl, c);


	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	panel.add (new JLabel ("Size ((2^n)+1): "), c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	panel.add (si, c);


	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	panel.add (new JLabel ("Smoothness (0.0-1.0): "), c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	panel.add (sm, c);

	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	panel.add (new JLabel ("Height scale: "), c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	panel.add (hs, c);

	int state = JOptionPane.showConfirmDialog (mainWindow,
						   panel,
						   "Random city - Terrain parameters",
						   JOptionPane.OK_CANCEL_OPTION,
						   JOptionPane.PLAIN_MESSAGE);
	if (state == JOptionPane.OK_OPTION)
	{
	    sideLength  = sl.getValue();
	    size        = si.getValue();
	    smoothness  = sm.getValue();
	    heightScale = hs.getValue();
	    start();
	}

    }

    protected static final int INIT          = 0;
    protected static final int SEED          = 1;
    protected static final int DIAMONDSQUARE = 2;
    protected static final int EXIT          = 10000;

    public void run()
    {
	monitor = new ProgressWindow (mainWindow,
				      "Generating random city");
	monitor.open();
	int state = INIT;
	while (state != EXIT)
	{
	    switch (state)
	    {
	   
	    case INIT:
		// Allocate vertices:

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
			vertices[x][z] = new Vertex3D (xf, 0, zf);
			vl.add (vertices[x][z]);
			xf += inc;
			monitor.setProgress (z * size + x);
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



		state = SEED;
		break;
	    
	    case SEED:
		//TODO: Apply given Y to selected vertices
		//TODO: Render all vertices
		state = DIAMONDSQUARE; //IDLE;
		break;

	    case DIAMONDSQUARE:
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
		while (stride != 0)
		{
		    // Diamond steps:
		    int count = 0;
		    for (int z = stride; z < subSize; z += stride)
		    {
			for (int x = stride; x < subSize; x += stride)
			{
			    vertices[x][z].y =
				(float)(scale * (Math.random() - 0.5f))
				+ avgSquareVals (z, x, stride, size, vertices);
			    x += stride;
			    count++;
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

		state = EXIT; //IDLE;
		break;

	    }
	}



	monitor.close();
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
