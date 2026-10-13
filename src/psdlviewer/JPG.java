package psdlviewer;
import psdlviewer.*;

/** Class for my JPG image memory loader.
 *
 */

import java.awt.Toolkit;
import java.awt.Image;

public class JPG
extends TextureLoader
{
    public String getExtension()
    {
	return "jpg";
    }

    public Image loadImage (String url)
    {
	return Toolkit.getDefaultToolkit().getImage (url);
    }
}
