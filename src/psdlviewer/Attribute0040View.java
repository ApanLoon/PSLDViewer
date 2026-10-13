package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single 0x0030-36 block attribute.
 *
 */

import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class Attribute0040View
extends BlockAttributeView
implements ActionListener
{
    Attribute0040 a;

    protected static String[] typeOptions    = {"Invisible", "Flat", "Flat, elevated", "Wedge"};

    protected JTextField tRef             = new JTextField();

    protected JComboBox  type             = new JComboBox (typeOptions);
    protected JCheckBox  unknown0         = new JCheckBox();
    protected JCheckBox  unknown1         = new JCheckBox();
    protected JCheckBox  unknown2         = new JCheckBox();
    protected JCheckBox  unknown3         = new JCheckBox();
    protected JCheckBox  closeStart       = new JCheckBox();
    protected JCheckBox  closeEnd         = new JCheckBox();
    protected JTextField height           = new JTextField();
    protected VertexRefListView vertices  = new VertexRefListView ("Vertices");

    public Attribute0040View (BlockAttribute a_)
    {
	super (a_, false);
	a = (Attribute0040)a_;

	tRef.addActionListener (this);
	type.addActionListener (this);
	unknown0.addActionListener (this);
	unknown1.addActionListener (this);
	unknown2.addActionListener (this);
	unknown3.addActionListener (this);
	closeStart.addActionListener (this);
	closeEnd.addActionListener (this);
	height.addActionListener (this);

	tRef.setText ("" + a.getTextureMap().toString());

	type.setSelectedIndex  (a.getType());
	unknown0.setSelected   (a.getFlag (a.UNKNOWN0));
	unknown1.setSelected   (a.getFlag (a.UNKNOWN1));
	unknown2.setSelected   (a.getFlag (a.UNKNOWN2));
	unknown3.setSelected   (a.getFlag (a.UNKNOWN3));
	closeStart.setSelected (a.getFlag (a.CLOSE_START));
	closeEnd.setSelected   (a.getFlag (a.CLOSE_END));

	height.setText ("" + a.getHeight());


	panel.add (new JLabel ("Texture"), leftC);
	panel.add (tRef,  rightC);

	panel.add (new JLabel ("Type"), leftC);
	panel.add (type,  rightC);

	panel.add (new JLabel ("Unknown0"), leftC);
	panel.add (unknown0,  rightC);

	panel.add (new JLabel ("Unknown1"), leftC);
	panel.add (unknown1,  rightC);

	panel.add (new JLabel ("Unknown2"), leftC);
	panel.add (unknown2,  rightC);

	panel.add (new JLabel ("Unknown3"), leftC);
	panel.add (unknown3,  rightC);

	panel.add (new JLabel ("Height"), leftC);
	panel.add (height,  rightC);

	panel.add (new JLabel ("Close start"), leftC);
	panel.add (closeStart,  rightC);

	panel.add (new JLabel ("Close end"), leftC);
	panel.add (closeEnd,  rightC);



	// Vertices:
	vertices.setList (a.getData());
	panel.add (vertices, fillC);
    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == tRef)
	{
	    a.setTextureMap (new TextureMapRef (
		              Integer.parseInt (tRef.getText(), 16),
			      Config.getTextureList()));
	}
	else if (src == height)
	{
	    String s = height.getText();
	    if (s != "" && s != "null")
	    {
		a.setHeight (Float.parseFloat (height.getText()));
	    }
	}
	else if (src == type)
	{
	    int i = type.getSelectedIndex();
	    a.setType (i);
	}
	else
	{
	    a.setFlag (a.UNKNOWN0,         unknown0.isSelected());
	    a.setFlag (a.UNKNOWN1,         unknown1.isSelected());
	    a.setFlag (a.UNKNOWN2,         unknown2.isSelected());
	    a.setFlag (a.UNKNOWN3,         unknown3.isSelected());
	    a.setFlag (a.CLOSE_START,      closeStart.isSelected ());
	    a.setFlag (a.CLOSE_END,        closeEnd.isSelected());

	    super.actionPerformed (e);
	}
//	((ListView)panel.getParent().getParent().getParent()).dataChanged();

    }
}

