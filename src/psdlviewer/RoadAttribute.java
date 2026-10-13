package psdlviewer;
import psdlviewer.*;

/** Abstract road attribute super class
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public abstract class RoadAttribute
extends VertexListAttribute
{
    char nSections;
    public abstract int nSections();
}
