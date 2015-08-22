# score_server

This is a sample score server. 

- Server runs on port 1435
- Configurations are located under com/king/config/ServerConfig.java
- To run the server, type this in the root directory: java -jar server.jar

It exposes 3 APIs
- Login
	- Return a session Id which is an md5 hash of the userId concatenated with the current time and a secret key.
	- The session Id is valid for 10 minutes only.

- Post Scores
	- Used for posting scores of a user for a certain level.
	- Requires a session Id to process request.
	- Uses the session Id to retrieve corresponding user Id for persisting the score.

- Get High Scores
	- Returns a list of no more than 15 highest scores in the csv format <user_id>=<score> for a level
