package com.danielrocks.function;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class CosmosInputBindingFunction {
    /**
     * This function listens at endpoint "/api/CosmosInputBindingFunction". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/CosmosInputBindingFunction
     * 2. curl {your host}/api/CosmosInputBindingFunction?name=HTTP%20Query
     */
    @FunctionName("CosmosInputBindingFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", 
                         methods = {HttpMethod.GET, HttpMethod.POST}, 
                         authLevel = AuthorizationLevel.ANONYMOUS) 
            HttpRequestMessage<Optional<String>> request,
            @CosmosDBInput(name = "database",
                      databaseName = "ToDoList",
                      collectionName = "Items",
                      id = "{Query.id}",
                      connectionStringSetting = "Cosmos_DB_Connection_String") 
            Optional<String> item,
            final ExecutionContext context) {
        
        // Item list
        context.getLogger().info("Item from the database is " + item);

        String documentCollection = item.orElse(null);

        // Convert and display
        if (documentCollection == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                          .body("Document not found.")
                          .build();
        } 
        else {
            return request.createResponseBuilder(HttpStatus.OK)
                          .body(documentCollection)
                          .build();
        }
    }
}
