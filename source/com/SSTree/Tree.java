package com.SSTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.Indexing.FindCertainExtension;
import com.Retrieval.Result;

public class Tree implements Serializable{
   /**
	 * 
	 */
    private static final long serialVersionUID = 8757219875853381011L;
    Node root;
 
 public ArrayList<Result>result;

 
      
/****** To Display the clusters in the Tree*****/
public void displayTree(){
	int pass=0;
	System.out.println("SSTREE IS:");
	  
	ArrayList<Node>Q1=new ArrayList<Node>();
	ArrayList<Node>Q2=new ArrayList<Node>();
	  
	Node p=root;
	  
	  
	if(p!=null)
	   Q1.add(p);
	else
	   System.out.println("Root is not there");
        
	  
	while(!Q1.isEmpty()){
	   
	   if(!Q2.isEmpty()) 
		   Q2.clear();
	   
	   pass++;
	   System.out.println("Pass " + pass);
	   
	   if(Q1.isEmpty()) 
		   System.out.println("Q2 takes away Q1");
		  
       System.out.println("Q1 Size="+Q1.size());
	   
       while(!Q1.isEmpty()){
		   p=Q1.remove(0);
			 
	       if(p==null){
				System.out.println("ROOT is null");
		   }
			   
		   p.displayNode();
		   System.out.println("displayTree:HEE");
			   
		   if(!p.isLeafNode()){
                System.out.println("displayTree:P is not a leaf node");
		    	for(int i=0;i<p.no_of_keys;i++){
                     if(p.arraySSElem[i].child!=null)  {
					   
                    	  Q2.add(p.arraySSElem[i].child);
				         System.out.println("Display children");
                     } 
				}//end for
				   
		   }//end outer if
			   
			 
		}//end inner while (!Q1.isEmpty())
		 
        System.out.println("Q1=Q2..Size of Q2=" + Q2.size());

		Q1.clear();
		Q1.addAll(Q2);
		if(Q2.isEmpty()){
		   System.out.println("Q2 Empty");
		}
		 
	}//end outer while (!Q1.isEmpty())
	  
	System.out.println("Exit displayTree");
	
}//end(displayTree)
   
   
   
   
   
/***** Insert a cluster in the tree*****/   
public void insert(SSElem element){

  	Node p,temp,q;
	float weights[]={1,1,1,0.5f,0.5f};

	 

    System.out.println("Inside insert:node to be inserted" );
    element.printSSElem();
     
    
    if(root==null)//tree hasn't been created before
    {
    	root=new Node();
    	root.insert_in_a_node(element);
    	if(root==null)
   	        System.out.println("Root entryNULL");
    	else
    		System.out.println("Root entryOver");


    }//end if (root==null)

    else
    {
   	    p=root;
   	    System.out.println("B4Descend:no of keys:"+p.no_of_keys);
   	    while(!p.isLeafNode())
   	    {
   	    	p=p.DescendTree(weights,element.centroid);
            System.out.println("After Descend:no of keys:"+p.no_of_keys);
   	    }

   	    if(p.no_of_keys < 4)
   	    {
            System.out.println("\nGoing 2 enter without split");
   	    	
            try{ 
          	  
                FindCertainExtension.writer.write("NonSplit entry="+element.data+"\n");
          	
            }catch(Exception e){e.printStackTrace();}
          	  
            p.insert_in_a_node(element);
   	    	System.out.println("Entered without splitting:"+p.no_of_keys);
   	        p.displayNode();
   	        Tree.upAdjust(p);
   	    }

   	    else
   	    {
            System.out.println("Going to SPLIT following node:");
   	    	p.displayNode();
             
   	    	try{ 
   	    	 
   	    		root=SSElem.splitNode(element,p,root); // split p and insert element appropriately
   	    		
   	    	}catch(Exception e){
   	    		
   	    		e.printStackTrace();
   	    		System.exit(1);
   	    	}
   	    	 
   	    	System.out.println("After split-node p's replacement:"  ); 
            System.out.println("\nEntered after splitting");
   	    }//end else of if(p.no_of_keys<4)

    }//end else of if(root==null)

   
    System.out.println("\nExit insert():Inserted node");
    element.printSSElem();
}//end insert(SSElem element,node **root)

 
  
  
  /**** Perform Adjustement after insertion in the Tree*******/
public static void upAdjust(Node NewP){
    SSElem upperElem;
    Node upperNode;

    upperElem=NewP.mother;
	upperNode=NewP.father;
	    
	while(upperNode!=null){

	    upperElem.calculateCentroid();
   	    upperElem=upperNode.mother;
	    upperNode=upperNode.father;
	    	
	}
	    
}//end upAdjust()
 
 
 /****** Retrieve the needed clusters******/
public  void search(float weights[],float FV[]){
	int pass=0;
	System.out.println("Search result:");
	  
	ArrayList<Node>Q1=new ArrayList<Node>();
	ArrayList<Node>Q2=new ArrayList<Node>();
	  
	Node p=root;
	Result r;
	int index=0;
	boolean flag=false;
	result=new ArrayList<Result>();
	if(p!=null)
	  
		Q1.add(p);
	
	else
		System.out.println("Root is not there");
        
	  
	while(!Q1.isEmpty()){
		if(!Q2.isEmpty()) 
			 Q2.clear();
		 
		pass++;
		System.out.println("Pass " + pass);
		if(Q1.isEmpty()) 
			 System.out.println("Q2 takes away Q1");
		  
        System.out.println("Q1 Size="+Q1.size());
		while(!Q1.isEmpty()){
			 p=Q1.remove(0);
			 
			 if(p==null){
					  System.out.println("ROOT is null");
		     }
			   

			 p.displayNodeResult(FV);
		    
			 flag=false;
			   
			 for(int i=0;i<p.no_of_keys;i++){
					  if( (Math.abs(FV[0]-p.arraySSElem[i].centroid[0])<= 30.0f) && (Math.abs(FV[1]-p.arraySSElem[i].centroid[1])<50.0f) && (Math.abs(FV[2]-p.arraySSElem[i].centroid[2])<= 75.0f))
						   if(p.arraySSElem[i].data!=null) {
							   r=new Result(p.arraySSElem[i].data,p.arraySSElem[i].centroid[4]);
							    
							   for(Result obj:result ){
								   if(r.imgName.equals(obj.imgName)){
									   if(r.getPercentage()>=obj.getPercentage()){
									   index=result.indexOf(obj);
									   result.set(index, r);
									   }
									   flag=true;
								   }
							   }
							   if(!flag ){
							   
								   System.out.println("Not Repeated");
								   result.add(r);
                                flag=false;				
							   }
							   
							   System.out.println(" ####### result at index"+i+"is : "+r.imgName +"\nSize of arraylist:"+ result.size());
								
						   }
			   }
			    
				
			   System.out.println("  no. of elements in the list ============="+result.size());
			   
			   System.out.println("displayTree:HEE");
			   
			   if(!p.isLeafNode()){
                  System.out.println("displayTree:P is not a leaf node");
				   for(int i=0;i<p.no_of_keys;i++){
                      if(p.arraySSElem[i].child!=null)  {
					   
                    	  Q2.add(p.arraySSElem[i].child);
				         System.out.println("Display children");
                      } 
				   }//end for
				   
			   }//end outer if
			   
			 
		 }//end inner while (!Q1.isEmpty())
		 System.out.println("Q1=Q2..Size of Q2=" + Q2.size());

		 Q1.clear();
		 Q1.addAll(Q2);
		 if(Q2.isEmpty()){
			 System.out.println("Q2 Empty");
		 }
		 
	  }//end outer while (!Q1.isEmpty())
	  
	  System.out.println("Exit  search");
	

	
	
}//exit search()

 
public static BufferedReader openFILE(){
	  try{
		  File myFile=new File("E:\\Eclipse\\be8506\\FV.txt");
	      FileReader fileReader=new FileReader(myFile);
	      BufferedReader reader=new BufferedReader(fileReader);  
	      /*** ||y extend it for A and P ***/
	      return reader;
	  }
	  catch(Exception ex){
	      ex.printStackTrace();
	  }
	   return null;
}//end openFILE()

 
/****** Serialise the Tree in a file ********/ 
public static void serialize(Tree SSTree){
	
   try{	
	     FileOutputStream fs=new FileOutputStream("/root/Desktop/ImageIndexDatabase.ser");
         ObjectOutputStream os=new ObjectOutputStream(fs);
    
         os.writeObject(SSTree);
         os.close();

   }catch(Exception e){
	     e.printStackTrace();
   }
   System.out.println("Tree serialized=");
}


/******* Deserialise the tree from a File*******/
public static Tree deserialize(){
   Tree SSTree=null;
	
   try{	
	      ObjectInputStream is=new ObjectInputStream(new FileInputStream("/root/Desktop/ImageIndexDatabase.ser"));

	      if(is==null)
		      return null;
	  
	      SSTree=(Tree)is.readObject();
          if(SSTree.root==null)
        	  System.out.println("NUUUUUUUULLLLLLLLLLLLLLLLL");

        
        
    }catch(Exception e){
	   e.printStackTrace();
       return null;
    }


    System.out.println("DESERIALIZED");
    return SSTree;
}

/***** BUild A Tree******/
public static void buildTree(){

    Tree SSTree=new Tree();
	 
	Node temp1,temp2;
	SSElem element;
	System.out.println("\nJOKER");

    System.out.println("\nInsertion OVER");
         

	SSTree= deserialize();
	
	
    System.out.println("Retrieval ends");   
} //end main()
 
public static void main(String args[])
{
	Tree obj;
	obj=new Tree();
	obj= Tree.deserialize();
    obj.displayTree();
}// end of main

}//end class Tree







