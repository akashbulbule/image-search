package com.SSTree;

 
import java.io.Serializable;
import com.Indexing.FeatureVector;
import com.Indexing.FindCertainExtension;


public class SSElem implements Serializable{

	private static final long serialVersionUID = 5693652606225804858L;
	Node child;
	int immed_children=0;	
	long total_children=0;
	int height;
	int update_count;
	float radius=0;
	float centroid[];
	String data;
    final int DIM=5;
	final int UPDATE_COUNT=5;

	public SSElem(){
		child=null;
		centroid=new float[5]	;
		total_children=immed_children=0;
	}
	
	public SSElem(float FV[],String str){
		int i,j,k;
		centroid=new float[5]	;
		   
		for(i=0;i<DIM;i++){
			  centroid[i]=FV[i];
		}//end for i
		update_count=UPDATE_COUNT;
		total_children=immed_children=1;
		height=0;
		radius=0;
		data=str;
		child=null;		
	}
	

	public SSElem(FeatureVector fv,String str){
		centroid=new float[5];
		int i,j,k; 
		centroid[0]=fv.getHue();
		centroid[1]=fv.getValue();
		centroid[2]=fv.getChromaticity();
		centroid[3]=fv.getAggreationDegree();
		centroid[4]=fv.getPixelcount();
		total_children=immed_children=1;
		height=0;
		radius=0;
		data=str;
		child=null;
	}//end SSElem()
	
	
	public void printSSElem(){    
	    for(int i=0;i<DIM;i++){
	    	System.out.println("Centroid["+i+"]" + centroid[i]);
	    } 
	    System.out.println("File name:" + data);
	}//end printSSElem()
	
 
	
