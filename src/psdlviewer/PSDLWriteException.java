package psdlviewer;
import psdlviewer.*;

import java.lang.Exception;

public class PSDLWriteException
extends Exception
{
    protected String message;
    public PSDLWriteException (String s)
    {
	super();
	message = s;
    }

    public void setMessage (String s)
    {
	message = s;
    }

    public String getMessage()
    {
	return message;
    }
}
