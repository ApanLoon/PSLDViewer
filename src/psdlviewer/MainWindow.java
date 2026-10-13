package psdlviewer;
import psdlviewer.*;
import psdlviewer.bai.BAIFile;
import psdlviewer.bai.RouteListView;

/** A class representing the main window of the PSDL Viewer program.
 *
 */

// For VRML export:
//import cv97.j3d.*;
//import javax.media.j3d.BranchGroup;

import javax.vecmath.Point3f;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import javax.swing.ImageIcon;

import java.awt.FileDialog;

import javax.swing.KeyStroke;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Container;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;

import java.util.Vector;


public class MainWindow
extends JFrame
implements WindowListener,
	   ActionListener
{
    // File:
    protected static final int MENU_NEW                =  0;
    protected static final int MENU_OPEN               =  1;
    protected static final int MENU_SAVE               =  2;
    protected static final int MENU_OPEN_INST          =  3;
    protected static final int MENU_SAVE_INST          =  4;
    protected static final int MENU_OPEN_BAI           =  5;
    protected static final int MENU_SAVE_BAI           =  6;
    protected static final int MENU_APPEND_PSDL        =  7;
    protected static final int MENU_OPTIONS            =  8;
    protected static final int MENU_EXIT               =  9;
    protected static final int MENU_IMPORT_VERTICES    = 10;
    protected static final int MENU_EXPORT_PSDLML      = 11;
//    protected static final int MENU_EXPORT_VRML        = 12;

    // View->Views:
    protected static final int MENU_BLOCK_LIST         = 101;
    protected static final int MENU_VERTEX_LIST        = 102;
    protected static final int MENU_TEXTURE_LIST       = 103;
    protected static final int MENU_TERRAIN_VIEW       = 104;
    protected static final int MENU_HEIGHT_LIST        = 105;
    protected static final int MENU_ROAD_LIST          = 106;
    protected static final int MENU_INST_LIST          = 107;
    protected static final int MENU_ROUTE_LIST         = 108;
    protected static final int MENU_PATHSET_LIST       = 109;

    // View:
    protected static final int MENU_REFRESH_BLOCKS     = 201;
    protected static final int MENU_FLUSH_TEXTURES     = 202;

    // Tools:
    protected static final int MENU_TOOLS_AUTO_NEIGHBOUR   = 301;
    protected static final int MENU_TOOLS_AUTO_LASTATTR    = 302;
    protected static final int MENU_TOOLS_AUTO_BLOCKTYPE   = 303;
    protected static final int MENU_TOOLS_AUTO_BLOCKSORT   = 304;
    protected static final int MENU_TOOLS_AUTO_DIMENSIONS  = 305;
    protected static final int MENU_TOOLS_RANDOM_TERRAIN   = 306;
    protected static final int MENU_TOOLS_AUTO_FACADEBOUND = 307;
    protected static final int MENU_TOOLS_GENERATE_CPVS    = 308;

    // Help:
    protected static final int MENU_LIST_ATTRIBUTES    = 400;
    protected static final int MENU_ABOUT              = 401;









//    protected PSDLFile psdlFile = new PSDLFile();

    protected FileDialog openFileChooser = new FileDialog (this,
						 "Load PSDL file",
						 FileDialog.LOAD);
    protected FileDialog saveFileChooser = new FileDialog (this,
						 "Save PSDL file",
						 FileDialog.SAVE);


//    protected VertexListView  vertexList  = new VertexListView  ("Vertices");
//    protected TextureListView textureList = new TextureListView ("Textures");


    ProgressWindow monitor;

    public MainWindow()
    {
	super ("PSDL Viewer");

	newPSDL();
	newBAI();

	addWindowListener (this);

	addMenu();

	setContentPane (new JDesktopPane());
	Container contentPane = getContentPane();

	Rectangle r = getGraphicsConfiguration().getBounds();
	setLocation (r.x, r.y);
	setSize (r.width, r.height);
	setVisible (true);
    }



    /** Adds the main menu bar to this window.
     *
     */
    protected void addMenu()
    {
	JMenuBar  bar  = new JMenuBar();


	JMenu     menu;
	JMenuItem item;

	// File menu:
	//
	//
	menu = new JMenu ("File");
	menu.setMnemonic (KeyEvent.VK_F);

	item = new JMenuItem ("New");
        item.setActionCommand ("" + MENU_NEW);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_N);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Create a new PSDL file");
	menu.add (item);

	item = new JMenuItem ("Open");
        item.setActionCommand ("" + MENU_OPEN);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_O);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_O,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a PSDL file");
	menu.add (item);

	item = new JMenuItem ("Save");
        item.setActionCommand ("" + MENU_SAVE);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_S);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Save a PSDL file");
	menu.add (item);

	menu.addSeparator();

	item = new JMenuItem ("Open INST");
        item.setActionCommand ("" + MENU_OPEN_INST);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Open an INST file");
	menu.add (item);

	item = new JMenuItem ("Save INST");
        item.setActionCommand ("" + MENU_SAVE_INST);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Save an INST file");
	menu.add (item);

	menu.addSeparator();

	item = new JMenuItem ("Open BAI");
        item.setActionCommand ("" + MENU_OPEN_BAI);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a BAI file");
	menu.add (item);

	item = new JMenuItem ("Save BAI");
        item.setActionCommand ("" + MENU_SAVE_BAI);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Save a BAI file");
	item.setEnabled (false);
	menu.add (item);

	menu.addSeparator();

	item = new JMenuItem ("Import vertices");
        item.setActionCommand ("" + MENU_IMPORT_VERTICES);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_V);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_V,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Import vertices from text file");
	menu.add (item);

	item = new JMenuItem ("Append PSDL file");
        item.setActionCommand ("" + MENU_APPEND_PSDL);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_A);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_A,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Append an entire PSDL file to the current one");
	menu.add (item);

