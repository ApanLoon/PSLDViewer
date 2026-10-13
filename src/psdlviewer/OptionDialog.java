package psdlviewer;
import psdlviewer.*;

/** Dialog box containing option tabs.
 *
 */

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;



public class OptionDialog
extends JDialog
implements ActionListener
{

    JTabbedPane tabs = new JTabbedPane();


    /** Creates a new dialog.
     *
     */
    public OptionDialog (Component parent)
    {
	super (JOptionPane.getFrameForComponent (parent), "Options", true);

	JPanel panel = new JPanel();

	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	panel.setLayout (gridBag);

	tabs.addTab ("File",
		     null,
		     new FileOptionTab(),
		     "File options");


	tabs.addTab ("Block lists",
		     null,
		     new ViewOptionTab (Config.blockListViews),
		     "Block list options");

	tabs.addTab ("Vertex lists",
		     null,
		     new ViewOptionTab (Config.vertexListViews),
		     "Vertex list options");

	tabs.addTab ("Texture lists",
		     null,
		     new ViewOptionTab (Config.textureListViews),
		     "Texture list options");

	tabs.addTab ("Road lists",
		     null,
		     new ViewOptionTab (Config.roadListViews),
		     "Road list options");

	tabs.addTab ("Terrain",
		     null,
		     new TerrainOptionTab (Config.terrain),
		     "Terrain options");

	tabs.addTab ("Terrain views",
		     null,
		     new ViewOptionTab (Config.terrainViews),
		     "Terrain view options");

	c.anchor = GridBagConstraints.NORTHWEST;
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1;
	c.weighty = 1;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.RELATIVE;
	panel.add (tabs, c );

	c.fill = GridBagConstraints.NONE;
	c.anchor = GridBagConstraints.SOUTHEAST;
	c.weighty = 0;
	c.gridwidth = 1;
	c.gridheight = GridBagConstraints.REMAINDER;

	JButton button;
	button = new JButton ("SAVE");
	button.addActionListener (this);
	c.anchor = GridBagConstraints.SOUTHWEST;
	c.weightx = 1;
	panel.add (button, c);

	c.anchor = GridBagConstraints.SOUTHEAST;
	c.weightx = 0;

	button = new JButton ("OK");
	button.addActionListener (this);
	panel.add (button, c);

	button = new JButton ("Cancel");
	button.addActionListener (this);
	c.gridwidth = GridBagConstraints.RELATIVE;
	panel.add (button, c);

	button = new JButton ("Apply");
	button.addActionListener (this);
	c.gridwidth = GridBagConstraints.REMAINDER;
	panel.add (button, c);

	setContentPane (panel);

	pack();
    }







    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("SAVE".equals (cmd))
	{
	    apply();
	    Config.writeOptions();
	    setVisible (false);
	}
	if ("OK".equals (cmd))
	{
	    apply();
	    setVisible (false);
	}
	else if ("Cancel".equals (cmd))
	{
	    setVisible (false);
	}
	else if ("Apply".equals (cmd))
	{
	    apply();
	}
    }



    /** Applies all options and writes the option file.
     *
     */
    protected void apply()
    {
	for (int i = 0; i < tabs.getTabCount(); i++)
	{
	    OptionTab t = (OptionTab)tabs.getComponentAt (i);
	    t.apply();
	}
    }

}
