package psdlviewer;
import psdlviewer.*;

/** Component displaying a list of pathsets.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JMenuItem;

import javax.swing.JOptionPane;

import javax.swing.BoxLayout;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.awt.Component;
import java.awt.Dimension;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PathsetListView
extends ListView
{
    protected Vector pathsetSelectionListeners = new Vector();

    protected FileDialog openFileChooser = new FileDialog (Config.mainWindow,
						 "Open pathset file",
						 FileDialog.LOAD);
    protected FileDialog saveFileChooser = new FileDialog (Config.mainWindow,
						 "Save pathset file",
						 FileDialog.SAVE);


    public PathsetListView (String name)
    {
	super (name, "Unknown0", null);
	initialize();
    }

    public PathsetListView (Element root)
    {
	super ("", "Unknown0", null);	initialize();

	setOptions (root);
    }


    protected void initialize()
    {

	JMenuItem item = new JMenuItem ("Open pathset...");
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Open a pathset");
	menu.add (item);

	item = new JMenuItem ("Save pathset...");
	item.addActionListener (this);
	item.getAccessibleContext().setAccessibleDescription (
						      "Save selected pathset");
	menu.add (item);


	menu.getComponent (0).setEnabled (false);
	menu.getComponent (1).setEnabled (false);
	menu.getComponent (2).setEnabled (false);
//	menu.getComponent (3).setEnabled (false);
	menu.getComponent (5).setEnabled (false);

	model.setEditable (false);

	jTable.getColumnModel().getColumn (1).setCellRenderer (new PathsetCellRenderer());
	jTable.setDefaultEditor (Pathset.class, new PathsetCellRenderer());
	model.setEditable (true);

    }





    public void setList (Vector v)
    {
	super.setList (v);
	setDataView (null);
    }





    /** Sets the options for this view reading elements in the given
     *  DOM element.
     *
     */
    public void setOptions (Element root)
    {
	setName (root.getAttribute ("NAME"));
    }

    /** @return a DOM element containing the options for this view.
     *
     */
    public Element getOptions (Document doc)
    {
	Element root = doc.createElement ("PATHSET_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }



    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Open pathset...".equals (cmd))
	{
//	    openFileChooser.setDirectory (Config.openPath);
	    openFileChooser.setVisible (true);

	    String fileName = openFileChooser.getFile();
	    if (fileName == null)
	    {
		return;
	    }

	    openPathsetFile (openFileChooser.getDirectory() + fileName);
	}
	else if ("Save pathset...".equals (cmd))
	{
	}
	else
	{
	    super.actionPerformed (e);
	}
    }



    protected void openPathsetFile (String filename)
    {
	try
	{
	    FileInputStream in = new FileInputStream (new File (filename));
	    byte[] id = new byte[4];
	    in.read (id, 0, 4);
	    if (   id[0] != 0x50    // 'P'
		|| id[1] != 0x54    // 'T'
		|| id[2] != 0x48    // 'H'
		|| id[3] != 0x31)   // '1'
	    {
		JOptionPane.showMessageDialog (this,
					       "Not a pathset file");
		return;
	    }

	    Pathset pathset = new Pathset();
	    pathset.setName (filename);

	    int nPaths = (int)ULong.parseULong (in);
	    int unknown0 = (int)ULong.parseULong (in);

	    pathset.setUnknown0 (unknown0);


	    byte[] nameBuf = new byte[32];
	    for (int pa = 0; pa < nPaths; pa++)
	    {
		in.read (nameBuf);
		for (int i = 0; i < nameBuf.length; i++)
		{
		    if (nameBuf[i] == 0x00)
		    {
			nameBuf[i] = 0x20;
		    }
		}
		String propName = new String (nameBuf).trim();

		Path path = new Path();
		path.setName (propName);

		int nPoints = (int)ULong.parseULong (in);

		int unknown1 = (int)ULong.parseULong (in);
		path.setUnknown1 (unknown1);

		for (int po = 0; po < nPoints; po++)
		{
		    int unknown2 = (int)ULong.parseULong (in);

		    float x = CFloat.parseCFloat (in);
		    float y = CFloat.parseCFloat (in);
		    float z = CFloat.parseCFloat (in);

		    PathPoint point = new PathPoint();
		    point.setUnknown2 (unknown2);
		    point.setPoint (new Vertex3D (x, y, z));
		    path.getPoints().add (point);
		}

		int type = in.read();
		path.setType (type);

		int spacing = in.read();
		path.setSpacing (spacing);

		int unknown3 = (int)UShort.parseUShort (in).getCharValue();
		path.setUnknown3 (unknown3);

		pathset.getPaths().add (path); 
	    }
	    model.getList().add (pathset);
	    model.fireTableDataChanged();
	}
	catch (IOException e)
	{
	    JOptionPane.showMessageDialog (this, e.getMessage());
	}
    }






    public void addPathsetSelectionListener (PathsetSelectionListener l)
    {
	pathsetSelectionListeners.add (l);
    }

    public void removePathsetSelectionListener (PathsetSelectionListener l)
    {
	pathsetSelectionListeners.remove (l);
    }







    /** Removes all selected rows from this table.
     *
     */
    protected void removeSelectedRows()
    {
	int[] indices = jTable.getSelectedRows();
	for (int i = indices.length - 1; i >= 0; i--)
	{
	    model.getList().remove (indices[i]);
	}
	model.fireTableDataChanged();

	if (jTable.getRowCount() == 0)
	{
	    removePathsetSelectionListener ((PathsetView)dataView);
	    setDataView (null);
	}

    }


    /** Adds a row to this table.
     *
     * @param mode defines where a row should be added. ADD_LAST, ADD_BEFORE
     *             or ADD_AFTER.
     *
     */
    protected void addRow (int mode)
    {
	PathsetList l = (PathsetList)model.getList();
	int index = 0;

	switch (mode)
	{
	case ADD_LAST:
	    index = l.size();
	    break;

	case ADD_BEFORE:
	    index = jTable.getSelectedRow();
	    if (index == -1)
	    {
		index = 0;
	    }
	    break;

	case ADD_AFTER:
	    int[] sel = jTable.getSelectedRows();
	    if (sel.length == 0)
	    {
		index = 0;
	    }
	    else
	    {
		index = sel[sel.length - 1] + 1;
	    }
	    break;
	}

	l.add (index, new Pathset()); // Inserts before index
	model.fireTableDataChanged();
    }








    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	if (getDataView() == null)
	{
	    PathsetView bv = new PathsetView();
	    setDataView (bv);
//	    setDividerLocation (0.5);

	    addPathsetSelectionListener ((PathsetView)dataView);
	}


	Object[] selection = getSelectedValues();

	// Old method of listener invocation:
	//
	for (int i = 0; i < pathsetSelectionListeners.size(); i++)
	{
	    PathsetSelectionListener l;
	    l = (PathsetSelectionListener)pathsetSelectionListeners.elementAt (i);
	    l.pathsetsSelected (selection);
	}


	// Notify the pathset list that selection has changed:
	//
	PathsetList l = (PathsetList)getList();
	l.pathsetsSelected (selection);
    }









    // SimpleDNDSourceTarget methods:
    //
    //

    /** @return the object that is dragged.
     *
     */
    public Object drag()
    {
	return null;
    }

    /** Handles the object dropped onto this component.
     *
     *  Use methods with different signatures to handle dropped objects of
     *  different types.
     *
     */
    public void drop (Object o)
    {
    }

}

