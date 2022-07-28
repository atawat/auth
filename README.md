# A simple authenticate service
# third party libraries
+ Spring
   + IOC and Spring MVC to process RestFul request
+ Spring Boot
   + Using self-host and auto-configuration
+ JackSon
   + json serialize and deserialize while processing token
+ lombok
   + auto create setter and getter for POJO
# API reference
+ POST /user
  + create user
+ DELETE /user/{userName}
  + delete user
+ POST /role
  + create role
+ DELETE /role/{roleName}
  + delete role
+ PUT /role/{roleName}?userName=123
  + add role to user
+ GET /role
  + list all roles
  + required token in Authorization header
+ POST /role/{roleName}/check
  + check role
+ POST /auth/login
  + authenticate
+ POST /auth/invalidate
  + invalidate token
  + required token in Authorization header