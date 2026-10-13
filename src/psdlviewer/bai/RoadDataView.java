package psdlviewer.bai;
import psdlviewer.*;

/** Displays the data of a single RoadData object.
 *
 */

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.util.Vector;

public class RoadDataView
extends JPanel
implements ActionListener
{
    protected TableView lanes     = new TableView ("Lanes");
    protected TableView various   = new TableView ("Unknown");
    protected TableView sidewalks = new TableView ("Sidewalks");
    protected TableView trams     = new TableView ("Trams");
    protected TableView trains    = new TableView ("Trains");
    protected HexField  u3        = new HexField();
    protected HexField  u4        = new HexField();

    public RoadDataView()
    {
	GridBagLayout gbl = new GridBagLayout();
	setLayout (gbl);

	GridBagConstraints c = new GridBagConstraints();

	u3.addActionListener (this);
	u4.addActionListener (this);

	lanes.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
	various.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
	sidewalks.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
	trams.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
	trains.setDefaultEditor (Vertex3D.class, new VertexCellEditor());

	lanes.addColumn ("Distance", new Vector());
	various.addColumn ("Unknown", new Vector());
	sidewalks.addColumn ("Inner", new Vector());
	sidewalks.addColumn ("Center", new Vector());
	sidewalks.addColumn ("Outer", new Vector());


	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.5;
	add (lanes, c);

	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.5;
	add (various, c);


	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.33;
	c.weighty = 0.5;
	add (sidewalks, c);

	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.33;
	c.weighty = 0.5;
	add (trams, c);

	c.fill       = GridBagConstraints.BOTH;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.33;
	c.weighty = 0.5;
	add (trains, c);


 	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("Unknown3"), c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 1.0;
	add (u3, c);

 	c.fill       = GridBagConstraints.NONE;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	add (new JLabel ("Unknown4"), c);

	c.fill       = GridBagConstraints.HORIZONTAL;
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 0.5;
	c.weighty = 0.0;
	add (u4, c);


    }





    public void setRoadData (RoadData r)
    {
	u3.setValue (r.getUnknown3());
	u4.setValue (r.getUnknown4());

	lanes.setColumnData (1, r.getDistances());
	setupColumnPairs (lanes,
			  r.getLaneDistances(),
			  r.getLaneVertices(),
			  "Distance",
			  "Vertex",
			  r.getNLanes(), 2);

	various.setColumnData (1, r.getUnknown0());

	sidewalks.setColumnData (1, r.getSidewalkInner());
	sidewalks.setColumnData (2, r.getSidewalkCenter());
	sidewalks.setColumnData (3, r.getSidewalkOuter());

	setupColumns (trams,
		      r.getTramVertices(),
		      "Vertex",
		      r.getNTrams(),
		      1);

	setupColumns (trains,
		      r.getTrainVertices(),
		      "Vertex",
		      r.getNTrains(),
		      1);

    }


    protected void setupColumns (TableView t,
				 Vector    data,
				 String    cName,
				 int       nColumns,
				 int       firstColumn)
    {
	int i;
	int n = t.getColumnCount();
	for (i = n - 1; i >= firstColumn; i--)
	{
	    t.removeColumn (i);
	}

	if (nColumns == 0)
	    return;

	int nRows = data.size() / nColumns;
	for (i = 0; i < nColumns; i++)
	{
	    Vector v = new Vector (data.subList (i * nRows,
						 (i + 1) * nRows));
	    t.addColumn (cName + " " + i, v);
	}
    }

    protected void setupColumnPairs (TableView t,
				     Vector    c0,
				     Vector    c1,
				     String    c0Name,
				     String    c1Name,
				     int       nPairs,
				     int       firstColumn)
    {
	int i;
	int n = t.getColumnCount();
	for (i = n - 1; i >= firstColumn; i--)
	{
	    t.removeColumn (i);
	}

	if (nPairs == 0)
	    return;

	int nRows = c0.size() / nPairs;
	for (i = 0; i < nPairs; i++)
	{
	    Vector v = new Vector (c0.subList (i * nRows,
					       (i + 1) * nRows));
	    t.addColumn (c0Name + " " + i, v);

	    v = new Vector (c1.subList (i * nRows,
					(i + 1) * nRows));
	    t.addColumn ("Vertices " + i, v);
	}
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

