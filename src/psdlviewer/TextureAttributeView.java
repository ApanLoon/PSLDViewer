package psdlviewer;
import psdlviewer.*;

/** Displays the data of a single texture block attribute.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;

import javax.swing.ImageIcon;

import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;

import java.awt.Component;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class TextureAttributeView
extends BlockAttributeView
implements ActionListener
{
    protected TextureAttribute attribute;
    protected JTextField       textField = new JTextField();
    protected JLabel           image     = new JLabel();

    public TextureAttributeView (BlockAttribute a)
    {
	super (a, false);
	attribute = (TextureAttribute)a;

	JLabel indexLabel = new JLabel ("Index: ");
	panel.add (indexLabel, leftC);

	textField.setText ("" + attribute.ref);
	textField.addActionListener (this);
	textField.setMinimumSize (new Dimension (50, 10));
	panel.add (textField, rightC);

	image.setBorder (BorderFactory.createEtchedBorder());
	setImage();
	panel.add (image, fillC);
    }


    public void actionPerformed (ActionEvent e)
    {
	Object src = e.getSource();
	if (src == textField)
	{
	    attribute.ref = new TextureMapRef (
			       Integer.parseInt (textField.getText(), 16),
			       Config.getTextureList());
	    setImage();
	}
	else
	{
	    super.actionPerformed (e);
	}
	((ListView)panel.getParent().getParent().getParent()).dataChanged();
    }


    protected void setImage()
    {
	image.setIcon (null);
	image.setText ("No image");

	if (   attribute.ref == null
	    || attribute.ref.getIndex() == 0xffff)
	{
	    return;
	}
	TextureMap tm = attribute.ref.getTextureMap();
	if (tm == null)
	{
	    return;
	}
	Image im = tm.getImage();
	if (im == null)
	{
	    return;
	}
	image.setIcon (new ImageIcon (im));
	image.setText (null);
    }


}

