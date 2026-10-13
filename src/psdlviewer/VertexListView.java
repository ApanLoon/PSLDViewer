package psdlviewer;
import psdlviewer.*;

/** Comonent displaying a list of vertices.
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JMenuItem;

import javax.swing.event.ListSelectionEvent;

import java.awt.Container;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VertexListView
extends ListView
implements VertexRefSelectionListener
{
    public VertexListView (String name)
    {
	super (name, "Coordinate (X, Y, Z)", null);
	initialize();
    }


    public VertexListView (Element root)
    {
	super ("", "-", null);
	initialize();
	setOptions (root);
    }

    protected void initialize()
    {
	menu.getComponent (1).setEnabled (false);
	menu.getComponent (2).setEnabled (false);

	// Add translate vertices function to pop up menu:
	JMenuItem item;

	item = new JMenuItem ("Translate...");
	item.addActionListener (this);
	menu.add (item);


	jTable.setDefaultEditor (Vertex3D.class, new VertexCellEditor());
	model.setEditable (true);
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
	Element root = doc.createElement ("VERTEX_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }




    // ActionListener methods:
    //
    //
    public void actionPerformed (ActionEvent e)
    {
	String cmd = e.getActionCommand();

	if ("Translate...".equals (cmd))
	{
	    translateSelectedVertices();
	}
	else
	{
	    super.actionPerformed (e);
	}
    }



    /** Pop up a dialog box with three translation offsets and translates the
     *  selected vertices accordingly.
     *
     */
    protected void translateSelectedVertices()
    {
	JPanel pane = new JPanel();
	JTextField x = new JTextField ("0");
	JTextField y = new JTextField ("0");
	JTextField z = new JTextField ("0");

	GridBagLayout gridBag = new GridBagLayout();
	pane.setLayout (gridBag);

	GridBagConstraints labelc  = new GridBagConstraints();
	GridBagConstraints valuec  = new GridBagConstraints();

	labelc.anchor = GridBagConstraints.NORTHWEST;
	labelc.gridwidth = GridBagConstraints.RELATIVE;
	labelc.weightx = 0;
	labelc.fill = GridBagConstraints.NONE;

	valuec.anchor = GridBagConstraints.NORTHWEST;
	valuec.gridwidth = GridBagConstraints.REMAINDER;
	valuec.weightx = 1;
	valuec.fill = GridBagConstraints.HORIZONTAL;

	pane.add (new JLabel ("X"), labelc);
	pane.add (x, valuec);

	pane.add (new JLabel ("Y"), labelc);
	pane.add (y, valuec);

	pane.add (new JLabel ("Z"), labelc);
	pane.add (z, valuec);


	if (JOptionPane.showOptionDialog (Config.mainWindow,
					  pane,
					  "Translate selected vertices",
					  JOptionPane.OK_CANCEL_OPTION,
					  JOptionPane.PLAIN_MESSAGE,
					  null,
					  null,
					  null) == JOptionPane.OK_OPTION)
	{
	    float xOffset = Float.parseFloat (x.getText());
	    float yOffset = Float.parseFloat (y.getText());
	    float zOffset = Float.parseFloat (z.getText());

	    int[] indices = jTable.getSelectedRows();
	    for (int i = 0; i < indices.length; i++)
	    {
		Vertex3D v = ((VertexList)model.getList()).vertexAt (indices[i]);
		v.x += xOffset;
		v.y += yOffset;
		v.z += zOffset;
	    }
//	    model.fireTableDataChanged();
	}
    }




    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	Object[] selection = getSelectedValues();
	VertexList l = (VertexList)getList();
	l.verticesSelected (selection);
    }





    // VertexRefSelectionListener methods:
    //
    //
    public void vertexRefsSelected (Object[] refs)
    {
	if (refs.length >= 1)
	{
	    int first = 0;
	    jTable.clearSelection();
	    for (int i = 0; i < refs.length; i++)
	    {
		VertexRef v = (VertexRef)refs[i];
		int index = v.getIndex();
		if (i == 0)
		{
		    first = index;
		}
		jTable.addRowSelectionInterval (index, index);
	    }

	    // Scroll to first selected row:
	    //
	    tableScrollPane.getViewport().setViewPosition (
			   jTable.getCellRect (first, 0, true).getLocation());
	}
    }





    // SimpleDNDSourceTarget methods:
    //
    //

    /** @return the object that is dragged.
     *
     */
    public Object drag()
    {
	int[] indices = jTable.getSelectedRows();
	VertexRef[] v = new VertexRef[indices.length];
	for (int i = 0; i < indices.length; i++)
	{
	    v[i] = new VertexRef ((char)indices[i]);
	}
	return v;
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
    }


    /** Adds a row to this table.
     *
     */
    protected void addRow (int mode)
    {
	// TODO: All modes adds last, should add a vertex relocation for BEFORE
        //       and AFTER modes.

	model.getList().add (new Vertex3D());
	model.fireTableDataChanged();
    }


}
