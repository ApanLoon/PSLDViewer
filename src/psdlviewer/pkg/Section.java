package psdlviewer.pkg;

import psdlviewer.Config;
import psdlviewer.UShort;
import psdlviewer.ULong;


import java.io.InputStream;

public class Section
    {
	Model   model;

	int        nStrips;
	int        unknown;
	int        shaderOffset;

	Strip[] strips;





	public Section (Model model_, InputStream in)
        throws Exception
	{
	    model = model_;

	    nStrips        = (int)UShort.parseUShort (in).getCharValue();
	    unknown        = (int)UShort.parseUShort (in).getCharValue();

	    if ((unknown & 0x0100) != 0)
	    {
		shaderOffset     = (int)UShort.parseUShort (in).getCharValue();
	    }
	    else
	    {
		shaderOffset     = (int)ULong.parseULong (in);
	    }

	    strips = new Strip[nStrips];
	    for (int i = 0; i < nStrips; i++)
	    {
		strips[i] = new Strip (this, in);
	    }

	}

    }