/* TODO: psdlML export is not yet public!!!
	item = new JMenuItem ("Export psdlML");
        item.setActionCommand ("" + MENU_EXPORT_PSDLML);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_E);
	item.getAccessibleContext().setAccessibleDescription (
				      "Export model to a psdlML file");
	menu.add (item);
*/
/*
	item = new JMenuItem ("Export VRML");
        item.setActionCommand ("" + MENU_EXPORT_VRML);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_V);
	item.getAccessibleContext().setAccessibleDescription (
				      "Export model to a VRML file");
	menu.add (item);
*/
	menu.addSeparator();

	item = new JMenuItem ("Options...");
        item.setActionCommand ("" + MENU_OPTIONS);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_P);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_P,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open options dialog");
	menu.add (item);
	menu.addSeparator();

	item = new JMenuItem ("Exit");
        item.setActionCommand ("" + MENU_EXIT);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_X);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_X,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Exit PSDL Viewer");
	menu.add (item);

	bar.add (menu);





	// View menu:
	//
	//
	menu = new JMenu ("View");
	menu.setMnemonic (KeyEvent.VK_V);

	JMenu views = new JMenu ("Views");
	views.setMnemonic (KeyEvent.VK_V);

	item = new JMenuItem ("Block list");
        item.setActionCommand ("" + MENU_BLOCK_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_B);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_B,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new block list");
	views.add (item);

	item = new JMenuItem ("Vertex list");
        item.setActionCommand ("" + MENU_VERTEX_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_V);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_V,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new vertex list");
	views.add (item);
	item = new JMenuItem ("Texture list");
        item.setActionCommand ("" + MENU_TEXTURE_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_T);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_T,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new texture list");
	views.add (item);
	item = new JMenuItem ("Height list");
        item.setActionCommand ("" + MENU_HEIGHT_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_F);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_F,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new height list");
	views.add (item);
	item = new JMenuItem ("Road list");
        item.setActionCommand ("" + MENU_ROAD_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_U);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_U,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new road list");
	views.add (item);

	item = new JMenuItem ("INST list");
        item.setActionCommand ("" + MENU_INST_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_I);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_I,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new INST list");
	views.add (item);

	item = new JMenuItem ("BAI Route list");
        item.setActionCommand ("" + MENU_ROUTE_LIST);
	item.addActionListener (this);
//	item.setMnemonic (KeyEvent.VK_I);
//	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_I,
//						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						  "Open a new BAI Route list");
	views.add (item);

	item = new JMenuItem ("Terrain view");
        item.setActionCommand ("" + MENU_TERRAIN_VIEW);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_N);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_3,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new terrain view");
	views.add (item);

	item = new JMenuItem ("Pathset list");
        item.setActionCommand ("" + MENU_PATHSET_LIST);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_P);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_P,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a new pathset list");
	views.add (item);

