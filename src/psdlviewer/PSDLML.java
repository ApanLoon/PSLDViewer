package psdlviewer;
import psdlviewer.*;

/* XML representation of a PSDL file
 *
 *
 */

import java.util.Vector;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;

import java.io.IOException;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.swing.JOptionPane;




public class PSDLML
{
    protected PSDLFile psdl;
    protected INSTFile inst;


    /** Welding range of vertices. */
    protected float weldRange = 0.001f;

    protected Vector texturePath;

    protected ProgressWindow monitor;


    protected boolean weldTextures = true; // TODO: Should be a parameter.





    /** Opens and reads a named PSDLML file.
     *
     */
    public PSDLML (String         name,
		   Vector         texturePath_,
		   float          weldRange_,
		   ProgressWindow monitor_)
    throws Exception
    {
	initialize (name, texturePath_, weldRange_, monitor_);
    }

    /** Opens and reads a named PSDLML file.
     *
     */
    public PSDLML (String name,
		   Vector texturePath_,
		   ProgressWindow monitor_)
    throws Exception
    {
	initialize (name, texturePath_, weldRange, monitor_);
    }




    protected void initialize (String name,
			       Vector texturePath_,
			       float weldRange_,
			       ProgressWindow monitor_)
    throws Exception
    {
	texturePath = texturePath_;
	weldRange = weldRange_;
	monitor = monitor_;

	psdl = new PSDLFile();
	inst = new INSTFile();

	read (name, texturePath);
    }





    public PSDLFile getPSDLFile()
    {
	return psdl;
    }

    public INSTFile getINSTFile()
    {
	return inst;
    }




