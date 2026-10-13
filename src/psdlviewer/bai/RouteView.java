package psdlviewer.bai;
import psdlviewer.*;

/** Displays the data of a single Route.
 *
 */

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.util.Vector;

public class RouteView
extends DefaultDataView
implements ActionListener
{
    protected Route route;

    protected JPanel    panel;

    protected HexField id = new HexField();
    protected HexField u0 = new HexField();
    protected FloatField u1 = new FloatField();
    protected FloatField u2 = new FloatField();
    protected BlockRefListView blocks = new BlockRefListView ("Blocks");

    protected TableView centerLine = new TableView ("Center line");

    protected JTabbedPane roadData = new JTabbedPane();
    protected RoadDataView right   = new RoadDataView();
    protected RoadDataView left    = new RoadDataView();

    protected JTabbedPane roadEnds = new JTabbedPane();
    protected RoadEndView start    = new RoadEndView();
    protected RoadEndView end      = new RoadEndView();

    public RouteView()
    {
	panel = new JPanel();
	component = panel;
//	component = new JScrollPane (panel);

	GridBagLayout gbl = new GridBagLayout();
	panel.setLayout (gbl);

	GridBagConstraints c = new GridBagConstraints();

	JLabel label = new JLabel ("Route");
	c.fill = GridBagConstraints.NONE;
	c.anchor = GridBagConstraints.NORTHWEST;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.weightx = 0.0;
	c.weighty = 0.0;
	panel.add (label, c);

	id.addActionListener (this);
	u0.addActionListener (this);
	u1.addActionListener (this);
	u2.addActionListener (this);


	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 4;
	c.weightx = 0.5;
	c.weighty = 0.20;
	panel.add (blocks, c);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	panel.add (new JLabel ("ID"), c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	panel.add (id, c);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	panel.add (new JLabel ("Unknown0"), c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	panel.add (u0, c);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	panel.add (new JLabel ("Unknown1"), c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	panel.add (u1, c);

	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	panel.add (new JLabel ("Unknown2"), c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	panel.add (u2, c);


	centerLine.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
	centerLine.addColumn ("Distance", new Vector());
	centerLine.addColumn ("Center",   new Vector());
	centerLine.addColumn ("X",        new Vector());
	centerLine.addColumn ("Y",        new Vector());
	centerLine.addColumn ("Z",        new Vector());
	centerLine.addColumn ("W",        new Vector());

	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.40;
	panel.add (centerLine, c);


	roadData.addTab ("Right",
			 null,
			 right,
			 "Right route data");
	roadData.addTab ("Left",
			 null,
			 left,
			 "Left route data");
	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.40;
	panel.add (roadData, c);


	roadEnds.addTab ("Start",
			 null,
			 start,
			 "Start of route");
	roadEnds.addTab ("End",
			 null,
			 end,
			 "End of route");
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	panel.add (roadEnds, c);
/*
	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = GridBagConstraints.REMAINDER;
	c.weightx = 0.5;
	c.weighty = 0.0;
	panel.add (start, c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	c.weightx = 0.5;
	c.weighty = 0.0;
	panel.add (end, c);
*/
    }





    public void setRoute (Route r)
    {
	route = r;
	id.setValue (r.getID());
	u0.setValue (r.getUnknown0());
	u1.setValue (r.getUnknown1());
	u2.setValue (r.getUnknown2());
	blocks.setList (r.getBlocks());

	centerLine.setColumnData (1, r.getCentralDistances());
	centerLine.setColumnData (2, r.getCentralLine());
	centerLine.setColumnData (3, r.getXOrientation());
	centerLine.setColumnData (4, r.getYOrientation());
	centerLine.setColumnData (5, r.getZOrientation());
	centerLine.setColumnData (6, r.getWOrientation());


	right.setRoadData (r.getRight());
	left.setRoadData (r.getLeft());

	start.setRoadEnd (r.getRoadStart());
	end.setRoadEnd (r.getRoadEnd());

    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();

/*
	if (src == leftSide)
	{
	}
	else if (src == rightSide)
	{
	}
	else if (src == leftHeight)
	{
	}
	else if (src == rightHeight)
	{
	}
	else if (src == leftStartClosed)
	{
	}
	else if (src == rightStartClosed)
	{
	}
	else if (src == leftEndClosed)
	{
	}
	else if (src == rightEndClosed)
	{
	}
	else if (src == leftStartOffset)
	{
	}
	else if (src == rightStartOffset)
	{
	}
	else if (src == leftEndOffset)
	{
	}
	else if (src == rightEndOffset)
	{
	}
	else if (src == type)
	{
	    int i = type.getSelectedIndex();
	}
	else if (src == subdivided)
	{
	}
	else if (src == thin)
	{
	}
	else if (src == ceiling)
	{
	    int i = ceiling.getSelectedIndex();
	}
	else if (src == unknown)
	{
	}
*/
    }
}

