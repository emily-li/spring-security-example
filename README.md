# spring-security-example
Example Spring Security project with Spring Boot

Contains three pages
* /home
  * Unsecured
* /secure
  * Secured. Redirects to login if user is unauthenticated
* /login
  * Unsecured. Redirects to login?error if login fails
  
# Requirements
* Java 1.8
  * Can easily be rewritten to lower versions, e.g. change IntStream foreach to for loop
* MySQL
  * Alternative database and driver can be configured in application.properties
  * schema.sql may need to be altered if language is changed