package psdlviewer;
import psdlviewer.*;

public interface BlockListChangedListener
{
    public void blockMoved (int from,
			    int to,
			    BlockList list);
    public void blockAdded (int index,
			    BlockList list);
    public void blockRemoved (int index,
			      BlockList list);
}
