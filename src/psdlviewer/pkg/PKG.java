package psdlviewer.pkg;

import psdlviewer.Config;
import psdlviewer.TextureStore;
import psdlviewer.ULong;

/** Representation of a PKG model.
 *
 */

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.lang.Exception;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Texture2D;

import javax.media.j3d.Shape3D;
import javax.media.j3d.IndexedGeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.IndexedTriangleStripArray;
import javax.media.j3d.IndexedQuadArray;

import javax.swing.JOptionPane;

public class PKG
{
    public static final int LOD_VL        = 0;
    public static final int LOD_L         = 1;
    public static final int LOD_M         = 2;
    public static final int LOD_H         = 3;
    public static final int LOD_NUMBER_OF = 4;

    public static final String lodNames[] = 
    {
	"VL",
	"L",
	"M",
	"H"
    };

    protected String fileName;

    protected boolean hasLength;

    protected Hashtable[]   model = {new Hashtable(),
				     new Hashtable(),
				     new Hashtable(),
				     new Hashtable()};

    protected ShaderSet shaderSet;
    protected Offset    offset;

    SharedGroup[][] rendered;


    // Bounding box:
    protected float minX = Float.MAX_VALUE;
    protected float maxX = Float.MIN_VALUE;
    protected float minY = Float.MAX_VALUE;
    protected float maxY = Float.MIN_VALUE;
    protected float minZ = Float.MAX_VALUE;
    protected float maxZ = Float.MIN_VALUE;


    /** Reads a PKG object from a given file.
     *
     */
    public PKG (String name,
		Vector gPath)
    throws Exception
    {
	try
	{
//	    Config.debug ("Reading PKG: " + name + "\n");

	    // Locate file:
	    //
	    File file = null;
	    boolean done = false;
	    int p = 0;
	    while (!done && p < gPath.size())
	    {
		String fileName = gPath.elementAt (p).toString() + '/'
		                  + name + ".pkg";
		File f = new File (fileName);
		if (f.exists())
		{
		    file   = f;
		    done   = true;
		}
		p++;
	    }
	    if (file == null)
	    {
		throw new FileNotFoundException (name + ".pkg");
	    }
	    FileInputStream in = new FileInputStream (file);

	    fileName = name;

//	    Config.debug ("Reading header...");
	    byte[] head = new byte[4];
	    in.read (head);

	    String s = new String (head);
	    if ("PKG2".equals (s))
	    {
		hasLength = false;
	    }
	    else if ("PKG3".equals (s))
	    {
		hasLength = true;
	    }
	    else
	    {
		throw new Exception ("Not a known PKG file.");
	    }
//	    Config.debug (s + "\n");

//	    Config.debug ("Reading \"FILE\"s\n");
	    done = false;
	    while (!done)
	    {
		done = !readPKGFile (in);
	    }

//	    Config.debug ("Bytes left in file: " + in.available() + "\n");


	    rendered = new SharedGroup[4][shaderSet.nPaintJobs()];
	}
	catch (Exception e)
	{
// TODO: Is it possible to alter the message of an exception?
//	    e.setMessage ("PKG: " + e.getMessage());
	    throw e;
	}
    }


    public String getFileName()
    {
	return fileName;
    }




    public float getMinX()
    {
	return minX;
    }
    public void setMinX (float v)
    {
	minX = v;
    }
    public float getMaxX()
    {
	return maxX;
    }
    public void setMaxX (float v)
    {
	maxX = v;
    }
    public float getMinY()
    {
	return minY;
    }
    public void setMinY (float v)
    {
	minY = v;
    }
    public float getMaxY()
    {
	return maxY;
    }
    public void setMaxY (float v)
    {
	maxY = v;
    }
    public float getMinZ()
    {
	return minZ;
    }
    public void setMinZ (float v)
    {
	minZ = v;
    }
    public float getMaxZ()
    {
	return maxZ;
    }
    public void setMaxZ (float v)
    {
	maxZ = v;
    }




