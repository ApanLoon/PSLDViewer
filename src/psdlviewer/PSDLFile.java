package psdlviewer;
import psdlviewer.*;

/** A PSDL file is the file containing the terrain data.
 *
 */


import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import java.io.OutputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.lang.Exception;

import java.util.Vector;


public class PSDLFile
{
    protected static final byte[] PSD0 = {0x50, 0x53, 0x44, 0x30};
    protected static final byte[] PSD1 = {0x50, 0x53, 0x44, 0x31};

    protected boolean mc = false;

    protected long bytesRead;
    protected int  writeProgress;
    protected ProgressWindow monitor;

    /** Unknown value immediately after PSD0.
     *
     * "Target size"
     *
     */
    protected long targetSize = 2;


    /** Vector holding all vertices.
     *
     */
    protected VertexList vertices;

    /** Vector holding all heights.
     *
     */
    protected HeightList heights;

    /** Vector holding all textures.
     *
     */
    protected TextureList textures;


    /** Unknown after number of blocks.
     *
     */
    protected long unknown3;

    /** Vector holding all blocks.
     *
     */
    protected BlockList blocks;


    /** Dimensions object.
     *
     */
    protected Dimensions dimensions;


    /** Vector holding all road definitions.
     *
     */
    protected RoadList roads;


    /** MC data
     *
     */
    protected class MCDimension
    {
	public Vertex3D centre;
	public float u0;
	public float u1;
	public float u2;
	public float u3;
    };
    protected Vector mcDimensions;





    /** Creates an empty PSDLFile object.
     *
     */
    public PSDLFile()
    {
	vertices   = new VertexList();
	heights    = new HeightList();
	textures   = new TextureList();
	blocks     = new BlockList();
	mcDimensions = new Vector();
	dimensions = new Dimensions();
	roads      = new RoadList();
    }




    /** Opens and reads a named PSDL file.
     *
     */
    public PSDLFile (String         name,
		     Vector         texturePath,
		     ProgressWindow monitor_)
    throws Exception
    {
	monitor = monitor_;
	vertices   = new VertexList();
	heights    = new HeightList();
	textures   = new TextureList();
	blocks     = new BlockList();
	mcDimensions = new Vector();
	dimensions = new Dimensions();
	roads      = new RoadList();
	read (name, texturePath, false);
    }







    /** Appends a named PSDL file to this one.
     *
     *  Only appends vertices, heights, textures and block. Unknown data blocks
     *  are unchanged.
     *
     */
    public void append (String name,
			Vector texturePath)
    throws Exception
    {
	read (name, texturePath, true);
    }



    /** Reads a PSDL file from disk int this object.
     *
     *  TODO: Currently only the block references in block perimeters are
     *        modified on append.
     *
     *
     *  @param name path and name of file to read
     *  @param texturePath vector of paths to the textures to use
     *  @param append if true, the PSDL file will be added to the current
     *                object and references will be modified.
     *
     */
    protected void read (String name,
			 Vector texturePath,
			 boolean append)
    throws Exception
    {
	try
	{
	    int vertexOffset = 0;
	    int heightOffset = 0;
	    int textureOffset = 0;
	    int blockOffset = 0;

	    if (append)
	    {
		vertexOffset = vertices.size();
		heightOffset = heights.size();
		textureOffset = textures.size();
		blockOffset = blocks.size();		
	    }

	    File f = new File (name);

	    FileInputStream in = new FileInputStream (f);

	    bytesRead = 0;


	    if (monitor != null)
	    {
		monitor.setMinimum (0);
		monitor.setMaximum (in.available());
		monitor.setProgress (0);
		monitor.setNote ("Reading header");
	    }


	    // Read header:
	    //
	    //
	    debug ("Reading header...");
	    byte[] head = new byte[4];
	    in.read (head);
	    addBytesRead (4);
	    String headString = new String (head);
	    if ("PSD0".equals (headString))
	    {
		debug ("PSD0\n");
		mc = false;
	    }
	    else if ("PSD1".equals (headString))
	    {
		debug ("PSD1\n");
		mc = true;
	    }
	    else
	    {
		throw new Exception ("Not a PSDL file.");
	    }



	    // Read unknown ulong (version?):
	    //
	    //
	    targetSize = ULong.parseULong (in);
	    addBytesRead (4);


	    // Read vertices:
	    //
	    //

	    if (monitor != null)
	    {
		monitor.setNote ("Reading vertices");
	    }
	    debug ("Reading vertices:\n");
	    readVertices (in);


	    // Read height floats:
	    //
	    //
	    if (monitor != null)
	    {
		monitor.setNote ("Reading heights");
	    }
	    debug ("Reading heights:\n");
	    readHeights (in);

	    // Read textures:
	    //
	    //
	    debug ("Reading textures:\n");
	    if (monitor != null)
	    {
		monitor.setNote ("Reading textures");
	    }
	    readTextures (in, texturePath);

	    // Read blocks:
	    //
	    //
	    debug ("Reading blocks: (" + Long.toHexString (bytesRead) + ")\n");
	    if (monitor != null)
	    {
		monitor.setNote ("Reading blocks");
	    }
	    readBlocks (in,
			vertexOffset,
			heightOffset,
			textureOffset,
			blockOffset);

	    if (mc)
	    {
		debug ("Reading MC dimensions:\n");
		if (monitor != null)
		{
		    monitor.setNote ("Reading MC dimensions");
		}
		readMCDimensions (in);

		//TODO: Skips unknown data 0: (Two bytes per block)
		in.skip (blocks.size() * 2);
		addBytesRead (blocks.size() * 2);

		//TODO: Skips unknown data 1: (730 bytes)
//Manhattan:
/*
		in.skip (730);
		addBytesRead (730);
*/
//London:
		in.skip (702);
		addBytesRead (702);
	    }

	    // Read dimensions:
	    //
	    //
	    debug ("Reading dimensions:\n");
	    if (monitor != null)
	    {
		monitor.setNote ("Reading dimensions");
	    }
	    readDimensions (in);

	    if (mc)
	    {
		// TODO: Skips unknown data
		in.skip (4);
		addBytesRead (4);
	    }

	    // Read road definitions:
	    //
	    //
	    debug ("Reading roads:\n");
	    if (monitor != null)
	    {
		monitor.setNote ("Reading roads");
	    }
	    readRoads (in);

	    debug ("Bytes left in file: " + in.available() + "\n");
	    in.close();
	}
	catch (FileNotFoundException e)
	{
	    throw new Exception ("File not found. (" + name + ")");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    throw e;
	}
    }




