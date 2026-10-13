package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single 0x006x roof attribute.
 *
 */

import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Attribute0060View
extends BlockAttributeView
implements ActionListener
{
    Attribute0060 attribute;

    protected JTextField        height   = new JTextField();
    protected VertexRefListView vertices = new VertexRefListView ("Vertices");

    public Attribute0060View (BlockAttribute a_)
    {
	super (a_, false);
	attribute = (Attribute0060)a_;

	// Height:
	panel.add (new JLabel ("Height: "), leftC);
	height.setText ("" + attribute.height);
	height.addActionListener (this);
//	height.setMinimumSize (new Dimension (50, 10));
	panel.add (height, rightC);

	vertices.setList (attribute.getData());
	panel.add (vertices, fillC);
    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == height)
	{
	    attribute.height = new HeightRef ((char)Integer.parseInt (height.getText(), 16));
	}
	else
	{
	    super.actionPerformed (e);
	}
	((ListView)panel.getParent().getParent().getParent()).dataChanged();
    }
}

