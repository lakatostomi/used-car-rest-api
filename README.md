The API documentation is available on `/swagger` endpoint.
The application can be run in the IDE or you should build the app with `mvn install` and run the `CarAdApp.jar` file as standalone java application.

In this release the application is not production ready, to achieve this, I would suggest the following development opportunities:
- The H2 in-memory database should be replaced with a RDBMS like MySQL, or PostgreSQL.
- Have to introduce a more granular Role Based Access Control, in this release every User is granted with ROLE_USER, there is no Administrator who can moderate the Users and the Ads.
- Need more functions like, where Users can update their profile and their Ads, the last one can be implemented with HATEOAS, in this way the Ads that are related to the Users are linked with a link to the User. (REST constraint)
- The User need to confirm that the email address is valid through a verification link sent to the email account of the User.
- Need to implement caching to reduce the database connections and cache the frequently requested data. (REST constraint)
- The searching among Ads can result a huge number of hit, the reduce the load from the client service need to implement pagination. 
- The searching results can not be ordered in this release, ordering is necessary.
- The application performance should be monitored with monitoring tools like Prometheus and Grafana.
- The application has to be containerized to easy deployment and scalability.
- The HTTP protocol need to extend with TLS.