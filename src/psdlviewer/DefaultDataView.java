package psdlviewer;
import psdlviewer.*;

/** Companionclass with the ListView class. Represents the visualization of the
 *  selected item in the listView.
 *
 */

import java.awt.Component;

public abstract class DefaultDataView
implements DataView
{
    protected Component component;

    public Component getComponent()
    {
	return component;
    }
}
