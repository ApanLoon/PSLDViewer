package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single height block attribute.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;

import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Attribute0051View
extends BlockAttributeView
implements ActionListener
{
    protected Attribute0051 attribute;
    protected JTextField    textField = new JTextField();

    public Attribute0051View (BlockAttribute a)
    {
	super (a_, false);
	attribute = (Attribute0051)a;

	panel.add (new JLabel ("Index: "), leftC);

	textField.setText ("" + attribute.height);
	textField.addActionListener (this);
	textField.setMinimumSize (new Dimension (50, 10));
	panel.add (textField, rightC);
    }


    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == textField)
	{
	    attribute.height = new HeightRef ((char)
				 Integer.parseInt (textField.getText(), 16));
	}
	else
	{
	    super.actionPerformed (e);
	}
	((ListView)panel.getParent().getParent().getParent()).dataChanged();
    }


}

