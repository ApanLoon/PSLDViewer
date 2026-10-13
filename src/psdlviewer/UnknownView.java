package psdlviewer;
import psdlviewer.*;

/** Component displaying an editable hex dump of data.
 *
 */

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import javax.swing.BoxLayout;

import javax.swing.event.ListSelectionEvent;

import java.beans.PropertyVetoException;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;

import java.awt.Component;
import java.awt.Dimension;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class UnknownView
extends JInternalFrame
implements MouseListener, InternalFrameListener
{

    protected byte[] data;


    protected HexArea hexArea = new HexArea();



    public void setData (byte[] v)
    {
	data = v;
	hexArea.setData (v);
    }



    public UnknownView (String name)
    {
	initialize();
	setTitle (name);
    }

    public UnknownView (Element root)
    {
	initialize();
	setOptions (root);
    }


    protected void initialize()
    {
	getContentPane().add (new JScrollPane (hexArea));

	setIconifiable (true);
	setClosable (true);
	setMaximizable (true);
	setResizable (true);

	pack();
	setVisible (true);

	addMouseListener (this);
	addInternalFrameListener (this);
    }





    /** Sets the options for this view reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	setTitle (root.getAttribute ("NAME"));
    }

    /** @return a DOM element containing the options for this view.
     *
     */
    public Element getOptions (Document doc)
    {
	Element root = doc.createElement ("UNKNOWN_VIEW");
	root.setAttribute ("NAME", getTitle());

	return root;
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
    public void mouseExited  (MouseEvent e){}
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
	Config.removeUnknownView (this);
    }


}

