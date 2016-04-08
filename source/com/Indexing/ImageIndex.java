package com.Indexing;
 

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

import com.SSTree.*;


/*****************Driver Class for Indexing******************/

public class ImageIndex extends Component{
 
 	static final float MIN_DISTANCE=(float)6.0;    
    private static ArrayList<Seed> SeedArray=new ArrayList<Seed>();
    
    private Cluster [] cluster=new Cluster[20];
    private FeatureVector [] featureVectors=new FeatureVector[20];
    int total_Clusters=0;
    static final int TOTAL_COLORS=7;
    private Seed [] seedMainColor=new Seed[TOTAL_COLORS];
     Tree SSTree=null;
    static final float mainColor[][]=new float[TOTAL_COLORS][3];
	
    
    
    
  
    class PixelCountComparator implements Comparator<Seed>{
		 public int compare(Seed one,Seed two){
 
             return	new Integer(two.pixelCount).compareTo(new Integer(one.pixelCount));		 
		 }
	}
    
	    		
	    
           private float U(float A)
	        {
	        	return ((float)11.6*(float)Math.pow(A,(float)0.333) -(float) 1.6);
	        }
	 

	   public void display(){
     	  System.out.println("Inside(II) display");
		   SSTree=Tree.deserialize();
           SSTree.displayTree();		   
	   }

               /***************SEEDING*******************/    
		private void seeding(int pixel,int x,int y) {
                     /**************RGB to HVC conversion*******************/ 

		    int alpha = (pixel >> 24) & 0xff;
		    int red = (pixel >> 16) & 0xff;
		    int green = (pixel >> 8) & 0xff;
		    int blue = (pixel) & 0xff;
		     float distance,x0,y0,z0,mindistance;
		     
 		     float R=(float)red,G=(float)green,B=(float)blue,Q,H,C,V,maxrgb,minrgb;
		 	float alph,gamma=3.0f;
		 	float hcl[];
		     
		 	hcl = new float[3];
		 	 maxrgb = Math.max(R, Math.max(G, B));
		 	 
		 	 minrgb = Math.min(R, Math.min(G, B));
		 	 if(maxrgb==0){alph=0.0f;}
		 	 else
		 	 {
		 	 alph = minrgb/(maxrgb*100.0f);
		 	 }
		 	  Q = (float)Math.pow(Math.E,(gamma*alph));
		 	  V = ((Q*maxrgb) + ((1-Q)*minrgb))/2.0f;
		 	  C = (Q * (Math.abs(R-G) + Math.abs(G-B) + Math.abs(B-R)))/3.0f;
		 	  if(R-G==0 && G-B==0){
		 		  H = (float)(Math.toDegrees(Math.atan2((G-B),(R-G))));
		 	  }
		 	  else
		 	  {  
		 	  H = (float)(Math.toDegrees(Math.atan((G-B)/(R-G))));
		 	  }
		  
		 	  if((R-G)>=0 && (G-B) >= 0){
		 		  H*=(2.0f/3.0f);		  
		 	  }
		 	  else if((R-G)>=0 && (G-B) < 0){
		 		  H*=(4.0f/3.0f);
		 	  }
		 	  else if ((R-G)<0 && (G-B) >= 0){
		 		  H = 180 + (4.0f/3.0f)*H;
		 	  }
		 	  else
		 	  {
		 		  H = (3.0f/4.0f)*H - 180;
		 	  }
		  

               Seed obj=new Seed(H,V,C,1,x,y);
		       
               
               boolean flag=false;
		      
         
		   if(!obj.equals(null)) {  
               for(Seed seed:SeedArray){
                	 if((distance=seed.NBSdistanceCalculate(obj))<=MIN_DISTANCE)
                	 {	 
                		 
                           seed.pixelCount=seed.pixelCount +1;
                          
                           seed.updateSeed(H, V, C);
                           seed.addPixel(x,y);
                           SeedArray.set(SeedArray.indexOf(seed ),seed);
                	       // SeedArray(i).pixelCount++;
                		     flag=true;
                             break;
                	 }	 
                	   
                	 
               
               }
             
		       mindistance=1319;
		       int minIndex=-1;
               for(int i=0;i<TOTAL_COLORS;i++)   
		       { 
		    	    
		    	   if((distance=obj.NBSdistanceCalculate(seedMainColor[i]))<=mindistance)  
		    	    {                                             
		    	    	 
		    	         mindistance=distance;
		    	         minIndex=i;
		    	         
		    	         if (obj.getHue()==0 && obj.getChromaticity()==0 && obj.getValue()>0){
		    	        	 minIndex=5;
		    	        	 break;
		    	        	 
		    	         }
		    	         else if (obj.getHue()==0 && obj.getChromaticity()==0 && obj.getValue()==0){
		    	        	 minIndex=6;
		    	        	 break;
		    	         }
		    	         else if(obj.getHue()<=20.0 &&  obj.getHue()> -30.0f ) {
		    	        	  minIndex=0;
		    	               break;
		    	         }
		    	         else if(obj.getHue()>=90.0f && obj.getHue()<=170.0f){
		    	        	 minIndex=1;
		    	        	  break;
		    	         }
		    	         else if(obj.getHue()> -180.0f && obj.getHue()<=-90.0f){
		    	        	 minIndex=2;
		    	        	 break;
		    	         }
		    	         else if(obj.getHue()> 30.0f && obj.getHue()< 80.0f){
		    	        	 minIndex=3;
		    	        	 break;
		    	         }
		    	         else if(obj.getHue()> -90.0f && obj.getHue()<= -30.0f){
		    	        	 minIndex=4;
		    	        	 break;
		    	         }
		    	         
		    	         
		    	    }
		    	    	
		       }
           	     seedMainColor[minIndex].pixelCount++;
                 seedMainColor[minIndex].addPixel(x,y);
		   }  


               if(!flag && !obj.equals(null))
                    SeedArray.add(obj);
               
               PixelCountComparator pixelCompare=new PixelCountComparator();
               Collections.sort(SeedArray,pixelCompare);
                 
 		
		}//end seeding()

