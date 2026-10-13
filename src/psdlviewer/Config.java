package psdlviewer;
import psdlviewer.*;

import psdlviewer.bai.BAIFile;
import psdlviewer.bai.RouteListView;

/** Class holding current settings of the PSDLViewer.
 *
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.lang.NoSuchMethodException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

import java.util.Hashtable;
import java.util.Vector;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import java.awt.Rectangle;
import java.awt.Container;

import javax.swing.JOptionPane;


public class Config
{
    public static String optionsFilename = "options.xml";

    public static String openPath    = "..\\..\\..\\mm2\\city";
    public static String savePath    = "..\\..\\..\\mm2\\city";

    public static Vector texturePath = new Vector();
    public static Vector geometryPath = new Vector();


    /** Reference to the main window.
     *
     */
    public static MainWindow mainWindow;



    public static BlockList   blockList;
    public static VertexList  vertexList;
    public static TextureList textureList;
    public static HeightList  heightList;
    public static Dimensions  dimensions;
    public static RoadList    roadList;
    public static long        unknown3;

    public static PathsetList pathsetList = new PathsetList();

    protected static BAIFile baiFile;
    protected static INSTFile instFile = new INSTFile();


    /** The terrain object used to render the 3D views.
     *
     */
    public static Terrain terrain = new Terrain();


    /** Vector holding all block list view objects.
     *
     */
    public static Vector blockListViews = new Vector();

    /** Vector holding all vertex list view objects.
     *
     */
    public static Vector vertexListViews = new Vector();

    /** Vector holding all texture list view objects.
     *
     */
    public static Vector textureListViews = new Vector();

    /** Vector holding all terrain view objects.
     *
     */
    public static Vector terrainViews = new Vector();

    /** Vector holding all height list view objects.
     *
     */
    public static Vector heightListViews = new Vector();

    /** Vector holding all road list view objects.
     *
     */
    public static Vector roadListViews = new Vector();

    /** Vector holding all inst component list view objects.
     *
     */
    public static Vector instComponentListViews = new Vector();

    /** Vector holding all route list view objects.
     *
     */
    public static Vector routeListViews = new Vector();

    /** Vector holding all pathset list view objects.
     *
     */
    public static Vector pathsetListViews = new Vector();




    /** reads option file and sets up options.
     *
     */
    public static void readOptions()
    {
	try
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder        builder = factory.newDocumentBuilder();

	    File f = new File (optionsFilename);
	    Document doc = builder.parse (f);

	    Element root = doc.getDocumentElement();
	    if (!"PSDLViewer".equals (root.getNodeName()))
	    {
		System.err.println ("Not an options file.");
		return;
	    }

	    setupMainWindowOptions (root.getElementsByTagName ("MAIN_WINDOW"));
	    setupFileOptions (root.getElementsByTagName ("FILE"));
	    setupTerrainOptions (root.getElementsByTagName ("TERRAIN"));

	    setupViews (root.getElementsByTagName ("BLOCK_LIST_VIEW"),
			BlockListView.class,
			blockListViews);
	    setupViews (root.getElementsByTagName ("VERTEX_LIST_VIEW"),
			VertexListView.class,
			vertexListViews);
	    setupViews (root.getElementsByTagName ("TEXTURE_LIST_VIEW"),
			TextureListView.class,
			textureListViews);
	    setupViews (root.getElementsByTagName ("HEIGHT_LIST_VIEW"),
			HeightListView.class,
			heightListViews);
	    setupViews (root.getElementsByTagName ("ROAD_LIST_VIEW"),
			RoadListView.class,
			roadListViews);
	    setupViews (root.getElementsByTagName ("INST_LIST_VIEW"),
			INSTComponentListView.class,
			instComponentListViews);
	    setupViews (root.getElementsByTagName ("ROUTE_LIST_VIEW"),
			RouteListView.class,
			routeListViews);
	    setupViews (root.getElementsByTagName ("TERRAIN_VIEW"),
			TerrainView.class,
			terrainViews);
	    setupViews (root.getElementsByTagName ("PATHSET_LIST_VIEW"),
			PathsetListView.class,
			pathsetListViews);
	}
	catch (ParserConfigurationException pce)
	{
	    String s =   "* Parser Configuration error\n"
		       + "  " + pce.getMessage();
	    JOptionPane.showMessageDialog (mainWindow, s);
	    System.out.println (s);
	}
	catch (SAXException e)
	{
	    JOptionPane.showMessageDialog (mainWindow, e);
	    System.out.println("  " + e);
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (mainWindow, e);
	    System.out.println("  " + e);
	}
    }


    protected static void setupMainWindowOptions (NodeList l)
    {
	Element e = (Element)l.item (0);
	Rectangle r = new Rectangle();
	String s;
	s = e.getAttribute ("LEFT");
	r.x = (s.length() != 0) ? Integer.parseInt (s) : 0;
	s = e.getAttribute ("TOP");
	r.y = (s.length() != 0) ? Integer.parseInt (s) : 0;
	s = e.getAttribute ("WIDTH");
	r.width = (s.length() != 0) ? Integer.parseInt (s) : 100;
	s = e.getAttribute ("HEIGHT");
	r.height = (s.length() != 0) ? Integer.parseInt (s) : 100;

	mainWindow.setBounds (r);
    }

    protected static void setupFileOptions (NodeList l)
    {
	for (int i = 0; i < l.getLength(); i++)
	{
	    Element e = (Element)l.item (i);
	    String s;
	    s = e.getAttribute ("OPEN");
	    if (s != null)
	    {
		openPath = s;
	    }

	    s = e.getAttribute ("SAVE");
	    if (s != null)
	    {
		savePath = s;
	    }

	    NodeList textureNodeList = e.getElementsByTagName ("TEXTURE");
	    for (int j = 0; j < textureNodeList.getLength(); j++)
	    {
		Element textureElement = (Element)textureNodeList.item (j);
		Node textNode = textureElement.getFirstChild();
		s = textNode.getNodeValue();
		if (s != null)
		{
		    texturePath.add (s);
		}
	    }

	    NodeList geometryNodeList = e.getElementsByTagName ("GEOMETRY");
	    for (int j = 0; j < geometryNodeList.getLength(); j++)
	    {
		Element geometryElement = (Element)geometryNodeList.item (j);
		Node textNode = geometryElement.getFirstChild();
		s = textNode.getNodeValue();
		if (s != null)
		{
		    geometryPath.add (s);
		}
	    }
	}
    }



    protected static void setupTerrainOptions (NodeList l)
    {
	if (l.getLength() != 0)
	{
	    Element e = (Element)l.item (0);
	    terrain.setOptions (e);
	}
    }




    public static void addView (BlockListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	blockListViews.add (f);
	mainWindow.getContentPane().add (f);
	f.moveToFront();

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);
    }


    public static void addView (VertexListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	vertexListViews.add (f);
	mainWindow.getContentPane().add (f);

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);

	vertexList.addVertexRefSelectionListener (v);
    }

    public static void addView (TextureListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	textureListViews.add (f);
	mainWindow.getContentPane().add (f);

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);

	blockList.addBlockSelectionListener (v);
    }

    public static void addView (TerrainView v, Rectangle geometry)
    {
	v.setLocation (geometry.x, geometry.y);
	v.setSize (geometry.width, geometry.height);
	terrainViews.add (v);
    }

    public static void addView (HeightListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	heightListViews.add (f);
	mainWindow.getContentPane().add (f);
	f.moveToFront();

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);
    }



    public static void addView (RoadListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	roadListViews.add (f);
	mainWindow.getContentPane().add (f);
	f.moveToFront();

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);
    }


    public static void addView (INSTComponentListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	instComponentListViews.add (f);
	mainWindow.getContentPane().add (f);
	f.moveToFront();

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);

	v.setList (instFile.getComponents());
    }


    public static void addView (RouteListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	routeListViews.add (f);
	mainWindow.getContentPane().add (f);
	f.moveToFront();

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);
    }



    public static void addView (PathsetListView v, Rectangle geometry)
    {
	ListViewFrame f = new ListViewFrame (v);
	f.setTitle (v.getName());
	v.setName (" ");
	f.setLocation (geometry.x, geometry.y);
	f.setSize (geometry.width, geometry.height);
	v.setList (pathsetList);
	pathsetListViews.add (f);
	mainWindow.getContentPane().add (f);
	f.moveToFront();

	if (v.getDataView() != null)
	    v.setDividerLocation (0.5);
	else
	    v.setDividerLocation (1.0);
    }



    public static void removeListView (ListViewFrame v)
    {
	blockListViews.remove (v);
	vertexListViews.remove (v);
	textureListViews.remove (v);
	heightListViews.remove (v);
	roadListViews.remove (v);
	instComponentListViews.remove (v);
	routeListViews.remove (v);
	pathsetListViews.remove (v);
    }

    public static void removeTerrainView (TerrainView v)
    {
	terrainViews.remove (v);
    }


    /** Creates one instance of a specific class for each node in the node list
     *  and adds them all to a vector.
     *
     */
    protected static void setupViews (NodeList l, Class c, Vector v)
    {
	try
	{
	    Class  types[] = {Element.class};
	    Constructor constr = c.getConstructor (types);

	    for (int i = 0; i < l.getLength(); i++)
	    {
		Element e = (Element)l.item (i);
		Object args[] = {e};
		Object o = constr.newInstance (args);

		Rectangle r = new Rectangle();
		String s;
		s = e.getAttribute ("LEFT");
		r.x = (s.length() != 0) ? Integer.parseInt (s) : 0;
		s = e.getAttribute ("TOP");
		r.y = (s.length() != 0) ? Integer.parseInt (s) : 0;
		s = e.getAttribute ("WIDTH");
		r.width = (s.length() != 0) ? Integer.parseInt (s) : 100;
		s = e.getAttribute ("HEIGHT");
		r.height = (s.length() != 0) ? Integer.parseInt (s) : 100;
		String name = e.getAttribute ("NAME");

		if (c == TerrainView.class)
		{
		    addView ((TerrainView)o, r);
		}
		else 

		{
		    ListView lv = (ListView)o;
		    lv.setName (name);

		    if (c == BlockListView.class)
			addView ((BlockListView)o, r);
		    else if (c == VertexListView.class)
			addView ((VertexListView)o, r);
		    else if (c == TextureListView.class)
			addView ((TextureListView)o, r);
		    else if (c == HeightListView.class)
			addView ((HeightListView)o, r);
		    else if (c == RoadListView.class)
			addView ((RoadListView)o, r);
		    else if (c == INSTComponentListView.class)
			addView ((INSTComponentListView)o, r);
		    else if (c == RouteListView.class)
			addView ((RouteListView)o, r);
		    else if (c == PathsetListView.class)
			addView ((PathsetListView)o, r);
		}
	    }
	}
	catch (NoSuchMethodException e)
	{
	    JOptionPane.showMessageDialog (mainWindow, e);
	    System.err.println ("Config.setupViews: " + e);
	    e.printStackTrace (System.err);
	}
	catch (InstantiationException e)
	{
	    JOptionPane.showMessageDialog (mainWindow, e);
	    System.err.println ("Config.setupViews: " + e);
	    e.printStackTrace (System.err);
	}
	catch (IllegalAccessException e)
	{
	    JOptionPane.showMessageDialog (mainWindow, e);
	    System.err.println ("Config.setupViews: " + e);
	    e.printStackTrace (System.err);
	}
	catch (InvocationTargetException e)
	{
	    Throwable x = e.getTargetException();
	    if (x == null)
		x = e;
	    System.err.println ("Config.setupViews: " + e);
	    x.printStackTrace (System.err);
	    JOptionPane.showMessageDialog (mainWindow, x);
	}
    }	






    /** Writes option file.
     *
     */
    public static void writeOptions()
    {
	try
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder        builder = factory.newDocumentBuilder();
	    Document               doc     = builder.newDocument();
	    Element                root    = doc.createElement ("PSDLViewer");
	    Element                e;
	    Text                   textNode;
	    doc.appendChild (root);
	    Rectangle r;

	    // Add main window options:
	    e = doc.createElement ("MAIN_WINDOW");
	    r = mainWindow.getBounds();
	    e.setAttribute ("LEFT", "" + r.x);
	    e.setAttribute ("TOP", "" + r.y);
	    e.setAttribute ("WIDTH", "" + r.width);
	    e.setAttribute ("HEIGHT", "" + r.height);
	    root.appendChild (e);

	    // Add file options:
	    //
	    //
	    e = doc.createElement ("FILE");
	    e.setAttribute ("OPEN",    openPath);
	    e.setAttribute ("SAVE",    savePath);

	    for (int i = 0; i < texturePath.size(); i++)
	    {
		Element texturePathElement = doc.createElement ("TEXTURE");
		textNode = doc.createTextNode ((String)texturePath.elementAt (i));
		texturePathElement.appendChild (textNode);
		e.appendChild (texturePathElement);
	    }
	    for (int i = 0; i < geometryPath.size(); i++)
	    {
		Element geometryPathElement = doc.createElement ("GEOMETRY");
		textNode = doc.createTextNode ((String)geometryPath.elementAt (i));
		geometryPathElement.appendChild (textNode);
		e.appendChild (geometryPathElement);
	    }
	    root.appendChild (e);

	    // Add terrain options:
	    //
	    //
	    e = terrain.getOptions (doc);
	    root.appendChild (e);

	    // Add block list view options:
	    //
	    //
	    for (int i = 0; i < blockListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)blockListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }

	    // Add vertex list view options:
	    //
	    //
	    for (int i = 0; i < vertexListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)vertexListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }

	    // Add texture list view options:
	    //
	    //
	    for (int i = 0; i < textureListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)textureListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }

	    // Add height list view options:
	    //
	    //
	    for (int i = 0; i < heightListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)heightListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }

	    // Add road list view options:
	    //
	    //
	    for (int i = 0; i < roadListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)roadListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }



	    // Add INST list view options:
	    //
	    //
	    for (int i = 0; i < instComponentListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)instComponentListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }



	    // Add Route list view options:
	    //
	    //
	    for (int i = 0; i < routeListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)routeListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }



	    // Add terrain view options:
	    //
	    //
	    for (int i = 0; i < terrainViews.size(); i++)
	    {
		TerrainView view = (TerrainView)terrainViews.elementAt (i);
		e = view.getOptions (doc);
		r = view.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }


	    // Add pathset list view options:
	    //
	    //
	    for (int i = 0; i < pathsetListViews.size(); i++)
	    {
		ListViewFrame frame = (ListViewFrame)pathsetListViews.elementAt (i);
		e = frame.getView().getOptions (doc);

		String name = frame.getTitle();
		if (name != null)
		    e.setAttribute ("NAME", name);

		r = frame.getBounds();
		e.setAttribute ("LEFT", "" + r.x);
		e.setAttribute ("TOP", "" + r.y);
		e.setAttribute ("WIDTH", "" + r.width);
		e.setAttribute ("HEIGHT", "" + r.height);
		root.appendChild (e);
	    }



	    // Write DOM to file:
	    //
	    //

	    // Use a Transformer for output
	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    transformer.setOutputProperty (OutputKeys.INDENT, "yes");
	    DOMSource source = new DOMSource (doc);

	    File f = new File (optionsFilename);
	    StreamResult result = new StreamResult (f);
	    transformer.transform (source, result);
	}

	catch (ParserConfigurationException pce)
	{
	    // Error generated by the parser
	    System.out.println ("* Parser Configuration error");
	    System.out.println("  " + pce.getMessage() );
	}
	catch (TransformerConfigurationException tce)
	{
	    // Error generated by the parser
	    System.out.println ("* Transformer Factory error");
	    System.out.println("  " + tce.getMessage() );

	    // Use the contained exception, if any
	    Throwable x = tce;
	    if (tce.getException() != null)
		x = tce.getException();
	    x.printStackTrace();
	}
	catch (TransformerException te)
	{
	    // Error generated by the parser
	    System.out.println ("* Transformation error");
	    System.out.println("  " + te.getMessage() );

	    // Use the contained exception, if any
	    Throwable x = te;
	    if (te.getException() != null)
		x = te.getException();
	    x.printStackTrace();
	}
    }



    public static Terrain getTerrain()
    {
	return terrain;
    }


    public static void setBlockList (BlockList v)
    {
	blockList = v;
	for (int i = 0; i < blockListViews.size(); i++)
	{
	    BlockListView lv = (BlockListView)((ListViewFrame)
				    blockListViews.elementAt (i)).getView();
	    lv.setList (v);

//TODO: This is when the old listener mechanism is removed:
//	    v.addBlockSelectionListener (lv);
	}

	for (int i = 0; i < routeListViews.size(); i++)
	{
	    RouteListView lv = (RouteListView)((ListViewFrame)
				    routeListViews.elementAt (i)).getView();
	    lv.setList (v);
	}

	for (int i = 0; i < textureListViews.size(); i++)
	{
	    TextureListView lv = (TextureListView)((ListViewFrame)
				    textureListViews.elementAt (i)).getView();
	    v.addBlockSelectionListener (lv);
	}

	if (getTerrain() != null)
	{
	    v.addBlockSelectionListener (getTerrain());
	}

    }

    public static void setVertexList (VertexList v)
    {
	vertexList = v;
	for (int i = 0; i < vertexListViews.size(); i++)
	{
	    VertexListView lv = (VertexListView)((ListViewFrame)
                                      vertexListViews.elementAt (i)).getView();
	    lv.setList (v);
	    v.addVertexRefSelectionListener (lv);
	}
	if (terrain != null)
	    vertexList.addVertexSelectionListener (terrain);
    }

    public static void setTextureList (TextureList v)
    {
	textureList = v;
	for (int i = 0; i < textureListViews.size(); i++)
	{
	    ((ListViewFrame)textureListViews.elementAt (i)).getView().setList (v);
	}
    }

    public static void setHeightList (HeightList v)
    {
	heightList = v;
	for (int i = 0; i < heightListViews.size(); i++)
	{
	    ((ListViewFrame)heightListViews.elementAt (i)).getView().setList (v);
	}
    }


    public static void setDimensions (Dimensions d)
    {
	dimensions = d;
    }



    public static void setRoadList (RoadList v)
    {
	roadList = v;
	for (int i = 0; i < roadListViews.size(); i++)
	{
	    RoadListView lv = (RoadListView)((ListViewFrame)
				    roadListViews.elementAt (i)).getView();
	    lv.setList (v);
	}
    }

    public static void setPathsetList (PathsetList v)
    {
	pathsetList = v;
	for (int i = 0; i < pathsetListViews.size(); i++)
	{
	    ((ListViewFrame)pathsetListViews.elementAt (i)).getView().setList (v);
	}
    }




    public static BlockList getBlockList()
    {
	return blockList;
    }
    public static VertexList getVertexList()
    {
	return vertexList;
    }
    public static TextureList getTextureList()
    {
	return textureList;
    }
    public static HeightList getHeightList()
    {
	return heightList;
    }

    public static Dimensions getDimensions()
    {
	return dimensions;
    }

    public static RoadList getRoadList()
    {
	return roadList;
    }

    public static PathsetList getPathsetList()
    {
	return pathsetList;
    }



    public static void setTerrainBlocks (VertexList v,
					 HeightList h,
					 TextureList t,
					 BlockList b,
					 ProgressWindow monitor)
    {
	terrain.createTerrain (v, h, t, b, monitor);
    }





    public static void setBAIFile (BAIFile f)
    {
	baiFile = f;
	baiFile.getRoutes().addRouteSelectionListener (terrain);
	for (int i = 0; i < routeListViews.size(); i++)
	{
	    ((ListViewFrame)routeListViews.elementAt (i)).getView().setList (f.getRoutes());
	}
    }


    public static void setINSTFile (INSTFile f)
    {
	instFile = f;
	blockList.addBlockListChangedListener (instFile.getComponents());

	for (int i = 0; i < instComponentListViews.size(); i++)
	{
	    ((ListViewFrame)instComponentListViews.elementAt (i)).getView().setList (f.getComponents());
	}
    }





    // Returns the selected vertices of the master vertex list.
    //
    //
    public static Object[] getSelectedVertices()
    {
	if (vertexListViews.size() >= 1)
	{
	    VertexListView lv = (VertexListView)((ListViewFrame)
                                      vertexListViews.elementAt (0)).getView();
	    return lv.getSelectedValues();
	}
	else
	{
	    return null;
	}
    }



    // Prints a message to std out.
    //
    public static void debug (String s)
    {
	System.out.print (s);
    }

}
