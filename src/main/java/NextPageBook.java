
public class NextPageBook {
	private int id;
	private String name;
	private String description;
	private String bookUrl;
	
	public NextPageBook(int id, String name, String description, String bookUrl){
		this.id = id;
		this.name = name;
		this.description = description;
		this.bookUrl = bookUrl;
	}
	
	public String getUrl(){
		return bookUrl;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
	}
	

	
}
