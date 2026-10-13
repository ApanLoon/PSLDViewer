package psdlviewer;
import psdlviewer.*;

/* Base class for panels showing options for views.
 *
 */

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.Rectangle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.lang.Class;

public class ViewOptionPanel
extends JPanel
{
    public static ViewOptionPanel getInstance (Object o)
    {
	Class c = o.getClass();

	if (c == TerrainView.class)
	{
	    return new TerrainViewOptionView ((TerrainView) o);
	}
	return new ViewOptionPanel ((ListViewFrame) o);
    }





    protected ListViewFrame view = null;
    protected JTextField name = new JTextField();
    protected JTextField left = new JTextField();
    protected JTextField top = new JTextField();
    protected JTextField width = new JTextField();
    protected JTextField height = new JTextField();


    public ViewOptionPanel (ListViewFrame view_)
    {
	view = view_;
	initialize();

	name.setText (view.getTitle());
	Rectangle r = view.getBounds();
	left.setText   ("" + r.x);
	top.setText    ("" + r.y);
	width.setText  ("" + r.width);
	height.setText ("" + r.height);

	GridBagConstraints c  = new GridBagConstraints();
	c.anchor = GridBagConstraints.SOUTH;
	c.weightx = 0f;
	c.weighty = 0f;
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	add (new JPanel(), c);

    }

    public ViewOptionPanel()
    {
	initialize();
    }





    protected void initialize()
    {
	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints c  = new GridBagConstraints();

	setLayout (gridBag);

	c.anchor = GridBagConstraints.NORTHWEST;
	c.weightx = 0f;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	add (new JLabel ("Name:"), c);

	c.weightx = 1f;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	add (name, c);

	c.weightx = 0f;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	add (new JLabel ("Top:"), c);

	c.weightx = 0.5f;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = 1;
	add (top, c);

	c.weightx = 0f;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	add (new JLabel ("Left:"), c);

	c.weightx = 0.5f;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	add (left, c);

	c.weightx = 0f;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	add (new JLabel ("Width:"), c);

	c.weightx = 0.5f;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = 1;
	add (width, c);

	c.weightx = 0f;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	add (new JLabel ("Height:"), c);

	c.weightx = 0.5f;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	add (height, c);

    }




    public String toString()
    {
	return view.getTitle();
    }



    public void apply()
    {
	if (view == null)
	    return;

	view.setTitle (name.getText());
	view.reshape (Integer.parseInt (left.getText()),
		      Integer.parseInt (top.getText()),
		      Integer.parseInt (width.getText()),
		      Integer.parseInt (height.getText()));
    }
}
