package com.Retrieval;



/***** Class for displaying images on GUI******/
public class Result {

	public String imgName;
	public double percentage;
	
	public Result(String img,double perc)
	{
		this.imgName=img;
		this.percentage=perc;
	}

	public String getName()
	{
		return imgName;		
	}
	
	public double getPercentage()
	{
		return percentage;
	}
   
}//end of class
