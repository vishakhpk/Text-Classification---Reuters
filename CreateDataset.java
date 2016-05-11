package tfidf;
import java.io.*;
import java.nio.file.Files;

import weka.core.*;
 
/**
 * Builds an arff dataset from the documents in a given directory.
 * Assumes that the file names for the documents end with ".txt".
 *
 * Usage:<p/>
 *
 * TextDirectoryToArff <directory path> <p/>
 *
 * @author Richard Kirkby (rkirkby at cs.waikato.ac.nz)
 * @version 1.0
 */
public class tfidf {
 
  public Instances createDataset(String directoryPath) throws Exception {
 
    @SuppressWarnings({ "deprecation", "rawtypes" })
	FastVector atts = new FastVector(1);
    atts.addElement(new Attribute("name", (FastVector) null));
    atts.addElement(new Attribute("body", (FastVector) null));
    Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);
    //int counter=0;
    File dir = new File(directoryPath);
    File[] files = dir.listFiles();
    System.out.println(files.length);
    for (int i = 0; i < files.length; i++) 
    {
	      if (files[i].getName().endsWith(".txt")) 
	      {
	    	  try
	    	  {
			      double[] newInst = new double[2];
			      newInst[0] = (double)data.attribute(0).addStringValue(files[i].getName());
			      //System.out.println("Here"+i);
			      File txt = new File(files[i].toString());
			      InputStreamReader is;
			      is = new InputStreamReader(new FileInputStream(txt));
			      StringBuffer txtStr = new StringBuffer();
			      //System.out.println("Here @" +i);
			      int c;
			      while ((c = is.read()) != -1) 
			      {
			        txtStr.append((char)c);
			      }
			      //System.out.println(txtStr.toString());
			      newInst[1] = (double)data.attribute(1).addStringValue(txtStr.toString());
			      data.add(new DenseInstance(1.0, newInst));
			      is.close();
			      //System.out.println("Here #" +i);
			      
			   } 
	    	  catch (Exception e) 
	    	  {
	    		  //counter++;
		      System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
	    	  }
	      }
    }
    //System.out.println("Number of text Files :" +counter);
    return data;
  }
 
  public static void main(String[] args) 
  {
	  
      tfidf tdta = new tfidf();
      try 
      {
    	  Instances dataset = tdta.createDataset("/home/vishakh/workspace/ParseSGM/Output");
    	  System.out.println("Instances in Dataset : " +dataset.numInstances());
    	  System.out.println("Attributes in Dataset : " +dataset.numAttributes());
    	  System.out.println(dataset.firstInstance().toString());
      } catch (Exception e) 
      {
    	  System.err.println(e.getMessage());
    	  e.printStackTrace();
      } 
    
  }
}
