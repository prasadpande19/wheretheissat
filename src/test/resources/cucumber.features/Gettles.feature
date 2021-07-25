Feature: Returns the TLE data for a given satellite

  @HappyPath
  Scenario Outline: To verify get tles endpoint returns the response as per the satelite
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 200 HTTP response
    Then verify response parameters are available
      | name  | id      |
      | iss   | 25544   |

    Examples:
      | sateliteattribute |
      | 25544/tles |

  @UnhappyPath
  Scenario Outline: To verify get tles endpoint returns error response for incorrect satellite
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 404 HTTP response
    Then verify response parameters are available
      | error               | status |
      | satellite not found | 404    |

    Examples:
      | sateliteattribute |
      | 35544/tles |


