package psdlviewer;
import psdlviewer.*;

/** Class for TGA image memory loader.
 *
 * TODO: Implement at least the types used in MM2.
 *
 */

import java.awt.Image;


public class TGA
extends TextureLoader
{
    public String getExtension()
    {
	return "tga";
    }


    public Image loadImage (String url)
    {
	return getUnknown();
    }
}
