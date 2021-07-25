Feature: Get satellites

  @HappyPath
  Scenario Outline: To verify user can fetch the list of satellites successfully
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 200 HTTP response
    Then verify response parameters are available
      | name  | id      |
      | iss   | 25544   |

    Examples:
      | sateliteattribute |
      |                   |


  @HappyPath
  Scenario Outline: To verify get satellites endpoint functionality using satellite ID
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 200 HTTP response
    Then verify response parameters are available
      | name  | id      |
      | iss   | 25544   |

    Examples:
      | sateliteattribute |
      | 25544      |

  @UnhappyPath
  Scenario Outline: To verify get satellites endpoint functionality using incorrect satellite id
    Given user call the get endpoint for satellites "<sateliteattribute>"
    When user receive a 404 HTTP response
    Then verify response parameters are available
      | error               | status |
      | satellite not found | 404    |

    Examples:
      | sateliteattribute |
      | 35544             |

