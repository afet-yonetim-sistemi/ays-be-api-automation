Feature: User Verification

  @wip
  Scenario: verify information about logged user

    Given body request credentials
    When I sent post request to "/api/v1/authentication/token" endpoint with request body
    Then status code should be 200
    And content type is "application/json"