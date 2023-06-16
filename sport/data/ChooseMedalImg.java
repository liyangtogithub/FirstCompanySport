package com.desay.sport.data;

import com.desay.sport.main.R;

public class ChooseMedalImg {
	
	 public int getMedalBackground(String n) {
	    	int a=StringToint(n);
	      	switch (a) {
	      	case 0:	
	  			return R.drawable.medal_0;
	      	case 18:	
	  			return R.drawable.medal_18;
	      	case 1:	
	  			return R.drawable.medal_1;
	      	case 2:	
	  			return R.drawable.medal_2;
	     	case 3:	
	  			return R.drawable.medal_3;
	    	
	  	   }
			return R.drawable.medal_18;
	      }
	  public int StringToint(String aa)
	    {
	    	int readd=0;
	       try{
	    	   readd=Integer.parseInt(aa.trim());
	        }catch(Exception e){
		    }  	
			return readd; 
	    }

}
