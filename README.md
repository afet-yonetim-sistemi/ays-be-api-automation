# AYS API Test Automation Repository

This repository contains automated API tests using the REST Assured framework. The tests are organized into packages for ease of use and maintainability.

## Project Structure
![Framework Flowchart](https://github.com/afet-yonetim-sistemi/ays-be-api-automation/blob/main/Flowcharts.jpeg?raw=true)


endpoints: Contains classes responsible for making HTTP requests to API endpoints.

tests: Houses the actual test cases that use endpoints from the `endpoints` package.

payload: Stores payload data used in API requests.

utility: Contains utility classes that assist in various testing tasks.

reports: Holds generated test reports.

pom.xml: Maven project configuration file specifying dependencies.

runners: TestNG configuration file for managing test suites.


## Contributing

If you'd like to contribute to this project, please follow these guidelines:
1.Clone this repository to your local machine.
git clone https://github.com/afet-yonetim-sistemi/ays-be-api-automation.git

2.Create a new branch for your feature or bug fix.

3.Store endpoints in ‘Routes’ class.

4.Create an http request method in the related ‘endpoints’ class.

5.Script your test case under the ‘tests’ package’s related test class.

6.Make your changes and commit them.

7.Create a pull request, describing your changes and why they should be merged.

## Running Specific Test Suites

You can run specific test suites by modifying the testng.xml file under the runners package. Add or remove <suite> elements to define the suites you want to run.

## Generating Reports

Test execution reports can be found in the ‘reports’ directory. You can open the HTML report in a web browser to view test results.