	public static Node splitNode(SSElem element,Node p,Node root){
	   double maxVariance= 3.4*Math.pow(10,8),variance;
	   int i,maxDimension=0,j,k,m;
	   Node parentF=null,New1,New2,NewP=null;	   
	   Node New[]=new Node[4];
	   SSElem parentM=null,motherUpdate;
	   float array[]= new float[root.MAX_NO_OF_CHILD+1];
	   int index[]=new int[root.MAX_NO_OF_CHILD+1];
	   int indexP=-1;
	   int rows=-1,i0=-1,i1=-1,i2=-1,i3=-1;
	   System.out.println("\nEnter splitNode()");
	   System.out.println("keys"+p.no_of_keys);
	   p.arraySSElem[p.no_of_keys++]=element;
       if(p!=root){
    	   parentF=p.father;
		   parentM=p.mother;
       }

       if(parentF!=null){
		  for(i=0;i<parentF.no_of_keys;i++){
			  if(parentF.arraySSElem[i].child==p)
				  break;
		  }
		  indexP=i;
		}

	   for(i=0;i<p.no_of_keys;i++){
		   array[i]=p.arraySSElem[i].centroid[maxDimension];
		   index[i]=i;
		}

	   	SSElem.sort(array,index,p.no_of_keys);  
        System.out.println("\nNode P to be split:");
        p.displayNode();
	   
        int splitArray[][]=new int[4][4];

        for(i=0;i<4;i++){
        	for(j=0;j<4;j++){
        		splitArray[i][j]=-1;
        	 }
         }

	    for(i=0;i<p.no_of_keys;i++){		  
		   for(j=0;j<=rows;j++){
			   if( (Math.abs(p.arraySSElem[splitArray[j][0]].centroid[0]-p.arraySSElem[i].centroid[0])< 0.34) && (Math.abs(p.arraySSElem[splitArray[j][0]].centroid[1]-p.arraySSElem[i].centroid[1])< 1.8) && ((p.arraySSElem[splitArray[j][0]].centroid[2]-p.arraySSElem[i].centroid[2])< 0.68)){                                      
				   break;				   
			   }
		   }//end for j
	    	
          if(j>rows){
        	  if(rows<3){
        		  splitArray[++rows][0]=i;
        	  if(rows==0)
        		  i0=0;
        	  else if(rows==1)
        		  i1=0;
        	  else if(rows==2)
        		  i2=0;
        	  else if(rows==3)
        		  i3=0;
        	  }//end if l<3
        	  else
        		  splitArray[3][++i3]=i;      
          }//end if(j>l)
          else{
        	  if(splitArray[j][3]!=-1){
        		  j=(j+1)%4;
          }
        	    
            if(j==0)
        		splitArray[j][++i0]=i;
        	else if (j==1)
        		splitArray[j][++i1]=i;
        	else if(j==2)
        		splitArray[j][++i2]=i;
        	else if(j==3)
        		splitArray[j][++i3]=i; 
        	if(j>rows)
        		rows=j;           	 
        }//end else
	 }//end for i  
	 System.out.println("SPLITARRAY: rows=" + rows);  
	 for(i=0;i<4;i++){
		 for(j=0;j<4;j++){
			 System.out.print("("+i+","+j+")"+"=" +splitArray[i][j]+"  ");
		 }
		 System.out.println("");
	 }//end for i,j
	 New1=new Node();
	 New2=new Node();
	 if(New1==null || New2==null){
		System.out.println("\nMemory not allocated in splitNode()");
		return null;
	 }
	   j=p.no_of_keys/2;
	   k=0;
	   m=0;
	   
	   int sselem=-1;
	              
	   for(i=0;i<p.MAX_NO_OF_CHILD;i++){
		   if(splitArray[i][0]!=-1){
			   sselem=-1;
	           New[i]=new Node();
               for(j=0;j<4;j++){
            	   if(splitArray[i][j]!=-1){ 
            		   New[i].arraySSElem[++sselem]=p.arraySSElem[splitArray[i][j]];
                       New[i].no_of_keys++;
                    }//end if splitArray[i][j]!=-1 
                    else
                       break;
                }//end for j
	        }//end if splitArray[i]
	     }//end for i
	   
	   
	   
	   /*******if parent node is not full && p is not root********/

		 if(parentF!=null && (parentF.no_of_keys + (rows+1) <=5 )){
			 for(i=0;i<p.MAX_NO_OF_CHILD;i++){
				 if(splitArray[i][0]!=-1) 
					 New[i].father=p.father;
                  }//end for i;
			     p.father.arraySSElem[indexP].child=null;
              	 for(i=0;i<p.MAX_NO_OF_CHILD;i++){
              		 if(splitArray[i][0]!=-1){
              			 for(j=0;j<p.MAX_NO_OF_CHILD;j++){
              				 if(p.father.arraySSElem[j].child==null){
              					 p.father.arraySSElem[j].child=New[i]; 
              					 p.father.arraySSElem[j].calculateCentroid();
              					 New[i].mother=p.father.arraySSElem[j];
              					 New[i].mother.immed_children=New[i].no_of_keys;
              					 New[i].mother.total_children=New[i].no_of_keys;
              					 break;
              				 }
              			 }//end for j
            	     }//end if splitArray
                  }//end for i
                  p.father.no_of_keys=0;
              	  for(i=0;i<p.MAX_NO_OF_CHILD;i++){
              		  if(p.father.arraySSElem[i] !=null && p.father.arraySSElem[i].child!=null)
              			   p.father.no_of_keys++;
              	  }
              	
           System.out.println("Adjusted in same father node:");
           if(p.father.arraySSElem[indexP].child!=null){ 
        	   System.out.println();
        	   p.father.arraySSElem[indexP].child.displayNode();
           } 
           Tree.upAdjust(p);
                
		}//end if 
		else{
	         NewP=new Node();
	         
	         /*****************Split node but not root*********/
              k=-1;
			  if(NewP!=null){
				 for(i=0;i<p.MAX_NO_OF_CHILD;i++){
			           if(splitArray[i][0]!=-1){
			        	   New[i].father=NewP;
			        	   NewP.arraySSElem[++k].child=New[i];
			               New[i].mother=NewP.arraySSElem[k];   
			           }
				 }//end for i

                 NewP.no_of_keys=rows+1;
				 NewP.father=p.father;  //retained as it is,unchanged
				 NewP.mother=p.mother;  //retained as it is, unchanged
				 for(i=0;i<p.MAX_NO_OF_CHILD;i++){
				    if(splitArray[i][0]!=-1){
				    	if(New[i].mother!=null){
				    		New[i].mother.immed_children=New[i].no_of_keys;
				    		New[i].mother.total_children=New[i].no_of_keys;
				    	}//end if New[i].mother
				    } //end if splitArray[i][0]
				 }//end for i
				 
				 if(NewP.mother!=null){
					 NewP.mother.immed_children=NewP.no_of_keys;
                     NewP.mother.total_children=NewP.no_of_keys;	
 
				 }
				 for(i=0;i<NewP.no_of_keys;i++){
					 NewP.arraySSElem[i].calculateCentroid();
				 }
				  if(parentF!=null){
			           System.out.println("\nReset IndexP="+indexP);
				        motherUpdate=p.mother;
		                motherUpdate.child=NewP;
		                parentF.arraySSElem[indexP].child=NewP;		           
		                NewP.father.arraySSElem[indexP].calculateCentroid();
		                Tree.upAdjust(NewP);       
				 }
				 
				 /******************Root split*********************/				 
				 else{
					   root=NewP;	
					   System.out.println("ROOT CHANGED");
					 }
			  }//end else of if(parentF!=null)
		}//end else of if if(parentF!=null && (parentF.no_of_keys < root.MAX_NO_OF_CHILD))

        if(NewP!=null){
        	System.out.println("NEWP:"+NewP.no_of_keys );
        	NewP.displayNode();
        }
		 
		for(i=0;i<p.MAX_NO_OF_CHILD;i++){     
			if(splitArray[i][0]!=-1){
			     System.out.println("i="+i);
			     New[i].displayNode();
			}
		}		 
         if(p.father!=null)
		 System.out.println("\nExit splitNode() with "+p.father.no_of_keys+"keys");
	     return root;
	}//end splitNode()


	
	
	
	
	
	public static float calculateDistance(float weights[],float centroid1[],float centroid2[]){
		float distance=0;
	  	float delh=Math.abs(centroid1[0]-centroid2[0]);
	  	float delc= Math.abs(centroid1[2]-centroid2[2]);
	  	float dell = Math.abs(centroid1[1]-centroid2[1]);
	  	float Ah = delh + 0.16f;
		float Al=1.4456f;
		distance = (float)Math.pow((Math.pow(Al*dell,2.0f) + Ah*(Math.pow(centroid1[2],2.0f) + Math.pow(centroid2[2],2.0f) - (2 * centroid1[2]*centroid2[2]*Math.cos(delh*Math.PI/180.0f)))),1/2.0f);
		return distance;	  
	}//end calculateDistance()

