package extraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractReuters 
{
	private File sourceDir;
	private File destDir;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    Pattern EXTRACTION_PATTERN = Pattern.compile("<BODY>(.*?)</BODY>");
    private static String[] META_CHARS = {"&", "<", ">", "\"", "'"};
    private static String[] META_CHARS_SERIALIZATIONS = {"&amp;", "&lt;", "&gt;", "&quot;", "&apos;"};
    private static long txtcounter=0;
    private static long sgmcounter=0;
	
    ExtractReuters(File sourceDir, File destDir)
	{
		this.sourceDir=sourceDir;
		this.destDir=destDir;
		System.out.println("In Constructor of Extract Reuters");
	}
	
    public void findSGMFiles()
    {
        FileFilter sgmOnly= new FileFilter()
        {
        	public boolean accept(File a)
        	{
        		return a.getName().endsWith(".sgm");
        	}
        };
    	
        File [] sgmFiles = sourceDir.listFiles(sgmOnly);
    	
        if(!destDir.exists())
    	{
    		destDir.mkdir();
    	}
    	else
    	{
    		File[] a = destDir.listFiles();
    		for(int i=0;i<a.length;i++)
    		{
    			a[i].delete();
    		}
    	}
        
    	if (sgmFiles != null && sgmFiles.length > 0)
        {
            for (int i = 0; i < sgmFiles.length; i++)
            {
                File sgmFile = sgmFiles[i];
                System.out.println("Found file : " + sgmFile.toString());
                sgmcounter++;
                extractFile(sgmFile);
            }
        }
    }
    
    protected void extractFile(File sgmFile)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(sgmFile));

            StringBuffer buffer = new StringBuffer(1024);
            StringBuffer outBuffer = new StringBuffer(1024);

            String line = null;
            int index = -1;
            int docNumber = 0;
            while ((line = reader.readLine()) != null)
            {
                //when we see a closing reuters tag, flush the file

                if ((index = line.indexOf("</REUTERS")) == -1)
                {
                    //Replace the SGM escape sequences

                    buffer.append(line).append(' ');//accumulate the strings for now, then apply regular expression to get the pieces,
                }
                else
                {
                    //Extract the relevant pieces and write to a file in the output dir
                    Matcher matcher = EXTRACTION_PATTERN.matcher(buffer);
                    while (matcher.find())
                    {
                        for (int i = 1; i <= matcher.groupCount(); i++)
                        {
                            if (matcher.group(i) != null)
                            {
                                outBuffer.append(matcher.group(i));
                            }
                        }
                        outBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
                    }
                    String out = outBuffer.toString();
                    for (int i = 0; i < META_CHARS_SERIALIZATIONS.length; i++)
                    {
                        out = out.replaceAll(META_CHARS_SERIALIZATIONS[i], META_CHARS[i]);
                    }
                    File outFile = new File(destDir, sgmFile.getName().substring(0, 9) + "-" + (docNumber++) + ".txt");
                    //System.out.println("Writing " + outFile.toString());
                    txtcounter++;
                    //System.out.println(out);
                    try(  PrintWriter writer = new PrintWriter( outFile.toString() )  ){
                        writer.println( out );
                    }
                    /*FileWriter writer = new FileWriter(outFile);
                    writer.write(out);
                    writer.close();*/
                    outBuffer.setLength(0);
                    buffer.setLength(0);
                }
            }
            reader.close();
        }

        catch (
                IOException e
                )

        {
            throw new RuntimeException(e);
        }
    }
    
	public static void main(String[] args) 
	{
		File sourceDir=new File("/home/vishakh/workspace/Classifiers/res");
		System.out.println("Found source at" + sourceDir.toString());
		File destDir=new File("Output");
		ExtractReuters e = new ExtractReuters(sourceDir,destDir);
		e.findSGMFiles();
		System.out.println("Success:\n Read sgm files : "+sgmcounter+"\n Made text files : "+txtcounter);
	}
}
