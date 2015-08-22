# score_server

This is a sample score server. 

- To run the server, type this in the root directory: java -jar server.jar

It exposes 3 APIs
- Login
	- Return a session Id which is an md5 hash of the userId concatenated with the current time and a secret key.
	- The session Id is valid for 10 minutes only.

- Post Scores
	- Used for posting scores of a user for a certain level.
	- Requires a session Id to process request.
	- Uses the session Id to retrieve the corresponding user Id for storing the score.
