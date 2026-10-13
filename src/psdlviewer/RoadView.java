package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single road.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import javax.swing.BoxLayout;


import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


import java.util.Vector;




public class RoadView
extends DefaultDataView
implements RoadSelectionListener, ActionListener
{
    protected Road   road;

    protected JPanel panel = new JPanel();

    protected JTextField u0 = new JTextField();
    protected JTextField u1 = new JTextField();
    protected JList fLanes = new JList();
    protected JList bLanes = new JList();
    protected JTextField u2 = new JTextField();
    protected VertexRefListView start = new VertexRefListView ("Start");
    protected VertexRefListView end = new VertexRefListView ("End");
    protected BlockRefListView blocks = new BlockRefListView ("Blocks");


    public RoadView()
    {
	super();

	component = panel;

	GridBagLayout gbl = new GridBagLayout();

	panel.setLayout (gbl);
	GridBagConstraints c = new GridBagConstraints();

	JLabel label;

	// U0:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Unknown 0: ");
	panel.add (label, c);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	u0.addActionListener (this);
	panel.add (u0, c);

	// U1:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Unknown 1: ");
	panel.add (label, c);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	u1.addActionListener (this);
	panel.add (u1, c);


	// FLanes:
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 1.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	panel.add (new JScrollPane (fLanes), c);

	// BLanes:
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 1.0;
	panel.add (new JScrollPane (bLanes), c);


	// U2:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Unknown 2: ");
	panel.add (label, c);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	u2.addActionListener (this);
	panel.add (u2, c);

	// Start:
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = 1;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.NORTH;
	panel.add (start, c);

	// End:
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.NORTH;
	panel.add (end, c);

	// Blocks:
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	c.weightx = 0.0;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.NORTH;
	panel.add (blocks, c);



    }

    // RoadSelectionListener methods:
    //
    //
    public void roadsSelected (Object[] roads)
    {
	if (roads.length >= 1)
	{
	    road = (Road)roads[0];
	    u0.setText ("" + road.getU0());
	    u1.setText ("" + road.getU1());
	    fLanes.setListData (road.getForwardLanes());
	    bLanes.setListData (road.getBackwardLanes());
	    u2.setText ("" + road.getU2());
	    start.setList (road.getStartVertices());
	    end.setList (road.getEndVertices());
	    blocks.setList (road.getBlockRefs());
	}
    }



    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == u0)
	{
	    road.setU0 (new UShort ((char)Integer.parseInt (u0.getText(), 16)));
	}
	else if (src == u1)
	{
	    road.setU1 (new UShort ((char)Integer.parseInt (u1.getText(), 16)));
	}
	else if (src == u2)
	{
	    road.setU2 (new UShort ((char)Integer.parseInt (u2.getText(), 16)));
	}
	((ListView)panel.getParent()).dataChanged();
    }

}