    protected void readVertices (InputStream in)
    throws IOException
    {
	long n = ULong.parseULong (in);
	addBytesRead (4);

	debug (" " + n + " vertices: ");

	for (long i = 0; i < n; i++)
	{
	    float x = CFloat.parseCFloat (in);
	    float y = CFloat.parseCFloat (in);
	    float z = CFloat.parseCFloat (in);
	    vertices.add (new Vertex3D (x, y, z));
	    addBytesRead (4 * 3);
	    debug (".");
	}
	debug ("\n");
    }



    protected void readHeights (InputStream in)
    throws IOException
    {
	long n = ULong.parseULong (in);
	addBytesRead (4);

	debug (" " + n + " heights: ");

	for (int i = 0; i < n; i++)
	{
	    heights.add (new Height (CFloat.parseCFloat (in)));
	    addBytesRead (4);
	    debug (".");
	}
	debug ("\n");
    }


    protected void readTextures (InputStream in,
				 Vector      texturePath)
    throws IOException
    {
	long n = ULong.parseULong (in) - 1;
	addBytesRead (4);

	debug (" " + n + " textures: ");

	for (long i = 0; i < n; i++)
	{
	    int len = in.read();
	    addBytesRead (1);

	    if (len != 0)
	    {
		byte[] name = new byte[len - 1];
		in.read (name, 0, len - 1);
		int dummy = in.read(); // Skip terminating '\0'.

		TextureMap t = new TextureMap (new String (name),
					       texturePath);
		textures.add (t);
		addBytesRead (len);
	    }
	    else
	    {
		// Create a dummy texture to maintain the index validity:
		//
		textures.add (new TextureMap());
	    }
	    if (mc)
	    {
		// Skip extra dummy nul byte:
		in.skip (1);
		addBytesRead (1);
	    }

	    debug (".");
	}
	debug ("\n");
    }