/*
	item = new JMenuItem ("Unknown data view");
        item.setActionCommand ("" + MENU_UNKNOWN_VIEW);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
			      "Open a new view of the unknown PSDL data");
	views.add (item);
*/
	menu.add (views);

	item = new JMenuItem ("Refresh blocks");
        item.setActionCommand ("" + MENU_REFRESH_BLOCKS);

	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_R);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_R,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Redraw all city blocks");
	menu.add (item);



	item = new JMenuItem ("Flush textures");
        item.setActionCommand ("" + MENU_FLUSH_TEXTURES);

	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_F);
	item.getAccessibleContext().setAccessibleDescription (
			   "Reloads all textures and redraws all city blocks");
	menu.add (item);

	menu.addSeparator();


	bar.add (menu);





	// Tools menu:
	//
	//
	menu = new JMenu ("Tools");
	menu.setMnemonic (KeyEvent.VK_T);

	item = new JMenuItem ("Auto neighbours");
        item.setActionCommand ("" + MENU_TOOLS_AUTO_NEIGHBOUR);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Automatically sets up all neighbour references of all block perimeters");
	menu.add (item);

	item = new JMenuItem ("Auto last attribute");
        item.setActionCommand ("" + MENU_TOOLS_AUTO_LASTATTR);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Automatically sets up the last attribute flag in all block attributes");
	menu.add (item);

	item = new JMenuItem ("Auto block type");
        item.setActionCommand ("" + MENU_TOOLS_AUTO_BLOCKTYPE);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Automatically sets up all block types based on contained block attributes");
	menu.add (item);

	item = new JMenuItem ("Auto block sort");
        item.setActionCommand ("" + MENU_TOOLS_AUTO_BLOCKSORT);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Automatically sorts all blocks according to block type");
	menu.add (item);

	item = new JMenuItem ("Auto dimension");
        item.setActionCommand ("" + MENU_TOOLS_AUTO_DIMENSIONS);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Automatically sets the min and max vectors");
	menu.add (item);

	item = new JMenuItem ("Random terrain");
        item.setActionCommand ("" + MENU_TOOLS_RANDOM_TERRAIN);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Generate a random city");
	menu.add (item);

	item = new JMenuItem ("Auto facade bounds");
        item.setActionCommand ("" + MENU_TOOLS_AUTO_FACADEBOUND);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Sets up a facade bound for every facade that doesn't have one.");
	menu.add (item);

	item = new JMenuItem ("Generate CPVS");
        item.setActionCommand ("" + MENU_TOOLS_GENERATE_CPVS);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
	    "Generates the simplest possible CPVS file from the perimeters.");
	menu.add (item);


	bar.add (menu);




	// Help menu:
	//
	//
	menu = new JMenu ("Help");
	menu.setMnemonic (KeyEvent.VK_H);

	item = new JMenuItem ("List block attributes...");
        item.setActionCommand ("" + MENU_LIST_ATTRIBUTES);
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Shows a list of all known attributes");
	menu.add (item);

	item = new JMenuItem ("About...");
        item.setActionCommand ("" + MENU_ABOUT);
	item.addActionListener (this);
	item.setMnemonic (KeyEvent.VK_A);
	item.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_A,
						     ActionEvent.ALT_MASK));
	item.getAccessibleContext().setAccessibleDescription (
						      "Shows info about this program");
	menu.add (item);

	bar.add (menu); 





	setJMenuBar (bar);
    }








    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();
	int i = Integer.parseInt (cmd);

	switch (i)
	{

	    // File menu:
	    //
	case MENU_NEW:
	    newPSDL();
	    break;

	case MENU_OPEN:
	    openFile();
	    break;

	case MENU_SAVE:
	    saveFile();
	    break;

	case MENU_OPEN_INST:
	    openINSTFile();
	    break;

	case MENU_SAVE_INST:
	    saveINSTFile();
	    break;

	case MENU_OPEN_BAI:
	    openBAIFile();
	    break;

	case MENU_SAVE_BAI:
	    saveBAIFile();
	    break;

	case MENU_OPTIONS:
	    showOptions();
	    break;

	case MENU_EXIT:
	    exit();
	    break;

	case MENU_IMPORT_VERTICES:
	    importVertices();
	    break;

	case MENU_APPEND_PSDL:
	    appendPSDL();
	    break;

	case MENU_EXPORT_PSDLML:
	    exportPSDLML();
	    break;

/*
	case MENU_EXPORT_VRML:
	    exportVRML();
	    break;
*/

	    // View menu:
	    //
	case MENU_BLOCK_LIST:
	    BlockListView blv = new BlockListView ("Blocks."
                                    + (Config.blockListViews.size() + 1));
	    blv.setList (Config.getBlockList());
	    Config.addView (blv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_VERTEX_LIST:
	    VertexListView vlv = new VertexListView ("Vertices."
                                    + (Config.vertexListViews.size() + 1));
	    vlv.setList (Config.getVertexList());
	    Config.addView (vlv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_TEXTURE_LIST:
	    TextureListView tlv = new TextureListView ("Textures."
                                    + (Config.textureListViews.size() + 1));
	    tlv.setList (Config.getTextureList());
	    Config.addView (tlv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_TERRAIN_VIEW:
	    TerrainView tv = new TerrainView ("Terrain."
                                    + (Config.terrainViews.size() + 1));
	    Config.addView (tv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_HEIGHT_LIST:
	    HeightListView hlv = new HeightListView ("Heights."
                                    + (Config.heightListViews.size() + 1));
	    hlv.setList (Config.getHeightList());
	    Config.addView (hlv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_ROAD_LIST:
	    RoadListView rlv = new RoadListView ("Roads."
                                    + (Config.roadListViews.size() + 1));
	    rlv.setList (Config.getRoadList());
	    Config.addView (rlv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_INST_LIST:
	    INSTComponentListView iblv = new INSTComponentListView ("INST."
					+ (Config.instComponentListViews.size() + 1));
	    iblv.setList (Config.instFile.getComponents());
	    Config.addView (iblv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_ROUTE_LIST:
	    RouteListView rtlv = new RouteListView ("Routes."
                                    + (Config.routeListViews.size() + 1));
	    rtlv.setList (Config.baiFile.getRoutes());
	    Config.addView (rtlv, new Rectangle (0, 0, 500, 500));
	    break;

	case MENU_PATHSET_LIST:
	    PathsetListView plv = new PathsetListView ("Pathsets."
                                    + (Config.pathsetListViews.size() + 1));
	    plv.setList (Config.getPathsetList());
	    Config.addView (plv, new Rectangle (0, 0, 200, 400));
	    break;

	case MENU_REFRESH_BLOCKS:
	    redrawTerrain();
/*
	    Config.setTerrainBlocks (Config.getVertexList(),
				     Config.getHeightList(),
				     Config.getTextureList(),
				     Config.getBlockList(),
				     null);
*/
	    break;

	case MENU_FLUSH_TEXTURES:
	    Config.getTextureList().flush();
	    Config.getTerrain().textureStore.flush();
	    redrawTerrain();
	    break;

	    // Tools menu:
	    //
	    //
	case MENU_TOOLS_AUTO_NEIGHBOUR:
	    autoNeighbour();
	    break;

	case MENU_TOOLS_AUTO_LASTATTR:
	    autoLastAttr();
	    break;

	case MENU_TOOLS_AUTO_BLOCKTYPE:
	    autoBlockType();
	    break;

	case MENU_TOOLS_AUTO_BLOCKSORT:
	    autoBlockSort();
	    break;

	case MENU_TOOLS_AUTO_DIMENSIONS:
	    autoDimension();
	    break;

	case MENU_TOOLS_RANDOM_TERRAIN:
	    randomCity();
	    break;

	case MENU_TOOLS_AUTO_FACADEBOUND:
	    autoFacadeBound();
	    break;

	case MENU_TOOLS_GENERATE_CPVS:
	    generateCPVS();
	    break;


	    // Help menu:
	    //
	case MENU_LIST_ATTRIBUTES:
	    JTextArea t = new JTextArea (BlockAttribute.getAttributeNames());
	    t.setEditable (false);
	    JScrollPane p = new JScrollPane (t);
	    p.setPreferredSize (new Dimension (256, 200));
	    JOptionPane.showMessageDialog (this,
					   p,
					   "Known block attributes",
					   JOptionPane.INFORMATION_MESSAGE);
	    break;

	case MENU_ABOUT:
	    String message = "";
	    message += "PSDL Viewer\n\n";
	    message += "Experimental analyzer/editor for PSDL files.\n";
	    message += "PSDL files define terrain, roads and simple\n";
	    message += "buildings for a city in the Microsoft game \n";
	    message += "Midtown Madness 2.\n\n";
	    message += "PSDL Viewer was created by Fredrik Bergholtz,\n";
	    message += "a.k.a. fre_ber. For more information about \n";
	    message += "Midtown Madness, check out\n";
	    message += "http://www.mm2c.com/";

	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    JOptionPane.showMessageDialog (this,
					   message,
		      "About PSDL Viewer",
		      JOptionPane.INFORMATION_MESSAGE,
		      new ImageIcon (cl.getResource ("icons/about_icon.gif")));
	    
	    break;
	}
    }




    public void itemStateChanged (ItemEvent e)
    {
	System.err.println ("MainWindow.itemStateChanged: " + e);
        //...Get information from the item event...
        //...Display it in the text area...
    }
















    // WindowListener methods:
    //
    //
    public void windowActivated (WindowEvent e)
    {
    }
    public void windowClosed (WindowEvent e)
    {
    }
    public void windowClosing (WindowEvent e)
    {
	exit();
    }
    public void windowDeactivated (WindowEvent e)
    {
    }
    public void windowDeiconified (WindowEvent e)
    {
    }
    public void windowIconified (WindowEvent e)
    {
    }
    public void windowOpened (WindowEvent e)
    {
    }









    /** Remove current PSDL and create a new one.
     *
     */
    public void newPSDL()
    {
	PSDLFile psdlFile = new PSDLFile();
	Config.setBlockList  (psdlFile.getBlocks());
	Config.setVertexList (psdlFile.getVertices());
	Config.setTextureList (psdlFile.getTextures());
	Config.setHeightList (psdlFile.getHeights());
	Config.setDimensions (psdlFile.getDimensions());
	Config.setRoadList (psdlFile.getRoads());
	Config.unknown3 = 0;

	Config.setTerrainBlocks (psdlFile.getVertices(),
				 psdlFile.getHeights(),
				 psdlFile.getTextures(),
				 psdlFile.getBlocks(),
				 null);
	Config.getTerrain().clearMarkers();
    }


    /** Remove current BAI and create a new one.
     *
     */
    public void newBAI()
    {
	Config.setBAIFile (new BAIFile());
    }






    /** Lets the user select a PSDL file to open.
     *
     */
    protected void openFile()
    {
	openFileChooser.setTitle ("Open PSDL file");
	openFileChooser.setDirectory (Config.openPath);
	openFileChooser.setVisible (true);

	String fileName = openFileChooser.getFile();
	if (fileName == null)
	{
	    return;
	}

	Config.openPath = openFileChooser.getDirectory();
	openFile (Config.openPath + fileName);
    }



    /** Lets the user select a PSDL file to open.
     *
     */
    protected void openFile (String fileName)
    {
	File file = new File (fileName);
	openFile (file);
    }




    /** Open a PSDL file.
     *
     */
    protected void openFile (File file)
    {
	try
	{
	    if (!file.exists())
	    {
		throw new FileNotFoundException (file.getAbsolutePath());
	    }



	    if (file.getName().endsWith (".xml"))
            {
		String s = (String)JOptionPane.showInputDialog(this,
                                     "Welding range (metres)",
                                     "Import psdl-ml",
				     JOptionPane.QUESTION_MESSAGE,
				     null,
				     null,
				     "0.1");
		float weldRange = Float.parseFloat (s);

		OpenPSDLMLThread t = new OpenPSDLMLThread (file.getAbsolutePath(),
							   weldRange,
							   this);
		t.start();
	    }
            else
	    {
		OpenPSDLThread t = new OpenPSDLThread (file.getAbsolutePath(),
						       this);
		t.start();
	    }

	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (this, "File not found. (" + e.getMessage() + ")");
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (this, "OpenPSDL: " + e.getMessage());
	}


    }







    /** Lets the user save a PSDL file.
     *
     */
    protected void saveFile()
    {
	saveFileChooser.setTitle ("Save PSDL file");
	saveFileChooser.setDirectory    (Config.savePath);
	saveFileChooser.setVisible (true);

	String fileName = saveFileChooser.getFile();
	if (fileName == null)
	{
	    return;
	}

	Config.savePath = saveFileChooser.getDirectory();
	SavePSDLThread t = new SavePSDLThread (Config.savePath + fileName,
					       this);
	t.start();
    }











    /** Lets the user select an INST file to open.
     *
     */
    protected void openINSTFile()
    {
	try
	{
	    openFileChooser.setTitle ("Open INST file");
	    openFileChooser.setDirectory (Config.openPath);
	    openFileChooser.setVisible (true);

	    String fileName = openFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.openPath = openFileChooser.getDirectory();
	    File file = new File (Config.openPath + fileName);

	    if (!file.exists())
	    {
		throw new FileNotFoundException (file.getAbsolutePath());
	    }

	    OpenINSTThread t = new OpenINSTThread (file.getAbsolutePath(),
						   this);
	    t.start();
	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (this, "File not found. (" + e.getMessage() + ")");
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (this, "OpenINST: " + e);
	}


    }







    /** Lets the user save an INST file.
     *
     */
    protected void saveINSTFile()
    {
	try
	{
	    saveFileChooser.setTitle ("Save INST file");
	    saveFileChooser.setDirectory    (Config.savePath);
	    saveFileChooser.setVisible (true);

	    String fileName = saveFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.savePath = saveFileChooser.getDirectory();
	    Config.instFile.write (Config.savePath + fileName);
	    JOptionPane.showMessageDialog (this,   "Wrote file \""
					         + Config.savePath + fileName
					         + "\"");
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (this, "saveINSTFile: " + e);
	}


    }




    /** Lets the user select a BAI file to open.
     *
     */
    protected void openBAIFile()
    {
	try
	{
	    openFileChooser.setTitle ("Open BAI file");
	    openFileChooser.setDirectory (Config.openPath);
	    openFileChooser.setVisible (true);

	    String fileName = openFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.openPath = openFileChooser.getDirectory();
	    File file = new File (Config.openPath + fileName);

	    if (!file.exists())
	    {
		throw new FileNotFoundException (file.getAbsolutePath());
	    }

	    OpenBAIThread t = new OpenBAIThread (file.getAbsolutePath(),
						 this);
	    t.start();
	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (this, "File not found. (" + e.getMessage() + ")");
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (this, "OpenBAI: " + e);
	}


    }







    /** Lets the user save an BAI file.
     *
     */
    protected void saveBAIFile()
    {
	try
	{
	    saveFileChooser.setTitle ("Save BAI file");
	    saveFileChooser.setDirectory    (Config.savePath);
	    saveFileChooser.setVisible (true);

	    String fileName = saveFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.savePath = saveFileChooser.getDirectory();
/*
	    Config.baiFile.write (Config.savePath + fileName);
	    JOptionPane.showMessageDialog (this,   "Wrote file \""
					         + Config.savePath + fileName
					         + "\"");
*/
	    if (false)
		throw new IOException ("apa");
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (this, "saveBAIFile: " + e);
	}


    }


	





    protected void importVertices()
    {
	try
	{
	    openFileChooser.setVisible (true);

	    String fileName = openFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.openPath = openFileChooser.getDirectory();
	    File file = new File (Config.openPath + fileName);

	    if (!file.exists())
	    {
		throw new FileNotFoundException (file.getAbsolutePath());
	    }

	    VertexList v = Config.getVertexList();
	    BufferedReader in = new BufferedReader (new FileReader (file));
	    boolean eof = false;
	    while (!eof)
	    {
		String s = in.readLine();
		if (s != null)
		{
		    StringTokenizer t = new StringTokenizer (s, ",");
		    float x = Float.parseFloat (t.nextToken());
		    float y = Float.parseFloat (t.nextToken());
		    float z = Float.parseFloat (t.nextToken());		
		    v.add (new Vertex3D (x, y, z));
		}
		else
		{
		    eof = true;
		}
	    }
	    Config.setVertexList (v);


	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (this, "File not found. (" + e.getMessage() + ")");
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (this, "importVertices: " + e);
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (this, "importVertices: " + e);
	}
    }






    /** Lets the user select a PSDL file to append.
     *
     */
    protected void appendPSDL()
    {
	try
	{
	    openFileChooser.setDirectory (Config.openPath);
	    openFileChooser.setVisible (true);

	    String fileName = openFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.openPath = openFileChooser.getDirectory();
	    File file = new File (Config.openPath + fileName);

	    if (!file.exists())
	    {
		throw new FileNotFoundException (file.getAbsolutePath());
	    }

	    PSDLFile psdlFile = new PSDLFile();
	    psdlFile.setBlocks     (Config.getBlockList());
	    psdlFile.setVertices   (Config.getVertexList());
	    psdlFile.setTextures   (Config.getTextureList());
	    psdlFile.setHeights    (Config.getHeightList());
	    psdlFile.setDimensions (Config.getDimensions());
	    psdlFile.setRoads      (Config.getRoadList());

	    psdlFile.append (file.getAbsolutePath(),
			     Config.texturePath);
	    Config.setBlockList   (psdlFile.getBlocks());
	    Config.setVertexList  (psdlFile.getVertices());
	    Config.setHeightList  (psdlFile.getHeights());
	    Config.setTextureList (psdlFile.getTextures());
	    Config.setDimensions (psdlFile.getDimensions());
	    Config.setRoadList    (psdlFile.getRoads());
	    Config.setTerrainBlocks (psdlFile.getVertices(),
				     psdlFile.getHeights(),
				     psdlFile.getTextures(),
				     psdlFile.getBlocks(),
				     monitor);
	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (this, "File not found. (" + e.getMessage() + ")");
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (this, "appendPSDL: " + e);
	}


    }






    protected void exportPSDLML()
    {
	try
	{
	    saveFileChooser.setDirectory    (Config.savePath);
	    saveFileChooser.setVisible (true);

	    String fileName = saveFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    Config.savePath = saveFileChooser.getDirectory();
	    PSDLFile psdlFile = new PSDLFile();
	    psdlFile.setBlocks     (Config.getBlockList());
	    psdlFile.setVertices   (Config.getVertexList());
	    psdlFile.setTextures   (Config.getTextureList());
	    psdlFile.setHeights    (Config.getHeightList());
	    psdlFile.setDimensions (Config.getDimensions());
	    psdlFile.setRoads      (Config.getRoadList());
	    PSDLML psdlML = new PSDLML (psdlFile);
	    psdlML.write (Config.savePath + fileName);
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (this, "exportPSDLML: " + e);
	}
    }



    protected void showOptions()
    {
	OptionDialog dialog = new OptionDialog (this); 
	dialog.setVisible (true);
    }


    /** Cleans up and exits the PSDL Viewer:
     *
     */
    protected void exit()
    {
	System.exit (0);
    }






    /** Sets up the neighbour block references of each perimeter.
     *
     */
    protected void autoNeighbour()
    {
	AutoNeighbourTask t = new AutoNeighbourTask (this);
	t.start();
    }

    /** Sets up the last attribute flag in all block attributes.
     *
     */
    protected void autoLastAttr()
    {
	AutoLastAttrTask t = new AutoLastAttrTask (this);
	t.start();
    }

    /** Sets up the block type based in the block attributes the block contains.
     *
     */
    protected void autoBlockType()
    {
	AutoBlockTypeTask t = new AutoBlockTypeTask (this);
	t.start();
    }

    /** Sorts all blocks according to block type.
     *
     */
    protected void autoBlockSort()
    {
	AutoBlockSortTask t = new AutoBlockSortTask (this);
	t.start();
    }


    protected void autoDimension()
    {
	Dimensions d = Config.getDimensions();

	d.min.x = d.min.y = d.min.z = Float.MAX_VALUE;
	d.max.x = d.max.y = d.max.z = Float.MIN_VALUE;

	VertexList vertices = Config.getVertexList();
	for (int i = 0; i < vertices.size(); i++)
	{

	    Vertex3D v = vertices.vertexAt (i);
	    if (v.x < d.min.x)
	    {
		d.min.x = v.x;
	    }
	    if (v.y < d.min.y)
	    {
		d.min.y = v.y;
	    }
	    if (v.z < d.min.z)
	    {
		d.min.z = v.z;
	    }
	    if (v.x > d.max.x)
	    {
		d.max.x = v.x;
	    }
	    if (v.y > d.max.y)
	    {
		d.max.y = v.y;
	    }
	    if (v.z > d.max.z)
	    {
		d.max.z = v.z;
	    }
	}
	d.center.x = (float)((d.min.x + d.max.x ) / 2);
	d.center.y = (float)((d.min.y + d.max.y ) / 2);
	d.center.z = (float)((d.min.z + d.max.z ) / 2);


	d.radius = (float)Math.abs (Math.sqrt (
				(d.max.x - d.center.x) * (d.max.x - d.center.x)
			      + (d.max.y - d.center.y) * (d.max.y - d.center.y)
			      + (d.max.z - d.center.z) * (d.max.z - d.center.z)
				));
    }





    protected void randomCity()
    {
	RandomCityWizard w = new RandomCityWizard (this);
	w.setVisible (true);
//	RandomCityGenerator g = new RandomCityGenerator (this);
    }









    public void moveFile (File src,
			  File dst)
    {
	try
	{
	    FileInputStream in   = new FileInputStream (src);
	    FileOutputStream out = new FileOutputStream (dst);
	    byte[] buf = new byte[in.available()];
	    in.read (buf);
	    out.write (buf);
	    out.close();
	    in.close();
	    src.delete();
	}
	catch (FileNotFoundException e)
	{
	    JOptionPane.showMessageDialog (this, "MainWindow.moveFile: File not found. (" + e.getMessage() + ")");
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog (this, "MainWindow.moveFile: " + e.getMessage());
	}
    }







// For VRML export:
    protected void exportVRML()
    {
	/*
	VRML97Saver saver = new VRML97Saver();
	saver.setBranchGroup (Config.getTerrain().psdl);
	saver.save ("apa.wrl");
	*/
    }









    // Helper classes that does stuff in separate threads:
    //
    //


    /** Class that is a thread and reads a PSDL file.
     *
     */
    protected class OpenPSDLThread
    extends Thread
    {
	String filename;
	ProgressWindow monitor;
	MainWindow mainWindow;

	public OpenPSDLThread (String filename_,
			       MainWindow mw)
	{
	    filename   = filename_;
	    mainWindow = mw;
	}

	public void run()
	{
	    try
	    {
		monitor = new ProgressWindow (mainWindow,
					      "Opening PSDL file");
		monitor.open();

		PSDLFile psdlFile = new PSDLFile (filename,
						  Config.texturePath,
						  monitor);
		Config.setBlockList  (psdlFile.getBlocks());
		Config.setVertexList (psdlFile.getVertices());
		Config.setHeightList (psdlFile.getHeights());
		Config.setTextureList (psdlFile.getTextures());
		Config.setDimensions (psdlFile.getDimensions());
		Config.setRoadList (psdlFile.getRoads());
		Config.unknown3 = psdlFile.getUnknown3();

		Config.setTerrainBlocks (psdlFile.getVertices(),
					 psdlFile.getHeights(),
					 psdlFile.getTextures(),
					 psdlFile.getBlocks(),
					 monitor);
		Config.getTerrain().clearMarkers();
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "openPSDL: " + e);
	    }

	    monitor.close();
	}
    }






    /** Class that is a thread and reads a PSDL-ML file.
     *
     */
    protected class OpenPSDLMLThread
    extends Thread
    {
	String filename;
	ProgressWindow monitor;
	MainWindow mainWindow;
	float weldRange;

	public OpenPSDLMLThread (String filename_,
				 float weldRange_,
				 MainWindow mw)
	{
	    filename   = filename_;
	    weldRange  = weldRange_;
	    mainWindow = mw;
	}

	public void run()
	{
	    try
	    {
		monitor = new ProgressWindow (mainWindow,
					      "Opening PSDL-ML file");
		monitor.open();
		PSDLML psdlML = new PSDLML (filename,
					    Config.texturePath,
					    weldRange,
					    monitor);

		PSDLFile psdlFile = psdlML.getPSDLFile();

		Config.setBlockList  (psdlFile.getBlocks());
		Config.setVertexList (psdlFile.getVertices());
		Config.setHeightList (psdlFile.getHeights());
		Config.setTextureList (psdlFile.getTextures());
		Config.setDimensions (psdlFile.getDimensions());
		Config.setRoadList (psdlFile.getRoads());
		Config.unknown3 = psdlFile.getUnknown3();

		Config.setINSTFile (psdlML.getINSTFile());

		Config.setTerrainBlocks (psdlFile.getVertices(),
					 psdlFile.getHeights(),
					 psdlFile.getTextures(),
					 psdlFile.getBlocks(),
					 monitor);
		Config.getTerrain().clearMarkers();

	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "openPSDLML: " + e);
	    }
	    monitor.close();
	}
    }






    /** Class that is a thread and saves a PSDL file.
     *
     */
    protected class SavePSDLThread
    extends Thread
    {
	String filename;
	ProgressWindow monitor;
	MainWindow mainWindow;

	public SavePSDLThread (String filename_,
			       MainWindow mw)
	{
	    filename   = filename_;
	    mainWindow = mw;
	}

	public void run()
	{
	    try
	    {
		monitor = new ProgressWindow (mainWindow,
					      "Saving PSDL file");
		monitor.open();
		File f = new File (filename + "#");

		PSDLFile psdlFile = new PSDLFile();
		psdlFile.setBlocks     (Config.getBlockList());
		psdlFile.setVertices   (Config.getVertexList());
		psdlFile.setTextures   (Config.getTextureList());
		psdlFile.setHeights    (Config.getHeightList());
		psdlFile.setDimensions (Config.getDimensions());
		psdlFile.setRoads      (Config.getRoadList());
		psdlFile.setUnknown3   (Config.unknown3);

		psdlFile.write (f, monitor);
		moveFile (f, new File (filename));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "savePSDL: " + e);
	    }

	    monitor.close();
	}
    }








    /** Class that is a thread and reads an INST file.
     *
     */
    protected class OpenINSTThread
    extends Thread
    {
	String filename;
	ProgressWindow monitor;
	MainWindow mainWindow;

	public OpenINSTThread (String filename_,
			       MainWindow mw)
	{
	    filename   = filename_;
	    mainWindow = mw;
	}

	public void run()
	{
	    try
	    {
		monitor = new ProgressWindow (mainWindow,
					      "Opening INST file");
		monitor.open();

		Config.setINSTFile (new INSTFile (filename,
						  monitor));
		Terrain t = Config.getTerrain();
		t.renderINST (Config.getVertexList(),
			      Config.getHeightList(),
			      Config.getTextureList(),
			      Config.getBlockList(),
			      monitor);
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "openINST: " + e);
	    }

	    monitor.close();
	}
    }


    /** Class that is a thread and reads a BAI file.
     *
     */
    protected class OpenBAIThread
    extends Thread
    {
	String filename;
	ProgressWindow monitor;
	MainWindow mainWindow;

	public OpenBAIThread (String filename_,
			      MainWindow mw)
	{
	    filename   = filename_;
	    mainWindow = mw;
	}

	public void run()
	{
	    try
	    {
		monitor = new ProgressWindow (mainWindow,
					      "Opening BAI file");
		monitor.open();

		Config.setBAIFile (new BAIFile (filename,
						monitor));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "openBAI: " + e);
	    }

	    monitor.close();
	}
    }








    protected class AutoNeighbourTask
    extends Thread
    {
	ProgressWindow monitor;
	MainWindow mainWindow;

	public AutoNeighbourTask (MainWindow mw)
	{
	    mainWindow = mw;
	    monitor = new ProgressWindow (mainWindow,
					  "Computing perimeters");
	    monitor.open();
	}

	public void run()
	{
	    BlockList blocks = Config.getBlockList();
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());

	    for (int b0 = 0; b0 < blocks.size(); b0++)
	    {
		monitor.setNote ("Block " + b0);
		monitor.setProgress (b0);

		Block block0 = blocks.blockAt (b0);

		PerimeterList perimeter0 = block0.getPerimeter();

		int v0;
		int lastV0 = -1;
		// Remove duplicate vertex references:
		//
		for (v0 = 0; v0 < perimeter0.size(); v0++)
		{
		    boolean first = true;

		    PerimeterVertex pv0 = perimeter0.perimeterAt (v0);
		    if (pv0.getIndex() == lastV0)
		    {
			perimeter0.remove (v0);
			v0--;
		    }
		    else
		    {
			lastV0 = pv0.getIndex();

			// Scan for neighbours:
			//
			for (int b1 = 0; b1 < blocks.size(); b1++)
			{
			    if (b0 == b1)
				continue;
			    Block block1 = blocks.blockAt (b1);
			    PerimeterList perimeter1 = block1.getPerimeter();

			    int lastV1 = -1;
			    for (int v1 = 0; v1 < perimeter1.size(); v1++)
			    {
				PerimeterVertex pv1 = perimeter1.perimeterAt (v1);

				// Skip duplicates:
				if (pv1.getIndex() == lastV1)
				{
				    continue;
				}
			    
				lastV1 = pv1.getIndex();


				if (pv1.getIndex() == lastV0)
				{
				    if (!first)
				    {
					perimeter0.add (v0++,
							new PerimeterVertex ((char)lastV0,
									     (char)b1));
				    }
				    else
				    {
					pv0.setConnectingBlock ((char)b1);
					first = false;
				    }
				}
			    }
			}
		    }
		}
	    }
	    monitor.close();
	}
    }




    protected class AutoLastAttrTask
    extends Thread
    {
	ProgressWindow monitor;
	MainWindow mainWindow;

	public AutoLastAttrTask (MainWindow mw)
	{
	    mainWindow = mw;
	    monitor = new ProgressWindow (mainWindow,
					  "Computing last attribute flags");
	    monitor.open();
	}

	public void run()
	{
	    BlockList blocks = Config.getBlockList();
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());

	    for (int b0 = 0; b0 < blocks.size(); b0++)
	    {
		monitor.setNote ("Block " + b0);
		monitor.setProgress (b0);

		Block block = blocks.blockAt (b0);

		BlockAttributeList attrlist = block.getAttributes();

		for (int a = 0; a < attrlist.size(); a++)
		{
		    BlockAttribute attribute = attrlist.attributeAt (a);
		    attribute.setLast (a == attrlist.size() - 1);
		}
	    }
	    monitor.close();
	}
    }


    protected class AutoBlockTypeTask
    extends Thread
    {
	ProgressWindow monitor;
	MainWindow mainWindow;

	public AutoBlockTypeTask (MainWindow mw)
	{
	    mainWindow = mw;
	    monitor = new ProgressWindow (mainWindow,
					  "Computing block types");
	    monitor.open();
	}

	public void run()
	{
	    BlockList blocks = Config.getBlockList();
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());

	    for (int b0 = 0; b0 < blocks.size(); b0++)
	    {
		monitor.setNote ("Block " + b0);
		monitor.setProgress (b0);

		Block block = blocks.blockAt (b0);


		boolean road = false;
		boolean crossing = false;

		BlockAttributeList attrlist = block.getAttributes();
		for (int a = 0; a < attrlist.size(); a++)
		{
		    BlockAttribute attribute = attrlist.attributeAt (a);
		    switch ((int)attribute.getID())
		    {
		    case 0x0000:
		    case 0x0002:
		    case 0x0003:
		    case 0x0004:
		    case 0x0005:
		    case 0x0006:
		    case 0x0007:
		    case 0x0010:
		    case 0x0012:
		    case 0x0013:
		    case 0x0014:
		    case 0x0015:
		    case 0x0016:
		    case 0x0017:
		    case 0x0040:
		    case 0x0042:
		    case 0x0043:
		    case 0x0044:
		    case 0x0045:
		    case 0x0046:
		    case 0x0047:
			road = true;
			break;
		    case 0x0008: //Sidewalk strips
		    case 0x000a:
		    case 0x000b:
		    case 0x000c:
		    case 0x000d:
		    case 0x000e:
		    case 0x000f:
		    case 0x0024: //Crosswalk rectangle
		    case 0x0028: //Road triangle fans
		    case 0x0029:
		    case 0x002a:
		    case 0x002b:
		    case 0x002c:
		    case 0x002d:
		    case 0x002e:
		    case 0x002f:

			crossing = true;
			break;
		    }
		}
		block.setType (Block.TYPE_ROAD, road);
		block.setType (Block.TYPE_CROSSING, crossing);
	    }
	    monitor.close();
	}
    }




    protected class AutoBlockSortTask
    extends Thread
    {
	ProgressWindow monitor;
	MainWindow mainWindow;

	public AutoBlockSortTask (MainWindow mw)
	{
	    mainWindow = mw;
	    monitor = new ProgressWindow (mainWindow,
					  "Sorting blocks");
	    monitor.open();
	}

	public void run()
	{
	    int nextPlain        = 0;
	    int nextRoad         = 0;
	    int nextIntersection = 0;

	    BlockList blocks = Config.getBlockList();
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());

	    for (int index = 0; index < blocks.size(); index++)
	    {
		monitor.setNote ("Block " + index);
		monitor.setProgress (index);

		Block block = blocks.blockAt (index);

		if (block.isType (Block.TYPE_CROSSING))
		{
		    if (index != nextIntersection)
		    {
			blocks.move (index, nextIntersection, true);
		    }
		    nextIntersection++;
		}
		else if (block.isType (Block.TYPE_ROAD))
		{
		    if (index != nextRoad)
		    {
			blocks.move (index, nextRoad, true);
		    }
		    nextRoad++;
		    nextIntersection++;
		}
		else if (block.isType (Block.TYPE_PLAIN))
		{
		    if (index != nextPlain)
		    {
			blocks.move (index, nextPlain, true);
		    }
		    nextPlain++;
		    nextRoad++;
		    nextIntersection++;
		}
	    }
	    monitor.close();
	}
    }









    /** Redraw terrain
     *
     */
    protected void redrawTerrain()
    {
	RedrawTerrainThread t = new RedrawTerrainThread (this);
	t.start();
    }

    /** Class that is a thread and redraws the terrain.
     *
     */
    protected class RedrawTerrainThread
    extends Thread
    {
	ProgressWindow monitor;
	MainWindow mainWindow;

	public RedrawTerrainThread (MainWindow mw)
	{
	    mainWindow = mw;
	}

	public void run()
	{
	    try
	    {
		monitor = new ProgressWindow (mainWindow,
					      "Rendering terrain");
		monitor.open();

		Config.setTerrainBlocks (Config.getVertexList(),
					 Config.getHeightList(),
					 Config.getTextureList(),
					 Config.getBlockList(),
					 monitor);
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "redrawTerrain: " + e);
	    }

	    monitor.close();
	}
    }





    /** Creates a facade bound attribute for facades that doesn't have one.
     *
     */
    protected void autoFacadeBound()
    {
	AutoFacadeBoundTask t = new AutoFacadeBoundTask (this);
	t.start();
    }

    protected class AutoFacadeBoundTask
    extends Thread
    {
	ProgressWindow monitor;
	MainWindow mainWindow;

	class Facade
	{
	    VertexRef p0;
	    VertexRef p1;
	    HeightRef h0;
	    HeightRef h1;
	    BlockAttribute firstAttr;
	    boolean hasBound;
	};

	public AutoFacadeBoundTask (MainWindow mw)
	{
	    mainWindow = mw;
	    monitor = new ProgressWindow (mainWindow,
					  "Computing facade bounds");
	    monitor.open();
	}

	protected Facade addFacade(VertexRef p0,
				   VertexRef p1,
				   HeightRef h0,
				   HeightRef h1,
				   BlockAttribute attr,
				   Vector v)
	{
	    boolean found = false;
	    HeightList hlist = Config.getHeightList();
	    Facade f = null;
	    Height top = hlist.getHeight (h1);
	    int i = 0;
	    while (i < v.size() && !found)
	    {
		f = (Facade)v.elementAt(i);
		if (   p0.getIndex() == f.p0.getIndex()
		    && p1.getIndex() == f.p1.getIndex())
		{
		    Height fTop = hlist.getHeight (f.h1);
		    if (top.getHeight() > fTop.getHeight())
		    {
			f.h1 = h1;
		    }
		    found = true;
		}
		else
		{
		    i++;
		}
	    }
	    if (!found)
	    {
		f = new Facade();
		f.p0 = p0;
		f.p1 = p1;
		f.h0 = h0;
		f.h1 = h1;
		f.firstAttr = attr;
		v.add (f);
	    }
	    return f;
	}

	public void run()
	{
	    BlockList blocks = Config.getBlockList();
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());

	    VertexList vList = Config.getVertexList();
	    VertexRefList refs;

	    Vector facades = new Vector();

	    for (int b0 = 0; b0 < blocks.size(); b0++)
	    {
		monitor.setNote ("Block " + b0);
		monitor.setProgress (b0);

		Block block = blocks.blockAt (b0);

		facades.clear();

		Facade f;

		BlockAttributeList attrlist = block.getAttributes();
		for (int a = 0; a < attrlist.size(); a++)
		{
		    BlockAttribute attribute = attrlist.attributeAt (a);
		    switch ((int)attribute.getID())
		    {
		    case 0x003c:
			Attribute003c a3c = (Attribute003c)attribute;
			refs = a3c.getVertexRefs();
			f = addFacade (refs.referenceAt(0),
				       refs.referenceAt(1),
				       null,
				       a3c.top,
				       attribute,
				       facades);
			f.hasBound = true;
			break;

		    case 0x001c:
			Attribute001c a1c = (Attribute001c)attribute;
			refs = a1c.getVertexRefs();
			f = addFacade (refs.referenceAt(0),
				       refs.referenceAt(1),
				       null,
				       a1c.top,
				       attribute,
				       facades);
			break;

		    case 0x005e:
			Attribute005e a5e = (Attribute005e)attribute;
			refs = a5e.getVertexRefs();
			f = addFacade (refs.referenceAt(0),
				       refs.referenceAt(1),
				       a5e.bottom,
				       a5e.top,
				       attribute,
				       facades);
			break;
		    }
		}
		for (int i = 0; i < facades.size(); i++)
		{
		    f = (Facade)facades.elementAt (i);
		    if (!f.hasBound)
		    {
			Attribute003c bound = new Attribute003c();
			Vertex3D v0;
			Vertex3D v1;
			refs = bound.getVertexRefs();
			VertexRef p = new VertexRef (f.p0.getIndex());
			v0 = vList.getVertex (p);
			refs.add (p);
			p = new VertexRef (f.p1.getIndex());
			v1 = vList.getVertex (p);
			refs.add (p);
			bound.top = new HeightRef (f.h1.getIndex());
			// TODO: Compute shade.

			attrlist.add (attrlist.indexOf (f.firstAttr), bound);
		    }
		}
	    }

	    monitor.close();
	}
    }







    /** Generates a CPVS file simply based on the connected blocks according
     *  to the perimeter.
     *
     */
    protected void generateCPVS()
    {
	String s = (String)JOptionPane.showInputDialog(this,
				       "Maximum radial distance (metres)",
				       "Generate CPVS",
				       JOptionPane.QUESTION_MESSAGE,
				       null,
				       null,
				       "200");
	float radius = Float.parseFloat (s);

	saveFileChooser.setTitle ("Save CPVS file");
	saveFileChooser.setDirectory    (Config.savePath);
	saveFileChooser.setVisible (true);

	String fileName = saveFileChooser.getFile();
	if (fileName == null)
	{
	    return;
	}

	Config.savePath = saveFileChooser.getDirectory();

	GenerateCPVSTask t = new GenerateCPVSTask (radius,
						   Config.savePath + fileName,
						   this);
	t.start();
    }

    protected class GenerateCPVSTask
    extends Thread
    {
	ProgressWindow monitor;
	float      radius;
	String     filename;
	MainWindow mainWindow;

	class Set
	{
	    public boolean visibleBlocks[];
	    public byte    buf[];
	    public int     length; // Number of bytes used of the buffer.

	    public Set (int n)
	    {
		visibleBlocks = new boolean[n];
		buf = new byte[n]; //[(int)Math.ceil (n / 4)];
	    }

	    /** Computes the encoded buffer and returns the actual number of
	     *  bytes used.
	     *
	     */
	    public int computeBuf()
	    {
		int shift = 2; // Shift position for the two bit status
		int bufPos = 0;

		int start = bufPos;
		int len   = 0;
		buf[bufPos++] = (byte)0x80; // The length will be filled in afterwards
		
		for (int i = 0; i < visibleBlocks.length; i++)
		{
		    if (visibleBlocks[i])
		    {
			buf[bufPos] += 0x03 << shift;
		    }
		    shift += 2;
		    if (shift == 8)
		    {
			bufPos++;
			shift = 0;
			len++;
		    }
		    if (len == 128)
		    {
			buf[start] += len - 1;
			start = bufPos;
			buf[bufPos++] = (byte)0x80;
			len = 0;
		    }
		}
		// Make sure that the last byte of the buffer is included:
		//
		if (shift != 0)
		{
		    len++;
		    bufPos++;
		}

		// Write any outstanding length indicator:
		//
		if (len != 0)
		{
		    buf[start] += len - 1;
		}
		length = bufPos;
		return bufPos;
	    }

	}

	public GenerateCPVSTask (float radius_,
				 String filename_,
				 MainWindow mw)
	{
	    radius = radius_;
	    filename = filename_;
	    mainWindow = mw;


	    monitor = new ProgressWindow (mainWindow,
					  "Generating CPVS file");
	    monitor.open();
	}


	protected final byte[] PVS0 = {0x50, 0x56, 0x53, 0x30};


	public void run()
	{
	    BlockList blocks = Config.getBlockList();
	    monitor.setMinimum (0);
	    monitor.setMaximum (blocks.size());

	    Vector sets = new Vector();

	    for (int b0 = 0; b0 < blocks.size(); b0++)
	    {
		monitor.setNote ("Block " + b0);
		monitor.setProgress (b0);

		Block block0 = blocks.blockAt (b0);
		Point3f midPoint = new Point3f();
		PerimeterList perimeter = block0.getPerimeter();
		int n = perimeter.size();
		for (int v = 0; v < n; v++)
		{
		    PerimeterVertex pv = perimeter.perimeterAt (v);
		    int i = (int)pv.getIndex();
		    Vertex3D v3d = Config.getVertexList().vertexAt (i);
		    midPoint.x += v3d.x;
		    midPoint.y += v3d.y;
		    midPoint.z += v3d.z;
		}
		midPoint.x /= n;
		midPoint.y /= n;
		midPoint.z /= n;



		Set set = new Set (blocks.size());
		sets.add (set);

		set.visibleBlocks[b0] = true;


		for (int b1 = 0; b1 < blocks.size(); b1++)
		{
		    Block block1 = blocks.blockAt (b1);
		    perimeter = block1.getPerimeter();
		    int v = 0;
		    while (v < perimeter.size() && !set.visibleBlocks[b1])
		    {
			PerimeterVertex pv = perimeter.perimeterAt (v);
			int i = (int)pv.getIndex();
			Point3f point = Config.getVertexList().vertexAt (i).toPoint3f();
			if (point.distance (midPoint) < radius)
			{
			    set.visibleBlocks[b1] = true;
			}
			v++;
		    }
		}
	    }

	    monitor.setNote ("Writing file");
	    try
	    {
		File f = new File (filename + "#");
		FileOutputStream out = new FileOutputStream (f);
		out.write (PVS0);
		ULong.write (blocks.size() + 2, out);

		//Write offsets:
		int offset = 0;
		for (int s = 0; s < sets.size(); s++)
		{
		    ULong.write (offset, out);
		    Set set = (Set)sets.elementAt (s);
		    offset += set.computeBuf();
		}
		ULong.write (offset, out);

		//Write sets:
		for (int s = 0; s < sets.size(); s++)
		{
		    Set set = (Set)sets.elementAt (s);
		    out.write (set.buf, 0, set.length);
		}
		out.close();
		moveFile (f, new File (filename));
	    }
	    catch (Exception e)
	    {
		JOptionPane.showMessageDialog (mainWindow, "generateCPVS: " + e);
		System.err.println (e);
		e.printStackTrace();
	    }





	    monitor.close();
	}
    }


}
