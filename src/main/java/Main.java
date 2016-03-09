
import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/", (req, res) -> "This is a change 2");
        
        
    	get("/bookURL/:variable", (req, res) ->{
    		DBDemo app = new DBDemo();
    		String request = null;
    		String myString = app.getABookPathAPI(req.params(":variable"));
    		return myString;
    	});

    	get("/availableBooks",(req,res)->{
    		DBDemo app = new DBDemo();
    		String myString = app.getListOfBooksAPI();
    		return myString;
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

