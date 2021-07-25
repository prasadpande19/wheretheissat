Feature: Returns result based on coordinates

  @HappyPath
  Scenario Outline: To verify get coordinates endpoint returns position, current time offset, country code, and timezone id for a given set of coordinates in the format of longitude,latitude
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 200 HTTP response
    Then verify response parameters for coordinates

    Examples:
      | sateliteattribute |
      | coordinates       |

  @UnhappyPath
  Scenario Outline: To verify get coordinates endpoint returns error response for incorrect lattitude and longitude
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 404 HTTP response
    Then verify error response body
    Examples:
      | sateliteattribute     |
      | coordinates/Incorrect |

  @UnhappyPath
  Scenario Outline: To verify get coordinates endpoint returns error response for empty lattitude and longitude
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 405 HTTP response
    Then verify response parameters are available
      | error                                            | status |
      | Invalid controller specified (coordinates_Empty) | 404    |
    Examples:
      | sateliteattribute           |
      | coordinates/Incorrect/Empty |