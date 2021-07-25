package com.example.iss.util;


import com.example.iss.filter.RestAssuredLogFilter;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;


@Component

public class Util {




    private static final String SERVICE_URL_HOST_FORMAT = "https://api.wheretheiss.at/v1";
    public static final String SATELITES = "satellites/";


    public String getServiceUrlWithPath(String path) {
        if(path.contains("coordinates"))
        {
            return format("%s", getServiceUrlHost()+ path);

        }
        else
        {
        return format("%s/%s", getServiceUrlHost(),SATELITES+ path);
    }
    }

   public String getServiceUrlHost() {
        return format(SERVICE_URL_HOST_FORMAT);
    }

    public static Response get(String url) {
        return given()
                .filters(new RestAssuredLogFilter())
                .when()
                .get(url)
                .thenReturn();
    }

}