	public  static float calculateVariance(Node x,int dimension){
		int i,j,k;
		float mean=0.0f;
		float variance=0.0f;
		for(i=0;i<x.no_of_keys;i++){
			mean = mean + (x.arraySSElem[i].centroid[dimension]);
		}
		mean=mean/x.no_of_keys;
		for(i=0;i<x.no_of_keys;i++){
			variance+=Math.pow(Math.abs(mean-x.arraySSElem[i].centroid[dimension]),2);
		}
		variance=variance/x.no_of_keys;
	    return variance;
	}// end of calculateVariance(Node)


	public float varianceFromArray(float vectors[],int size){
		float mean=0.0f;
		float variance=0.0f;
		int i;

		for(i=0;i<size;i++){
			if(mean!=mean)
				mean=vectors[i];
			mean=mean + vectors[i];
		}
		mean=mean/size;
		for(i=0;i<size;i++){
			variance=variance + (float)Math.pow(Math.abs(vectors[i]-mean),2);
		}
		variance=variance/size;		 
		return variance;

	}// end of varianceFromArray()
	

	public void calculateCentroid(){
	   int i,j;
	   float sumH=0,sumV=0,sumC=0,sumA=0,sumP=0,dist;
	   float weights[]={1.0f,1.0f,1.0f,0.5f,0.5f};
	   long totalChildren=0;

	   immed_children=child.no_of_keys;
	   
	   for(i=0;i<child.no_of_keys;i++){
		   sumH=sumH + child.arraySSElem[i].centroid[0]*child.arraySSElem[i].total_children;
		   sumV=sumV + child.arraySSElem[i].centroid[1]*child.arraySSElem[i].total_children;
		   sumC=sumC + child.arraySSElem[i].centroid[2]*child.arraySSElem[i].total_children;
		   sumA=sumA + child.arraySSElem[i].centroid[3]*child.arraySSElem[i].total_children;
		   sumP=sumP + child.arraySSElem[i].centroid[4]*child.arraySSElem[i].total_children;

		   totalChildren=totalChildren + child.arraySSElem[i].total_children;
	   }

	    total_children=totalChildren;	 
 
	    centroid[0]=sumH/totalChildren;
		centroid[1]=sumV/totalChildren;
		centroid[2]=sumC/totalChildren;
		centroid[3]=sumA/totalChildren;
	    centroid[4]=sumP/totalChildren;

	    radius=0;
 
		for(i=0;i<child.no_of_keys;i++){
			if((dist=calculateDistance(weights,child.arraySSElem[i].centroid,centroid))>radius){
				radius=dist;
			 }//end if
		 }//end for
		 
	}//end calculateCentroid(node *child,node *parent)


	public static void sort(float array[],int index[],int size){
	  int i,j,k;
	  float tempn,tempi;
	  for(i=1;i<size;i++){
		   tempn=array[i];
		   for(j=i-1;j>=0 && array[j]>tempn;j--){
			   array[j+1]=array[j];
			   index[j+1]=index[j];
			   array[j]=tempn;
			   index[j]=i;
		   }
	  }//end for(i)
	}//end sort()

}//end class SSElem
