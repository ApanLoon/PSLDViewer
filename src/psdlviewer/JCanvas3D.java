package psdlviewer;
import psdlviewer.*;

/** JPanel containing a Canvas3D object.
 *
 */


import java.awt.GraphicsConfiguration;
import javax.media.j3d.Canvas3D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.AbstractBorder;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

public class JCanvas3D
extends JPanel
{
    protected Canvas3D canvas;

    public JCanvas3D (GraphicsConfiguration gc)
    {
	canvas = new Canvas3D (gc);
	add (canvas);
    }


    public Canvas3D getCanvas3D()
    {
	return canvas;
    }




    /*
    public void reshape (int x, int y, int w, int h)
    {
	super.reshape (x, y, w, h);
	System.err.println ("reshape: " + x + ", " + y + ", " + w + ", " + h);

	Border b = getBorder();
	if (b == null)
	{
	    b = BorderFactory.createEmptyBorder();
	}
	AbstractBorder ab = (AbstractBorder)b;
	Rectangle r = ab.getInteriorRectangle (this, x, y, w, h);


	// Move and resize canvas:
	//
	//
	canvas.setLocation (r.getLocation());
	canvas.setSize (r.getSize());
    }
*/

    public void setBounds (int x, int y, int w, int h)
    {
	super.setBounds (x, y, w, h);
//	System.err.println ("setBounds: " + x + ", " + y + ", " + w + ", " + h);
//	System.err.println ("setBounds: " + getLocation() + ", " + getSize());


	Border b = getBorder();
	if (b == null)
	{
	    b = BorderFactory.createEmptyBorder();
	}
	AbstractBorder ab = (AbstractBorder)b;
	Rectangle r = ab.getInteriorRectangle (this, x, y, w, h);


	// Move and resize canvas:
	//
	//
	canvas.setLocation (r.getLocation());
	canvas.setSize (r.getSize());
    }



}


