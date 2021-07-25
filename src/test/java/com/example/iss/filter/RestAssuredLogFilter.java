package com.example.iss.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.internal.NoParameterValue;
import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Slf4j
public class RestAssuredLogFilter implements Filter {

    private static final String TAB = "\t";
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String EQUALS = "=";
    private static final String NONE = "<none>";
    private static final String HEADER_NAME_AND_VALUE_SEPARATOR = ": ";

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
            String requestId = UUID.randomUUID().toString();
            logRequest(requestSpec, requestId);
            logResponse(response, requestId, true);

        return response;
    }

    private void logRequest(FilterableRequestSpecification requestSpec, String requestId) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("## Request details: [%s] ##", requestId)).append(NEW_LINE);
        addSingle(builder, "Request method:", requestSpec.getMethod());
        addSingle(builder, "Request path:", requestSpec.getURI());
        addMapDetails(builder, "Request params:", requestSpec.getRequestParams());
        addMapDetails(builder, "Query params:", requestSpec.getQueryParams());
        addMapDetails(builder, "Path params:", requestSpec.getNamedPathParams());

        addHeaders(requestSpec, builder);
        addBody(requestSpec, builder, true);
        String logString = builder.toString();

        log.info(logString);
    }

    private void logResponse(Response response, String requestId,
                             boolean shouldPrettyPrint) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("## Response details: [%s] ##", requestId)).append(NEW_LINE);
        builder.append(response.getStatusLine());
        final Headers headers = response.headers();
        if (headers.exist()) {
            builder.append("\n").append(toString(headers));
        }
        final Cookies cookies = response.getDetailedCookies();
        if (cookies.exist()) {
            builder.append("\n").append(cookies.toString());
        }
        String responseBodyToAppend;
        if (shouldPrettyPrint) {
            responseBodyToAppend = new Prettifier()
                    .getPrettifiedBodyIfPossible(response, response.body());
        } else {
            responseBodyToAppend = response.body().asString();
        }
        builder.append(responseBodyToAppend).append(NEW_LINE);
        String responseStr = builder.toString();
        log.info(responseStr);
    }

    private static void addBody(FilterableRequestSpecification requestSpec, StringBuilder builder,
                                boolean shouldPrettyPrint) {
        builder.append("Body:");
        if (requestSpec.getBody() != null) {
            final String body;
            if (shouldPrettyPrint) {
                body = new Prettifier().getPrettifiedBodyIfPossible(requestSpec);
            } else {
                body = requestSpec.getBody();
            }
            builder.append(NEW_LINE).append(body);
        } else {
            appendTab(appendTwoTabs(builder)).append(NONE);
        }
    }

    private static void addHeaders(FilterableRequestSpecification requestSpec,
                                   StringBuilder builder) {
        builder.append("Headers:");
        final Headers headers = requestSpec.getHeaders();
        if (!headers.exist()) {
            appendTwoTabs(builder).append(NONE).append(NEW_LINE);
        } else {
            int i = 0;
            for (Header header : headers) {
                if (i++ == 0) {
                    appendTwoTabs(builder);
                } else {
                    appendFourTabs(builder);
                }
                builder.append(header).append(NEW_LINE);
            }
        }
    }

    private static void addSingle(StringBuilder builder, String str, String requestPath) {
        appendTab(builder.append(str)).append(requestPath).append(NEW_LINE);
    }

    private static void addMapDetails(StringBuilder builder, String title, Map<String, ?> map) {
        appendTab(builder.append(title));
        if (map.isEmpty()) {
            builder.append(NONE).append(NEW_LINE);
        } else {
            int i = 0;
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                if (i++ != 0) {
                    appendFourTabs(builder);
                }
                final Object value = entry.getValue();
                builder.append(entry.getKey());
                if (!(value instanceof NoParameterValue)) {
                    builder.append(EQUALS).append(value);
                }
                builder.append(NEW_LINE);
            }
        }
    }

    private static StringBuilder appendFourTabs(StringBuilder builder) {
        appendTwoTabs(appendTwoTabs(builder));
        return builder;
    }

    private static StringBuilder appendTwoTabs(StringBuilder builder) {
        appendTab(appendTab(builder));
        return builder;
    }

    private static StringBuilder appendTab(StringBuilder builder) {
        return builder.append(TAB);
    }

    private static String toString(Headers headers) {
        if (!headers.exist()) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();
        for (Header header : headers) {
            builder.append(header.getName()).append(HEADER_NAME_AND_VALUE_SEPARATOR)
                    .append(header.getValue()).append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}