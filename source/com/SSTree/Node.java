package com.SSTree;

import java.io.Serializable;

public class Node implements Serializable{

	private static final long serialVersionUID = 1105132523938864002L;
	SSElem arraySSElem[];
	Node father;
	SSElem mother;
	int no_of_keys;
	
    int MAX_NO_OF_CHILD =4;
	
	public Node(){
		MAX_NO_OF_CHILD=4;
		arraySSElem=new SSElem[MAX_NO_OF_CHILD+1];
            
		for(int i=0;i<MAX_NO_OF_CHILD+1;i++){
			arraySSElem[i]=new SSElem();
		}
		no_of_keys=0;
		father=null;
		mother=null;
	}
	

	public void displayNode(){	 
       System.out.println("Node with :="+no_of_keys + "keys");
       for(int i=0;i<no_of_keys;i++){
        arraySSElem[i].printSSElem();
       }	
	}//end void displayNode()


	public void displayNodeResult(float FV[]){
       System.out.println("Node with :="+no_of_keys + "keys");
       for(int i=0;i<no_of_keys;i++){
		   if( (Math.abs(FV[0]-arraySSElem[i].centroid[0])<30.0f) && (Math.abs(FV[1]-arraySSElem[i].centroid[1])<50.0f) && (Math.abs(FV[2]-arraySSElem[i].centroid[2])<75.0f))
			   if(arraySSElem[i].data!=null) 
				   arraySSElem[i].printSSElem();	
       }
	}//end void displayNodeResult()


	public void diplayChild(){
		System.out.println("Node:="+no_of_keys);
		for(int i=0;i<no_of_keys;i++){
			arraySSElem[i].child.displayNode();	
		}
	}//end displayChild


	public boolean isLeafNode(){
		int i=0;
		try{
			for(i=0;i<no_of_keys;i++){
				if(arraySSElem[i].child==null);
				else{
					return false;
				}
		}//end for i
		return true;
		}catch(Exception e){
			System.out.println("Exception(isLeafNode)i=" +i);    	
			displayNode();
			System.out.println("Children:");
			diplayChild();
			System.exit(1);
		}
		return true;
	}//end int isLeafNode()	
	
	
	
	
	public Node DescendTree(float weights[],float centroid[]){
	   int i=0,mini=-1;
	   float mindist=(float)9.9f*(float)Math.pow(10, 10);
	   float dist;
	   for(i=0;i<no_of_keys;i++){
	    	arraySSElem[i].printSSElem();
	    	dist=SSElem.calculateDistance(weights,centroid,arraySSElem[i].centroid);
			 if(mindist>dist){
				  mindist=dist;
				  mini=i;
			 }
	   }//end for i
		return arraySSElem[mini].child;
	}//end DescendTree()
	
	
		
	void insert_in_a_node(SSElem element){
	   int i;
	   if(no_of_keys>3){
		   System.out.println("Array length exceeded");
	       System.exit(1);
	   }		   
	   arraySSElem[no_of_keys]=element;
	   no_of_keys++;
	   System.out.println("(insertinanode)No of keys:" + no_of_keys);
	   if(this.mother!=null) 
	   this.mother.immed_children=no_of_keys;
	}//end insert_in_a_node()


	public long countTotal_children(){//total children in this node;
	  long sum=0;
	  for(int i=0;i<no_of_keys;i++){
		  sum+=arraySSElem[i].immed_children;
	  }
	  return sum;
  }

}//end class



