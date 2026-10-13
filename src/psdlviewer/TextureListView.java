package psdlviewer;
import psdlviewer.*;

/** Comonent displaying a list of textures.
 *
 */

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.BoxLayout;

import javax.swing.ImageIcon;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import java.awt.Dimension;
import java.awt.Image;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextureListView
extends ListView
implements BlockSelectionListener,
           TableModelListener
{
    protected Vector textureSelectionListeners = new Vector();

    public TextureListView (String name)
    {
	super (name, "Name", null);
	initialize();
    }




    public TextureListView (Element root)
    {
	super ("", "-", null);
	initialize();

	setOptions (root);
    }



    protected void initialize()
    {
	menu.getComponent (1).setEnabled (false);
	menu.getComponent (2).setEnabled (false);

	setDataView (new TextureView());
	addTextureSelectionListener ((TextureView)dataView);

	jTable.setDefaultEditor (TextureMap.class, new TextureCellEditor());
	model.setEditable (true);
        model.addTableModelListener (this);
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
	Element root = doc.createElement ("TEXTURE_LIST_VIEW");
	root.setAttribute ("NAME", viewName);

	return root;
    }







    public void addTextureSelectionListener (TextureSelectionListener l)
    {
	textureSelectionListeners.add (l);
    }



    protected void fireTexturesSelected()
    {
	Object[] selection = getSelectedValues();

	for (int i = 0; i < textureSelectionListeners.size(); i++)
	{
	    TextureSelectionListener l;
	    l = (TextureSelectionListener)textureSelectionListeners.elementAt (i);
	    l.texturesSelected (selection);
	}
    }



    public void tableChanged (TableModelEvent e)
    {
	fireTexturesSelected();
    }




    // ListSelectionListener methods:
    //
    //
    public void valueChanged (ListSelectionEvent e)
    {
	if (e.getValueIsAdjusting())
	    return;

	fireTexturesSelected();
/*
	Object[] selection = getSelectedValues();

	for (int i = 0; i < textureSelectionListeners.size(); i++)
	{
	    TextureSelectionListener l;
	    l = (TextureSelectionListener)textureSelectionListeners.elementAt (i);
	    l.texturesSelected (selection);
	}
*/
    }


    /** Selects all textures directly indicated by texture attributes in
     *  the first of the selected blocks.
     *
     */
    public void blocksSelected (Object[] blocks)
    {
	if (blocks.length >= 1)
	{

	    // TODO: This should be done in a new thread. If another thread is
	    //       already running doing this it must be stopped.
	    //
	    jTable.clearSelection();

	    Block  p = (Block)blocks[0];
	    BlockAttributeList l = p.getAttributes();

	    int nTextures =  model.getList().size();
	    int first = -1;
	    for (int i = 0; i < l.size(); i++)
	    {
	        BlockAttribute a = l.attributeAt (i);
		if (TextureAttribute.class.isInstance (a))
		{
		    TextureAttribute ta = (TextureAttribute)a;
		    TextureMapRef t = ta.ref;
		    if (t == null)
			continue;
		    int index = t.getIndex();
		    // TODO: A bit fishy, this is...
		    if (index != 0xffff)
		    {
			jTable.addRowSelectionInterval (index, index);
			if (first == -1)
			{
			    first = index;
			}
		    }
		}
	    }
	    // Scroll to first selected row:
	    //
	    if (first != -1)
	    {
		tableScrollPane.getViewport().setViewPosition (
			   jTable.getCellRect (first, 0, true).getLocation());
	    }
	}
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
     *  Override this method in subclasses.
     *
     */
    protected void addRow (int mode)
    {
	// TODO: All modes adds last, should add a texture relocation for BEFORE
        //       and AFTER modes.

	model.getList().add (new TextureMap ("", Config.texturePath));
	model.fireTableDataChanged();
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
	TextureMapRef[] v = new TextureMapRef[indices.length];
	for (int i = 0; i < indices.length; i++)
	{
	    v[i] = new TextureMapRef ((char)i,
				      (TextureList)model.getList());
	}
	return v;
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
