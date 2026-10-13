package psdlviewer.bai;
import psdlviewer.*;

/** Displays the data of a single RoadEnd object.
 *
 */

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class RoadEndView
extends JPanel
implements ActionListener
{
    protected RoadEnd roadEnd = new RoadEnd();

    protected HexField    intersection = new HexField();
    protected HexField    u0           = new HexField();
    protected HexField    u1           = new HexField();
    protected HexField    u2           = new HexField();
    protected VertexField origin       = new VertexField();
    protected VertexField axis         = new VertexField();

    public RoadEndView()
    {
	GridBagLayout gbl = new GridBagLayout();
	setLayout (gbl);

	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets (2, 2, 1, 0);

	intersection.addActionListener (this);
	u0.addActionListener (this);
	u1.addActionListener (this);
	u2.addActionListener (this);
	origin.addActionListener (this);
	axis.addActionListener (this);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("ISection"), c);
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	add (intersection, c);


	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("Unknown0"), c);
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	add (u0, c);


	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("Origin"), c);
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	add (origin, c);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("Unknown1"), c);
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	add (u1, c);


	c.insets = new Insets (2, 2, 1, 1);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 1.0;
	add (new JLabel ("Axis"), c);
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 1.0;
	add (axis, c);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("Unknown2"), c);
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	add (u2, c);

    }





    public void setRoadEnd (RoadEnd r)
    {
	roadEnd = r;
	intersection.setValue ((int)r.getIntersection());
	u0.setValue (r.getUnknown0());
	u1.setValue ((int)r.getUnknown1());
	u2.setValue ((int)r.getUnknown2());
	origin.setValue (r.getOrigin());
	axis.setValue (r.getAxis());

    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();

	if (src == intersection)
	{
	    roadEnd.setIntersection ((long)intersection.getValue());
	}
	else if (src == u0)
	{
	    roadEnd.setUnknown0 (u0.getValue());
	}
	else if (src == u1)
	{
	    roadEnd.setUnknown1 ((long)u1.getValue());
	}
	else if (src == u2)
	{
	    roadEnd.setUnknown2 ((long)u2.getValue());
	}
	else if (src == origin)
	{
	    roadEnd.setOrigin (origin.getValue());
	}
	else if (src == u0)
	{
	    roadEnd.setAxis (axis.getValue());
	}
    }
}

