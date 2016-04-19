//class for creating and maniuplating the database


import java.sql.*;
import java.util.Properties;
	
public class DBDemo {
		/** The name of the MySQL account to use (or empty for anonymous) */
		private final String userName = "awsmaster";
		/** The password for the MySQL account (or empty for anonymous) */
		private final String password = "randomkey123";
		/** The name of the computer running MySQL */
		private final String serverName = "nextpagedb2.cy9nrdlkzrrv.us-east-1.rds.amazonaws.com";
		/** The port of the MySQL server (default is 3306) */
		private final int portNumber = 3306;
		/** The name of the database we are testing with (this default is installed with MySQL) */
		private final String dbName = "mydb2";

		//Code from git to JSONINZE a Result
		public  String singlerStoJason(ResultSet rs) throws SQLException 
		{
		  if(rs.first() == false) {return "[]";} else {rs.beforeFirst();} // empty rs
		  StringBuilder sb=new StringBuilder();
		  Object item; String value;
		  java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		  int numColumns = rsmd.getColumnCount();

		  sb.append("{");
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
		 sb.append("}");

		 return sb.toString();
		}
		public  String rStoJason(ResultSet rs) throws SQLException 
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
		
		public Connection getConnection() throws SQLException {
			Connection conn = null;
			Properties connectionProps = new Properties();
			connectionProps.put("user", this.userName);
			connectionProps.put("password", this.password);
			String connectionString = "jdbc:mysql://"+ this.serverName + ":" + this.portNumber + "/" + this.dbName;
			
			conn = DriverManager.getConnection(connectionString,
					connectionProps);
			return conn;
		}
		
		public boolean executeUpdate(Connection conn, String command) throws SQLException {
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        stmt.executeUpdate(command); // This will throw a SQLException if it fails
		        return true;
		    } finally {

		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}
		