    /** Reads a PSDLML file from disk int this object.
     *
     *
     *
     *  @param name path and name of file to read
     *  @param texturePath vector of paths to the textures to use
     *
     */
    protected void read (String name,
			 Vector texturePath_)
    throws Exception
    {
	texturePath = texturePath_;
	try
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder        builder = factory.newDocumentBuilder();

	    File f = new File (name);
	    Document doc = builder.parse (f);

	    Element root = doc.getDocumentElement();
	    if (!"psdl-ml".equals (root.getNodeName()))
	    {
		throw new Exception ("Not a psdl-ml file.");
	    }


	    String s;
	    Node     n0;
	    NodeList l0;
	    Element  e0;

	    l0 = root.getChildNodes();

	    if (monitor != null)
	    {
		monitor.setMinimum (0);
		monitor.setMaximum (l0.getLength());
		monitor.setProgress (0);
		monitor.setNote ("Reading PSDL-ML elements");
	    }

	    for (int i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (n0.getNodeType() == Node.ELEMENT_NODE)
		{
		    e0 = (Element)n0;
		    if ("target_size".equals (e0.getNodeName()))
		    {
			psdl.setTargetSize (Long.parseLong (
					      e0.getAttribute ("target_size")));
		    }
		    else if ("vertex".equals (e0.getNodeName()))
		    {
			addVertex (e0, false);
		    }
		    else if ("height".equals (e0.getNodeName()))
		    {
			addHeight (e0, false);
		    }
		    else if ("texture".equals (e0.getNodeName()))
		    {
			addTexture (e0);
		    }
		    else if ("block".equals (e0.getNodeName()))
		    {
			addBlock (e0);
		    }
		}
		monitor.setProgress (i);
	    }
	}
	catch (ParserConfigurationException pce)
	{
	    String s =   "* Parser Configuration error\n"
		       + "  " + pce.getMessage();
	    JOptionPane.showMessageDialog (Config.mainWindow, s);
	    System.out.println (s);
	}
	catch (SAXException e)
	{
	    JOptionPane.showMessageDialog (Config.mainWindow, e);
	    System.out.println("  " + e);
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (Config.mainWindow, e);
	    System.out.println("  " + e);
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (Config.mainWindow, e);
	    System.out.println("  " + e);
	    e.printStackTrace (System.out);
	}
    }





    protected int addVertex (Element e, boolean weld)
    {
	int index = -1;
	Vertex3D v = null;
	VertexList vl = psdl.getVertices();

	String s = e.getAttribute ("index");
	if (s.length() != 0)
	{
	    index = Integer.parseInt (s);
	}
	else
	{
	    float x = Float.parseFloat (e.getAttribute ("x"));
	    float y = Float.parseFloat (e.getAttribute ("y"));
	    float z = Float.parseFloat (e.getAttribute ("z"));
	    v = new Vertex3D (x, y, z);
	}

	if (weld)
	{
	    float d = Float.MAX_VALUE;
	    int i = 0;
	    while (d > weldRange && i < vl.size())
	    {
		Vertex3D v1 = vl.vertexAt (i);
		d = (float)Math.sqrt (  (v.x - v1.x) * (v.x - v1.x)
				      + (v.y - v1.y) * (v.y - v1.y)
				      + (v.z - v1.z) * (v.z - v1.z));
		i++;
	    }
	    if (d <= weldRange)
	    {
		index = i - 1;
	    }
	}


	if (index == -1)
	{
	    index = vl.size();
	    vl.add (v);
	}
	return index;
    }





    protected int addHeight (Element e, boolean weld)
    {
	int index = -1;
	Height h = null;
	HeightList hl = psdl.getHeights();

	String s = e.getAttribute ("index");
	if (s.length() != 0)
	{
	    index = Integer.parseInt (s);
	}
	else
	{
	    float v = Float.parseFloat (e.getAttribute ("value"));
	    h = new Height (v);
	}

	if (weld)
	{
	    float d = Float.MAX_VALUE;
	    int i = 0;
	    while (d > weldRange && i < hl.size())
	    {
		Height h1 = hl.heightAt (i);
		d = h1.getHeight() - h.getHeight();
		i++;
	    }
	    if (d <= weldRange)
	    {
		index = i - 1;
	    }
	}


	if (index == -1)
	{
	    index = hl.size();
	    hl.add (h);
	}
	return index;
    }


    protected int addHeight (float f, boolean weld)
    {
	int index = -1;
	Height h = null;
	HeightList hl = psdl.getHeights();

	h = new Height (f);
	if (weld)
	{
	    float d = Float.MAX_VALUE;
	    int i = 0;
	    while (d > weldRange && i < hl.size())
	    {
		Height h1 = hl.heightAt (i);
		d = Math.abs (h1.getHeight() - h.getHeight());
		i++;
	    }
	    if (d <= weldRange)
	    {
		index = i - 1;
	    }
	}


	if (index == -1)
	{
	    index = hl.size();
	    hl.add (h);
	}
	return index;
    }






    // Can't be welded. The welding must take place in the texture
    // attribute where a sequence of textures can be established.
    protected int addTexture (Element e)
    {
	int index = -1;
	TextureMap t = null;
	TextureList tl = psdl.getTextures();

	String s = e.getAttribute ("index");
	if (s.length() != 0)
	{
	    index = Integer.parseInt (s);
	}
	else
	{
	    s = e.getAttribute ("name");
	    t = new TextureMap (s, texturePath);
	}

	if (index == -1)
	{
	    index = tl.size();
	    tl.add (t);
	}
	return index;
    }








    protected void addBlock (Element root)
    {
	String s;
	Block b = new Block();
	BlockList bl = psdl.getBlocks();

	int blockIndex = bl.size();

	Node n0;
	Node n1;
	NodeList l0;
	NodeList l1;
	Element e0;
	Element e1;
	int j;

	l0 = root.getChildNodes();
	for (int i = 0; i < l0.getLength(); i++)
	{
	    n0 = l0.item (i);
	    if (n0.getNodeType() == Node.ELEMENT_NODE)
	    {
		e0 = (Element)n0;
		if ("perimeter".equals (e0.getNodeName()))
		{
		    l1 = e0.getChildNodes();
		    e1 = null;
		    for (j = 0; j < l1.getLength(); j++)
		    {
			n1 = l1.item (j);
			if (   n1.getNodeType() == Node.ELEMENT_NODE
			    && "vertex".equals (n1.getNodeName()))
			{
			    e1 = (Element)n1;
			}
		    }

		    int neighbour = -1;
		    s = e0.getAttribute ("block");
		    if (s.length() != 0)
		    {
			neighbour = Integer.parseInt (s);
		    }

		    int vertex = addVertex (e1, true);
		    b.addVertex (new PerimeterVertex ((char)vertex,
						      (char)neighbour));
		}
		else if ("attributes".equals (e0.getNodeName()))
		{
		    l1 = e0.getChildNodes();
		    for (j = 0; j < l1.getLength(); j++)
		    {
			n1 = l1.item (j);
			if (n1.getNodeType() == Node.ELEMENT_NODE)
			{
			    e1 = (Element)n1;
			    addAttribute (e1, b);
			}
		    }
		}
		else if ("inst".equals (e0.getNodeName()))
		{
		    l1 = e0.getChildNodes();
		    for (j = 0; j < l1.getLength(); j++)
		    {
			n1 = l1.item (j);
			if (n1.getNodeType() == Node.ELEMENT_NODE)
			{
			    e1 = (Element)n1;
			    addINST (e1, blockIndex);
			}
		    }
		}
	    }
	}
	bl.add (b);
    } 







    protected void addAttribute (Element root, Block b)
    {
	String s = root.getNodeName();
	s = s.substring (s.lastIndexOf ("_") + 1);
	int id = Integer.parseInt (s, 16);

	int      index;
	boolean  first;
	int      i;
	int      h;
	Node     n0;
	NodeList l0;
	Element  e0;



	Attribute0000    a_0000; // Road
	Attribute0010    a_0010; // Road without sidewalks
	Attribute0040    a_0040; // Divided road

	Attribute0008    a_0008; // Sidewalk strip
	Attribute0024    a_0024; // Crosswalk rectangle
	Attribute0028    a_0028; // Road fan

	Attribute0048    a_0048; // Tunnel junction
	Attribute004b    a_004b; // Tunnel

	Attribute003c    a_003c; // Facade bound
	Attribute001c    a_001c; // Sliver
	Attribute005e    a_005e; // Facade
	Attribute0060    a_0060; // Roof

	TextureAttribute a_0050; // Material

	Attribute0030    a_0030; // Triangle fan

	Vertex3D         p0;
	Vertex3D         p1;

	switch (id)
	{
	case 0x0008:
	case 0x000a:
	case 0x000b:
	case 0x000c:
	case 0x000d:
	case 0x000e:
	case 0x000f:
	    switch (id)
	    {
	    case 0x000a:
		a_0008 = new Attribute000a();
		break;
	    case 0x000b:
		a_0008 = new Attribute000b();
		break;
	    case 0x000c:
		a_0008 = new Attribute000c();
		break;
	    case 0x000d:
		a_0008 = new Attribute000d();
		break;
	    case 0x000e:
		a_0008 = new Attribute000e();
		break;
	    case 0x000f:
		a_0008 = new Attribute000f();
		break;
	    default:
		a_0008 = new Attribute0008();
		break;
	    }
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    index = addVertex (e0, true);
		    a_0008.vertexrefs.add (new VertexRef ((char)index));
		}
	    }
	    b.addAttribute (a_0008);
	    break;




	case 0x001c:
	    a_001c = new Attribute001c();

	    s = root.getAttribute ("topheight");
	    if (s.length() != 0)
	    {
		index = addHeight (Float.parseFloat (s), true);
		System.out.println ("topheight:" + index + ": " + s);
		a_001c.top = new HeightRef ((char)index);
	    }

	    s = root.getAttribute ("textureScale");
	    if (s.length() != 0)
	    {
		index = addHeight (Float.parseFloat (s), true);
		System.out.println ("textureScale:" + index + ": " + s);
		a_001c.textureScale = new HeightRef ((char)index);
	    }

	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (n0.getNodeType() == Node.ELEMENT_NODE)
		{
		    e0 = (Element)n0;
		    if ("vertex".equals (n0.getNodeName()))
		    {
			index = addVertex (e0, true);
			a_001c.vertexrefs.add (new VertexRef ((char)index));
		    }
		}
	    }
	    b.addAttribute (a_001c);
	    break;



	case 0x0024:
	    a_0024 = new Attribute0024();
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    index = addVertex (e0, true);
		    a_0024.vertexrefs.add (new VertexRef ((char)index));
		}
	    }
	    b.addAttribute (a_0024);
	    break;


	case 0x0028:
	case 0x0029:
	case 0x002a:
	case 0x002b:
	case 0x002c:
	case 0x002d:
	case 0x002e:
	case 0x002f:
	    switch (id)
	    {
	    case 0x0029:
		a_0028 = new Attribute0029();
		break;
	    case 0x002a:
		a_0028 = new Attribute002a();
		break;
	    case 0x002b:
		a_0028 = new Attribute002b();
		break;
	    case 0x002c:
		a_0028 = new Attribute002c();
		break;
	    case 0x002d:
		a_0028 = new Attribute002d();
		break;
	    case 0x002e:
		a_0028 = new Attribute002e();
		break;
	    case 0x002f:
		a_0028 = new Attribute002f();
		break;
	    default:
		a_0028 = new Attribute0028();
		break;
	    }
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    index = addVertex (e0, true);
		    a_0028.vertexrefs.add (new VertexRef ((char)index));
		}
	    }
	    b.addAttribute (a_0028);
	    break;


	case 0x003c:
	    a_003c = new Attribute003c();

	    s = root.getAttribute ("shade");
	    if (s.length() != 0)
	    {
		a_003c.shade = Integer.parseInt (s);
	    }
	    s = root.getAttribute ("height");
	    if (s.length() != 0)
	    {
		index = addHeight (Float.parseFloat (s), true);
		a_003c.top = new HeightRef ((char)index);
	    }

	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (n0.getNodeType() == Node.ELEMENT_NODE)
		{
		    e0 = (Element)n0;
		    if ("vertex".equals (n0.getNodeName()))
		    {
			index = addVertex (e0, true);
			a_003c.vertexrefs.add (new VertexRef ((char)index));
		    }
		}
	    }
	    b.addAttribute (a_003c);
	    break;




	case 0x0048:
	    a_0048 = new Attribute0048();

	    s = root.getAttribute ("left");
	    if ("yes".equals (s))
	    {
		a_0048.setFlag (Attribute0048.LEFT_BARRIER, true);
	    }

	    s = root.getAttribute ("right");
	    if ("yes".equals (s))
	    {
		a_0048.setFlag (Attribute0048.RIGHT_BARRIER, true);
	    }

	    s = root.getAttribute ("wall");
	    if ("yes".equals (s))
	    {
		a_0048.setFlag (Attribute0048.WALL, true);
	    }

	    s = root.getAttribute ("ceiling");
	    if ("yes".equals (s))
	    {
		s = root.getAttribute ("subdividedceiling");
		if ("yes".equals (s))
		{
		    a_0048.setFlag (Attribute0048.SUBDIVIDED_CEILING, true);
		}
		else
		{
		    a_0048.setFlag (Attribute0048.FLAT_CEILING, true);
		}
	    }

	    s = root.getAttribute ("subdividedwall");
	    if ("yes".equals (s))
	    {
		a_0048.setFlag (Attribute0048.SUBDIVIDED_INNER_WALLS, true);
	    }

	    s = root.getAttribute ("thick");
	    if ("yes".equals (s))
	    {
		a_0048.setFlag (Attribute0048.THICK_BARRIERS, true);
	    }


	    s = root.getAttribute ("leftheight");
	    if (s.length() != 0)
	    {
		a_0048.setLeftHeight (Float.parseFloat (s));
	    }

	    s = root.getAttribute ("rightheight");
	    if (s.length() != 0)
	    {
		a_0048.setRightHeight (Float.parseFloat (s));
	    }

	    l0 = root.getChildNodes();
	    a_0048.nWalls = (char)(l0.getLength() / 2);
	    i = 0;
	    while (i < l0.getLength())
	    {
		n0 = l0.item (i++);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    float x = Float.parseFloat (e0.getAttribute ("x"));
		    float y = Float.parseFloat (e0.getAttribute ("y"));
		    float z = Float.parseFloat (e0.getAttribute ("z"));
		    p0 = new Vertex3D (x, y, z);

		}
		n0 = l0.item (i++);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    float x = Float.parseFloat (e0.getAttribute ("x"));
		    float y = Float.parseFloat (e0.getAttribute ("y"));
		    float z = Float.parseFloat (e0.getAttribute ("z"));
		    p1 = new Vertex3D (x, y, z);
		}
		PerimeterList pl = b.getPerimeter();
		// TODO: Do something.
