package psdlviewer;
import psdlviewer.*;

/** Interface that enables a component to be a drag source and a drop target
 *  for a DNDComponent.
 *
 */

public interface SimpleDNDSourceTarget
{
    /** @return the object that is dragged.
     *
     */
    public Object drag();

    /** Handles the object dropped onto this component.
     *
     *  Use methods with different signatures to handle dropped objects of
     *  different types.
     *
     */
//    public void drop (Object o, DropTargetDropEvent event);
}