    protected void readBlocks (InputStream in,
			       int vertexOffset,
			       int heightOffset,
			       int textureOffset,
			       int blockOffset)
    throws IOException
    {
	long n = ULong.parseULong (in) - 1;
	addBytesRead (4);

	unknown3 = ULong.parseULong (in); // ??
	addBytesRead (4);

	debug (" " + n + " blocks: ");

	// Read the blocks:
	//
	//
	for (long i = 0; i < n; i++)
	{
	    long nVertices = ULong.parseULong (in);
	    addBytesRead (4);

	    long nAttributes = ULong.parseULong (in);
	    addBytesRead (4);

	    Block p = new Block();

	    for (int j = 0; j < nVertices; j++)
	    {
		UShort index = UShort.parseUShort (in);
		addBytesRead (2);
		UShort connectingBlock = (UShort.parseUShort (in));
		connectingBlock.add (-1 + blockOffset);
		index.add (vertexOffset);
		addBytesRead (2);
		p.addVertex (new PerimeterVertex (index, connectingBlock));
	    }

	    int j = 0;
	    while (j < nAttributes)
	    {
		char id = UShort.parseUShort (in).getCharValue();
		j++;

		BlockAttribute a = BlockAttribute.getInstance (id,
							       vertices,
							       heights,
							       textures);
		j += a.read (in, nAttributes - j);
		a.addOffsets (vertexOffset,
			      heightOffset,
			      textureOffset,
			      blockOffset);
		p.addAttribute (a);
	    }
	    addBytesRead (j * 2);

	    blocks.add (p);
	    debug (".");
	}
	debug ("\n");



	// Read block types:
	//
	//
	debug ("Reading block types\n");
	int i = 0;
	int ch = in.read(); // Skip the first byte (0x00)
	ch = in.read();
	addBytesRead (2);
	while (ch != -1 && ch != 0xcd)
	{
	    if (i < blocks.size())
	    {
		blocks.blockAt (i).setType(ch);
	    }
	    ch = in.read();
	    addBytesRead (1);
	    i++;
	}

	// Read unknown block info:
	//
	//
	debug ("Reading proprule indices\n");
	ch = 0;
	i = 0;
	while (ch != -1 && i < blocks.size())
	{
	    ch = in.read();
	    addBytesRead (1);
	    if (ch != -1)
	    {
		blocks.blockAt (i).setUnknown (ch);
		i++;
	    }
	}
    }




    /** Reads the dimensions of the PSDL.
     *
     */
    protected void readDimensions (InputStream in)
    throws IOException
    {
	float x = CFloat.parseCFloat (in);
	addBytesRead (4);
	float y = CFloat.parseCFloat (in);
	addBytesRead (4);
	float z = CFloat.parseCFloat (in);
	addBytesRead (4);
	dimensions.setMin (new Vertex3D (x, y, z));

	x = CFloat.parseCFloat (in);
	addBytesRead (4);
	y = CFloat.parseCFloat (in);
	addBytesRead (4);
	z = CFloat.parseCFloat (in);
	addBytesRead (4);
	dimensions.setMax (new Vertex3D (x, y, z));

	x = CFloat.parseCFloat (in);
	addBytesRead (4);
	y = CFloat.parseCFloat (in);
	addBytesRead (4);
	z = CFloat.parseCFloat (in);
	addBytesRead (4);

	dimensions.setCenter (new Vertex3D (x, y, z));

	dimensions.setRadius (CFloat.parseCFloat (in));
	addBytesRead (4);
    }



    protected void readMCDimensions (InputStream in)
    throws IOException
    {
	for (int i = 0; i < blocks.size(); i++)
	{
	    MCDimension dim = new MCDimension();
	    float x = CFloat.parseCFloat (in);
	    addBytesRead (4);
	    float y = CFloat.parseCFloat (in);
	    addBytesRead (4);
	    float z = CFloat.parseCFloat (in);
	    addBytesRead (4);
	    dim.centre = new Vertex3D (x, y, z);
	    dim.u0 = CFloat.parseCFloat (in);
	    addBytesRead (4);
	    dim.u1 = CFloat.parseCFloat (in);
	    addBytesRead (4);
	    dim.u2 = CFloat.parseCFloat (in);
	    addBytesRead (4);
	    dim.u3 = CFloat.parseCFloat (in);
	    addBytesRead (4);

	    mcDimensions.add (dim);
	}
    }



    protected void readRoads (InputStream in)
    throws IOException
    {
	long nRoads = ULong.parseULong (in);
	addBytesRead (4);

	debug (" " + nRoads + " roads: ");

	for (int i = 0; i < nRoads; i++)
	{
	    Road road = new Road();

	    road.setU0 (UShort.parseUShort (in));
	    addBytesRead (2);

	    road.setU1 (UShort.parseUShort (in));
	    addBytesRead (2);

	    int nFLanes = in.read();
	    addBytesRead (1);
	    int nBLanes = in.read();
	    addBytesRead (1);

	    for (int j = 0; j < nFLanes; j++)
	    {
		road.addForwardLane (new Float (CFloat.parseCFloat (in)));
		addBytesRead (4);
	    }

	    for (int j = 0; j < nBLanes; j++)
	    {
		road.addBackwardLane (new Float (CFloat.parseCFloat (in)));
		addBytesRead (4);
	    }

	    road.setU2 (UShort.parseUShort (in));
	    addBytesRead (2);


	    for (int j = 0; j < 4; j++)
	    {
		road.addStartVertex (new VertexRef (UShort.parseUShort (in)));
		addBytesRead (2);
	    }

	    for (int j = 0; j < 4; j++)
	    {
		road.addEndVertex (new VertexRef (UShort.parseUShort (in)));
		addBytesRead (2);
	    }


	    int n = in.read();
	    addBytesRead (1);

	    for (int j = 0; j < n; j++)
	    {
		char ch = UShort.parseUShort (in).getCharValue();
		road.addBlockRef (new BlockRef ((char)(ch - 1)));
		addBytesRead (2);
	    }

	    roads.add (road);
	    debug (".");
	}
	debug ("\n");


//	debug ("" + roads.toString());
    }






