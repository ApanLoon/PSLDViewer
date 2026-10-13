package psdlviewer;
import psdlviewer.*;

/** Class for my GIF image memory loader.
 *
 */

import java.awt.Toolkit;
import java.awt.Image;

public class GIF
extends TextureLoader
{
    public String getExtension()
    {
	return "gif";
    }

    public Image loadImage (String url)
    {
	return Toolkit.getDefaultToolkit().getImage (url);
    }
}
