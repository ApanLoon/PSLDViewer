package psdlviewer;
import psdlviewer.*;

/** Option panel for a Terrain View.
 *
 */

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import java.awt.GridLayout;
import java.awt.GridBagConstraints;

import java.awt.Rectangle;

public class TerrainViewOptionView
extends ViewOptionPanel
{

    protected TerrainView view;

    protected JCheckBox visible;


    /** Creates a new option panel.
     *
     */
    public TerrainViewOptionView (TerrainView view_)
    {
	view = view_;

	name.setText (view.getTitle());
	Rectangle r = view.getBounds();
	left.setText   ("" + r.x);
	top.setText    ("" + r.y);
	width.setText  ("" + r.width);
	height.setText ("" + r.height);

	JPanel panel = new JPanel();
	panel.setLayout (new GridLayout (1, 2));

	panel.add (new JLabel ("Visible"));
	visible = new JCheckBox();
	panel.add (visible);

	if (view != null)
	{
	    visible.setSelected (view.getVisible());
	}


	GridBagConstraints c  = new GridBagConstraints();
	c.anchor = GridBagConstraints.SOUTH;
	c.weightx = 0f;
	c.weighty = 0f;
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	add (panel, c);
	

    }





    /** @return the view this option view represents.
     *
     */
    public TerrainView getView()
    {
	return view;
    }



    public void apply()
    {
	if (view != null)
	{
	    view.setTitle  (name.getText());
	    view.setBounds (Integer.parseInt (left.getText()),
			    Integer.parseInt (top.getText()),
			    Integer.parseInt (width.getText()),
			    Integer.parseInt (height.getText()));

	    view.setVisible          (visible.isSelected());
	}
    }





    public String toString()
    {
	return view.getTitle();
    }
}
