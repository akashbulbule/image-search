package com.Indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class GenerateFiles {
	public static ArrayList<String> filelist,flrtmp;
    static int count =0,cnt=0;
    static ImageIndex indexObj=null;
    public static PrintStream lg=null;
    public static String currfle = null;
    public static String dir1=null,dir=null;
    public static boolean flag1=false;
    public static void main(String[] args) {
	   String cmd = "./temp1.sh"; // this is the command to execute in the Unix shell
	   filelist = new ArrayList<String>();
	   String file=null; 
	
	   String fle=null;	   
	   Runtime.getRuntime().addShutdownHook(new Thread() {
           public void run() {
           	boolean flag=false;
               System.out.println("Program will exit now..."+"\nSee log for details.");
                
               FileWriter fp1=null;
               
       	
           
               try {
       			fp1 = new FileWriter("/home/akashbulbule/Desktop/curfile.txt",true);
       		} catch (IOException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace(lg);
       		}
               
               try{
               fp1.write("Current Directory is:\n");
               
          	 int len = currfle.length()-4;
          	 while(currfle.charAt(len)!='/')len--;
          	 dir1 = currfle.substring(0, len);
             
             
               fp1.write(dir1 +"\n");
               }catch(Exception e){e.printStackTrace(lg);}
               try{
           		fp1.write("Current File to be indexed\n");
           		if(flag1){currfle=null;}
           		fp1.write(currfle + "\n");
       			 		
                   }
                   catch(Exception e){e.printStackTrace(lg);}
               try{
                   fp1.write("list to be indexed\n");
                   if(flag){
                   	fp1.write("none");
                   }
                   else
                   {
                   	while(filelist.iterator().hasNext()){
                   		
                   			if(!flag){
                   		    while(filelist.iterator().next()!=currfle);
                   		    flag=true;
                   			}
                   			else
                   			{
                   		    fp1.write(filelist.iterator().next() + "\n");
                   			}
                   		
                   	}
                   }
                 fp1.close();
               }
               catch(Exception e){e.printStackTrace(lg);}
               Runtime.getRuntime().halt(0);
           }
            
	   }
	    
           );
 
	   
	   
	   FileOutputStream log=null;
       try {
			log = new FileOutputStream("/home/akashbulbule/Desktop/log.txt",true);
			lg = new PrintStream(log);
			//printStackTrace(lg);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			e.printStackTrace(lg);
		}
	  /* Runtime.getRuntime().addShutdownHook(new Thread(){
		  	public void run(){
		  		Iterator<String> it = filelist.iterator();
		  		System.out.println("script file list:");
		  		System.out.println("no of files" + cnt + "\n");
		  		while(it.hasNext()){
		  			String str = it.next();
		  			System.out.println(str);
		  		}
		  	}
		});*/   
	// create a process for the shell
	ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
	pb.redirectErrorStream(true); // use this to capture messages sent to stderr
	Process shell=null;
	pb.directory(new File("/home/akashbulbule/images"));//set up working directory(for the script)
	try {
		shell = pb.start();// start the script
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace(lg);
	}
	BufferedReader buff = new BufferedReader(new InputStreamReader(shell.getInputStream()));  // this captures the output from the command
	/*try {
		//int shellExitStatus = shell.waitFor();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // wait for the shell to finish and get the return code
	// at this point you can process the output issued by the command
	// for instance, this reads the output and writes it to System.out:*/
	
	//int cnt=0;
	
	try {
		while ((file = buff.readLine()) != null) {
			 
			 if(file.endsWith(".jpg") || file.endsWith(".bmp") || file.endsWith(".png")){
				 fle = new  StringBuffer(dir).append(File.separator)
							.append(file).toString();
				 cnt++;
				 filelist.add(fle);
				 dir1=dir;
			 }
			 else
			 {
				 dir=file;
			 }
			 System.out.println(file);
			 count++;
			 if (count==10000)
			 System.exit(0);
			 
			
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// close the stream
	try {buff.close();} catch (IOException ignoreMe) {}
	for(String f : filelist){
		currfle=f;
		
		
		if(indexObj==null)	
			 indexObj=new ImageIndex(f);
   	   else
   		   indexObj.indexImage(f);
	}
	System.out.println("Over: total " + filelist.size() + "images indexed in all"); 
    flag1=true;
   }
   
}



