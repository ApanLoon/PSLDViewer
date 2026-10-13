package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single Attribute003c facade bound block attribute.
 *
 */

import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Attribute003cView
extends BlockAttributeView
implements ActionListener
{
    Attribute003c a;

    protected JTextField shade   = new JTextField();
    protected JTextField top      = new JTextField();
    protected VertexRefListView vertices = new VertexRefListView ("Vertices");

    public Attribute003cView (BlockAttribute a_)
    {
	super (a_, false);
	a = (Attribute003c)a_;

	// Shade:
	panel.add (new JLabel ("Unknown0: "), leftC);
	shade.setText ("" + a.shade);
	shade.addActionListener (this);
	panel.add (shade, rightC);

	// top:
	panel.add (new JLabel ("Top height: "), leftC);
	top.setText ("" + a.top);
	top.addActionListener (this);
	panel.add (top, rightC);

	// Vertices:
	vertices.setList (a.getVertexRefs());
	panel.add (vertices, fillC);
    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == shade)
	{
	    a.shade = Integer.parseInt (shade.getText(), 16);
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

