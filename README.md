# cs680-restapi

Root URL for the API is http://cs680-restapi.herokuapp.com/

#Books

**Fetch all books available for download**

To get information for all books in the database that can be downloaded, send a GET request to
````
http://cs680-restapi.herokuapp.com/availableBooks 
````

Information that will currently be returned by the API for each book is: 

bookId: Int(11)  
title: Varchar(45)  
pages: Int(11)  
path: Varchar(99)  
author: Varchar(99)  
year: Varchar(99)  
coverpath: Varchar(99)  
tags: Varchar(99)  
rating: Int(11)  

The format of the response string is:  

````
[
  {
   "bookid":"1",
   "title":"Jack and the Elastic Beanstalk",
   "pages":"5",
   "path":"http://s3-us-west-2.amazonaws.com/nextpage/books/jackebs.pdf",
   "author":"Amazon",
   "year":"2016",
   "coverpath":"https://s3-us-west-2.amazonaws.com/nextpage/images/jackebs.PNG",
   "tags":"funny",
   "rating":"5"
  },
 ...
]
````

**Fetch all books that a user has downloaded**
 
To retrieve information on all books that a certain user has downloaded send a GET request to
````
http://cs680-restapi.herokuapp.com/purchasedBooks/:userid
````
This will return an array of purchased book JSON objects 
Fields retrieved for each book are: 

purchaseid   
bookPurchased    
userpurchased    
date    

````
[
  {
   "purchaseid":"12",
   "bookpurchased":"1",
   "userpurchased":"1",
   "date":"2016-03-12 11:11:11.0"
  },
  ...
]
````

**Associate a purchased book with a user**

To associate a book with a user, you will send a POST request to 

````
http://cs680-restapi.herokuapp.com/buyABookpost
````

The body of the request will contain two parameters:  
userid  
bookid  


**Register a new user**

To register a new user send a POST request to
````
http://cs680-restapi.herokuapp.com/newUser
````

The body of the request will contain five parameters:
username
password
firstname
lastname
email

A successful registration will make the API respond the new users ID number in JSON:

````
[
  {
    "userid":"17"
  }
]
````
