///cs680-restapi 

import static spark.Spark.*;

public class Main {
	
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/", (req, res) -> "Next page has successfully been created");
        
    	get("/availableBooks",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.getListOfBooksAPI();
    		return myString;
    	});
    	
    	get("/purchasedBooks/:userid",(req,res)->{// returns list of purchased books
    		DBDemo app = new DBDemo();
    		String myString = app.getListOfPurchasedBooksAPI(req.params(":userid"));
    		return myString;
    	});
    	
    }

    static int getHerokuAssignedPort() {//gets a port for Heroku
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}

