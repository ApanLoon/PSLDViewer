package psdlviewer;
import psdlviewer.*;

/** Interface with DND functions.
 *
 */

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;


public interface DND
{
    public Transferable startDrag();

    public void drop (DropTargetDropEvent event);
}
