package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single INST component.
 *
 */

import javax.swing.JScrollPane;
import javax.swing.JPanel;
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




public class INSTComponentView
extends DefaultDataView
implements ActionListener
{
    protected INSTComponent   inst;

    protected JScrollPane panel      = new JScrollPane();
    protected ScrollableJPanel simplePanel     = new ScrollableJPanel();
    protected ScrollableJPanel coordinatePanel = new ScrollableJPanel();


    // Simple attributes:
    protected HexField    s_unknown = new HexField();
    protected JTextField  s_pkg     = new JTextField();
    protected VertexField s_origin  = new VertexField();
    protected JTextField  scale     = new JTextField();
    protected JTextField  angle     = new JTextField();

    // Coordinate attributes:
    protected HexField    c_unknown = new HexField();
    protected JTextField  c_pkg     = new JTextField();
    protected VertexField c_origin  = new VertexField();
    protected VertexField xAxis     = new VertexField();
    protected VertexField yAxis     = new VertexField();
    protected VertexField zAxis     = new VertexField();



    public INSTComponentView()
    {
	super();

	component = panel;

	GridBagLayout gbl = new GridBagLayout();
	simplePanel.setLayout (gbl);
	coordinatePanel.setLayout (new GridBagLayout());

	GridBagConstraints c = new GridBagConstraints();

	JLabel label;

	// PKG:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Geometry: ");
	coordinatePanel.add (label, c);
	label = new JLabel ("Geometry: ");
	simplePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	s_pkg.addActionListener (this);
	c_pkg.addActionListener (this);
	simplePanel.add (s_pkg, c);
	coordinatePanel.add (c_pkg, c);



	// Unknown:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Unknown: ");
	simplePanel.add (label, c);
	label = new JLabel ("Unknown: ");
	coordinatePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	s_unknown.addActionListener (this);
	c_unknown.addActionListener (this);
	simplePanel.add (s_unknown, c);
	coordinatePanel.add (c_unknown, c);



	// Origin:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Origin: ");
	simplePanel.add (label, c);
	label = new JLabel ("Origin: ");
	coordinatePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	s_origin.addActionListener (this);
	c_origin.addActionListener (this);
	simplePanel.add (s_origin, c);
	coordinatePanel.add (c_origin, c);


	// Simple panel:

	// Scale:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Scale: ");
	simplePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	scale.addActionListener (this);
	simplePanel.add (scale, c);

	// Angle:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 1.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Angle: ");
	simplePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 1.0;
	angle.addActionListener (this);
	simplePanel.add (angle, c);



	// Coordinate panel:

	// XAxis:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("X axis: ");
	coordinatePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	xAxis.addActionListener (this);
	coordinatePanel.add (xAxis, c);

	// YAxis:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 0.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Y axis: ");
	coordinatePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 0.0;
	yAxis.addActionListener (this);
	coordinatePanel.add (yAxis, c);

	// ZAxis:
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.gridheight = 1;
	c.weightx = 0.0;
	c.weighty = 1.0;
	c.anchor = GridBagConstraints.NORTHWEST;
	label = new JLabel ("Z axis: ");
	coordinatePanel.add (label, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.weightx = 1.0;
	c.weighty = 1.0;
	zAxis.addActionListener (this);
	coordinatePanel.add (zAxis, c);






    }




    public void setINSTComponent (INSTComponent c)
    {
	inst = c;
	if (inst.getClass() == INSTSimpleComponent.class)
	{
	    INSTSimpleComponent co = (INSTSimpleComponent)inst;
	    s_pkg.setText (inst.getPKGName());
	    s_unknown.setValue (inst.getUnknown());
	    s_origin.setValue (co.getOrigo());
	    scale.setText ("" + co.getScale());
	    angle.setText ("" + Math.toDegrees (co.getAngle()));

	    panel.setViewportView (simplePanel);
	}
	else if (inst.getClass() == INSTCoordinateSystemComponent.class)
	{
	    INSTCoordinateSystemComponent co = (INSTCoordinateSystemComponent)inst;
	    c_pkg.setText (inst.getPKGName());
	    c_unknown.setValue (inst.getUnknown());
	    c_origin.setValue (co.getOrigo());
	    xAxis.setValue (co.getXAxis());
	    yAxis.setValue (co.getYAxis());
	    zAxis.setValue (co.getZAxis());

	    panel.setViewportView (coordinatePanel);
	}
	panel.revalidate();
    }



    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (inst.getClass() == INSTSimpleComponent.class)
	{
	    INSTSimpleComponent co = (INSTSimpleComponent)inst;
	    if (src == s_pkg)
	    {
		co.setPKGName (s_pkg.getText());
	    }
	    else if (src == s_unknown)
	    {
		co.setUnknown (s_unknown.getValue());
	    }
	    else if (src == s_origin)
	    {
		co.setOrigo (s_origin.getValue());
	    }
	    else if (src == scale || src == angle)
	    {
		float s = Float.parseFloat (scale.getText());
		float a = (float)Math.toRadians (Float.parseFloat (angle.getText()));
		co.setOrientationScale (s, a);
					
	    }
	}
	else if (inst.getClass() == INSTCoordinateSystemComponent.class)
	{
	    INSTCoordinateSystemComponent co = (INSTCoordinateSystemComponent)inst;
	    if (src == c_pkg)
	    {
		co.setPKGName (c_pkg.getText());
	    }
	    else if (src == c_unknown)
	    {
		co.setUnknown (c_unknown.getValue());
	    }
	    else if (src == c_origin)
	    {
		co.setOrigo (c_origin.getValue());
	    }
	    else if (src == xAxis)
	    {
		co.setXAxis (xAxis.getValue());
	    }
	    else if (src == yAxis)
	    {
		co.setYAxis (yAxis.getValue());
	    }
	    else if (src == zAxis)
	    {
		co.setZAxis (zAxis.getValue());
	    }
	}
    }

}

