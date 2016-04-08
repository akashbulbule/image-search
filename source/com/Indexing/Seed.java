package com.Indexing;
 
class Seed{
	          private float Hue;
	          private float Value;
	          private float Chromaticity;
             private int [] x;  //used to
             private int [] y;  // store x,y coordinates of pixels
	          public int pixelCount;
	          
             
  	          public Seed(float h,float v,float c,int numP,int x,int y){
           	  Hue=h;
           	  Value=v;
           	  Chromaticity=c;
           	  pixelCount=numP;
           	  this.x=new int[20000];
           	  this.y=new int[20000];
           	  if(pixelCount>0){
           		  this.x[this.pixelCount-1]=x;
   	        	  this.y[this.pixelCount-1]=y;
           	  }
             }
  	          
  	          
             public Seed(){
                         Hue=0;
                         Value=0;
                         Chromaticity=0;
                         pixelCount=1;
                         this.x=new int[20000];
                      	  this.y=new int[20000];
              }

             public float getHue(){return this.Hue;}
             public float getValue(){return this.Value;}
             public float getChromaticity(){return this.Chromaticity;}

	          public void addPixel(int x,int y){
	        
	        	  this.x[this.pixelCount-1]=x;
	        	  this.y[this.pixelCount-1]=y;
	          }//end addPIxel()

	          public void updateSeed(float h,float v,float c) {
	        	  Hue=h;
	        	  Value=v;
	        	  Chromaticity=c;
	          }//end updateSeed()
             
              public void copyPixel(Seed obj,int index){
           
            	  for(int i=0;i<obj.pixelCount && i+index<35000;i++){
           		   
           		   this.x[i+index]=obj.x[i];
           		   this.y[i+index]=obj.y[i];
           	   }
              }//end copyPixel
	          
              public void copyPixel(Seed obj1,Seed obj2){
           	   int i=0;

           	   for(i=0;i<obj1.pixelCount;i++){
           		   this.x[i]=obj1.x[i];
           		   this.y[i]=obj1.y[i];
           	   }
           	   
           	   for(int j=0;j<obj2.pixelCount;j++){
           		   this.x[i++]=obj2.x[j];
           		   this.y[i++]=obj2.y[j];
           	   }
              }//end copyPixel()
              
	          public void printSeed(){
                 
	        	  System.out.println("H,V,C:"+Hue+","+Value+","+Chromaticity+","+pixelCount);
                     
	           }//end printSeed()

		      public float NBSdistanceCalculate(Seed seed1){
    			   float distance=0.0f;
    			   
    			   float delh = Math.abs(this.Hue-seed1.Hue);
    			   float delc = Math.abs(this.Chromaticity-seed1.Chromaticity);
    			   float dell = Math.abs(this.Value-seed1.Value);
    				
    				
    			   float Ah = delh + 0.16f;
    			   float Al=1.4456f;
    			   distance = (float)Math.pow(Math.abs((Math.pow(Al*dell,2.0f) + Ah*(Math.pow(this.Chromaticity,2.0f) + Math.pow(seed1.Chromaticity,2.0f) - (2 * this.Chromaticity*seed1.Chromaticity*Math.cos(delh*Math.PI/180.0f))))),1/2.0f);
    			   return distance;
     		    }//end NBSdistanceCalculate


		      
		      public static float NBSdistanceCalculate(float features1[],float features2[]){
   			       float distance=0.0f;
   			   
			       float delh = Math.abs(features1[0]-features2[0]);
				   float delc = Math.abs(features1[2]-features2[2]);
				   float dell = Math.abs(features1[1]-features2[1]);
				
				
				   float Al=1.4456f;
				   float Ah = delh + 0.16f;
				
				   distance = (float)Math.pow((Math.pow(Al*dell,2.0f) + Ah*(Math.pow(features1[2],2.0f) + Math.pow(features2[2],2.0f) - (2 *features1[2]*features2[2]*Math.cos(delh*Math.PI/180.0f)))),1/2.0f);
   			       return distance;
    		    }//end NBSdistanceCalculate

		      
		      
		      /************Check whether a pixel (x,y) exists in this seed or not********/   
		      
		      public boolean checkSeed(int x,int y){
                  for(int i=0;i<this.pixelCount;i++){
                     
               	   if(this.x[i]==x && this.y[i]==y){
               		    
               		   return true;
               	   }
                  }                                    
                   return false; 
		        }//end checkSeed
		      
		      /***********************Calculate Aggregation Degree of Cluster*******************/
		     public float feedNeighbourPixels(int width,int height){
                int k=0;
		    	 float distance=0,sumOfDistance=0;
		    	 Seed s1;

		    	 try{
		    	 for(int i=0;i<pixelCount;i++){
                        distance=0;k=0;
		    		 if(x[i]-1>0 && y[i]-1>0){
		    			   s1	= ImageIndex.findSeed(x[i]-1, y[i]);
		    		       distance+=s1.NBSdistanceCalculate(this);
		    		       s1=ImageIndex.findSeed(x[i]-1, y[i]-1);
		    		       distance+=s1.NBSdistanceCalculate(this);
		    		       s1 = ImageIndex.findSeed(x[i],y[i]-1);
		    		       distance+=s1.NBSdistanceCalculate(this);
                         
		    		       k=k+3;
		    		 }
               
		    	   if(this.x[i]+1<width && this.y[i]+1<height){
		    		   s1=ImageIndex.findSeed(x[i]+1, y[i]);
		    		   distance+=s1.NBSdistanceCalculate(this);
		    		   s1=ImageIndex.findSeed(x[i]+1,y[i]+1);
                     if(s1!=null)

		    		   distance+=s1.NBSdistanceCalculate(this);
		    		   s1=ImageIndex.findSeed(x[i],y[i]+1);
		    		   distance+=s1.NBSdistanceCalculate(this);		    		   		    		 		    		   

		    		   k=k+3;
		    	   }
		    	 
		    	   if(this.x[i]-1>0 && this.y[i]+1<height){
		    		   s1=ImageIndex.findSeed(x[i]-1, y[i]+1);
		    		   distance+=s1.NBSdistanceCalculate(this);

		    		   k++;
		    		   
		    	   }
                 
		    	   if(this.x[i]+1<width && this.y[i]-1>0){
		    		   s1=ImageIndex.findSeed(x[i]+1, y[i]-1);
		    		   distance+=s1.NBSdistanceCalculate(this);

		    		   k++;
		    	   }
		    	     distance=distance/k; 
		    	     sumOfDistance+=distance;

		    	 }
                }
                catch(Exception e){
                	e.printStackTrace(/*com.Indexing.GenerateFiles.lg*/);
                	System.exit(1);
                }

                return sumOfDistance;
		     }//end feedNeighbourPixels()
}
