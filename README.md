# Best Matched Restaurants - Technical Assessment

## About the Project
This project, Best Matched Restaurants, is a Java-based technical assessment utilizing Spring Web and Spring Boot frameworks. It primarily operates as an API architecture project designed to search for the best restaurants based on specified parameters.

The API endpoint receives five possible search parameters as RequestParams and orders them according to priority:
1. Distance (ASC)
2. Rating (DESC)
3. Price (ASC)

The response provides a list of the top five restaurants, filtered using the RequestParams and ordered based on the aforementioned priority criteria.

### Testing the API Endpoint
There are two recommended ways to test this API endpoint:
1. **Using Postman or Insomnia:** Utilize these tools to perform API requests and test responses.
   - To test using Postman or Insomnia, create a GET request to the URL `http://localhost:8080/search/restaurants`.
   - Include optional Query Parameters:
     - `restaurantName`
     - `customerRating`
     - `distance`
     - `price`
     - `cuisineName`
2. **Built-in Command Line Prompt:** The application includes a command line prompt, allowing searches without the need for third-party tools or interfaces.

### Libraries and Tools Used
- **Lombok:** Reduces boilerplate code in models.
- **jline:** Facilitates user-friendly command-line prompting.
- **Gson:** Enables pretty printing of results in the terminal.
- **Spotless:** Enforces Google code formatting standards.

## Build Instructions
The project is already compiled into the jar file `best-matched-restaurants-esf.jar`. But if you wish to build and compile the project again, ensure you have Maven and JDK 21 installed.

### Building the Project
Run the following command:
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
To run the application from the project structure, execute the following command:
```bash
java -jar target/best-matched-restaurants-esf.jar
```
If you have only the jar file, execute the following command:
```bash
java -jar best-matched-restaurants-esf.jar
```