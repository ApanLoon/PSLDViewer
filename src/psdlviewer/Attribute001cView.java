package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single Attribute001c block attribute.
 *
 */

import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Attribute001cView
extends BlockAttributeView
implements ActionListener
{
    Attribute001c a;

    protected JTextField top     = new JTextField();
    protected JTextField textureScale = new JTextField();
    protected VertexRefListView vertices = new VertexRefListView ("Vertices");

    public Attribute001cView (BlockAttribute a_)
    {
	super (a_, false);
	a = (Attribute001c)a_;

	// Top
	panel.add (new JLabel ("Top height: "), leftC);
	top.setText ("" + a.top);
	top.addActionListener (this);
	panel.add (top, rightC);

	// Texture scale:
	panel.add (new JLabel ("Texture scale: "), leftC);
	textureScale.setText ("" + a.textureScale);
	textureScale.addActionListener (this);
	panel.add (textureScale, rightC);

	// Vertices:
	vertices.setList (a.getVertexRefs());
	panel.add (vertices, fillC);
    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == textureScale)
	{
	    a.textureScale = new HeightRef ((char)Integer.parseInt (textureScale.getText(), 16));
	}
	else if (src == top)
	{
	    a.top = new HeightRef ((char)Integer.parseInt (top.getText(), 16));
	}
	else
	{
	    super.actionPerformed (e);
	}
	((ListView)panel.getParent().getParent().getParent()).dataChanged();
    }
}