    /** Returns a Java3D object representing this PKG.
     *
     */
    public SharedGroup render (int           lod,
			       int           paintJob,
			       TextureStore  tStore,
			       Vector        tPath)
    throws Exception
    {
	if (paintJob >= shaderSet.paintJobs)
	{
	    JOptionPane.showMessageDialog (null,
					   "Unavailable paint job on PKG requested for " + fileName + ". (" + paintJob + ")",
					   "PKG",
					   JOptionPane.INFORMATION_MESSAGE);

	    paintJob = 0;
//	    throw new Exception ("Unavailable paint job on PKG requested. (" + paintJob + ")");
	}
	SharedGroup root = rendered[lod][paintJob];
	if (root == null)
	{
	    root = new SharedGroup();
	    rendered[lod][paintJob] = root;


	    Model m = null;
	    while (model[lod].size() == 0)
	    {
		lod--;
	    }
	    if (lod < 0)
	    {
		return root;
	    }
	    Enumeration e = model[lod].elements();
	    while (e.hasMoreElements())
	    {
		m = (Model)e.nextElement();

	    for (int section = 0; section < m.sections.length; section++)
	    {
//		Config.debug ("section: " + section + "\n");
		Section s = m.sections[section];

		Shader shader = shaderSet.getShader (s.shaderOffset,
						     paintJob);


		for (int i = 0; i < s.strips.length; i++)
		{
		    Strip strip = s.strips[i];

		    IndexedGeometryArray array = null;
		    int count = strip.index.length;

		    int[] stripIndexCounts = {count};
		    int[] indexArray = new int[count];
		    int stripCount = 0; // Number of sub strips.
		    int indexCount = 0; // Number of indices in current strip
		    boolean reverse = false;
		    for (int ia = 0; ia < count; ia++)
		    {
			indexArray[ia] = strip.index[ia] & 0x3fff;
			indexCount++;

			if (reverse && (indexCount % 2) == 0)
			{
			    int tmp = indexArray[ia - 1];
			    indexArray[ia - 1] = indexArray[ia];
			    indexArray[ia] = tmp;
			}

			if ((strip.index[ia] & 0x8000) != 0)
			{
			    stripCount++;
			    indexCount = 0;
			    reverse = false;
			}

			if ((strip.index[ia] & 0x4000) != 0)
			{
			    reverse = true;
			}
		    }

		    if (strip.nVerticesPerFace == 3)
		    {
			if (shader.isTextured())
			{
			    int[] texCoordSetMap = {0};
			    array = new IndexedTriangleArray (
				       strip.coordinate.length,
				       IndexedTriangleArray.COORDINATES
				     | IndexedTriangleArray.NORMALS
				     | IndexedTriangleArray.TEXTURE_COORDINATE_2,
				       1,
				       texCoordSetMap,
				       indexArray.length);
			    array.setTextureCoordinates (0, 0, strip.texCoord);
			    array.setTextureCoordinateIndices (0, 0, indexArray);
			}
			else
			{
			    array = new IndexedTriangleArray (
					strip.coordinate.length,
					IndexedTriangleArray.COORDINATES
				      | IndexedTriangleArray.NORMALS,
					indexArray.length);
			}
		    }
		    else if (strip.nVerticesPerFace == 4)
		    {
			int[] stripCounts = new int[stripCount];
			int si = 0;
			for (int ia = 0; ia < count; ia++)
			{
			    stripCounts[si]++;
			    if ((strip.index[ia] & 0x8000) != 0)
			    {
				si++;
			    }
			}

			if (shader.isTextured())
			{
			    int[] texCoordSetMap = {0};
			    array = new IndexedTriangleStripArray (
				       strip.coordinate.length,
				       IndexedTriangleArray.COORDINATES
				     | IndexedTriangleArray.NORMALS
				     | IndexedTriangleArray.TEXTURE_COORDINATE_2,
				       1,
				       texCoordSetMap,
				       indexArray.length,
				       stripCounts);
			    array.setTextureCoordinates (0, 0, strip.texCoord);
			    array.setTextureCoordinateIndices (0, 0, indexArray);
			}
			else
			{
			    array = new IndexedTriangleStripArray (
					strip.coordinate.length,
					IndexedTriangleArray.COORDINATES
				      | IndexedTriangleArray.NORMALS,
					indexArray.length,
					stripCounts);
			}
		    }

		    if (array == null)
		    {
			Config.debug ("Ignored strip, unknown type.\n");
			continue;
		    }

		    array.setCoordinates (0, strip.coordinate);
		    array.setNormals (0, strip.normal);
		    array.setCoordinateIndices (0, indexArray);
		    array.setNormalIndices (0, indexArray);

		    Shape3D shape = new Shape3D (array);

		    shape.setAppearance (shaderSet.getAppearance (s.shaderOffset,
								  paintJob,
								  tStore,
								  tPath));
		    root.addChild (shape);
		}
	    }
	}
	}
	return root;

    }









    /** Reads the PKG file and buffers up all information.
     *
     */
    protected boolean readPKGFile (InputStream in)
    throws Exception
    {
	// Seek "FILE"
	byte[] head = new byte[4];
	if (in.read (head) != 4)
	{
	    return false;
	}
	if (!"FILE".equals (new String (head)))
	    throw new Exception ("Expected \"FILE\".");


	// Read name
	String name = readString (in);
	Config.debug ("Reading file \"" + name + "\"\n");

	// Read len
	long len = 0;
	if (hasLength)
	{
	    len = ULong.parseULong (in);
	}

	name = name.toUpperCase();

	if (name.endsWith ("VL"))
	{
	    model[LOD_VL].put (name, new Model (this, in));
	}
	else if (name.endsWith ("L"))
	{
	    model[LOD_L].put (name, new Model (this, in));
	}
	else if (name.endsWith ("M"))
	{
	    model[LOD_M].put (name, new Model (this, in));
	}
	else if (name.endsWith ("H"))
	{
	    model[LOD_H].put (name, new Model (this, in));
	}
	else if ("SHADERS".equals (name))
	{
	    shaderSet = new ShaderSet (in, len);
	}
	else if ("OFFSET".equals (name))
	{
	    offset = new Offset (in);
	}
	else
	{
	    if (hasLength)
	    {
		in.skip (len);
	    }
	}
	return true;
    }














    public static String readString (InputStream in)
    throws IOException
    {
	int len = in.read();
	if (len == 0)
	{
	    return ("");
	}
	byte[] buf = new byte[len - 1];
	int i = 0;
	int ch = in.read();
	while (i < len && ch != 0)
	{
	    buf[i++] = (byte)ch;
	    ch = in.read();
	}
	return new String (buf);
    }




    public int countPaintJobs()
    {
	return shaderSet.nPaintJobs();
    }



    public boolean hasLOD (int i)
    {
	return model[i].size() != 0;
    }







    public static int getLODByName (String s)
    {
	int i = 0;
	while (i < lodNames.length && !lodNames[i].equals (s))
	{
	    i++;
	}
	if (i < lodNames.length)
	    return i;
	else
	    return -1;
    }

    public static String getLODName (int i)
    {
	return lodNames[i];
    }

}