    /*****************CLUSTERING SEEDS TOGETHER*************/                    
          private void clustering(){
                 float minDistance=(float)12.0,distance;
                 Seed ClusterCol=new Seed(0,0,0,0,-1,-1);
                 Seed minobjs=null;
                 Seed obj2=null,obj;
        	      int j=0,i=0,minj,n=0;
        	      boolean flag=false,flagMerge=true;;
        	      /***CLUSTER IS EMPTY INITIALLY---So No Initialization of CLUSTERS**/
                   total_Clusters=SeedArray.size();
                   
                 
                   for(i=0;i<7;i++)
                	   System.out.println("Seed Main Color pixelcount:"+seedMainColor[i].pixelCount);
                   
                   
                   
              while(flagMerge){   
                 for(i=0;i<TOTAL_COLORS;i++){
                      flag=false;
                      minDistance=(float)10000.0f;
                	 for(Seed seed:SeedArray){
                    	    if(SeedArray.indexOf(seed)>=20) {break;}
                		 if((distance=seed.NBSdistanceCalculate(seedMainColor[i]))<minDistance && seedMainColor[i].pixelCount>0){
                			 				System.out.println("NBS");
                			                 minDistance=distance;     	       		    
                                             ClusterCol=seed;                  	       		     
                    	                     flag=true;       	  
                    	      }
                		 System.out.println("Distance="+distance);
                	 }//end for Seed seed
                	 System.out.println("minDistance="+minDistance);
                    	  if((flag==true)){   
                    		   
                    			     j=SeedArray.indexOf(ClusterCol);
                    			   System.out.println("j="+j);
                    			  if(cluster[j]==null)
                    			   cluster[j]=new Cluster(seedMainColor[i]);	  
                    			  else 
                    			   cluster[j].add(seedMainColor[i]); 
                    			   n++;
                    		    
                    	      		   
                    	  }//end if  flag==true
                       }//end for i

                 System.out.println("n="+n);
                 System.out.println("CLUSTYERS");
                 for(i=0;i<20;i++){
                	 if(cluster[i]!=null)
                		 cluster[i].printCluster();
                 }
                 
                 
                 for(i=0;i<20 && i<SeedArray.size();i++){
                	 obj=SeedArray.get(i);
                	 
                          
                     if(cluster[i]!=(null)){
                         cluster[i].printCluster();
                    	 obj2=cluster[i].updateSeed() ;
                      
                       if(obj2==null || i<0){
                	        System.out.println("\nNULL ObJ "+i);
                	      }
                       else
                       {  
                           System.out.println("\nBefore setting seed");
                    	   obj.printSeed();
                    	   SeedArray.set(i,obj2);
                       }   
                     } //end if cluster[i]!=null
               
                     if(cluster[i]!=null){  
                       System.out.println("CLUSTER");
                    	  cluster[i].printCluster();
                      }
                      System.out.println("Seed after setting:");
                     if(cluster[i]!=null)  
                      obj2.printSeed();
                 }//end for
                
                  
                  System.out.println("\nStart Merging");
               /******************MERGING OF CLUSTERS***********/
                  flagMerge=false;
                   ArrayList <Seed> tempSeedList=new ArrayList <Seed>();  
                  tempSeedList=(ArrayList <Seed>)SeedArray.clone();
                  
                   
                  for(Seed seed:tempSeedList) {
                    	minDistance=MIN_DISTANCE;  
                	     minj=-1;
                    	for(Seed objs:tempSeedList) {
                                i=tempSeedList.indexOf(seed);
                                j=tempSeedList.indexOf(objs);
                            if(i<20 && j<20){
                             if(cluster[i]!=null && cluster[j]!=null ){   
                    		  if(!seed.equals(objs) && cluster[i].getRadius()<3.0 && cluster[j].getRadius()<3.0){
                    			   if((distance=seed.NBSdistanceCalculate(objs))<=minDistance) {
                    	                 
                    	                 
                    				      minDistance=distance;		
                          			       minobjs=objs;
                          			       minj=j;
                    			   }
                    		  }
                           }
                            }//end if i<20 && j < 20   
                         }//end for Seed objs
                         
              
                    if(minj!=-1){
                            //MERGE cluster[i] and cluster[minj] into cluster[i]
                    	obj=cluster[i].updateSeed(cluster[minj]);
                         if(obj!=null){
                              
                          SeedArray.set(i,obj);
	                      SeedArray.remove(minobjs);
                          total_Clusters--;
                          flagMerge=true;
                         }
                         else
                         {
                        	 flagMerge=false;
                        	  
                         }
                    }
                 }//end for Seed seed:
                   System.out.println("\nEnd Merging")   ;
              }//end while (flagMerge)
                 System.out.println("\nCLUSTERS!!CLUSTERS!!!CLUSTERS!!!");
                    if(total_Clusters>20)
                    	total_Clusters=20;
     
                    System.out.println("***********Final Clusters**********");
                 for(i=0;i<total_Clusters;i++){
                    	if(cluster[i]!=null)
                    	 cluster[i].printCluster();
                    }

                     
          }//end void clustering()


