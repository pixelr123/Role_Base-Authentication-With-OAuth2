# Role_Base-Authentication-With-OAuth2

This is a simple application of authentication with role based using OAuth2 and Spring Security

Basic Auth: 
  Username: auth-client
  Password: auth-secret

Step 1: create token: localhost:8080/oauth/token

Step 2: put the access_token in header - 
  Post: localhost:8080/users?access_token=<token>
  Get: localhost:8080/users/2?access_token=<token>
 
