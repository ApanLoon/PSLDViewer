package psdlviewer;
import psdlviewer.*;

/** Option panel for file options.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;

import javax.swing.ListModel;
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.FileDialog;

import java.awt.Rectangle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;

import java.util.Vector;

public class FileOptionTab
extends JPanel
implements OptionTab,
	   ActionListener
{
    protected FileDialog texturePathChooser = new FileDialog (Config.mainWindow,
					 "Select a file in the texture folder",
					 FileDialog.LOAD);
    protected FileDialog geometryPathChooser = new FileDialog (Config.mainWindow,
					 "Select a file in the geometry folder",
					 FileDialog.LOAD);

    protected JTextField        openPath;
    protected JTextField        savePath;
    protected JList             texturePath;
    protected JList             geometryPath;
    protected DefaultListModel  textureModel = new DefaultListModel();
    protected DefaultListModel  geometryModel = new DefaultListModel();


    /** Creates a new option panel.
     *
     */
    public FileOptionTab()
    {
	GridBagLayout gridBag = new GridBagLayout();
	setLayout (gridBag);

	GridBagConstraints labelc  = new GridBagConstraints();
	GridBagConstraints valuec  = new GridBagConstraints();

	labelc.anchor = GridBagConstraints.NORTHWEST;
	labelc.gridwidth = 1; //GridBagConstraints.RELATIVE;
	labelc.weightx = 0;
	labelc.weighty = 0;
	labelc.fill = GridBagConstraints.NONE;

	valuec.anchor = GridBagConstraints.NORTHWEST;
	valuec.gridwidth = GridBagConstraints.REMAINDER;
	valuec.weightx = 1;
	valuec.weighty = 0;
	valuec.fill = GridBagConstraints.HORIZONTAL;


	add (new JLabel ("Open path"), labelc);
	openPath = new JTextField (Config.openPath);
	add (openPath, valuec);

	add (new JLabel ("Save path"), labelc);
	savePath = new JTextField (Config.savePath);
	add (savePath, valuec);


	// TexturePath:
	GridBagConstraints c = new GridBagConstraints();

	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridheight = 2;
	c.weighty    = 1;
	c.fill       = GridBagConstraints.NONE;
	add (new JLabel ("Texture path"), c);


	for (int i = 0; i < Config.texturePath.size(); i++)
	{
	    textureModel.addElement (Config.texturePath.elementAt (i));
	}
	texturePath = new JList (textureModel);

	c.weightx    = 1;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.gridheight = 2;
	c.fill       = GridBagConstraints.BOTH;
	add (new JScrollPane (texturePath), c);

	JButton button;
	button = new JButton ("Add");
	c.anchor     = GridBagConstraints.NORTHEAST;
	c.weightx    = 0;
	c.weighty    = 0;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	c.fill       = GridBagConstraints.HORIZONTAL;
	button.addActionListener (this);
	add (button, c);

	button = new JButton ("Del");
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = 1;
	button.addActionListener (this);
	add (button, c);


	// GeometryPath:
	c.anchor     = GridBagConstraints.NORTHWEST;
	c.gridwidth  = 1;
	c.gridheight = 2;
	c.weighty    = 1;
	c.fill       = GridBagConstraints.NONE;
	add (new JLabel ("Geometry path"), c);


	for (int i = 0; i < Config.geometryPath.size(); i++)
	{
	    geometryModel.addElement (Config.geometryPath.elementAt (i));
	}
	geometryPath = new JList (geometryModel);

	c.weightx    = 1;
	c.gridwidth  = GridBagConstraints.RELATIVE;
	c.fill       = GridBagConstraints.BOTH;
	add (new JScrollPane (geometryPath), c);

	button = new JButton ("Add");
	button.setActionCommand ("ADD_GEO");
	c.anchor     = GridBagConstraints.NORTHEAST;
	c.weightx    = 0;
	c.weighty    = 0;
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.RELATIVE;
	c.fill       = GridBagConstraints.HORIZONTAL;
	button.addActionListener (this);
	add (button, c);

	button = new JButton ("Del");
	button.setActionCommand ("DEL_GEO");
	c.gridwidth  = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	button.addActionListener (this);
	add (button, c);



    }





    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();
	if ("Add".equals (cmd))
	{
	    texturePathChooser.setVisible (true);
	    File path = new File (texturePathChooser.getDirectory());
	    if (!path.exists())
	    {
		JOptionPane.showMessageDialog (this, "Path not found. ("
					       + path.getAbsolutePath() + ")");
	    }
	    DefaultListModel model = (DefaultListModel)texturePath.getModel();
	    String p = texturePathChooser.getDirectory();
	    p = p.substring (0, p.length() - 1);
	    textureModel.addElement (p);
	}
	else if ("Del".equals (cmd))
	{
	    int i = texturePath.getMinSelectionIndex();
	    if (i >= 0 && i < textureModel.getSize())
	    {
		textureModel.remove (i);
	    }
	}
	else if ("ADD_GEO".equals (cmd))
	{
	    geometryPathChooser.setVisible (true);
	    File path = new File (geometryPathChooser.getDirectory());
	    if (!path.exists())
	    {
		JOptionPane.showMessageDialog (this, "Path not found. ("
					       + path.getAbsolutePath() + ")");
	    }
	    DefaultListModel model = (DefaultListModel)geometryPath.getModel();
	    String p = geometryPathChooser.getDirectory();
	    p = p.substring (0, p.length() - 1);
	    geometryModel.addElement (p);
	}
	else if ("DEL_GEO".equals (cmd))
	{
	    int i = geometryPath.getMinSelectionIndex();
	    if (i >= 0 && i < geometryModel.getSize())
	    {
		geometryModel.remove (i);
	    }
	}
    }











    public void apply()
    {
	Config.openPath    = openPath.getText();
	Config.savePath    = savePath.getText();

	Config.texturePath = new Vector ();
	for (int i = 0; i < textureModel.getSize(); i++)
	{
	    Config.texturePath.add (textureModel.elementAt (i));
	}

	Config.geometryPath = new Vector ();
	for (int i = 0; i < geometryModel.getSize(); i++)
	{
	    Config.geometryPath.add (geometryModel.elementAt (i));
	}
    }





    public String toString()
    {
	return "File";
    }








}