          public static Seed findSeed(int x,int y){
        	  for(Seed seed:SeedArray){
                 //System.out.println("pixelcount:"+seed.pixelCount+"i:"+SeedArray.indexOf(seed)+"x~"+x+"y~"+y);
        		  if(seed.checkSeed(x, y))
        			//seed.printSeed();
        		  return seed;
        	   }
                  System.out.println("Not found");
        	  return null;
          }//end findSeed()
          
       /*************Calculate feature vectors and insert into tree*********/   
          private void FeatureVectorCalculation(int width,int height,String fileName){
                   int i=0;

                   System.out.println("***************FINAL FVS*******************");
                    
                   try{

                        for(Seed seed:SeedArray){
                          if(SeedArray.indexOf(seed) <20 ){  
         		           featureVectors[i++]=new FeatureVector(seed, width, height);
                           featureVectors[i-1].printFeatureVector();   
                        
                           if(featureVectors[i-1].getPixelcount()>0.03f){
  
                           if(SSTree==null){
                        	   SSTree=Tree.deserialize();
                        	    if(SSTree==null){
                             	   System.out.println("Tree obj created");
                        	    	SSTree=new Tree();
                        	    } 
                     	 
                        	  try{  
                        	    SSTree.insert(new SSElem(featureVectors[i-1],fileName));
                        	  }catch(Exception e){
                        		  e.printStackTrace();
                        		  System.exit(1);
                        	  }
                           
                           }
                           else{
            	                 try{
                        	      SSTree.insert(new SSElem(featureVectors[i-1],fileName));
            	                 }catch(Exception e){
            	                	 e.printStackTrace();
            	                	 System.exit(1);
            	                 }
                           }
                             }
                          
                           }
                           
                        }    //end for 
          
                            Tree.serialize(SSTree);
                                 
                     }                          
                   catch(Exception e){
                	  
                	   e.printStackTrace(/*com.Indexing.GenerateFiles.lg*/);
                	   System.exit(1);
                   }
          
                        
          }//end FeatureVectorCalculation()

