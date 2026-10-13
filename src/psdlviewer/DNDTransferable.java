package psdlviewer;
import psdlviewer.*;

/** Small vessel of transfer data between drag sources and drop targets.
 *  The data includes the source of the transfer data.
 *
 */

import java.awt.Component;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class DNDTransferable
implements Transferable
{
    /** Source component of this transferable.
     *
     */
    protected Component source;



    protected static DataFlavor flavor = new DataFlavor (
						   DNDTransferable.class,
						  "DNDTransferable");



    public static DataFlavor getDataFlavor()
    {
	return flavor;
    }


    public DNDTransferable (Component source_)
    {
	super();
	source = source_;
    }


    public Component getSource()
    {
	return source;
    }


    // Transferable methods:
    //
    //
    public DataFlavor[] getTransferDataFlavors()
    {
	DataFlavor[] df = new DataFlavor[1]; 
	df[0] = flavor;
	return df;
    }

    public boolean isDataFlavorSupported (DataFlavor f)
    {
	return flavor.equals (f);
    }

    public Object getTransferData (DataFlavor f)
    throws UnsupportedFlavorException
    {
	if (!isDataFlavorSupported (f))
	{
	    throw new UnsupportedFlavorException (f);
	}
	return this;
    }
}
