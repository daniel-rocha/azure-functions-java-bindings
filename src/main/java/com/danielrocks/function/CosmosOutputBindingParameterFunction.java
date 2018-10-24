package com.danielrocks.function;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class CosmosOutputBindingParameterFunction {
    /**
     * This function listens at endpoint "/api/CosmosOutputBindingParameterFunction". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/CosmosOutputBindingParameterFunction
     * 2. curl {your host}/api/CosmosOutputBindingParameterFunction?name=HTTP%20Query
     */
    @FunctionName("CosmosOutputBindingParameterFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", 
                         methods = {HttpMethod.GET, HttpMethod.POST}, 
                         authLevel = AuthorizationLevel.ANONYMOUS) 
            HttpRequestMessage<Optional<String>> request,
            @CosmosDBOutput(name = "database", 
                            databaseName = "ToDoList", 
                            collectionName = "Items", 
                            connectionStringSetting = "Cosmos_DB_Connection_String") 
            OutputBinding<String> outputItem,
            final ExecutionContext context) {
  
        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                          .body("Please pass a name on the query string or in the request body")
                          .build();
        } 
        else {
            String document = "{\"idcode\": \"123\", \"description\": \"" + name + "\"}";
            outputItem.setValue(document);
            return request.createResponseBuilder(HttpStatus.OK)
                          .body("Document created successfully.")
                          .build();
        }
    }
}
