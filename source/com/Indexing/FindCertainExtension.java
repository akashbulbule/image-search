package com.Indexing;

import java.io.*;
import java.util.*;

public class FindCertainExtension {
 
	private static final String FILE_DIR = "/root/Desktop/Pics4";
	private static final String FILE_TEXT_EXT = ".jpg";
    ImageIndex indexObj=null;
    public static String currfle = null,dr=FILE_DIR;
    ArrayList<String> filelst;
    static String[] list;
    private static int count=0;
    public static PrintWriter lg=null;
    public static FileWriter writer;
    
	public static void main(String args[]) {
		int totalFiles;
        /*Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	boolean flag=false;
                System.out.println("Program will exit now..."+"\nSee log for details.");
                 
                FileWriter fp1=null;
                
        	
            
                try {
        			fp1 = new FileWriter("curfile.txt",true);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                
                try{
                fp1.write("Current Directory is:\n");
                fp1.write(dr +"\n");
                }catch(Exception e){}
                try{
            		fp1.write("Current File to be indexed\n");
            		fp1.write(currfle + "\n");
        			 		
                    }
                    catch(Exception e){}
                try{
                    fp1.write("list to be indexed\n");
                    if(count==list.length){
                    	fp1.write("none");
                    }
                    else
                    {
                    	for (String f : list){
                    		if(f.equals(currfle)){
                    			flag=true;
                    		}
                    		if(flag=true){
                    			fp1.write(f + "\n");
                    		}
                    	}
                    }
                  fp1.close();
                }
                catch(Exception e){}
           }
            }
            );*/
    	FileOutputStream log=null;
    	
        try {
			log = new FileOutputStream("log.txt",true);
			writer=new FileWriter("Files_indexed.txt",true);
			lg = new PrintWriter(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			
		}
		totalFiles=new FindCertainExtension().listFile(FILE_DIR, FILE_TEXT_EXT);
		System.out.println("Over: total " + totalFiles + "images indexed in all");
	    try{	
		 writer.close();
	    }catch(Exception e){e.printStackTrace();}
	}
 
	public int listFile(String folder, String ext) {
        String imageFile=null;
		GenericExtFilter filter = new GenericExtFilter(ext);
        filelst = new ArrayList<String>();
		File dir = new File(folder);
 
		if(dir.isDirectory()==false){
			System.out.println("Directory does not exists : " + FILE_DIR);
			return 0;
		}
 
		// list out all the file name and filter by the extension
		
        
		
		list = dir.list(filter);
        
		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return 0;
		}
        
        
		
		for (String file : list) {
			imageFile = new StringBuffer(FILE_DIR).append(File.separator)
					.append(file).toString();
			currfle=imageFile;
			count++;
			if(indexObj==null)	
			 indexObj=new ImageIndex(imageFile);
    	   else
    		   indexObj.indexImage(imageFile);
		}//end for

		
		System.out.println("\n************SSTREE is*************");
		indexObj.display();
	     
	   
	     System.out.println("FILE INDEXED: " + imageFile);
	     currfle = "none";
	     
          return list.length;
	}//end void listFile()
 

	// inner class, generic extension filter
	public class GenericExtFilter implements FilenameFilter {
 
		private String ext;
 
		public GenericExtFilter(String ext) {
			this.ext = ext;
		}
 
		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
}//end class FindCertainExtension
