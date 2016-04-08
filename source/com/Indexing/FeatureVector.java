package com.Indexing;
 
import java.io.Serializable;

/**** returns the Feature Vector for a cluster*****/
public class FeatureVector implements Serializable {
	                  private float Hue,Value,Chromaticity;
	                  private float normalizedPixelcount;
	                  private float aggregationDegree;
	                         
	                  public FeatureVector(Seed obj,int width,int height){
	                	    Hue=obj.getHue();
	                	    Value=obj.getValue();
	                	    Chromaticity=obj.getChromaticity();
	                	  
	                	    normalizedPixelcount=(float)obj.pixelCount/(width*height);
	                	    System.out.println("\nPixelcount:"+obj.pixelCount+"\nwidth*height="+width*height);  
                            aggregationDegree=obj.feedNeighbourPixels(width,height); 
   	                  }
                
                     public void printFeatureVector(){
                        System.out.println("Hue:"+Hue+",Value:"+Value+"Chromaticity:"+Chromaticity+"NP:"+normalizedPixelcount+"Aggregation degree:"+aggregationDegree);
                      }

                     public float getHue(){return this.Hue;}
                     public float getValue(){return this.Value;}
                     public float getChromaticity(){return this.Chromaticity;}
                     public float getPixelcount(){return this.normalizedPixelcount;}
                     public float getAggreationDegree() {return this.aggregationDegree;}
                    	 
}	                           	                 	                               
