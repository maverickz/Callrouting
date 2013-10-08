package com.project.callrouting;

//import com.project.callrouting.CallRouter;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import junit.framework.TestCase;

public class CallRouterTest extends TestCase {
    public void test() throws JSONException {
      CallRouter controller = new CallRouter();
  	  JSONObject input = new JSONObject();
  	  JSONArray recipientList = new JSONArray();
  	  input.put("message", "SendHub Rocks");
/*    recipientList.put("+15555555517");
  	  recipientList.put("+15555555516");
  	  recipientList.put("+15555555515");
  	  recipientList.put("+15555555514");
  	  recipientList.put("+15555555513");
  	  recipientList.put("+15555555512");
  	  recipientList.put("+15555555511");
  	  recipientList.put("+15555555510");
  	  recipientList.put("+15555555559");
  	  recipientList.put("+15555555558");
  	  recipientList.put("+15555555557");*/
  	  recipientList.put("+15555555556");
  	  recipientList.put("+15555555555");
  	  recipientList.put("+15555555554");
  	  recipientList.put("+15555555553");
  	  recipientList.put("+15555555552");
  	  recipientList.put("+15555555551");
  	  input.put("recipients", recipientList);
  	  String JsonOutput = controller.getCallRoutes(input);
  	  System.out.println(JsonOutput);
    }
}
