package psdlviewer.pkg;

import psdlviewer.ULong;

import java.io.InputStream;

public class Model
{
    Section sections[];
    
    public long nSections;
    public long nVerticesTot;
    public long nIndicesTot;
    public long nSections2;
    public long flags;

    public static final int FLAG_COORDINATE = 0x0002;
    public static final int FLAG_NORMAL     = 0x0010;
    public static final int FLAG_NGON       = 0x0040;
    public static final int FLAG_TEXCOORD   = 0x0100;

    public PKG pkg;

    public Model (PKG pkg_, InputStream in)
    throws Exception
    {
	pkg = pkg_;

	nSections    = ULong.parseULong (in);
	nVerticesTot = ULong.parseULong (in);
	nIndicesTot  = ULong.parseULong (in);
	nSections2   = ULong.parseULong (in);
	flags        = ULong.parseULong (in);

	sections = new Section[(int)nSections];
	for (int section = 0; section < nSections; section++)
	{
	    sections[section] = new Section (this, in);
	}
    }
}
