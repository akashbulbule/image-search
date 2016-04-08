package com.Indexing;
 
import java.util.ArrayList;


class Cluster{
	
	             private ArrayList<Seed>clusterPixel=new ArrayList<Seed>();//Main color Pr
	             private float Radius;
                                
	             public float getRadius(){
	            	  return Radius;
	             }
	             
	             public Cluster(Seed obj){
                       clusterPixel.add(obj);      	  
                       this.Radius=(float)0.0;
                  }
               
	             public void add(Seed obj){
	            	 clusterPixel.add(obj);
	             }
                  
              /************for each CLUSTER update its SEED******************/
	                public Seed updateSeed(){
                	
                	 float h=0,v=0,c=0,radius2=0;
                     int pixelcount=0;
                    
                     Seed retObj=new Seed(0,0,0,0,-1,-1);
                     
                     for(Seed seed:clusterPixel){
                         h+=seed.getHue()*seed.pixelCount;
                         v+=seed.getValue()*seed.pixelCount;
                         c+=seed.getChromaticity()*seed.pixelCount;
                         pixelcount+=seed.pixelCount;
                         retObj.copyPixel(seed,pixelcount-seed.pixelCount);         
                     }

                        h=h/pixelcount;
                        v=v/pixelcount;
                        c=c/pixelcount;
                    
                        	retObj.updateSeed(h, v, c);
                        	retObj.pixelCount=pixelcount;
                        	
                    
                        
                          for(Seed seed:clusterPixel){
                              	radius2+= Math.pow(retObj.NBSdistanceCalculate(seed),2)*seed.pixelCount;
                        	}                        
                         
                        radius2=radius2/pixelcount;
                        this.Radius=(float)Math.sqrt(radius2);
                    
                        return retObj;
                 }


                 /**********MERGE and form a new CLUSTER*****/ 
                 public Seed updateSeed(Cluster C){
                	  float h=0,v=0,c=0,radius2=0,coeff;
                      int pixelcount=0;
                      Seed retObj=new Seed(0,0,0,0,-1,-1);
                      
                      for(Seed seed:clusterPixel){
                          h+=seed.getHue()*seed.pixelCount;
                          v+=seed.getValue()*seed.pixelCount;
                          c+=seed.getChromaticity()*seed.pixelCount;
                          pixelcount+=seed.pixelCount;
                          retObj.copyPixel(seed,pixelcount-seed.pixelCount);                                          
                      }
                   
                      for(Seed seed1:C.clusterPixel){
                    	  h+=seed1.getHue()*seed1.pixelCount;
                          v+=seed1.getValue()*seed1.pixelCount;
                          c+=seed1.getChromaticity()*seed1.pixelCount;
                          pixelcount+=seed1.pixelCount;
                          retObj.copyPixel(seed1,pixelcount-seed1.pixelCount);
                      }
                      
                	  h=h/pixelcount;
                	  v=v/pixelcount;
                	  c=c/pixelcount;

                	  retObj.updateSeed(h, v, c);
                	  retObj.pixelCount=pixelcount;
                	  

                	  
                	  for(Seed seed:clusterPixel){
                      	  radius2+= Math.pow(retObj.NBSdistanceCalculate(seed),2)*seed.pixelCount;
                      	}
                	  
                	  for(Seed seed:C.clusterPixel){
                      	  radius2+= Math.pow(retObj.NBSdistanceCalculate(seed),2)*seed.pixelCount;
                      	  clusterPixel.add(seed);
                        }
                	  
                	  radius2=radius2/pixelcount;
                      Radius=(float)Math.sqrt(radius2);
                      return retObj;
                  }

                     public void printCluster(){
                    	 System.out.println("Radius: "+Radius);
                    	 for(Seed obj:clusterPixel){
                    		 obj.printSeed();
                    	 }
                     }


}               
