Feature: Get satellites position

  @HappyPath
  Scenario Outline: To verify get position endpoint returns success response with correct timestamp
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 200 HTTP response
    Then verify response parameters are available
      | name  | id      |
      | iss   | 25544   |

    Examples:
      | sateliteattribute |
      | 25544/positions   |

  @UnhappyPath
  Scenario Outline: To verify get position endpoint returns error response with incorrect timestamp
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 404 HTTP response
    Then verify response parameters are available
      | error                                                        | status |
      | Invalid controller specified (satellites_Incorrectpositions) | 404    |

    Examples:
      | sateliteattribute        |
      | 25544/Incorrectpositions |

  @UnhappyPath
  Scenario Outline: To verify get position endpoint returns error response with incorrect sateliteID
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 404 HTTP response
    Then verify response parameters are available
      | error                                                        | status |
      | Invalid controller specified (satellites_Incorrectpositions) | 404    |

    Examples:
      | sateliteattribute        |
      | 31544/Incorrectpositions |