    public long getTargetSize()
    {
	return targetSize;
    }

    public VertexList getVertices()
    {
	return vertices;
    }

    public HeightList getHeights()
    {
	return heights;
    }

    public TextureList getTextures()
    {
	return textures;
    }

    public long getUnknown3()
    {
	return unknown3;
    }

    public BlockList getBlocks()
    {
	return blocks;
    }

    public Dimensions getDimensions()
    {
	return dimensions;
    }

    public RoadList getRoads()
    {
	return roads;
    }






    public void setTargetSize (long v)
    {
	targetSize = v;
    }

    public void setVertices (VertexList v)
    {
	vertices = v;
    }

    public void setHeights (HeightList v)
    {
	heights = v;
    }

    public void setTextures (TextureList v)
    {
	textures = v;
    }

    public void setUnknown3 (long v)
    {
	unknown3 = v;
    }

    public void setBlocks (BlockList v)
    {
	blocks = v;
    }

    public void setDimensions (Dimensions d)
    {
	dimensions = d;
    }

    public void setRoads (RoadList r)
    {
	roads = r;
    }





    public void write (String fileName,
		       ProgressWindow monitor)
    throws IOException, PSDLWriteException
    {
	write (new File (fileName), monitor);
    }

    public void write (File f,
		       ProgressWindow monitor)
    throws IOException, PSDLWriteException
    {
	write (new FileOutputStream (f), monitor);
    }

    public void write (OutputStream out,
		       ProgressWindow monitor_)
    throws IOException, PSDLWriteException
    {
	monitor = monitor_;


	if (monitor != null)
	{
	    monitor.setMinimum (0);
	    int i = vertices.size()
		+ heights.size()
		+ textures.size()
		+ blocks.size()
		+ roads.size();
	    monitor.setMaximum (i);
	    monitor.setProgress (0);
	    monitor.setNote ("Writing header");
	}

	// Write header:
	//
	//
	out.write (PSD0);

	// Write TargetSize:
	//
	//
	ULong.write (targetSize, out);

	if (monitor != null)
	{
	    monitor.setNote ("Writing vertices");
	}
	writeVertices (out);

	if (monitor != null)
	{
	    monitor.setNote ("Writing heights");
	}
	writeHeights (out);

	if (monitor != null)
	{
	    monitor.setNote ("Writing textures");
	}
	writeTextures (out);

	if (monitor != null)
	{
	    monitor.setNote ("Writing blocks");
	}
	writeBlocks (out);

	if (monitor != null)
	{
	    monitor.setNote ("Writing dimensions");
	}
	writeDimensions (out);

	if (monitor != null)
	{
	    monitor.setNote ("Writing roads");
	}
	writeRoads (out);

	out.close();
    }










    protected void writeVertices (OutputStream out)
    throws IOException
    {

	ULong.write (vertices.size(), out);

	for (int i = 0; i < vertices.size(); i++)
	{
	    Vertex3D v = vertices.vertexAt (i);
	    CFloat.write (v.x, out);
	    CFloat.write (v.y, out);
	    CFloat.write (v.z, out);
	    addWriteProgress();
	}

    }

    protected void writeHeights (OutputStream out)
    throws IOException
    {
	if (heights != null)
	{
	    ULong.write (heights.size(), out);

	    for (int i = 0; i < heights.size(); i++)
	    {
		CFloat.write (heights.heightAt (i).h, out);
		addWriteProgress();
	    }
	}
	else
	{
	    ULong.write (0, out);
	}
    }


    protected void writeTextures (OutputStream out)
    throws IOException
    {
	ULong.write (textures.size() + 1, out);
	
	for (int i = 0; i < textures.size(); i++)
	{
	    TextureMap t = textures.textureAt (i);
	    String s = t.getShortName();
	    if (s == null)
	    {
		out.write (0x00);
	    }
	    else
	    {
		byte[] buf = s.getBytes();
		out.write (buf.length + 1);
		out.write (buf);
		out.write (0x00);
	    }
	    addWriteProgress();
	}
    }




