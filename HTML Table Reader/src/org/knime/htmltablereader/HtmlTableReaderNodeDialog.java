package org.knime.htmltablereader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "HtmlTableReader" node.
 * 
 * @author Maxson Jordan
 */
public class HtmlTableReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the HtmlTableReader node.
     */
    protected HtmlTableReaderNodeDialog() {

    	SettingsModelString location = HtmlTableReaderNodeModel.createLocalFileSetting();
    	addDialogComponent(new DialogComponentFileChooser(location,"", "html","Texto"));  	
    }
}