		//Give it the connection and it returns a JSON string of the entire book table (all info)
		public String getAllBooks(Connection conn) throws SQLException {
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "SELECT * FROM mydb2.books";
		        ResultSet  rs = stmt.executeQuery(sql);
		        //Rogelio: Use our own JSONizer here???
		        String response = rStoJason(rs);

		        rs.close();
		        return response;
		    } finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}
		
		public String getListOfBooksAPI(){
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not connect to the database");
				e.printStackTrace();
				return "Error Could Not Connect to the DB";
			}
			//Get list of books
			try { 			
				String booklist = getAllBooks(conn);
				return(booklist);	
		    } catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not get List of Books");
				e.printStackTrace();
				return "Returned - Error Could Not Get List Of Books";
			}
		}
		
		public String getListOfPurchasedBooksAPI(String userid){
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not connect to the database");
				e.printStackTrace();
				return "Error Could Not Connect to the DB";
			}
			//Return purchased books
			try { 			
				String booklist = getPurchasedBooks(userid, conn);
				return(booklist);	
		    } catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not get List of  Purchased Books");
				e.printStackTrace();
				return "Returned - Error Could Not Get List Of Purchased Books";
			}
		}

		public String getPurchasedBooks(String userid, Connection conn) throws SQLException {
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "SELECT * FROM mydb2.purchases WHERE userpurchased ='" +userid+ "'";
		        ResultSet  rs = stmt.executeQuery(sql);
		        String response = rStoJason(rs);

		        rs.close();
		        return response;
		    } finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}

		//Methods to buy a book
		public int registerBookPurchase(String userid, String bookid, Connection conn) throws SQLException{
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "INSERT INTO `mydb2`.`purchases` (`bookpurchased`, `userpurchased`) VALUES ('"+bookid+"', '"+userid+"');";
		        int rs = stmt.executeUpdate(sql);
		        return 1;
		    }catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not push purchase SQL table");
				e.printStackTrace();
				return 0;
			} finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}
		
		public String registerBookPurchaseAPI(String userid, String bookid){
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not connect to the database");
				e.printStackTrace();
				return "Error Could Not Connect to the DB";
			}
			try { 			
				int response = registerBookPurchase(userid, bookid, conn);
				if(response ==1){
					return "Success! Purchase was pushed";
				}else{
				return "Somethign Broke :(";
				}
		    } catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not get List of Purchased Books");
				e.printStackTrace();
				return "Returned - Error Could Not Get List Of Purchased Books";
			}
		}
		
		//Method to create a new user
		public String registerUserAPI(String username, String password, String firstname, String lastname, String email){
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not register user - No Connectino to DB");
				e.printStackTrace();
				return "Error Could Not Connect to the DB";
			}
			try { 			
				int response = registerNewUser(username, firstname, lastname, password, email, conn);
				if(response==1){
					String useridresponse = getUserId(username, conn);
					//String someString =  "[{userid:"+ useridresponse +"}]"; 
					return useridresponse;
				}else{
				return "-1";
				}
		    } catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not register user");
				e.printStackTrace();
				return "Returned - Error Could Not register user";
			}
		}
		
		public int registerNewUser(String username, String firstname, String lastname, String password, String email, Connection conn) throws SQLException{
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "INSERT INTO `mydb2`.`user` (`fname`, `lname`, `uname`,`password`,`email`) VALUES ('"+firstname+"', '"+lastname+"','"+username+"','"+password+"','"+email+"');";
		        int rs = stmt.executeUpdate(sql);
		        return 1;
		    }catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not create new user");
				e.printStackTrace();
				return 0;
			} finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}

		public String getUserId(String username, Connection conn) throws SQLException{
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "SELECT userid FROM mydb2.user WHERE uname ='" +username+ "'";
		        ResultSet  rs = stmt.executeQuery(sql);
		        String response = singlerStoJason(rs);
		        rs.close();
		        return response;
		    }catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not create new user");
				e.printStackTrace();
				return "Failed Select User ID";
			} finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}

		//Methods to authenticate user
		public String authUserAPI(String username, String password){
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not register user - No Connectino to DB");
				e.printStackTrace();
				return "Error Could Not Connect to the DB";
			}
			try { 			
				String response = authUser(username, password, conn);
				return response;
		    } catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not register user");
				e.printStackTrace();
				return "Returned - Error Could Not register user";
			}
		}
		
		public String authUser(String username, String password, Connection conn) throws SQLException{
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "SELECT userid FROM mydb2.user WHERE uname ='"+username+"' AND password='"+password+"'";
		        ResultSet  rs = stmt.executeQuery(sql);
		        if (!rs.next() ) {
		            return "-1";
		        }
		        String response = singlerStoJason(rs);
		        rs.close();
		        return response;
		    }catch (SQLException e) {
				System.out.println("System Out - Cannot verify identity");
				e.printStackTrace();
				return "-1";
			} finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}
		
		
		
		public int newRecordingAPI(String userid, String bookid, String name, String type, String path) throws SQLException{
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not insert recording data into DB - no connection");
				e.printStackTrace();
				return -1;
			}
			int response = newRecording(userid, bookid, name, type, path, conn);
			return response;
		}
		
		
		public int newRecording(String userid, String bookid, String name, String type, String path, Connection conn){
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "INSERT INTO `mydb2`.`recording` (`owner`, `bookassoc`, `name`,`type`,`path`) VALUES ('"+userid+"', '"+bookid+"','"+name+"','"+type+"','"+path+"');";
		        int response = stmt.executeUpdate(sql);
		        return response;
		    }catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not add path to DB");
				e.printStackTrace();
				return -1;
			} finally {
		    	// This will run whether we throw an exception or not
				if (stmt != null) { try {
						stmt.close();
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} }
		   		}
		}
		
		public String getMyRecordingsAPI(String userid){
			//Connect to DB
			Connection conn = null;
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not connect to the database");
				e.printStackTrace();
				return "Error Could Not Connect to the DB";
			}
			//Return the recordings books
			try { 			
				String booklist = getMyRecordings(userid, conn);
				return(booklist);	
		    } catch (SQLException e) {
				System.out.println("System Out - ERROR: Could not get List of  Purchased Books");
				e.printStackTrace();
				return "Returned - Error Could Not Get List Of Purchased Books";
			}
		}
		
		public String getMyRecordings(String userid, Connection conn) throws SQLException {
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        String sql = "SELECT * FROM mydb2.recording WHERE owner ='" +userid+ "'";
		        ResultSet  rs = stmt.executeQuery(sql);
		        if (!rs.next() ) {
		            return "-1";
		        }
		        String response = rStoJason(rs);
		        rs.close();
		        return response;
		    } finally {
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}
		
}


