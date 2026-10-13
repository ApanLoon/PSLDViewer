package psdlviewer;
import psdlviewer.*;

/** Component displaying a 3D view of the blocks.
 *
 */

import java.lang.ClassLoader;

import java.util.Vector;

import java.awt.Dialog;

import java.awt.Panel;
import java.awt.Label;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;

import java.awt.Dimension;

import java.awt.GridLayout;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsConfigTemplate3D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;

import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ColoringAttributes;

import javax.media.j3d.Material;

import javax.media.j3d.BoundingBox;

import javax.vecmath.Point3f;



import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class TerrainView
extends Dialog
implements ActionListener, WindowListener
{

    protected static final float ROTATION_FACTOR  = 0.01f;
    protected static final float ZOOM_FACTOR      = 0.01f;
    protected static final float TRANSLATE_FACTOR = 0.5f;

    // Options:
    //
    //

    /** Name of this terrain view.
     *
     */
    protected String viewName;

    /** True when this view is visible.
     *
     */
    protected boolean visible = true;





    // Other variables:
    //
    //

    protected Camera camera;

    protected Label   title;
    protected Canvas3D canvas;
    protected JPanel   control = new JPanel();

    protected JButton front;
    protected JButton side;
    protected JButton top;







    /** Creates a new terrain view with the given name.
     *
     */
    public TerrainView (String name)
    {
	super (Config.mainWindow);
	viewName = name;
	initialize();

    }


    /** Creates a new terrain view with options defined by an XML element.
     *
     */
    public TerrainView (Element e)
    {
	super (Config.mainWindow);
	setOptions (e);
	initialize();
    }





    protected void initialize()
    {
	GraphicsConfigTemplate3D gct = new GraphicsConfigTemplate3D();
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice device = env.getDefaultScreenDevice();
	GraphicsConfiguration gc = device.getBestConfiguration (gct);
	canvas = new Canvas3D (gc);
	canvas.setSize (new Dimension (200, 200));

	camera = new Camera (canvas);
	Config.getTerrain().addCamera (camera);

	setTitle (viewName);

	addWindowListener (this);

	// Control panel:
	//
	//
	ClassLoader cl = ClassLoader.getSystemClassLoader();

	JButton button;
	Dimension d = new Dimension (32 + 10, 23 + 10);
	control.setLayout (new BoxLayout (control, BoxLayout.Y_AXIS));

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/rotate.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	RotationListener rl = new RotationListener (camera);
	button.addMouseListener (rl);
	button.addMouseMotionListener (rl);
	control.add (button);

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/zoom.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	ZoomListener zl = new ZoomListener (camera);
	button.addMouseListener (zl);
	button.addMouseMotionListener (zl);
	control.add (button);

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/pan.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	PanListener pl = new PanListener (camera);
	button.addMouseListener (pl);
	button.addMouseMotionListener (pl);
	control.add (button);

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/distance.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	DistanceListener dl = new DistanceListener (camera);
	button.addMouseListener (dl);
	button.addMouseMotionListener (dl);
	control.add (button);

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/front.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	button.setActionCommand ("front");
	button.addActionListener (this);
	control.add (button);

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/side.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	button.setActionCommand ("side");
	button.addActionListener (this);
	control.add (button);

	button = new JButton (new ImageIcon (
				       cl.getResource ("icons/top.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	button.setActionCommand ("top");
	button.addActionListener (this);
	control.add (button);

	button = new JButton (new ImageIcon (
			       cl.getResource ("icons/origo.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	button.setActionCommand ("origo");
	button.addActionListener (this);
	control.add (button);

	button = new JButton (new ImageIcon (
			       cl.getResource ("icons/autofocus.png")));
	button.setAlignmentX (0.5f);
	button.setMinimumSize (d);
	button.setPreferredSize (d);
	button.setMaximumSize (d);
	button.setActionCommand ("autofocus");
	button.addActionListener (this);
	control.add (button);

	setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	add (control);
	add (canvas);

// TODO: How to add minimize and maximize icons?
//	setIconifiable (true);
//	setClosable (true);
//	setMaximizable (true);
	setResizable (true);
	pack();
//	((java.awt.Window)this).setVisible (true);
	setVisible(true); // TODO: This does not open the window!!
	show(); // TODO: This is deprecated!
    }






    /** @return true if this view is visible.
     *
     */
    public boolean getVisible()
    {
	return visible;
    }

    /** Sets the visibility of this view.
     *
     * TODO: This actually does nothing...
     *
     */
    public void setVisible (boolean b)
    {
	visible = b;
	// TODO: Perhaps this one should open/close an AWT window for this
	//       view...
    }




    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("front".equals (cmd))
	{
	    camera.setOrientation (0, 0, 0);
	}
	else if ("side".equals (cmd))
	{
	    camera.setOrientation (0, (float)Math.PI/2, 0);
	}
	else if ("top".equals (cmd))
	{
	    camera.setOrientation ((float)-Math.PI/2, 0, 0);
	}
	else if ("origo".equals (cmd))
	{
	    camera.setPosition (0, 0, 0);
	}
	else if ("autofocus".equals (cmd))
	{
	    autofocus();
	}
    }


    protected void autofocus()
    {
	Point3f midPoint = new Point3f();
	Object[] vl = Config.getSelectedVertices();
	if (vl == null)
	{
	    return;
	}

	int n = vl.length;
	for (int i = 0; i < n; i++)
	{
	    Vertex3D v = (Vertex3D)vl[i];
	    midPoint.x += v.x;
	    midPoint.y += v.y;
	    midPoint.z += v.z;
	}
	midPoint.x /= n;
	midPoint.y /= n;
	midPoint.z /= n;
	camera.setPosition (midPoint.x, midPoint.y, midPoint.z);

	// TODO: Should be computed from the maximum distance in X or Y of
	//       the projected vertices.
	//
	float distance = 100f;

	camera.translate (0f,
			  0f,
			  distance);
    }





    // Window listener methods:
    //
    //
    public void windowClosed(WindowEvent e)
    {
	Config.getTerrain().removeCamera (camera);
	Config.removeTerrainView (this);
	((java.awt.Window)this).setVisible (false);
    }

    public void windowClosing(WindowEvent e)
    {
	Config.getTerrain().removeCamera (camera);
	Config.removeTerrainView (this);
	((java.awt.Window)this).setVisible (false);
    }

    public void windowActivated (WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowOpened(WindowEvent e){}







    protected class RotationListener
    implements MouseMotionListener, MouseListener
    {
	protected Camera camera;

	protected int lastX;
	protected int lastY;

	public RotationListener (Camera c)
	{
	    camera = c;
	}

	public void mouseDragged (MouseEvent e)
	{
	    int x = e.getY();
	    int y = e.getX();
	    camera.rotate ((lastX - x) * ROTATION_FACTOR,
			   (lastY - y) * ROTATION_FACTOR,
			   0);
	    lastX = x;
	    lastY = y;
	}

	public void mouseMoved (MouseEvent e){}

	public void mouseClicked (MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseExited  (MouseEvent e){}
	public void mousePressed (MouseEvent e)
	{
	    lastX = e.getY();
	    lastY = e.getX();
	}
	public void mouseReleased (MouseEvent e){}

    }


    protected class ZoomListener
    implements MouseMotionListener, MouseListener
    {
	protected Camera camera;

	protected int lastY;

	public ZoomListener (Camera c)
	{
	    camera = c;
	}

	public void mouseDragged(MouseEvent e)
	{
	    int y = e.getY();
	    camera.zoom (-(lastY - y) * ZOOM_FACTOR);
	    lastY = y;
	}

	public void mouseMoved (MouseEvent e){}

	public void mouseClicked (MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseExited  (MouseEvent e){}
	public void mousePressed (MouseEvent e)
	{
	    lastY = e.getY();
	}
	public void mouseReleased (MouseEvent e){}
    }




    protected class PanListener
    implements MouseMotionListener, MouseListener
    {
	protected Camera camera;

	protected int lastX;
	protected int lastY;

	public PanListener (Camera c)
	{
	    camera = c;
	}

	public void mouseDragged(MouseEvent e)
	{
	    int x = e.getX();
	    int y = e.getY();
	    camera.translate ( (lastX - x) * TRANSLATE_FACTOR,
			      -(lastY - y) * TRANSLATE_FACTOR,
			       0);
	    lastX = x;
	    lastY = y;
	}

	public void mouseMoved (MouseEvent e){}

	public void mouseClicked (MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseExited  (MouseEvent e){}
	public void mousePressed (MouseEvent e)
	{
	    lastX = e.getX();
	    lastY = e.getY();
	}
	public void mouseReleased (MouseEvent e){}
    }




    protected class DistanceListener
    implements MouseMotionListener, MouseListener
    {
	protected Camera camera;

	protected int lastY;

	public DistanceListener (Camera c)
	{
	    camera = c;
	}

	public void mouseDragged(MouseEvent e)
	{
	    int y = e.getY();
	    camera.translate (0,
			      0,
			      -(lastY - y) * TRANSLATE_FACTOR);
	    lastY = y;
	}

	public void mouseMoved (MouseEvent e){}

	public void mouseClicked (MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseExited  (MouseEvent e){}
	public void mousePressed (MouseEvent e)
	{
	    lastY = e.getY();
	}
	public void mouseReleased (MouseEvent e){}
    }


















    /** Sets the options for this terrain view reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	viewName = root.getAttribute ("NAME");
    }




    /** @return a DOM element containing the options for this terrain view.
     *
     */
    public Element getOptions (Document doc)
    {
	Element root = doc.createElement ("TERRAIN_VIEW");
	root.setAttribute ("NAME", viewName);
	return root;
    }



    public String toString()
    {
	return getTitle();
    }

}
