package psdlviewer;
import psdlviewer.*;

import java.io.IOException;
import java.util.Vector;

public class TestWrite
{
    protected static String fileName = "../samples/Trig/city/trig.psdl";
    public static int nBlocksX = 45;
    public static int nBlocksZ = 45;

/*
    public static int xStep    = 30;   // Metres per block
    public static int zStep    = 30;   // Metres per block

    protected static float xStart = -(nBlocksX / 2) * xStep;
    protected static float zStart = (nBlocksZ / 2) * zStep;
*/
    protected static float xStart = -660.0f;
    protected static float zStart = 660.0f;

    public static float xStep    = -xStart / (nBlocksX / 2); // Metres per block
    public static float zStep    = zStart / (nBlocksZ / 2); // Metres per block


    protected static double radius = Math.sqrt (  xStart * xStart
						+ zStart * zStart);
    protected static double frequency = 12 * Math.PI;
    protected static double amplitude = 5;

    protected static double singularRadius = 7;

    protected static float yOffset = 0; //-75;

    public static void main (String[] argv)
    {
	VertexList  vertices = new VertexList();
	TextureList textures = new TextureList();
	BlockList   blocks   = new BlockList();

	try
	{
	    xStart += xStep / 2;
	    zStart -= zStep / 2;

	    int n;

//	    PSDLFile f1 = new PSDLFile ("../samples/city/city.psdl", ".");

	    System.out.println ("(" + xStart + ", " + zStart + ")");


	    // Create textures:
	    //
	    //
	    Vector texturePath = new Vector();
	    texturePath.add (new String ("H:\\Homes\\FredrikBergholtz\\Projects\\MidtownMadnessII\\mm2\\texture"));
	    textures.add (new TextureMap ("rinter_f",
					  texturePath));
	    textures.add (new TextureMap ("s_grass",
					  texturePath));
	    textures.add (new TextureMap ("r1_stone256_l",
					  texturePath));

	    System.out.println ("Added 3 textures.");


	    // Create vertices:
	    //
	    //
	    n = 0;
	    float z = zStart;
	    for (int j = 0; j <= nBlocksZ; j++, z -= zStep)
	    {
		float x = xStart;
		for (int i = 0; i <= nBlocksX; i++, x += xStep, n += 4)
		{
		    addVertex (x,                z,                vertices);
		    addVertex (x - (xStep / 2f), z,                vertices);
		    addVertex (x - (xStep / 2f), z + (zStep / 2f), vertices);
		    addVertex (x,                z + (zStep / 2f), vertices);
		}
	    }
	    System.out.println ("Added " + n + " vertices.");



	    // Create blocks:
	    //
	    //
	    n = 0;
	    int base = 0;
	    for (int j = 0; j < nBlocksZ; j++, base += 4)
	    {
		for (int i = 0; i < nBlocksX; i++, base += 4, n++)
		{
		    Block block = new Block();

		    // Set up perimeter:
		    //
		    //

		    int north = -1;
		    int south = -1;
		    int east  = -1;
		    int west  = -1;

		    if (j > 0)
		    {
			south = n - nBlocksX;
		    }
		    if (j < nBlocksZ - 1)
		    {
			north = n + nBlocksX;
		    }

		    if (i > 0)
		    {
			west = n - 1;
		    }
		    if (i < nBlocksX - 1)
		    {
			east = n + 1;
		    }


		    block.addVertex (new PerimeterVertex ((char) (0 + base), (char)  -1));
		    block.addVertex (new PerimeterVertex ((char) (1 + base), (char)  west));
		    block.addVertex (new PerimeterVertex ((char) (2 + base), (char)  west));
		    block.addVertex (new PerimeterVertex ((char) (2 + base), (char)  south));
		    block.addVertex (new PerimeterVertex ((char) (3 + base), (char)  south));
		    block.addVertex (new PerimeterVertex ((char) (6 + base), (char)  south));
		    block.addVertex (new PerimeterVertex ((char) (6 + base), (char)  east));
		    block.addVertex (new PerimeterVertex ((char) (5 + base), (char)  east));
		    block.addVertex (new PerimeterVertex ((char) (6 + base + 4 * (nBlocksX + 1)), (char)  east));
		    block.addVertex (new PerimeterVertex ((char) (6 + base + 4 * (nBlocksX + 1)), (char)  north));
		    block.addVertex (new PerimeterVertex ((char) (3 + base + 4 * (nBlocksX + 1)), (char)  north));
		    block.addVertex (new PerimeterVertex ((char) (2 + base + 4 * (nBlocksX + 1)), (char)  north));
		    block.addVertex (new PerimeterVertex ((char) (2 + base + 4 * (nBlocksX + 1)), (char)  west));
		    block.addVertex (new PerimeterVertex ((char) (1 + base), (char)  west));
		    block.addVertex (new PerimeterVertex ((char) (0 + base), (char)  -1));
		    
		    // Set up material:
		    //
		    //
		    TextureAttribute material = new TextureAttribute();
		    if (j % 4 == 0)
		    {
			material.ref = new TextureMapRef (2, textures);
		    }
		    else if (j % 2 == 0)
		    {
			material.ref = new TextureMapRef (((i % 2 == 0) ? 0 : 1), textures);
		    }
		    else
		    {
			material.ref = new TextureMapRef (((i % 2 == 0) ? 1 : 0), textures);
		    }
		    block.addAttribute (material);

		    // Set up geometry:
		    //
		    //
		    Attribute0030 bounds = new Attribute0030();
		    bounds.data.add (new VertexRef ((char) (0 + base)));
		    bounds.data.add (new VertexRef ((char) (1 + base)));
		    bounds.data.add (new VertexRef ((char) (2 + base)));
		    bounds.data.add (new VertexRef ((char) (3 + base)));
		    bounds.data.add (new VertexRef ((char) (6 + base)));
		    bounds.data.add (new VertexRef ((char) (5 + base)));
		    bounds.data.add (new VertexRef ((char) (6 + base + 4 * (nBlocksX + 1))));
		    bounds.data.add (new VertexRef ((char) (3 + base + 4 * (nBlocksX + 1))));
		    bounds.data.add (new VertexRef ((char) (2 + base + 4 * (nBlocksX + 1))));
		    bounds.data.add (new VertexRef ((char)(1 + base)));
		    bounds.data.add (new VertexRef ((char)(0 + base)));
		    block.addAttribute (bounds);

		    blocks.add (block);
		}
	    }
	    System.out.println ("Added " + n + " blocks.");




/*
	    // Create vertices:
	    //
	    //
	    vertices.add (new Vertex3D ( 200f, 10f,  200f)); // 0
	    vertices.add (new Vertex3D ( 200f, 10f, -100f)); // 1
	    vertices.add (new Vertex3D ( 200f, 10f, -200f)); // 2
	    vertices.add (new Vertex3D (-200f, 10f, -200f)); // 3
	    vertices.add (new Vertex3D (-200f, 10f, -100f)); // 4
	    vertices.add (new Vertex3D (-200f, 10f,  200f)); // 5

	    vertices.add (new Vertex3D (-100f, 10f, -100f)); // 6
	    vertices.add (new Vertex3D ( -50f, 10f,  -50f)); // 7
	    vertices.add (new Vertex3D (   0f, 10f,  -50f)); // 8
	    vertices.add (new Vertex3D (  50f, 10f,  -50f)); // 9
	    vertices.add (new Vertex3D ( 100f, 10f, -100f)); //10
	    vertices.add (new Vertex3D (  50f, 10f, -150f)); //11
	    vertices.add (new Vertex3D (   0f, 10f, -150f)); //12
	    vertices.add (new Vertex3D ( -50f, 10f, -150f)); //13

	    vertices.add (new Vertex3D (   0f, 15f, -100f)); //14


	    // Create blocks:
	    //
	    //

	    Block            block;
	    TextureAttribute material;
	    Attribute00b0    bounds;


	    // Block 0:
	    //
	    block = new Block();
	    block.addVertex (new PerimeterVertex ((char) 0,(char)  -1));
	    block.addVertex (new PerimeterVertex ((char) 1,(char)   1));
	    block.addVertex (new PerimeterVertex ((char)10,(char)   1));
	    block.addVertex (new PerimeterVertex ((char)10,(char)   2));
	    block.addVertex (new PerimeterVertex ((char) 9,(char)   2));
	    block.addVertex (new PerimeterVertex ((char) 8,(char)   2));
	    block.addVertex (new PerimeterVertex ((char) 7,(char)  -1));
	    block.addVertex (new PerimeterVertex ((char) 6,(char)   1));
	    block.addVertex (new PerimeterVertex ((char) 4,(char)   1));
	    block.addVertex (new PerimeterVertex ((char) 5,(char)  -1));

	    material = new TextureAttribute();
	    material.setIndex ((char)0);
	    block.addAttribute (material);

	    bounds = new Attribute00b0();
	    bounds.data.add (new VertexRef ((char) 0));
	    bounds.data.add (new VertexRef ((char) 1));
	    bounds.data.add (new VertexRef ((char)10));
	    bounds.data.add (new VertexRef ((char) 9));
	    bounds.data.add (new VertexRef ((char) 8));
	    bounds.data.add (new VertexRef ((char) 7));
	    bounds.data.add (new VertexRef ((char) 6));
	    bounds.data.add (new VertexRef ((char) 4));
	    bounds.data.add (new VertexRef ((char) 5));
	    bounds.data.add (new VertexRef ((char) 0));
	    bounds.data.add (new VertexRef ((char) 1));
	    block.addAttribute (bounds);

	    blocks.add (block);

	    // Block 1:
	    //
	    block = new Block();
	    block.addVertex (new PerimeterVertex ((char) 3,(char)  -1));
	    block.addVertex (new PerimeterVertex ((char) 4,(char)   0));
	    block.addVertex (new PerimeterVertex ((char) 6,(char)   0));
	    block.addVertex (new PerimeterVertex ((char)13,(char)  -1));
	    block.addVertex (new PerimeterVertex ((char)12,(char)   2));
	    block.addVertex (new PerimeterVertex ((char)11,(char)   2));
	    block.addVertex (new PerimeterVertex ((char)10,(char)   2));
	    block.addVertex (new PerimeterVertex ((char)10,(char)   0));
	    block.addVertex (new PerimeterVertex ((char) 1,(char)   0));
	    block.addVertex (new PerimeterVertex ((char) 2,(char)  -1));

	    material = new TextureAttribute();
	    material.setIndex ((char)0);
	    block.addAttribute (material);

	    bounds = new Attribute00b0();
	    bounds.data.add (new VertexRef ((char)13));
	    bounds.data.add (new VertexRef ((char)12));
	    bounds.data.add (new VertexRef ((char)11));
	    bounds.data.add (new VertexRef ((char)10));
	    bounds.data.add (new VertexRef ((char) 1));
	    bounds.data.add (new VertexRef ((char) 2));
	    bounds.data.add (new VertexRef ((char) 3));
	    bounds.data.add (new VertexRef ((char) 4));
	    bounds.data.add (new VertexRef ((char) 6));
/-*
	    bounds.data.add (new VertexRef ((char) 3));
	    bounds.data.add (new VertexRef ((char) 4));
	    bounds.data.add (new VertexRef ((char) 6));
	    bounds.data.add (new VertexRef ((char)13));
	    bounds.data.add (new VertexRef ((char)12));
	    bounds.data.add (new VertexRef ((char)11));
	    bounds.data.add (new VertexRef ((char)10));
	    bounds.data.add (new VertexRef ((char) 1));
	    bounds.data.add (new VertexRef ((char) 2));
*-/
	    bounds.data.add (new VertexRef ((char)2));
	    bounds.data.add (new VertexRef ((char)3));
	    block.addAttribute (bounds);

	    blocks.add (block);




	    // Block 2:
	    //
	    block = new Block();
	    block.addVertex (new PerimeterVertex ((char)14,(char)  -1));
	    block.addVertex (new PerimeterVertex ((char) 8,(char)   0));
	    block.addVertex (new PerimeterVertex ((char) 9,(char)   0));
	    block.addVertex (new PerimeterVertex ((char)10,(char)   0));
	    block.addVertex (new PerimeterVertex ((char)10,(char)   1));
	    block.addVertex (new PerimeterVertex ((char)11,(char)   1));
	    block.addVertex (new PerimeterVertex ((char)12,(char)   1));

	    material = new TextureAttribute();
	    material.setIndex ((char)1);
	    block.addAttribute (material);

	    bounds = new Attribute00b0();
	    bounds.data.add (new VertexRef ((char)14));
	    bounds.data.add (new VertexRef ((char) 8));
	    bounds.data.add (new VertexRef ((char) 9));
	    bounds.data.add (new VertexRef ((char)10));
	    bounds.data.add (new VertexRef ((char)11));
	    bounds.data.add (new VertexRef ((char)12));
	    bounds.data.add (new VertexRef ((char)14));
	    bounds.data.add (new VertexRef ((char)8));
	    block.addAttribute (bounds);

	    blocks.add (block);


*/


	    PSDLFile f2 = new PSDLFile();
	    f2.setTargetSize (2);
	    f2.setVertices   (vertices);
	    f2.setHeights    (new HeightList());
	    f2.setTextures   (textures);
	    f2.setUnknown3   (0L);
	    f2.setBlocks     (blocks);
//	    f2.setUnknown4   (new byte[0]);

	    f2.write (fileName, null);
	}
	catch (IOException e)
	{
	    System.err.println (e);
	    e.printStackTrace (System.err);
	}
	catch (Exception e)
	{
	    System.err.println (e);
	    e.printStackTrace (System.err);
	}
    }




    protected static void addVertex (float x,
				     float z,
				     VertexList v)
    {
	double r = Math.sqrt (x * x + z * z);
	double relR = r / radius;
	float y = (float)( 
			    Math.sin ((relR) * frequency)
			  * (1d / relR)
			  );

/*
	if (r == 0)
	{
	    r = singularRadius;
	}
	float y = (float)(1d 
			  * Math.cos ((r / radius) * frequency) * amplitude
			  + 1 / (r / radius)
			  );
*/
	v.add (new Vertex3D (x, y + yOffset, z));
    }
	    


}
