package psdlviewer;
import psdlviewer.*;

/** Internal frame holding a single listView component.
 *
 *
 */

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

public class ListViewFrame
extends JInternalFrame
implements MouseListener, InternalFrameListener
{
    protected ListView view;


    public ListViewFrame (ListView v)
    {
	view = v;

	getContentPane().add (v);

	setIconifiable (true);
	setClosable (true);
	setMaximizable (true);
	setResizable (true);

	// The glass pane makes the mouse pointer changes above the dividers
	// disappear, but the window activation works much better. Still no
	// events are raised during a drag'n drop though.
//	Config.mainWindow.getGlassPane().setVisible (true);
//	getGlassPane().addMouseListener (this);

	addMouseListener (this);
	addInternalFrameListener (this);

	pack();
	setVisible (true);
    }


    public ListView getView()
    {
	return view;
    }


    public String toString()
    {
	return getTitle();
    }




    // MouseListener methods:
    //
    //
    public void mouseClicked (MouseEvent e){}
    public void mouseEntered (MouseEvent e)
    {
	try
	{
	    setSelected (true);
	}
	catch (PropertyVetoException x)
	{
	}
    }
    public void mouseExited  (MouseEvent e)
    {
    }
    public void mousePressed (MouseEvent e){}
    public void mouseReleased (MouseEvent e){}




    public void internalFrameActivated(InternalFrameEvent e){}
    public void internalFrameClosed(InternalFrameEvent e) 
    {
	closeWindow();
    }
    public void internalFrameClosing(InternalFrameEvent e) 
    {
	closeWindow();
    }
    public void internalFrameDeactivated(InternalFrameEvent e){}
    public void internalFrameDeiconified(InternalFrameEvent e){}
    public void internalFrameIconified(InternalFrameEvent e){}
    public void internalFrameOpened(InternalFrameEvent e){}

    protected void closeWindow()
    {
	Config.removeListView (this);
    }


}
