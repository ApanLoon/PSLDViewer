package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single path.
 *
 */

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class PathView
extends DefaultDataView
implements PathSelectionListener,
	   ActionListener
{
    Path path;

    protected static String[] typeOptions    = {"Points", "Lines", "Line strip"};

    protected JPanel panel = new JPanel();

    protected JTextField name           = new JTextField();
    protected JTextField unknown1       = new JTextField();
    protected JComboBox  type           = new JComboBox (typeOptions);
    protected JTextField spacing        = new JTextField();
    protected JTextField unknown3       = new JTextField();
    protected PathPointListView points  = new PathPointListView ("Points");

    public PathView()
    {
	super();


	GridBagLayout gbl = new GridBagLayout();

	name.addActionListener (this);
	unknown1.addActionListener (this);
	type.addActionListener (this);
	spacing.addActionListener (this);
	unknown3.addActionListener (this);

	panel.setLayout (gbl);
	GridBagConstraints c = new GridBagConstraints();

	GridBagConstraints leftC        = new GridBagConstraints();
	GridBagConstraints rightC       = new GridBagConstraints();

	leftC.fill            = GridBagConstraints.NONE;
	leftC.gridwidth       = 1;
	leftC.anchor          = GridBagConstraints.NORTHEAST;

	rightC.fill           = GridBagConstraints.HORIZONTAL;
	rightC.gridwidth      = GridBagConstraints.REMAINDER;
	rightC.weightx        = 0.5;
	rightC.anchor         = GridBagConstraints.NORTHEAST;



	JLabel label;

	panel.add (new JLabel ("Name"), leftC);
	panel.add (name,  rightC);

	panel.add (new JLabel ("Unknown1"), leftC);
	panel.add (unknown1,  rightC);

	panel.add (new JLabel ("Type"), leftC);
	panel.add (type,  rightC);

	panel.add (new JLabel ("Spacing"), leftC);
	panel.add (spacing,  rightC);

	panel.add (new JLabel ("Unknown3"), leftC);
	panel.add (unknown3,  rightC);

	panel.add (points, rightC);

	component = panel;
    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == name)
	{
	    path.setName (name.getText());
	}
	else if (src == unknown1)
	{
	    path.setUnknown1 (Integer.parseInt (unknown1.getText(), 16));
	}
	else if (src == spacing)
	{
	    String s = spacing.getText();
	    if (s != "" && s != "null")
	    {
		path.setSpacing (Float.parseFloat (s));
	    }
	}

	((ListView)panel.getParent().getParent().getParent().getParent().getParent()).dataChanged();

    }







    // PathSelectionListener methods:
    //
    //
    public void pathsSelected (Object[] paths)
    {
	if (paths.length >= 1)
	{
	    path = (Path)paths[0];
	    name.setText ("" + path.getName());
	    unknown1.setText ("" + Integer.toHexString (path.getUnknown1()));
	    type.setSelectedIndex (path.getType());
	    spacing.setText ("" + path.getSpacing());
	    unknown3.setText ("" + Integer.toHexString (path.getUnknown3()));
	    points.setList (path.getPoints());
	}
    }
}