    protected void writeBlocks (OutputStream out)
    throws IOException, PSDLWriteException
    {
	ULong.write (blocks.size() + 1, out);
	ULong.write (unknown3, out);

	int bIndex = -1;
	for (bIndex = 0; bIndex < blocks.size(); bIndex++)
	{
	    Block p = blocks.blockAt (bIndex);

	    PerimeterList      perimeter  = p.getPerimeter();
	    BlockAttributeList attributes = p.getAttributes();

	    ULong.write (perimeter.size(), out);


	    long attribLength = 0;
	    for (int j = 0; j < attributes.size(); j++)
	    {
		BlockAttribute a = attributes.attributeAt (j);
		attribLength += a.getLength();
	    }
	    ULong.write (attribLength, out);

	    for (int j = 0; j < perimeter.size(); j++)
	    {
		PerimeterVertex v = perimeter.perimeterAt (j);
		UShort.write ((char)v.getIndex(), out);
		UShort.write ((char)(v.getConnectingBlock() + 1), out);
	    }

	    int aIndex = -1;
	    try
	    {
		for (aIndex = 0; aIndex < attributes.size(); aIndex++)
		{
		    BlockAttribute a = attributes.attributeAt (aIndex);
		    a.write (out);
		}
	    }
	    catch (PSDLWriteException e)
	    {
		e.setMessage (   "Block " + Integer.toHexString (bIndex)
			      + ", attribute " + Integer.toHexString (aIndex)
			      + ": " + e.getMessage());
		throw e;
	    }
	    catch (Exception e)
	    {
		PSDLWriteException ex = new PSDLWriteException (
				"Block " + Integer.toHexString (bIndex)
			      + ", attribute " + Integer.toHexString (aIndex)
			      + ": " + e);
		throw ex;
	    }
	    addWriteProgress();
	}



	// Write block types:
	//
	//
	out.write (0x00);
	for (bIndex = 0; bIndex < blocks.size(); bIndex++)
	{
	    out.write (blocks.blockAt(bIndex).getType());
	}
	out.write (0xcd);

	// Write unknown block info:
	//
	//
	for (bIndex = 0; bIndex < blocks.size(); bIndex++)
	{
	    out.write (blocks.blockAt (bIndex).getUnknown());
	}

    }





    protected void writeDimensions (OutputStream out)
    throws IOException, PSDLWriteException
    {
	Vertex3D v;

	v = dimensions.getMin();
	CFloat.write (v.x, out);
	CFloat.write (v.y, out);
	CFloat.write (v.z, out);

	v = dimensions.getMax();
	CFloat.write (v.x, out);
	CFloat.write (v.y, out);
	CFloat.write (v.z, out);

	v = dimensions.getCenter();
	CFloat.write (v.x, out);
	CFloat.write (v.y, out);
	CFloat.write (v.z, out);

	CFloat.write (dimensions.getRadius(), out);
    }


    protected void writeRoads (OutputStream out)
    throws IOException, PSDLWriteException
    {
	ULong.write (roads.size(), out);


	for (int i = 0; i < roads.size(); i++)
	{
	    Road road = roads.roadAt (i);

	    road.getU0().write (out);
	    road.getU1().write (out);

	    out.write ((byte)road.getNFLanes());
	    out.write ((byte)road.getNBLanes());

	    for (int j = 0; j < road.getNFLanes(); j++)
	    {
		CFloat.write (road.getForwardLane (j), out);
	    }

	    for (int j = 0; j < road.getNBLanes(); j++)
	    {
		CFloat.write (road.getBackwardLane (j), out);
	    }

	    road.getU2().write (out);


	    for (int j = 0; j < 4; j++)
	    {
		road.getStartVertex (j).write (out);
	    }

	    for (int j = 0; j < 4; j++)
	    {
		road.getEndVertex (j).write (out);
	    }

	    out.write ((byte)road.getNBlocks());
	    for (int j = 0; j < road.getNBlocks(); j++)
	    {
		UShort.write ((char)(road.getBlockRef (j).getIndex() + 1), out);
	    }
	    addWriteProgress();
	}

    }




    protected void addBytesRead (long l)
    {
	bytesRead += l;

	if (monitor != null)
	{
	    monitor.setProgress ((int)bytesRead);
	}
    }


    protected void addWriteProgress()
    {
	writeProgress++;
	if (monitor != null)
	{
	    monitor.setProgress (writeProgress);
	}
    }


    protected void debug (String s)
    {
	System.out.print (s);
    }





}



