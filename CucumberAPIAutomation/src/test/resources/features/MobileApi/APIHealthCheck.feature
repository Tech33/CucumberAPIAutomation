@parallel
Feature: This feature file is created to validate the health of API

  @healthcheck
  Scenario Outline: Initialize a Patient "PUT" Example for "200 response"
    Given the user has a valid authentication token for credentials
      | email | password |
      | XXXXX | Abcde123 |
    And user submit a request body with <bodySchema> and body parameters
      | countryCallingCode | dateOfBirth | email | firstName | gender | lastName | timeZoneName |
      |                  1 | 1952-01-23  | XXXXX | Bob       | male   | Smith    | US/Mountain  |
    When user submit a PUT Request on the <endpoint> endpoint with body
    Then API should return Status code <responseCode>
    And API response schema matches <JsonSchema>
    And Verify that id is not blank
    And Verify that uuid is not blank
    And the body response content should be matched
      #	 | key       | value   |
      | type      | Patient |
      | onboarded | true    |

    Examples: 
      | httpMethod | endpoint                    | responseCode | JsonSchema      | bodySchema |
      | PUT        | /api/v2/patients/initialize |          200 | Initialize.json | file.json  |

  @healthcheck1
  Scenario Outline: Segmentation-Questions for Patient "GET" Example with "200 response"
    Given the user has a valid authentication token for credentials
      | email | password |
      | XXXXX | Abcde123 |
    When user submit a GET Request on the <endpoint> endpoint
    Then API should return Status code <responseCode>
    And API response schema matches <JsonSchema>
    And the response body will have <id>
    And the response body should contain lable:
      | Which of the following best describe how you make decisions about healthcare?                                                                                                              |
      | I do a lot of research and discuss it with my physician. I trust them to provide valuable input in to my decision.                                                                         |
      | Traditional healthcare is one part of my overall health perspective. I use traditional, complementary, eastern, chiropractic and other areas of medicine to maintain my optimum health.    |
      | A family member usually encourages me to go to the doctor and helps me make decisions about my health. I want my healthcare to be convenient and efficient.                                |
      | I use the healthcare system frequently, and I am frustrated by it. I wish there were more convenient and efficient options for me and my family. The system doesn’t work very well for me. |
      | I trust my physician, they are well trained and so I listen and do what they say.                                                                                                          |

    Examples: 
      | httpMethod | endpoint                       | id | responseCode | JsonSchema                 |
      | PUT        | /api/v2/segmentation-questions |  3 |          200 | SegmentationQuestions.json |
