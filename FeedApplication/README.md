# FeedApplication Service
================================

I chose Spring-Boot for building the application, because it helps for rapid building of application. 
It provides lot of out of box configurations and has embedded tomcat support out of the box.
I chose Spring Data JPA for building DAO layer the reason is it supports everything from Native SQL 
queries, JPA Queries and in addition to that Query DSL which I feel are easy to use
I picked Spring REST for building the REST service because of it's ease of use with Spring Boot
Hibernate Validator for all basic validations and Jackson for Json serializing and de-serializing
I used Model Mapper for all mappings between Domain and POJO

I picked MySQL as a database because I felt RDBMS database made sense to me because of the relationships
mentioned in requirements. Any RDBMS would have worked, I preferred MySQL because of its ease of use.

# DAO Layer Design
================================
Since one User can subscribe to multiple feeds and same feed can be subscribed by multiple users, 
I created the relationship as many-to-many between User and Feed
Same way one Feed can have multiple Articles and same article can be in multiple feeds, I created
the relationship as many-to-many between Feed and Article

# GUID (UUID)
================================
The reason I added GUID to all the tables is, I don't want my Id's to be exposed. Since Id's are
generally sequential it is to guess Id's of records which is security vulnerability. All applications 
who wish to use my API will communicate through GUID.

# Running Application
================================
Command Prompt: Navigate to FeedApplication and run 'mvn spring-boot:run'
IDE : Open 'FeedApplication.java' and run the class.

# Configuration
================================
All Configurations related to application are in 'application.properties'
Configuring DB : 
Run the script provided in 'db.sql' on 'MySQL Workbench' and create the schema.
The creation of tables will be automatically taken care by 'spring.jpa.hibernate.ddl-auto = update'
Please update properties 'spring.datasource.username' & 'spring.datasource.password' with mysql username and password

Most of the configurations are performed in application.properties file

# Error Handling
================================
I have centralized the error handling of the application in 'ControllerErrorHandler.java'
I am handling all the application errors there and handling graceful JSON error responses
Also currently I am returning 400 error response for validation errors and resource not found scenarios
This can be configured as well (I know some prefer 404 in some cases)
'FeedValidationError.java' creates the custom JSON object error response

# Handling Concurrent Updates
================================
The Concurrent updates to the records by multiple client is handled by version column in the database

# REST End Points
================================

Create User
===========
POST REQUEST  http://localhost:8080/api/feed-app/v1/user
Sample Request {
               	"first_name": "foo",
               	"last_name": "bar",
               	"user_name": "foo-bar",
               	"email": "foobar@test.com"
               }
Response 200 for valid Requests
         400 for Invalid Request

GET ALL USERS
==============
GET REQUEST http://localhost:8080/api/feed-app/v1/user

GET Each USER
==============
GET REQUEST 
http://localhost:8080/api/feed-app/v1/user/{userGuid}
Ex : http://localhost:8080/api/feed-app/v1/user/379bd4df-b616-46f3-a614-60ebb6a17540

SUBSCRIBE TO FEED
=================
Subscribe a User to a Feed
POST REQUEST
http://localhost:8080/api/feed-app/v1/user/subscribe
Sample Request :
{
	"feed_attribute": "guid",
	"feed_attr_value": "5a20f135-6016-40f6-b079-ba7488b50f70",
	"user_name": "foo-bar",
    "user_guid": "379bd4df-b616-46f3-a614-60ebb6a17540"
}
Response 200 for valid Requests
         400 for Invalid Request

UNSUBSCRIBE TO FEED
===================
Unsubscribe a User to a Feed
POST REQUEST
http://localhost:8080/api/feed-app/v1/user/unSubscribe
Sample Request :
{
	"feed_attribute": "guid",
	"feed_attr_value": "5a20f135-6016-40f6-b079-ba7488b50f70",
	"user_name": "foo-bar",
    "user_guid": "379bd4df-b616-46f3-a614-60ebb6a17540"
}
Response 200 for valid Requests
         400 for Invalid Request
         
GET ALL SUBSCRIPTIONS 
=====================
Get all Feeds a Subscriber is following
GET REQUEST
http://localhost:8080/api/feed-app/v1/user/379bd4df-b616-46f3-a614-60ebb6a17540/subscriptions
Ex: http://localhost:8080/api/feed-app/v1/user/{userGuid}/subscriptions

GET ALL Articles
================
Get Articles from the set of Feeds a Subscriber is following
GET REQUEST
http://localhost:8080/api/feed-app/v1/user/379bd4df-b616-46f3-a614-60ebb6a17540/articles
Ex: http://localhost:8080/api/feed-app/v1/user/{userGuid}/articles



Create Feed
===========
POST REQUEST http://localhost:8080/api/feed-app/v1/feed
Sample Request {
                  "feed_name" : "Test Feed",
                  "category":"Technology"
               }
Response 200 for valid Requests
         400 for Invalid Request

GET ALL FEEDS
=============
GET REQUEST http://localhost:8080/api/feed-app/v1/feed

GET Each FEED
=============
GET REQUEST 
http://localhost:8080/api/feed-app/v1/feed/{feedGuid}
Ex : http://localhost:8080/api/feed-app/v1/feed/5a20f135-6016-40f6-b079-ba7488b50f70

Add Article to Feed
===================
Add Articles to a Feed

POST REQUEST : Create Article and add to feed
http://localhost:8080/api/feed-app/v1/feed/{feedGuid}/article/add
Ex: http://localhost:8080/api/feed-app/v1/feed/5a20f135-6016-40f6-b079-ba7488b50f70/article/add

Sample Request {
                  "article_name" : "Test Art1",
                  "article_url" : "https://www.foobar.com/"
              }
              
POST REQUEST : Add existing article to feed
http://localhost:8080/api/feed-app/v1/feed/article/add
Sample Request 
{
    "article_guid" : "0ce92c9e-fe41-4a50-832e-bcb4ea934a0e",
    "feed_guid" : "5a20f135-6016-40f6-b079-ba7488b50f70"
}
              
Create Article
==============
POST REQUEST http://localhost:8080/api/feed-app/v1/article
Sample Request {
               	"article_name" : "Foo Article",
                "article_url" : "https://www.article.com/"      
               }
Response 200 for valid Requests
         400 for Invalid Request

GET ALL ARTICLES
================
GET REQUEST http://localhost:8080/api/feed-app/v1/article

GET Each FEED
=============
GET REQUEST 
http://localhost:8080/api/feed-app/v1/article/{articleGuid}
Ex : http://localhost:8080/api/feed-app/v1/article/0ce92c9e-fe41-4a50-832e-bcb4ea934a0e

 
