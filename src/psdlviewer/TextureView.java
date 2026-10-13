package psdlviewer;
import psdlviewer.*;

/** Displays a single texture.
 *
 */

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import javax.swing.ImageIcon;

import javax.swing.BorderFactory;

import javax.swing.BoxLayout;

import java.awt.BorderLayout;

import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;


public class TextureView
extends DefaultDataView
implements TextureSelectionListener
{
    protected JPanel panel = new JPanel();
    protected ScrollableJPanel imagePanel = new ScrollableJPanel();
    protected JScrollPane pane;

    public TextureView()
    {
	component = panel;
	panel.setLayout (new BoxLayout (panel, BoxLayout.Y_AXIS));

	JLabel title = new JLabel ("Images");
	title.setAlignmentX (0f);
	title.setAlignmentY (0f);

	imagePanel.setLayout (new BoxLayout (imagePanel, BoxLayout.X_AXIS));
	pane = new JScrollPane (imagePanel);
        pane.setPreferredSize (new Dimension (256, 256));
        pane.setViewportBorder (BorderFactory.createLineBorder (Color.black));
	pane.setAlignmentY (0f);
	pane.setAlignmentX (0f);

	panel.add (title);
	panel.add (pane);
    }

    // TextureSelectionListener methods:
    //
    //
    public void texturesSelected (Object[] textures)
    {
	imagePanel.removeAll();

	for (int i = 0; i < textures.length; i++)
	{
	    TextureMap texture = (TextureMap)textures[i];

	    JPanel p = new JPanel();
	    p.setLayout (new BoxLayout (p, BoxLayout.Y_AXIS));

	    JLabel image = new JLabel();
	    image.setBorder (BorderFactory.createEtchedBorder());

	    Image im = texture.getImage();
	    if (im != null)
	    {
		image.setIcon (new ImageIcon (im));
		image.setText (null);
	    }
	    else
	    {
		image.setIcon (null);
		image.setText ("No image");
	    }
	    p.add (image);

	    JLabel title = new JLabel (texture.getName());
	    title.setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));
	    p.add (title);

	    imagePanel.add (p);
	}
	imagePanel.revalidate();
    }

}
