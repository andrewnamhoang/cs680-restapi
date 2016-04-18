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
    	
    	get("/buyABook/:userid/:bookid",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.registerBookPurchaseAPI(req.params(":userid"), req.params(":bookid"));
    		return myString;
    	});
    	
    	post("/buyABookpost",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.registerBookPurchaseAPI(req.queryParams("userid"), req.queryParams("bookid"));
    		return myString;
    	});
    	
    	
    	//registerUserAPI(String username, String password, String firstname, String lastname, String email)
    	
    	post("/newUser",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.registerUserAPI(req.queryParams("username"), req.queryParams("password"), req.queryParams("firstname"), req.queryParams("lastname"),req.queryParams("email"));
    		if(myString.equals("-1")){
    			res.status(401);
    			return "Status 401";
    		}
    		return myString;
    	});
    	
    	post("/authUser",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.authUserAPI(req.queryParams("username"), req.queryParams("password"));
    		if(myString.equals("-1")){
    			res.status(401);
    			return "Status 401";
    		}
    		return myString;
    	});
    	
    	post("/updateUser",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.registerBookPurchaseAPI(req.queryParams("userid"), req.queryParams("bookid"));
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

