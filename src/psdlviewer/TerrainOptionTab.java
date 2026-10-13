package psdlviewer;
import psdlviewer.*;

/** Option panel for the Terrain.
 *
 */

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.Font;

import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;

public class TerrainOptionTab
extends JPanel
implements OptionTab
{
    protected Terrain terrain;

    protected JComboBox drawingMode;
    protected JComboBox colouringMode;
    protected JComboBox lightingMode;

    protected JCheckBox showBlockIndices;
    protected JCheckBox renderBlockAttributes;
    protected JCheckBox showBlockBound;

    protected JCheckBox showBound;

    protected JCheckBox showINST;
    protected JCheckBox showPathsets;
    protected JCheckBox showBAI;

    protected JComboBox  fontFamily;
    protected JTextField fontSize;


    /** Creates a new option panel.
     *
     */
    public TerrainOptionTab (Terrain t)
    {
	terrain = t;

	GridBagLayout gridBag = new GridBagLayout();
	setLayout (gridBag);

	GridBagConstraints singlec = new GridBagConstraints();
	GridBagConstraints labelc  = new GridBagConstraints();
	GridBagConstraints valuec  = new GridBagConstraints();

	singlec.anchor = GridBagConstraints.NORTHWEST;
	singlec.gridwidth = 1;

	labelc.anchor = GridBagConstraints.NORTHWEST;
	labelc.gridwidth = GridBagConstraints.RELATIVE;
	labelc.weightx = 0;
	labelc.fill = GridBagConstraints.NONE;

	valuec.anchor = GridBagConstraints.NORTHWEST;
	valuec.gridwidth = GridBagConstraints.REMAINDER;
	valuec.weightx = 1;
	valuec.fill = GridBagConstraints.HORIZONTAL;


	add (new JLabel ("Drawing mode"), singlec);
	drawingMode = new JComboBox (Terrain.drawingNames);
	add (drawingMode, valuec);

	add (new JLabel ("Colouring mode"), singlec);
	colouringMode = new JComboBox (Terrain.colouringNames);
	add (colouringMode, valuec);

	add (new JLabel ("Lighting mode"), singlec);
	lightingMode = new JComboBox (Terrain.lightingNames);
	add (lightingMode, valuec);


	add (new JLabel ("Show block indices"), singlec);
	showBlockIndices = new JCheckBox();
	add (showBlockIndices, singlec);

	add (new JLabel ("Show INST"), labelc);
	showINST = new JCheckBox();
	add (showINST, valuec);

	add (new JLabel ("Render block attributes"), singlec);
	renderBlockAttributes = new JCheckBox();
	add (renderBlockAttributes, singlec);

	add (new JLabel ("Show pathsets"), labelc);
	showPathsets = new JCheckBox();
	add (showPathsets, valuec);


	add (new JLabel ("Show block bounds"), singlec);
	showBlockBound = new JCheckBox();
	add (showBlockBound, singlec);

	add (new JLabel ("Show BAI"), labelc);
	showBAI = new JCheckBox();
	add (showBAI, valuec);

	add (new JLabel ("Show city bounds"), singlec);
	showBound = new JCheckBox();
	add (showBound, valuec);


	singlec.gridheight = GridBagConstraints.REMAINDER;
	singlec.weighty = 1;

	labelc.gridheight = GridBagConstraints.REMAINDER;
	labelc.weighty = 1;

	valuec.gridheight = GridBagConstraints.REMAINDER;
	valuec.weighty = 1;


	add (new JLabel ("Font family"), singlec);
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	fontFamily = new JComboBox (ge.getAvailableFontFamilyNames());
	add (fontFamily, singlec);

	add (new JLabel ("Font size"), singlec);
	fontSize = new JTextField();
	add (fontSize, valuec);


	if (terrain != null)
	{
	    drawingMode.setSelectedIndex (terrain.getDrawingMode());
	    colouringMode.setSelectedIndex (terrain.getColouringMode());
	    lightingMode.setSelectedIndex (terrain.getLightingMode());
	    showBlockIndices.setSelected (terrain.getShowBlockIndices());
	    renderBlockAttributes.setSelected (terrain.getRenderBlockAttributes());
	    showBlockBound.setSelected (terrain.getShowBlockBound());
	    showBound.setSelected (terrain.getShowBound());

	    showINST.setSelected (terrain.getShowINST());
	    showPathsets.setSelected (terrain.getShowPathset());
	    showBAI.setSelected (terrain.getShowBAI());


	    if (terrain.font != null)
	    {
		Font font = terrain.font.getFont();
		fontFamily.setSelectedItem (font.getFamily());
		fontSize.setText ("" + font.getSize2D());
	    }
	}

    }





    /** @return the terrain this option view represents.
     *
     */
    public Terrain getTerrain()
    {
	return terrain;
    }



    public void apply()
    {
	if (terrain != null)
	{
	    terrain.setDrawingMode      (drawingMode.getSelectedIndex());
	    terrain.setColouringMode    (colouringMode.getSelectedIndex());
	    terrain.setLightingMode     (lightingMode.getSelectedIndex());
	    terrain.setShowBlockIndices (showBlockIndices.isSelected());
	    terrain.setRenderBlockAttributes (renderBlockAttributes.isSelected());
	    terrain.setShowBlockBound   (showBlockBound.isSelected());
	    terrain.setShowBound        (showBound.isSelected());

	    terrain.setShowINST         (showINST.isSelected());
	    terrain.setShowPathset      (showPathsets.isSelected());
	    terrain.setShowBAI          (showBAI.isSelected());

	    terrain.setFont ((String)fontFamily.getSelectedItem(),
			     fontSize.getText());

	    terrain.redraw();
	}
    }





    public String toString()
    {
	return "Terrain";
    }
}
