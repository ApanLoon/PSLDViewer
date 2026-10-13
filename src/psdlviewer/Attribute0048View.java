package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single Attribute0048 block attribute.
 *
 */

import java.awt.GridBagConstraints;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JTable;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.Component;
import javax.swing.ListCellRenderer;

public class Attribute0048View
extends BlockAttributeView
implements ActionListener
{
    Attribute0048 a;

    protected static String[] typeOptions    = {"Railing", "Wall"};
    protected static String[] ceilingOptions = {"None", "Flat", "Subdivided", "Both"};

    protected JCheckBox  leftSide         = new JCheckBox();
    protected JCheckBox  rightSide        = new JCheckBox();
    protected JCheckBox  leftStartClosed  = new JCheckBox();
    protected JCheckBox  leftEndClosed    = new JCheckBox();
    protected JCheckBox  rightStartClosed = new JCheckBox();
    protected JCheckBox  rightEndClosed   = new JCheckBox();
    protected JCheckBox  leftStartOffset  = new JCheckBox();
    protected JCheckBox  leftEndOffset    = new JCheckBox();
    protected JCheckBox  rightStartOffset = new JCheckBox();
    protected JCheckBox  rightEndOffset   = new JCheckBox();
    protected JComboBox  type             = new JComboBox (typeOptions);
    protected JCheckBox  subdivided       = new JCheckBox();
    protected JCheckBox  thin             = new JCheckBox();
    protected JComboBox  ceiling          = new JComboBox (ceilingOptions);
    protected JCheckBox  unknown          = new JCheckBox();
    protected JTextField leftHeight       = new JTextField();
    protected JTextField rightHeight      = new JTextField();

//    protected JList walls;
//    protected JTable walls;
    protected BoolListView walls = new BoolListView ("Enabled walls");

    public Attribute0048View (BlockAttribute a_)
    {
	super (a_, false);
	a = (Attribute0048)a_;

	leftSide.addActionListener (this);
	rightSide.addActionListener (this);
	leftHeight.addActionListener (this);
	rightHeight.addActionListener (this);
	leftStartClosed.addActionListener (this);
	rightStartClosed.addActionListener (this);
	leftEndClosed.addActionListener (this);
	rightEndClosed.addActionListener (this);
	leftStartOffset.addActionListener (this);
	rightStartOffset.addActionListener (this);
	leftEndOffset.addActionListener (this);
	rightEndOffset.addActionListener (this);
	type.addActionListener (this);
	subdivided.addActionListener (this);
	thin.addActionListener (this);
	ceiling.addActionListener (this);
	unknown.addActionListener (this);

	leftSide.setSelected         (a.getFlag (Attribute0048.LEFT_BARRIER));
	rightSide.setSelected        (a.getFlag (Attribute0048.RIGHT_BARRIER));

	leftHeight.setText           ("" + a.getLeftHeight());
	rightHeight.setText          ("" + a.getRightHeight());

	leftStartClosed.setSelected  (a.getFlag (Attribute0048.CLOSE_START_LEFT_BARRIER));
	rightStartClosed.setSelected (a.getFlag (Attribute0048.CLOSE_START_RIGHT_BARRIER));
	leftEndClosed.setSelected    (a.getFlag (Attribute0048.CLOSE_END_LEFT_BARRIER));
	rightEndClosed.setSelected   (a.getFlag (Attribute0048.CLOSE_END_RIGHT_BARRIER));

	leftStartOffset.setSelected  (a.getFlag (Attribute0048.OFFSET_START_LEFT_BARRIER));
	rightStartOffset.setSelected (a.getFlag (Attribute0048.OFFSET_START_RIGHT_BARRIER));
	leftEndOffset.setSelected    (a.getFlag (Attribute0048.OFFSET_END_LEFT_BARRIER));
	rightEndOffset.setSelected   (a.getFlag (Attribute0048.OFFSET_END_RIGHT_BARRIER));

	type.setSelectedIndex        (a.getFlag (a.WALL) ? 1 : 0);

	subdivided.setSelected       (a.getFlag (Attribute0048.SUBDIVIDED_INNER_WALLS));

	thin.setSelected             (!a.getFlag (Attribute0048.THICK_BARRIERS));
	ceiling.setSelectedIndex     (  (a.getFlag (a.FLAT_CEILING)       ? 1 : 0)
				      + (a.getFlag (a.SUBDIVIDED_CEILING) ? 2 : 0));

	unknown.setSelected          (a.getFlag (Attribute0048.UNKNOWN));


	Config.debug (a.walls.getClass().toString() + ": " + a.walls.size() + " rows\n");
//	walls = new JList (a.walls);
//	walls.setCellRenderer (new CheckBoxRenderer());
/*
	Vector cNames = new Vector();
	cNames.add ("Enabled walls");
	Vector c = new Vector();
	c.add (a.walls);
	walls = new JTable (c, cNames);
	Config.debug (walls.getClass().toString() + ": " + walls.getRowCount() + " rows\n");
*/
	walls.setList (a.walls);


	GridBagConstraints leftDoubleC  = new GridBagConstraints();
	GridBagConstraints middleC      = new GridBagConstraints();


	leftDoubleC.fill      = GridBagConstraints.NONE;
	leftDoubleC.gridwidth = 2;
	leftDoubleC.anchor    = GridBagConstraints.NORTHEAST;

	middleC.fill          = GridBagConstraints.HORIZONTAL;
	middleC.gridwidth     = GridBagConstraints.RELATIVE;;
	middleC.weightx       = 0.5;
	middleC.anchor        = GridBagConstraints.NORTHEAST;

	panel.add (new JLabel (""),      leftC);
	panel.add (new JLabel ("Left"),  middleC);
	panel.add (new JLabel ("Right"), rightC);

	panel.add (new JLabel ("Sides"), leftC);
	panel.add (leftSide,  middleC);
	panel.add (rightSide, rightC);

	panel.add (new JLabel ("Height"), leftC);
	panel.add (leftHeight,  middleC);
	panel.add (rightHeight, rightC);

	panel.add (new JLabel ("Closed start"), leftC);
	panel.add (leftStartClosed,  middleC);
	panel.add (rightStartClosed, rightC);

	panel.add (new JLabel ("Closed end"), leftC);
	panel.add (leftEndClosed,  middleC);
	panel.add (rightEndClosed, rightC);
	
	panel.add (new JLabel ("Offset start"), leftC);
	panel.add (leftStartOffset,  middleC);
	panel.add (rightStartOffset, rightC);

	panel.add (new JLabel ("Offset end"), leftC);
	panel.add (leftEndOffset,  middleC);
	panel.add (rightEndOffset, rightC);

	panel.add (new JLabel ("Type"), leftC);
	panel.add (type,  rightC);

	panel.add (new JLabel ("Subdivided"), leftC);
	panel.add (subdivided,  rightC);

	panel.add (new JLabel ("Thin (Culled)"), leftC);
	panel.add (thin,  rightC);

	panel.add (new JLabel ("Ceiling"), leftC);
	panel.add (ceiling,  rightC);

	panel.add (new JLabel ("Unknown"), leftC);
	panel.add (unknown,  rightC);

	leftC.gridheight = GridBagConstraints.REMAINDER;
	leftC.weighty = 1.0;
	middleC.gridheight = GridBagConstraints.REMAINDER;
	middleC.weighty = 1.0;
	rightC.gridheight = GridBagConstraints.REMAINDER;
	rightC.weighty = 1.0;

	panel.add (new JScrollPane (walls),  middleC);

    }




    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();


	if (src == leftSide)
	{
	    a.setFlag (a.LEFT_BARRIER, leftSide.isSelected());
	}
	else if (src == rightSide)
	{
	    a.setFlag (a.RIGHT_BARRIER, rightSide.isSelected());
	}
	else if (src == leftHeight)
	{
	    a.setLeftHeight (Float.parseFloat (leftHeight.getText()));
	}
	else if (src == rightHeight)
	{
	    a.setRightHeight (Float.parseFloat (rightHeight.getText()));
	}
	else if (src == leftStartClosed)
	{
	    a.setFlag (a.CLOSE_START_LEFT_BARRIER, leftStartClosed.isSelected());
	}
	else if (src == rightStartClosed)
	{
	    a.setFlag (a.CLOSE_START_RIGHT_BARRIER, rightStartClosed.isSelected());
	}
	else if (src == leftEndClosed)
	{
	    a.setFlag (a.CLOSE_END_LEFT_BARRIER, leftEndClosed.isSelected());
	}
	else if (src == rightEndClosed)
	{
	    a.setFlag (a.CLOSE_END_RIGHT_BARRIER, rightEndClosed.isSelected());
	}
	else if (src == leftStartOffset)
	{
	    a.setFlag (a.OFFSET_START_LEFT_BARRIER, leftStartOffset.isSelected());
	}
	else if (src == rightStartOffset)
	{
	    a.setFlag (a.OFFSET_START_RIGHT_BARRIER, rightStartOffset.isSelected());
	}
	else if (src == leftEndOffset)
	{
	    a.setFlag (a.OFFSET_END_LEFT_BARRIER, leftEndOffset.isSelected());
	}
	else if (src == rightEndOffset)
	{
	    a.setFlag (a.OFFSET_END_RIGHT_BARRIER, rightEndOffset.isSelected());
	}
	else if (src == type)
	{
	    int i = type.getSelectedIndex();
	    a.setFlag (a.WALL, i == 1);
	}
	else if (src == subdivided)
	{
	    a.setFlag (a.SUBDIVIDED_INNER_WALLS, subdivided.isSelected());
	}
	else if (src == thin)
	{
	    a.setFlag (a.THICK_BARRIERS, !thin.isSelected());
	}
	else if (src == ceiling)
	{
	    int i = ceiling.getSelectedIndex();
	    a.setFlag (a.FLAT_CEILING, (i & 1) != 0);
	    a.setFlag (a.SUBDIVIDED_CEILING, (i & 2) != 0);
	}
	else if (src == unknown)
	{
	    a.setFlag (a.UNKNOWN, unknown.isSelected());
	}
	else
	{
	    super.actionPerformed (e);
	}

    }




    /*
    class CheckBoxRenderer
	extends JCheckBox
	implements ListCellRenderer
    {
	public CheckBoxRenderer()
	{
	    setOpaque(true);
	    setHorizontalAlignment(CENTER);
	    setVerticalAlignment(CENTER);
	}

	public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus)
	{
	    if (isSelected)
	    {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    }
	    else
	    {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }

	    setText ("" + index);
	    setSelected (((Boolean)value).booleanValue());

	    return this;
	}
    }
    */

}

