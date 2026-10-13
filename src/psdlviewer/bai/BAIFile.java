package psdlviewer.bai;
import psdlviewer.*;

/** A BAI file is the file containing the routes and intersections for
 *  ambients.
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


public class BAIFile
{
    protected static final byte[] id = {0x43, 0x41, 0x49, 0x31}; // CAI1

    protected long           bytesRead;
    protected int            writeProgress;
    protected ProgressWindow monitor;

    /** List of routes.
     *
     */
    protected RouteList routes;

    /** List of intersections.
     *
     */
    protected IntersectionList intersections;

    /** Start road end:
     *
     */
    RoadEnd start;

    /** End road end:
     *
     */
    RoadEnd end;




    /** Creates an empty BAIFile object.
     *
     */
    public BAIFile()
    {
	routes        = new RouteList();
	intersections = new IntersectionList();
    }




    /** Opens and reads a named BAI file.
     *
     */
    public BAIFile (String name,
		    ProgressWindow monitor_)
    throws Exception
    {
	monitor = monitor_;
	routes        = new RouteList();
	intersections = new IntersectionList();
	read (name);
    }





    /** Reads a BAI file from disk into this object.
     *
     *  @param name path and name of file to read
     *
     */
    protected void read (String name)
    throws Exception
    {
	try
	{
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
	    if (!"CAI1".equals (new String (head)))
		throw new Exception ("Not a BAI file.");
	    addBytesRead (4);
	    debug ("CAI1\n");

	    int nIntersections = (int)UShort.parseUShort (in).getCharValue();
	    debug (" " + nIntersections + " intersections.\n");
	    addBytesRead (2);
	    int nRoutes = (int)UShort.parseUShort (in).getCharValue();
	    debug (" " + nRoutes + " routes.\n");
	    addBytesRead (2);

	    // Read routes:
	    //
	    //
	    if (monitor != null)
	    {
		monitor.setNote ("Reading routes");
	    }
	    debug ("Reading routes:\n");
	    readRoutes (nRoutes, in);

	    // Read intersections:
	    //
	    //
	    if (monitor != null)
	    {
		monitor.setNote ("Reading intersections");
	    }
	    debug ("Reading intersections:\n");
	    readIntersections (nIntersections, in);


	    // Read culling:
	    //
	    //
	    if (monitor != null)
	    {
		monitor.setNote ("Reading culling");
	    }
	    debug ("Reading culling:\n");
	    readCulling (in);

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




    protected void readRoutes (int         n,
			       InputStream in)
    throws Exception
    {
	for (int r = 0; r < n; r++)
	{
	    Route route = new Route();
	    route.setID ((int)UShort.parseUShort (in).getCharValue());
	    addBytesRead (2);

	    int nSections =(int)UShort.parseUShort (in).getCharValue();
	    addBytesRead (2);

	    route.setUnknown0 ((int)UShort.parseUShort (in).getCharValue());
	    addBytesRead (2);

	    int nBlocks =(int)UShort.parseUShort (in).getCharValue();
	    addBytesRead (2);

	    for (int b = 0; b < nBlocks; b++)
	    {
		char ch = UShort.parseUShort (in).getCharValue();
		addBytesRead (2);
		route.addBlockRef (new BlockRef ((char)(ch - 1)));
	    }

	    route.setUnknown1 (CFloat.parseCFloat (in));
	    addBytesRead (4);

	    route.setUnknown2 (CFloat.parseCFloat (in));
	    addBytesRead (4);

	    // Right:
	    route.setRight (readRoadData (nSections, in));

	    // Left:
	    route.setLeft (readRoadData (nSections, in));

	    int s;
	    for (s = 0; s < nSections; s++)
	    {
		route.addCentralDistance (CFloat.parseCFloat (in));
		addBytesRead (4);
	    }

	    for (s = 0; s < nSections; s++)
	    {
		route.addCentralVertex (readVertex (in));
	    }
	    for (s = 0; s < nSections; s++)
	    {
		route.addXOrientationVertex (readVertex (in));
	    }
	    for (s = 0; s < nSections; s++)
	    {
		route.addYOrientationVertex (readVertex (in));
	    }
	    for (s = 0; s < nSections; s++)
	    {
		route.addZOrientationVertex (readVertex (in));
	    }
	    for (s = 0; s < nSections; s++)
	    {
		route.addWOrientationVertex (readVertex (in));
	    }

	    // End:
	    route.setRoadEnd (readRoadEnd (in));

	    // Start:
	    route.setRoadStart (readRoadEnd (in));


	    routes.add (route);
	    debug (".");
	}
	debug ("\n");
    }



    protected RoadData readRoadData (int         nSections,
				     InputStream in)
    throws Exception
    {
	int i;
	RoadData roadData = new RoadData();

	int nLanes = (int)UShort.parseUShort (in).getCharValue();
	addBytesRead (2);
	roadData.setNLanes (nLanes);

	int nTrams = (int)UShort.parseUShort (in).getCharValue();
	addBytesRead (2);
	roadData.setNTrams (nTrams);

	int nTrains = (int)UShort.parseUShort (in).getCharValue();
	addBytesRead (2);
	roadData.setNTrains (nTrains);

	roadData.setUnknown3 ((int)UShort.parseUShort (in).getCharValue());
	addBytesRead (2);

	roadData.setUnknown4 ((int)UShort.parseUShort (in).getCharValue());
	addBytesRead (2);

	for (i = 0; i < nLanes * nSections; i++)
	{
	    roadData.addLaneDistance (CFloat.parseCFloat (in));
	    addBytesRead (4);
	}

	for (i = 0; i < nSections; i++)
	{
	    roadData.addDistance (CFloat.parseCFloat (in));
	    addBytesRead (4);
	}

	for (i = 0; i < 11 + nLanes; i++)
	{
	    roadData.addUnknown0 (CFloat.parseCFloat (in));
	    addBytesRead (4);
	}

	for (i = 0; i < nLanes * nSections; i++)
	{
	    roadData.addLaneVertex (readVertex (in));
	}

	for (i = 0; i < nSections; i++)
	{
	    roadData.addSidewalkCenterVertex (readVertex (in));
	}

	for (i = 0; i < nTrams * nSections; i++)
	{
	    roadData.addTramVertex (readVertex (in));
	}

	for (i = 0; i < nTrains * nSections; i++)
	{
	    roadData.addTrainVertex (readVertex (in));
	}

	for (i = 0; i < nSections; i++)
	{
	    roadData.addSidewalkInnerVertex (readVertex (in));
	}

	for (i = 0; i < nSections; i++)
	{
	    roadData.addSidewalkOuterVertex (readVertex (in));
	}
	return roadData;
    }


    protected RoadEnd readRoadEnd (InputStream in)
    throws Exception
    {
	RoadEnd re = new RoadEnd();
	re.setIntersection (ULong.parseULong (in));
	addBytesRead (4);

	re.setUnknown0 ((int)UShort.parseUShort (in).getCharValue());
	addBytesRead (2);

	re.setUnknown1 (ULong.parseULong (in));
	addBytesRead (4);
	
	re.setUnknown2 (ULong.parseULong (in));
	addBytesRead (4);

	re.setOrigin (readVertex (in));
	re.setAxis   (readVertex (in));

	return re;
    }


    protected void readIntersections (int         n,
				      InputStream in)
    throws Exception
    {
	for (int i = 0; i < n; i++)
	{
	    Intersection intersection = new Intersection();
	    intersection.setID (UShort.parseUShort (in).getCharValue());
	    addBytesRead (2);

	    char ch = UShort.parseUShort (in).getCharValue();
	    addBytesRead (2);
	    intersection.setBlock (new BlockRef ((char)(ch - 1)));

	    intersection.setCenter (readVertex (in));

	    int nRoutes = (int)UShort.parseUShort (in).getCharValue();
	    for (int r = 0; r < nRoutes; r++)
	    {
		intersection.addRoute (ULong.parseULong (in));
		addBytesRead (4);
	    }

	    intersections.add (intersection);
	    debug (".");
	}
	debug ("\n");
    }






    protected void readCulling (InputStream in)
    throws Exception
    {
	long nBlocks = ULong.parseULong (in);
	addBytesRead (4);

	for (int b = 0; b < nBlocks * 2; b++)
	{
	    int nRoutes = (int)UShort.parseUShort (in).getCharValue();
	    addBytesRead (2);
	    for (int r = 0; r < nRoutes; r++)
	    {
		char route = UShort.parseUShort (in).getCharValue();
		addBytesRead (2);
	    }
	    debug (".");
	}
	debug ("\n");
    }





    protected Vertex3D readVertex (InputStream in)
    throws Exception
    {
	float x = CFloat.parseCFloat (in);
	float y = CFloat.parseCFloat (in);
	float z = CFloat.parseCFloat (in);
	addBytesRead (4 * 3);
	return new Vertex3D (x, y, z);
    }



    public RouteList getRoutes()
    {
	return routes;
    }
    public void setRoutes (RouteList v)
    {
	routes = v;
    }

    public IntersectionList getIntersections()
    {
	return intersections;
    }
    public void setIntersections (IntersectionList v)
    {
	intersections = v;
    }





/*
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
*/



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



