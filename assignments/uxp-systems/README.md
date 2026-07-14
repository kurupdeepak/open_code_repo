# Amdocs Assignment
User Management API

• GET /user <br>
• POST /user <br>
• PUT /user <br>
• DELETE /user <br>

Pre-Requisite
Java 8 ( >1.8.0_191)

Build and Test 

• Clone from the repo <br>
• Run mvn clean install <br>
• Run mvn clean test <br>
• Run mvn -Dtest=com.uxpsystems.assignment.UserControllerIT test or <br>
• mvn -Dtest=com.uxpsystems.assignment.UserServiceIT test <br>

Run as a standalone web application <br>
• • Run mvn clean install<br>
• • Locate *.war inside target<br>
• • Run from the command prompt following <br>
java -jar assignment-0.0.1-SNAPSHOT.war<br>
• • Open the browser/postman <br>
http://localhost:8080/user/0<br>
• • Prompted for username/password ? enter "systemuser@testdomain.com/password1"<br>
• • Following message should be displayed<br>
{<br>
"message": "User not found : 0",<br>
"errors": null,<br>
"status": "NOT_FOUND"<br>
}<br>
