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
    	
    	post("/newUser",(req,res)->{//returns userid
    		DBDemo app = new DBDemo();
    		String myString = app.registerUserAPI(req.queryParams("username"), req.queryParams("password"), req.queryParams("firstname"), req.queryParams("lastname"),req.queryParams("email"));
    		if(myString.equals("-1")){
    			res.status(401);
    			return "Status 401";
    		}
    		res.body(myString);
    		return myString;
    	});
    	
    	post("/authUser",(req,res)->{//returns userid
    		DBDemo app = new DBDemo();
    		String myString = app.authUserAPI(req.queryParams("username"), req.queryParams("password"));
    		if(myString.equals("-1")){
    			res.status(401);
    			return "Status 401";
    		}
    		return myString;
    	});
    	
    	post("/newRecording",(req,res)->{//returns recordingid
    		DBDemo app = new DBDemo();
    		int myInt = app.newRecordingAPI(req.queryParams("userid"), req.queryParams("bookid"),req.queryParams("name"),req.queryParams("type"),req.queryParams("path"));
    		if(myInt == -1 ){
    			res.status(401);
    			return "Status 401";
    		}
    		return "";
    	});
    	
    	post("/myRecordings",(req,res)->{//returns recordingid
    		DBDemo app = new DBDemo();
    		String myString = app.getMyRecordingsAPI(req.queryParams("userid"));
    		if(myString.equals("-1")){
    			res.status(401);
    			return "Status 401";
    		}
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

