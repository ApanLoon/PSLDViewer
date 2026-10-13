package psdlviewer;

import psdlviewer.*;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JViewport;


/** JTable subclass that extends mouse tracking to the entire viewport.
 *
 */
class ExtendedJTable
extends JTable
{
    public ExtendedJTable()
    {
	super();
    }
    
    public ExtendedJTable (TableModel m)
    {
	super (m);
    }

    
    public boolean getScrollableTracksViewportHeight()
    {
	if (getParent() instanceof JViewport)
	{
	    return (((JViewport)getParent()).getHeight()
		    > getPreferredSize().height);
	}
	return false;
    }
}
