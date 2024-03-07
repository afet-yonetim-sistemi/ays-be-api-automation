# AYS API Test Automation Repository

This repository contains automated API tests using the REST Assured framework. The tests are organized into packages for ease of use and maintainability.

## Project Structure
![Framework Flowchart](https://github.com/afet-yonetim-sistemi/ays-be-api-automation/blob/main/Flowcharts.jpeg?raw=true)


*__endpoints__*: 
This Java classes serves as a utility for handling various API requests related to actions in the application. It offers methods to interact with the API endpoints, utilizing RestAssured library.These methods simplify making API requests by handling headers, content types, and endpoints, allowing for easier integration into test classes or other parts of your application.

Please ensure that the *Routes* class contains the appropriate endpoint URLs. Also, consider providing details about the *Authorization* class, which handles authorization for API requests.

*__tests__*: 
Each test method performs specific scenarios, sends requests to the API using related enpoints which defined in *endpoints* class, validates the response received, and performs assertions to ensure the expected behavior.

The logging statements within each test method (logger.info) indicate the test case currently being executed.

This class helps ensure that user update functionalities work correctly, handle various scenarios gracefully, and properly validate error responses.

*__payload__*: 
In this framework, payload classes serve as structured data models utilized during the process of making requests to APIs and handling the subsequent responses. These classes, also known as POJOs (Plain Old Java Objects), play a role in representing and organizing data entities.

__Handling Requests__:

*Request Payload Representation*: When initiating API requests, these payload classes define the structure and content of the data to be sent. Attributes within the payload classes correspond to the data fields required in the request body.

*Request Construction*: Instances of payload classes are created and populated with relevant information before being used as the request payload. They facilitate clear and organized representation of data intended for API endpoints.

__Managing Responses__:

*Response Data Organization*: Upon receiving responses from API calls, these same payload classes are initialized to extract and structure the response data. This approach allows for the organized handling and manipulation of data retrieved from API responses.

*Verification and Validation*: Using payload classes for responses enables straightforward verification and assertion processes. Attributes within these classes are compared against the received response data, facilitating validation checks and assertions to ensure expected outcomes.


*__utility__*: Contains utility classes that assist in various testing tasks.

*__reports__*: Holds generated test reports.

*__pom.xml__*: Maven project configuration file specifying dependencies.

*__runners__*: TestNG configuration file for managing test suites.

---

## Contributing

If you'd like to contribute to this project, please follow these guidelines:

1. Clone this repository to your local machine.
git clone https://github.com/afet-yonetim-sistemi/ays-be-api-automation.git

2. Create a new branch for your feature or bug fix.

3. Store endpoints in ‘Routes’ class.

4. Create an http request method in the related ‘endpoints’ class.

5. Script your test case under the ‘tests’ package’s related test class.

6. Make your changes and commit them.

7. Create a pull request, describing your changes and why they should be merged.

---

## Using the Configuration File

The project leverages a configuration file (configuration.properties) to securely manage sensitive or environment-specific data. 

To effectively utilize this configuration file, follow these steps:

1. **Create a Configuration File:** Duplicate the sample_configuration.properties file provided in the framework,
   renaming it as configuration.properties. Input your sensitive data in the form of key-value pairs within this file.

2. **Accessing Configuration Data:** To access the stored data, use the ConfigurationReader.getProperty("your key")
   method available under the utility package. This method retrieves the values associated with specific keys from the
   configuration.properties file.

3. **Example Usage - HowToUseConfigurationFile Class:**
Review the HowToUseConfigurationFile class located in the utility package. This class demonstrates practical usage by showcasing examples of calling the ConfigurationReader.getProperty("your key") method to retrieve configuration values.

By following these steps, you can securely manage sensitive information and easily access it within the project using the ConfigurationReader utility.

---

## Running Specific Test Suites

You can run specific test suites by modifying the testng.xml file under the runners package. Add or remove <suite>
elements to define the suites you want to run.

---

## Running Tests from the Command Line

You can run tests from the command line using Maven. Navigate to the project directory and run the following command:

**Daily Test Execution:**

- Smoke Test

```bash
mvn clean test -P daily
```

**Weekly(All) Test Execution:**

- Smoke Test
- Regression Test

```bash
mvn clean test -P weekly
```

---

## Generating Reports

Test execution reports can be found in the ‘reports’ directory. You can open the HTML report in a web browser to view
test results.
