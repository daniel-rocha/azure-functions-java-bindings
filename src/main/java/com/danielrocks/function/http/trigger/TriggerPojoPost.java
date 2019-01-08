package com.danielrocks.function.http.trigger;

import java.util.Optional;

import com.danielrocks.function.common.ToDoItem;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 * 
 * The following example shows a Java function that reads:
 * 
 * 1) The request body, for POST requests;
 * 
 * as a POJO and returns a JSON document with it. The Azure Functions runtime will try to deserialize
 * the JSON payload into an instance of ToDoItem automatically.
 * 
 */

public class TriggerPojoPost {

    /**
     * This function listens at endpoint "/api/TriggerPojoPost". Invoke it using "curl" command in bash:
     * 1. curl -d "{\"id\": \"123456\", \"description\": \"my description\"}" {your host}/api/TriggerStringPostPojo
     */

    @FunctionName("TriggerPojoPost")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", 
              methods = {HttpMethod.POST}, 
              authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<ToDoItem>> request,
            final ExecutionContext context) {
        
        // Item list
        context.getLogger().info("Request body is: " + request.getBody().orElse(null));

        // Check request body
        if (!request.getBody().isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                          .body("Document not found.")
                          .build();
        } 
        else {
            // return JSON from to the client
            // Generate document
            final ToDoItem body = request.getBody().get();
            return request.createResponseBuilder(HttpStatus.OK)
                          .header("Content-Type", "application/json")
                          .body(body)
                          .build();
        }
    }
}
