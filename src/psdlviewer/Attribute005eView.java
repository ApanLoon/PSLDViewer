package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single 0x005e block attribute.
 *
 */

import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Attribute005eView
extends BlockAttributeView
implements ActionListener
{
    Attribute005e a;

    protected JTextField bottom  = new JTextField();
    protected JTextField top     = new JTextField();
    protected JTextField xRepeat = new JTextField();
    protected JTextField yRepeat = new JTextField();
    protected VertexRefListView vertices = new VertexRefListView ("Vertices");

    public Attribute005eView (BlockAttribute a_)
    {
	super (a_, false);
	a = (Attribute005e)a_;

	// Bottom:
	panel.add (new JLabel ("Bottom height: "), leftC);
	bottom.setText ("" + a.bottom);
	bottom.addActionListener (this);
	panel.add (bottom, rightC);

	// Top:
	panel.add (new JLabel ("Top height: "), leftC);
	top.setText ("" + a.top);
	top.addActionListener (this);
	panel.add (top, rightC);

	// xRepeat:
	panel.add (new JLabel ("Repeat texture x: "), leftC);
	xRepeat.setText ("" + (int)a.xRepeat);
	xRepeat.addActionListener (this);
	panel.add (xRepeat, rightC);

	// yRepeat:
	panel.add (new JLabel ("Repeat texture y: "), leftC);
	yRepeat.setText ("" + (int)a.yRepeat);
	yRepeat.addActionListener (this);
	panel.add (yRepeat, rightC);

	// Vertices:
	vertices.setList (a.getData());
	panel.add (vertices, fillC);
    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == bottom)
	{
	    a.bottom = new HeightRef ((char)Integer.parseInt (bottom.getText(), 16));
	}
	else if (src == top)
	{
	    a.top = new HeightRef ((char)Integer.parseInt (top.getText(), 16));
	}
	else if (src == xRepeat)
	{
	    a.xRepeat = (char)Integer.parseInt (xRepeat.getText());
	}
	else if (src == yRepeat)
	{
	    a.yRepeat = (char)Integer.parseInt (yRepeat.getText());
	}
	else
	{
	    super.actionPerformed (e);
	}
	((ListView)panel.getParent().getParent().getParent()).dataChanged();
    }
}

