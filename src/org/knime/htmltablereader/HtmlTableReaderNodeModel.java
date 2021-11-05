package org.knime.htmltablereader;


import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;

/**
 * <code>NodeModel</code> for the "HtmlTableReader" node.
 *
 * @author Maxson Jordan
 */
public class HtmlTableReaderNodeModel extends NodeModel {
	
	private static final String KEY_LOCATION_FILE = "location";
	
	private final SettingsModelString localFileSetting = createLocalFileSetting();
	static SettingsModelString createLocalFileSetting() {
		return new SettingsModelString(KEY_LOCATION_FILE, "");
	}
	
	private static final NodeLogger LOGGER = NodeLogger.getLogger(HtmlTableReaderNodeModel.class);

	
    /**
     * Constructor for the node model.
     */
    protected HtmlTableReaderNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(new PortType[0], new PortType[]{BufferedDataTable.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

    
    	String locFile = localFileSetting.getStringValue();
    	File file = new File(locFile);
    	if(!file.exists())
    	{
    		InvalidSettingsException ivalid = new InvalidSettingsException("Arquivo não encontrado!");
			throw ivalid ;
    	}
    	
    	LOGGER.info(locFile);
    	FileReader input = new FileReader(locFile,Charset.forName("UTF8"));
      
    	 BufferedReader br = new BufferedReader(new FileReader(locFile));
       
    	String resultFinal = "";
         try {
        	 String line;
             while ((line = br.readLine()) != null) {
                resultFinal+=line;
             }
         } finally {
             br.close();
         }
    	
         //System.out.println(resultFinal);
         System.out.println("Inicio");
         
         BufferedDataTable out = null;
         
         //Converter Html Table para BufferedContainer
         Document html = Jsoup.parse(resultFinal);
         Elements linhas = html.getElementsByTag("tr");
         System.out.println("Linhas :"+linhas.size());
         if(linhas.size() >0) {
   
        	 int numColumns = 0;
        	 for (int i = 0; i < linhas.size(); i++) {
        		 int nSize = linhas.get(i).getElementsByTag("td").size();
        		 if(numColumns < nSize)
        			 numColumns =nSize;
			}
        	 
        	 Elements heads = linhas.get(0).getElementsByTag("th");
        	 int numHeads = heads.size();
        	 
        	 
        	 DataColumnSpec[] colList = new DataColumnSpec[numColumns];
        	 for (int i = 0; i < colList.length; i++) {
        		 String colName = "Unknown";
        		 if(i<numHeads) {
        			 colName = heads.get(i).html();
        		 }
        		 colList[i] = (new DataColumnSpecCreator(colName, StringCell.TYPE).createSpec());
         	 }
        	 DataTableSpec spec = new DataTableSpec(colList);
        	 BufferedDataContainer bufferedDataContainer = exec.createDataContainer(spec, true);
        	 
        	 
        	 
        	 //Alimentar
	         for (int i = numHeads>0?1:0; i < linhas.size(); i++) {
	        	
	 			Element linha = linhas.get(i);
	 			Elements celulas = linha.getElementsByTag("td");
	 			
	 			
	 			 System.out.println("Linhas :"+i+"columns:"+celulas.size());
	 			
	 			DataCell[] cells = new DataCell[spec.getNumColumns()];
	 			int numCells = celulas.size();
	 			
	 			for (int j = 0; j < numColumns; j++) {
	 				if(j < numCells ) {
	 					Element celula = celulas.get(j);
		 				cells[j] = new StringCell(celula.html());
	 				}else {
	 					cells[j] = new StringCell("");
	 				}
	 				
	 			}
	 			DataRow dataRow = new DefaultRow("RowID"+i, cells);
	 			bufferedDataContainer.addRowToTable(dataRow);
	 		}
	         bufferedDataContainer.close();
	         out = bufferedDataContainer.getTable();
         }
         
         
         
    	//Necessita da seguinte estrutura
    	//BufferedContainer->DataTableSpec->DataColumnSpec
    	//BufferedContainer<-Rows
    	//DataSpec <- DataRows
    	
    	// Criar as Colunas
    	
    	
    	
    	 // Criar Data Spec 
    	
    	
    	 
    	 //Criar Linhas
   
    	 
    	 
    	 //Criar Container
    	

        
         return new BufferedDataTable[]{out};
   
        // TODO: Return a BufferedDataTable for each output port 
        //return new BufferedDataTable[]{};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	//InvalidSettingsException ivalid = new InvalidSettingsException("Arquivo não encontrado!");
		//throw ivalid ;
        // TODO: generated method stub
    	
    	String locFile = localFileSetting.getStringValue();
    	File file = new File(locFile);
    	if(!file.exists()) {
    		throw new InvalidSettingsException("Arquivo não encontrado.");
    	}
   
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    	localFileSetting.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    	localFileSetting.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    	
    	
    	localFileSetting.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	System.out.print(internDir.getAbsolutePath());
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}

