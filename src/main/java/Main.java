//cs680-restapi 

import static spark.Spark.*;

import java.sql.*;

public class Main {
	
	public static  String rStoJason(ResultSet rs) throws SQLException 
	{
	  if(rs.first() == false) {return "[]";} else {rs.beforeFirst();} // empty rs
	  StringBuilder sb=new StringBuilder();
	  Object item; String value;
	  java.sql.ResultSetMetaData rsmd = rs.getMetaData();
	  int numColumns = rsmd.getColumnCount();

	  sb.append("[{");
	  while (rs.next()) {

	    for (int i = 1; i < numColumns + 1; i++) {
	        String column_name = rsmd.getColumnName(i);
	        item=rs.getObject(i);
	        if (item !=null )
	           {value = item.toString(); value=value.replace('"', '\'');}
	        else 
	           {value = "null";}
	        sb.append("\"" + column_name+ "\":\"" + value +"\",");

	    }                                   //end For = end record

	    sb.setCharAt(sb.length()-1, '}');   //replace last comma with curly bracket
	    sb.append(",{");
	 }                                      // end While = end resultset

	 sb.delete(sb.length()-3, sb.length()); //delete last two chars
	 sb.append("}]");

	 return sb.toString();
	}

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/", (req, res) -> "This is a change");
        
    	get("/bookURL/:variable", (req, res) ->{
    		DBDemo app = new DBDemo();
    		String myString = app.getABookPathAPI(req.params(":variable"));
    		return myString;
    	});

    	get("/availableBooks",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.getListOfBooksAPI();
    		return myString;
    	});
    	
    	get("/resultset",(req,res)->{
    		DBDemo app = new DBDemo();
    		ResultSet mySet = app.getResultSetAPI();
    		//String myString = rStoJason(mySet);
    		return mySet;
    	});
    	

    	
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}

