### How to run
- To run the app, it's enough to execute it through your IDE (Tested in Intellij).
- The app will run in port 8080.
- The app creates an H2 in memory instance at startup, so no needed to run an external DB.

### How to use the app
- In /docs/postman you can find a Postman collection set up to exercise the code
- Run the generateBooks request to create 5 testing books
- Play with the requests in the Reservations folder inside the collection
- To check the database, go to http://localhost:8080/h2-console/ and add this info:
  - Driver class: org.h2.Driver
  - JDBC URL: jdbc:h2:mem:testdb
  - User Name: sa
  - Password:
    - No password is needed, leave it blank


### Restrictions
- No user management was implemented. A global user it's being persisted when the Reservation controller is instantiated.