//		int p0Index = findVertexInPerimeter (p0, b);
//		int p1Index = findVertexInPerimeter (p1, b);
		
	    }
	    // TODO: Set the number of walls and read the wall points.


	    b.addAttribute (a_0048);
	    break;





	case 0x004b:
	    a_004b = new Attribute004b();

	    s = root.getAttribute ("left");
	    if ("yes".equals (s))
	    {
		a_004b.setFlag (Attribute004b.LEFT_BARRIER, true);
	    }

	    s = root.getAttribute ("right");
	    if ("yes".equals (s))
	    {
		a_004b.setFlag (Attribute004b.RIGHT_BARRIER, true);
	    }

	    s = root.getAttribute ("wall");
	    if ("yes".equals (s))
	    {
		a_004b.setFlag (Attribute004b.WALL, true);
	    }

	    s = root.getAttribute ("ceiling");
	    if ("yes".equals (s))
	    {
		s = root.getAttribute ("subdividedceiling");
		if ("yes".equals (s))
		{
		    a_004b.setFlag (Attribute004b.SUBDIVIDED_CEILING, true);
		}
		else
		{
		    a_004b.setFlag (Attribute004b.FLAT_CEILING, true);
		}
	    }

	    s = root.getAttribute ("subdividedwall");
	    if ("yes".equals (s))
	    {
		a_004b.setFlag (Attribute004b.SUBDIVIDED_INNER_WALLS, true);
	    }

	    s = root.getAttribute ("thick");
	    if ("yes".equals (s))
	    {
		a_004b.setFlag (Attribute004b.THICK_BARRIERS, true);
	    }


	    s = root.getAttribute ("leftheight");
	    if (s.length() != 0)
	    {
		a_004b.setLeftHeight (Float.parseFloat (s));
	    }

	    s = root.getAttribute ("rightheight");
	    if (s.length() != 0)
	    {
		a_004b.setRightHeight (Float.parseFloat (s));
	    }


	    b.addAttribute (a_004b);
	    break;



	case 0x0050: // TODO: Does not handle indexed textures yet.
	    b.addAttribute (readMaterials (root));
	    break;




	case 0x005e:
	    a_005e = new Attribute005e();
	    s = root.getAttribute ("xRepeat");
	    if (s.length() != 0)
	    {
		a_005e.xRepeat = (char)Integer.parseInt (s);
	    }
	    s = root.getAttribute ("yRepeat");
	    if (s.length() != 0)
	    {
		a_005e.yRepeat = (char)Integer.parseInt (s);
	    }

	    s = root.getAttribute ("bottomheight");
	    if (s.length() != 0)
	    {
		index = addHeight (Float.parseFloat (s), true);
		System.out.println ("bottomheight:" + index + ": " + s);
		a_005e.bottom = new HeightRef ((char)index);
	    }
	    s = root.getAttribute ("topheight");
	    if (s.length() != 0)
	    {
		index = addHeight (Float.parseFloat (s), true);
		System.out.println ("topheight:" + index + ": " + s);
		a_005e.top = new HeightRef ((char)index);
	    }


	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (n0.getNodeType() == Node.ELEMENT_NODE)
		{
		    e0 = (Element)n0;
		    if ("vertex".equals (n0.getNodeName()))
		    {
			index = addVertex (e0, true);
			a_005e.vertexrefs.add (new VertexRef ((char)index));
		    }
		}
	    }
	    b.addAttribute (a_005e);
	    break;

	case 0x0060:
	case 0x0062:
	case 0x0063:
	case 0x0064:
	case 0x0065:
	case 0x0066:
	case 0x0067:
	    switch (id)
	    {
	    case 0x0062:
		a_0060 = new Attribute0062();
		break;
	    case 0x0063:
		a_0060 = new Attribute0063();
		break;
	    case 0x0064:
		a_0060 = new Attribute0064();
		break;
	    case 0x0065:
		a_0060 = new Attribute0065();
		break;
	    case 0x0066:
		a_0060 = new Attribute0066();
		break;
	    case 0x0067:
		a_0060 = new Attribute0067();
		break;
	    default:
		a_0060 = new Attribute0060();
		break;
	    }
	    s = root.getAttribute ("height");
	    if (s.length() != 0)
	    {
		index = addHeight (Float.parseFloat (s), true);
		a_0060.height = new HeightRef ((char)index);
	    }
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (n0.getNodeType() == Node.ELEMENT_NODE)
		{
		    e0 = (Element)n0;
		    if ("vertex".equals (n0.getNodeName()))
		    {
			index = addVertex (e0, true);
			a_0060.vertexrefs.add (new VertexRef ((char)index));
		    }
		}
	    }
	    b.addAttribute (a_0060);
	    break;

	case 0x0000:
	case 0x0002:
	case 0x0003:
	case 0x0004:
	case 0x0005:
	case 0x0006:
	case 0x0007:
	    switch (id)
	    {
	    case 0x0002:
		a_0000 = new Attribute0002();
		break;
	    case 0x0003:
		a_0000 = new Attribute0003();
		break;
	    case 0x0004:
		a_0000 = new Attribute0004();
		break;
	    case 0x0005:
		a_0000 = new Attribute0005();
		break;
	    case 0x0006:
		a_0000 = new Attribute0006();
		break;
	    case 0x0007:
		a_0000 = new Attribute0007();
		break;
	    default:
		a_0000 = new Attribute0000();
		break;
	    }
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    index = addVertex (e0, true);
		    a_0000.vertexrefs.add (new VertexRef ((char)index));
		}
	    }
	    b.addAttribute (a_0000);
	    break;
	    
	case 0x0010:
	case 0x0012:
	case 0x0013:
	case 0x0014:
	case 0x0015:
	case 0x0016:
	case 0x0017:
	    switch (id)
	    {
	    case 0x0012:
		a_0010 = new Attribute0012();
		break;
	    case 0x0013:
		a_0010 = new Attribute0013();
		break;
	    case 0x0014:
		a_0010 = new Attribute0014();
		break;
	    case 0x0015:
		a_0010 = new Attribute0015();
		break;
	    case 0x0016:
		a_0010 = new Attribute0016();
		break;
	    case 0x0017:
		a_0010 = new Attribute0017();
		break;
	    default:
		a_0010 = new Attribute0010();
		break;
	    }
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    index = addVertex (e0, true);
		    a_0010.vertexrefs.add (new VertexRef ((char)index));
		}
	    }
	    b.addAttribute (a_0010);
	    break;
	    
	case 0x0030:
	case 0x0031:
	case 0x0032:
	case 0x0033:
	case 0x0034:
	case 0x0035:
	case 0x0036:
	case 0x0037:
	    switch (id)
	    {
	    case 0x0031:
		a_0030 = new Attribute0031();
		break;
	    case 0x0032:
		a_0030 = new Attribute0032();
		break;
	    case 0x0033:
		a_0030 = new Attribute0033();
		break;
	    case 0x0034:
		a_0030 = new Attribute0034();
		break;
	    case 0x0035:
		a_0030 = new Attribute0035();
		break;
	    case 0x0036:
		a_0030 = new Attribute0036();
		break;
	    case 0x0037:
		a_0030 = new Attribute0037();
		break;
	    default:
		a_0030 = new Attribute0030();
		break;
	    }
	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "vertex".equals (n0.getNodeName()))
		{
		    e0 = (Element)n0;
		    index = addVertex (e0, true);
		    a_0030.vertexrefs.add (new VertexRef ((char)index));
		}
	    }
	    b.addAttribute (a_0030);
	    break;

	case 0x0040:
	case 0x0042:
	case 0x0043:
	case 0x0044:
	case 0x0045:
	case 0x0046:
	case 0x0047:
	    switch (id)
	    {
	    case 0x0042:
		a_0040 = new Attribute0042();
		break;
	    case 0x0043:
		a_0040 = new Attribute0043();
		break;
	    case 0x0044:
		a_0040 = new Attribute0044();
		break;
	    case 0x0045:
		a_0040 = new Attribute0045();
		break;
	    case 0x0046:
		a_0040 = new Attribute0046();
		break;
	    case 0x0047:
		a_0040 = new Attribute0047();
		break;
	    default:
		a_0040 = new Attribute0040();
		break;
	    }

	    s = root.getAttribute ("type");
	    if (s.length() != 0)
	    {
		a_0040.setType (Integer.parseInt (s));
	    }

	    s = root.getAttribute ("value");
	    if (s.length() != 0)
	    {
		a_0040.setHeight (Float.parseFloat (s));
	    }

	    s = root.getAttribute ("closestart");
	    if ("yes".equals (s))
	    {
		a_0040.setFlag (Attribute0040.CLOSE_START, true);
	    }
	    s = root.getAttribute ("closeend");
	    if ("yes".equals (s))
	    {
		a_0040.setFlag (Attribute0040.CLOSE_END, true);
	    }



	    l0 = root.getChildNodes();
	    for (i = 0; i < l0.getLength(); i++)
	    {
		n0 = l0.item (i);
		if (n0.getNodeType() == Node.ELEMENT_NODE)
		{
		    e0 = (Element)n0;
		    if ("attribute_50".equals (n0.getNodeName()))
		    {
			a_0050 = readMaterials (e0);
			a_0040.setTextureMap (a_0050.ref);
		    }
		    else if ("vertex".equals (n0.getNodeName()))
		    {
			index = addVertex (e0, true);
			a_0040.vertexrefs.add (new VertexRef ((char)index));
		    }
		}
	    }
	    b.addAttribute (a_0040);
	    break;
	    
	}
    }



    protected TextureAttribute readMaterials (Node root)
    {
	    TextureAttribute a_0050 = null;
	    boolean first = true;
	    NodeList l0 = root.getChildNodes();
	    String[] texturenames = new String[l0.getLength()];
	    int ti = 0;
	    int      index;
	    for (int i = 0; i < l0.getLength(); i++)
	    {
		Node n0 = l0.item (i);
		if (   n0.getNodeType() == Node.ELEMENT_NODE
		    && "texture".equals (n0.getNodeName()))
		{
		    Element e0 = (Element)n0;
		    String s = e0.getAttribute ("name");
		    if (s.length() != 0)
		    {
			texturenames[ti++] = s;
		    }
		}
	    }
	    index = -1;
	    if (weldTextures)
	    {
		index = findTextureList (texturenames);
	    }
	    if (index == -1)
	    {
		index = addTextureList (texturenames);
	    }
	    if (index <= 255)
	    {
		a_0050 = new TextureAttribute();
	    }
	    else
	    {
		a_0050 = new Attribute0051();
	    }
	    a_0050.ref = new TextureMapRef ((char)index,
					    psdl.getTextures());
	    return a_0050;
    }



    protected int findTextureList (String[] l)
    {
//	System.out.println ("findTextureList:");
	int index = -1;
	int tempIndex;
	TextureList tl = psdl.getTextures();

	int li  = 0;
	int tli = 0;
	boolean match = false;
	while (!match && tli < tl.size())
	{
//	    System.out.println (" start scan");
	    li = 0;
	    tempIndex = tli;
	    while (   li < l.length
		   && tli < tl.size()
		   && l[li] != null
		   && l[li].equals (tl.textureAt (tli).getName()))
	    {
//		System.out.println ("  match");
		li++;
		tli++;
	    }
	    if (li == l.length || l[li] == null)
	    {
//		System.out.println (" complete match");
		match = true;
		index = tempIndex;
	    }
	    else
	    {
//		System.out.println (" no match");
	    }
	    tli++;
	}
	return index;
    }

    protected int addTextureList (String[] l)
    {
	TextureList tl = psdl.getTextures();
	int index = tl.size();
	int i = 0;
	while (i < l.length && l[i] != null)
	{
//	    System.out.println ("Adding texture: " + l[i] + " (" + index + i + ")");
	    tl.add (new TextureMap (l[i], texturePath));
	    i++;
	}
	return index;
    }





    protected void addINST (Element root, int blockIndex)
    {
	INSTComponent c = null;
	String s = root.getNodeName();
	String pkg = root.getAttribute ("pkg");
	int flags = Integer.parseInt (root.getAttribute ("flags"));
	int paintjob = Integer.parseInt (root.getAttribute ("paintjob"));
	Vertex3D origo = new Vertex3D (0.0f, 0.0f, 0.0f);
	origo.x = Float.parseFloat (root.getAttribute ("x"));
	origo.y = Float.parseFloat (root.getAttribute ("y"));
	origo.z = Float.parseFloat (root.getAttribute ("z"));
	if ("simple".equals (s))
	{
	    float dx = Float.parseFloat (root.getAttribute ("dx"));
	    float dz = Float.parseFloat (root.getAttribute ("dz"));
	    c = new INSTSimpleComponent (blockIndex,
					 flags << 8 + paintjob,
					 pkg,
					 dx,
					 dz,
					 origo);
	}
	else if ("coordsys".equals (s))
	{
	    Vertex3D xAxis = new Vertex3D (0.0f, 0.0f, 0.0f);;
	    Vertex3D yAxis = new Vertex3D (0.0f, 0.0f, 0.0f);;
	    Vertex3D zAxis = new Vertex3D (0.0f, 0.0f, 0.0f);;
	    xAxis.x = Float.parseFloat (root.getAttribute ("rightX"));
	    xAxis.y = Float.parseFloat (root.getAttribute ("rightY"));
	    xAxis.z = Float.parseFloat (root.getAttribute ("rightZ"));
	    yAxis.x = Float.parseFloat (root.getAttribute ("upX"));
	    yAxis.y = Float.parseFloat (root.getAttribute ("upY"));
	    yAxis.z = Float.parseFloat (root.getAttribute ("upZ"));
	    zAxis.x = Float.parseFloat (root.getAttribute ("lookX"));
	    zAxis.y = Float.parseFloat (root.getAttribute ("lookY"));
	    zAxis.z = Float.parseFloat (root.getAttribute ("lookZ"));
	    c = new INSTCoordinateSystemComponent (blockIndex,
						   flags << 8 + paintjob,
						   pkg,
						   xAxis,
						   yAxis,
						   zAxis,
						   origo);
	}
	if (c != null)
	{
	    INSTList il = inst.getComponents();
	    il.add (c);
	}
    }














    /*  Old attempt of making XML export!! Does not actually make XML.
     *
     */

    protected String s;

    public PSDLML (PSDLFile f)
    {
	psdl = f;

	s = buildDOM();
    }

    /** Builds the DOM:
     *
     *  TODO: Now it only builds a string, should build a DOM.
     *
     */
    protected String buildDOM()
    {
	String s = "";
	s += buildVertices();
	s += "\n";
	s += buildTextures();
	s += buildBlocks();
	return s;
    }


    protected String buildVertices()
    {
	String s = "";
	VertexList vertices = psdl.getVertices();
	for (int i = 0; i < vertices.size(); i++)
	{
	    Vertex3D v = vertices.vertexAt (i);
	    s += "v " + v.x + " " + v.y + " " + v.z + "\n";
	}
	return s;
    }

    protected String buildTextures()
    {
	String s = "";
	TextureList textures = psdl.getTextures();
	for (int i = 0; i < textures.size(); i++)
	{
	    TextureMap t = textures.textureAt (i);
	    String n = "" + t;
	    if (n.length() == 0)
	    {
		n = "---notexture---";
	    }
	    s += "td " + n + "\n";
	}
	return s;
    }

    protected String buildBlocks()
    {
	String s = "";
	BlockList blocks = psdl.getBlocks();
	s += "\nrooms 1 1 " + (blocks.size() - 1) + "\n";
	for (int i = 0; i < blocks.size(); i++)
	{
	    Block b = blocks.blockAt (i);
	    s += "\nroom " + (i + 1) + "\n";
	    s += "id " + (i + 1) + "\n";
	    s += "type 1\n";
	    s += buildBlockAttributes (b);

	    PerimeterList perimeter = b.getPerimeter();
	    s += "perimeter " + perimeter.size() + "\n";
	    for (int j = 0; j < perimeter.size(); j++)
	    {
		PerimeterVertex p = perimeter.perimeterAt (j);
		s += "    ";
		s += (int)p.getIndex();
		s += " ";
		int cb = p.getConnectingBlock();
		s += ((cb == 65535) ? 0 : (cb + 1)) + "\n";
	    }
	}
	return s;
    }


    // TODO: Build a toXML() method in each attribute instead of this:
    protected String buildBlockAttributes (Block b)
    {
	String s = "";
	BlockAttributeList attr = b.getAttributes();
	for (int i = 0; i < attr.size(); i++)
	{
	    BlockAttribute a = attr.attributeAt (i);
	    char id = a.getID();
	    switch (id)
	    {
	    case 0x0050:
		TextureAttribute t = (TextureAttribute)a;
		s += "tex " + (int)t.ref.getIndex() + "\n";
		break;
	    default:
		Vector data = a.getData();
		for (int j = 0; j < data.size(); j++)
		{
		    s += data.elementAt (j) + " ";
		}
		s += "\n";
		break;
	    }
	}

	return s;
    }




    public void write (String filename)
    throws IOException
    {
	// TODO: Should set up an identity transformer and output XML.

	PrintWriter out = new PrintWriter (new FileWriter (filename));
	out.write (s);
	out.close();
    }






}
