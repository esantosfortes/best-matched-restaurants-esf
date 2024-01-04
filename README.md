# Best Matched Restaurants - Technical Assessment

## About the Project
This project, Best Matched Restaurants, is a Java-based technical assessment utilizing Spring Web and Spring Boot frameworks. It primarily operates as an API architecture project designed to search for the best restaurants based on specified parameters.

The API endpoint receives five possible search parameters as RequestParams and orders them according to priority:
1. Distance (ASC)
2. Rating (DESC)
3. Price (ASC)

The response provides a list of the top five restaurants, filtered using the RequestParams and ordered based on the aforementioned priority criteria.

### Testing the API Endpoint
There are three recommended ways to test this API endpoint:
1. **Using Postman or Insomnia:** Utilize these tools to perform API requests and test responses.
   - To test using Postman or Insomnia, create a GET request to the URL `http://localhost:8080/search/restaurants`.
   - Include optional Query Parameters:
     - `restaurantName`
     - `customerRating`
     - `distance`
     - `price`
     - `cuisineName`
   - ![Postman usage](https://i.ibb.co/KW8tC3y/using-postman.jpg "Postman usage")

2. **Built-in Command Line Prompt:** The application includes a command line prompt, allowing searches without the need for third-party tools or interfaces.
3. **Browsers like Chrome or Firefox:** The same URL you'd use in Postman or Imsomnia can be used in the browser.
    - You will have to manually insert each of the aforementioned search parameter by hand:
    - ![Browser usage](https://i.ibb.co/CwbxpR4/using-browser.jpg "Browser usage")

### Libraries and Tools Used
- **Lombok:** Reduces boilerplate code in models.
- **jline:** Facilitates user-friendly command-line prompting.
- **Gson:** Enables pretty printing of results in the terminal.
- **Spotless:** Enforces Google code formatting standards.
- **Swagger-UI:** For documenting the API.
- **JUnit & Mockito:** For Unit tests and Integration tests.

## Build Instructions
The project is already compiled into the jar file `best-matched-restaurants-esf.jar`. But if you wish to build and compile the project again, ensure you have Maven and JDK 21 installed.

### Building the Project
Building the project involves performing checks for code style, running unit tests and integration tests, and packaging the application into a JAR file.

To build the project, execute the following command:
```bash
mvn clean install
```


### Code Formatting
Use Spotless to check and apply code formatting:
```bash
mvn spotless:check
mvn spotless:apply
```

## Run instructions
To run the application from the jar file in the project structure, you'll need minimal JRE 17 version or JDK 21.

Execute the following command from the project root:
```bash
java -jar target/best-matched-restaurants-esf.jar
```
If you have only the jar file, execute the following command:
```bash
java -jar best-matched-restaurants-esf.jar
```
#### Additional Info
Both .csv files `/src/main/resources/data/cuisines.csv` and `/src/main/resources/data/restaurants.csv` are already packaged with the jar file `best-matched-restaurants-esf.jar` so there is no need to have these files if you are running the jar file.

### Run the application with Spring-boot:
If you want to run the application from the project root using spring-boot run:
```bash
mvn spring-boot:run
```

## View API Documentation
To view the API documentation using Swagger-UI, visit the following link after the Application is Running:
`http://localhost:8080/swagger-ui/index.html`

![Swagger-UI Docs](https://i.ibb.co/sHq53jV/swagger-docs.jpg "Swagger-UI Docs")