        /************function for Image Processing******************/    
          private void parseImage(BufferedImage image,String fileName) {
		    int w = image.getWidth();
		    int h = image.getHeight();
		  
		    if(w*h>20000 )
		    {
		    	System.out.println("Input Image size exceeds the limit of 150 * 120");
		          System.exit(1);
		    }
		    
		    
           seedMainColor[0]=new Seed((float)0.0f,(float)127.5f,(float)170.0f,0,-1,-1);         	//RED
     	   seedMainColor[1]=new Seed((float)120.0f,(float)127.5f,(float)170.0f,0,-1,-1);        //GREEN
     	   seedMainColor[2]=new Seed((float)-120.0f,(float)127.5f,(float)170.0f,0,-1,-1);       //BLUE
     	   seedMainColor[3]=new Seed((float)60.0f,(float)127.5f,(float)170.0f,0,-1,-1);         //YELLOW
     	   seedMainColor[4]=new Seed((float)-60.0f,(float)127.5f,(float)170.0f,0,-1,-1);        //PURPLE
           seedMainColor[5]=new Seed((float)0.0,(float)127.5f,(float)0.0f,0,-1,-1);             //WHITE
     	   seedMainColor[6]=new Seed((float)0.0,(float)0.0,(float)0.0,0,-1,-1);                 //BLACK
     	  
		    for (int i = 0; i < h; i++) {
		      for (int j = 0; j < w; j++) {
	 
		        int pixel = image.getRGB(j, i);
		        seeding(pixel,i,j);
 		      }
		    } 
               
		    System.out.println("\n*****************SEEDS***********\n\n\n");
		    for(Seed seed:SeedArray){
          
     			
      		  seed.printSeed();
      	   }
		                  
               try{   
		        clustering();
               }catch(Exception e){
            	   e.printStackTrace();
            	   System.exit(1);
               }
		        
		        System.out.println("\nAFTER CLUSTERING");
		        for(Seed seed:SeedArray){
		               System.out.println("Index:"+SeedArray.indexOf(seed));
		     			seed.printSeed();
		     	   }		    
                    
             try{
		        FeatureVectorCalculation(h,w,fileName);
                   System.out.println("Done N Dusted");
             }catch(Exception e){
            	 e.printStackTrace(/*com.Indexing.GenerateFiles.lg*/);
            	 System.exit(1);
             }
          }//end parseImage()

 

		  public ImageIndex(String file){
		    	 
             
			  mainColor[0][0]=0.0f;
			  mainColor[0][1]=127.5f;
			  mainColor[0][2]=170.0f;
			  mainColor[1][0]=120.0f;
			  mainColor[1][1]=127.5f;
			  mainColor[1][2]=170.0f;
			  mainColor[2][0]=-120.0f;
			  mainColor[2][1]=127.5f;
			  mainColor[2][2]=170.0f;
			  mainColor[3][0]=60.0f;
			  mainColor[3][1]=127.5f;
			  mainColor[3][2]=170.0f;
			  mainColor[4][0]=-60.0f;
			  mainColor[4][1]=127.5f;
			  mainColor[4][2]=170.0f;
			  mainColor[5][0]=0.0f;
			  mainColor[5][1]=127.5f;
			  mainColor[5][2]=0.0f;
			  mainColor[6][0]=0.0f;
			  mainColor[6][1]=0.0f;
			  mainColor[6][2]=0.0f;
			  			  			  			 
			  
			  
			  try{ 
			
				     File f= new File(file);
		    	     String fileName=f.getAbsolutePath();
		    	     BufferedImage image=ImageIO.read(f);

		    	   parseImage(image,fileName);
		    	 }catch(IOException e){
		    		 e.printStackTrace(/*com.Indexing.GenerateFiles.lg*/);
		    		 System.exit(1);
		    	 }
		     }//end ImageIndex(String file)


		  public void indexImage(String file){
			   try{ 
				   SeedArray=new ArrayList<Seed>();
				   seedMainColor=new Seed[TOTAL_COLORS];
				   cluster=new Cluster[20];
				   featureVectors=new FeatureVector[20];
				   total_Clusters=0;
			

				   
				    File f= new File(file);
		    	     String fileName=f.getAbsolutePath();
		    	     BufferedImage image=ImageIO.read(f);

		    	   parseImage(image,fileName);
		    	 }catch(IOException e){
		    		 System.out.println("CAUGHT");
		    		 e.printStackTrace(/*com.Indexing.GenerateFiles.lg*/);
		    		 System.exit(1);
		    	 }
			  
		  }//end indexImage(String file)
		  
		 

		  
		  
		  
}//end class ImageIndex
