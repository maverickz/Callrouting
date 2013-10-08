package com.project.callrouting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.project.domain.RoutingInput;


@SuppressWarnings("unused")
@Path("/json/route")
public class CallRouter {
	public int optimalSoln[][]; // contains the optimal solution at every recurrence step.
    protected Specifications []  items = {
            new Specifications("Small", "10.0.1.0/24",  1, 0.01),
            new Specifications("Medium", "10.0.2.0/24",  5, 0.05),
            new Specifications("Large", "10.0.3.0/24",  10, 0.1),
            new Specifications("Super", "10.0.4.0/24",  25, 0.25)
            };	
	
	private String getRequestIp(int idx) {
		String subnetIp = items[idx].getIpSubnet();
		StringBuilder requestIp = new StringBuilder(); 
		StringTokenizer stringTokenizer = new StringTokenizer(subnetIp, "/");
		while (stringTokenizer.hasMoreElements()) {
			String subnetPrefix = stringTokenizer.nextElement().toString();
			requestIp.append(subnetPrefix);
			break;
		}
		requestIp.setCharAt(requestIp.length()-1, '1');
		return requestIp.toString();
	}
	
	/**
	   * Find the optimal solution for a given target value and the set of denominations
	   * @param target - number of phone numbers to be assigned routes
	   * @param itemUsed - array containing the minimum number of items to process all the phone numbers
	   * @param lastItem - linked list of optimal items chosen, with lastItem[target] containing 
	   *                   the throughput of first item chosen
	   * @param itemIndex - array of indices of chosen items
	   * @return
	   */
	  public void findOptimalCost(int target, int[] itemUsed, int[] lastItem, int[] itemIndex) {
	     
		  itemUsed[0] = 0;
		  lastItem[0] = 1;
		  int numItems = items.length;
	      for(int thput = 1; thput <= target; thput++)
	        {
	            int minThput = thput;
	            int newItem  = 1;
	            int minItemIndex = 0;

	            for(int j = 0; j < numItems; j++)
	            {
	            	int itemThroughput = items[j].getThroughput();
	                if( itemThroughput > thput )   // Cannot use coin j
	                    continue;
	                if( itemUsed[ thput - itemThroughput ] + 1 < minThput )
	                {
	                    minThput = itemUsed[ thput - itemThroughput ] + 1;
	                    newItem  = itemThroughput;
	                    minItemIndex = j;
	                }
	            }
	            itemUsed[thput] = minThput;
	            lastItem[thput]  = newItem;
	            itemIndex[thput] = minItemIndex;
	        }
	  }	
	  
	 /**
	   * Format the output JSON object based on the optimal solution returned by findOptimalCost()
	   * @param target - number of phone numbers to be assigned routes
	   * @param lastItem - linked list of optimal items chosen, with lastItem[target] containing 
	   *                   the throughput of first item chosen
	   * @param itemIndex - array of indices of chosen items
	   * @param recipientsList - list of recipient phone numbers
	   * @param routes - list of individual routes across different item categories
	   * @return
	   * @throws JSONException 
	   */
	  public void formatOutput(int target, int[] lastItem, int[] itemIndex, List<String> recipientsList, ArrayList<JSONObject> routes) throws JSONException {
		  int lastIndex = -1;
		  int[] numItemsSelected = new int[items.length];
		  int numInstance = 0;
		  int listIndex = 0;
	      
		  //Find the number of instances chosen in each category
          for(int i = target; i > 0; )
          {
        	  numItemsSelected[itemIndex[i]]++;
              i -= lastItem[i];
          }
          
          //Process items that are selected 
          for(int i = 0; i < numItemsSelected.length; i++) {
        	  //Skip items that are not selected
        	  if(numItemsSelected[i] == 0) {
        		  continue;
        	  }
              JSONObject individualRoute = new JSONObject();
              JSONArray recipientNumbers = new JSONArray();
          	  String ipAddress = getRequestIp(i);
          	  individualRoute.put("ip", ipAddress);
          	  int throughput = items[i].getThroughput();
          	  //Add list of numbers that will be routed through the given item category
      		  for(int j = numItemsSelected[i]*throughput-1; j>=0; j--) {
    			  recipientNumbers.put((String)recipientsList.get(listIndex));
    			  listIndex++;
    		  }
      		  individualRoute.put("recipients", recipientNumbers);
      		  routes.add(individualRoute);
          }
	  }

	  
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public RoutingInput getRoutingInputInJSON() {

		RoutingInput defaultInput = new RoutingInput();
		List<String> numbers = new ArrayList<String>();
		defaultInput.setMessage("SendHub Rocks");
		numbers.add("+15555555556");
		defaultInput.setNumbers(numbers);
		return defaultInput;
	}

	
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoutingInputInJSON(RoutingInput input) throws JSONException {
    	System.out.println("");
    	try {
    		int used[];
    		int last[];
    		int index[];
    		JSONObject output = new JSONObject();
    		String message = (String) input.getMessage();         
    		// loop recipients array
    		List<String>  recipientsList = input.getNumbers();
    		
    		/*for(int i = 0 ; i < recipients.length() ; i++){
    			recipientsList.add(recipients.getString(i));
    		}*/
    		ArrayList<JSONObject> routes = new ArrayList<JSONObject>();
    		int target = recipientsList.size();
    		if(target == 0) {
    			return null;
    		}
    		used = new int[target+1];
    		last = new int[target+1];
    		index = new int[target+1];
    		// Find the items that would constitute the optimal solution
    		// under the constraint of not under-utilizing a resource
    		findOptimalCost(target, used, last, index);
    		output.put("message", message);
    		formatOutput(target, last, index, recipientsList, routes);
    		//Append the list of routes from all the items to the output
    		output.put("routes", routes);
    		return Response.status(201).entity(output.toString()).build();	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}  
    	return null;
    }
}

