package com.danielrocks.function.cosmos.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.danielrocks.function.common.ToDoItem;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 * 
 * The following example shows a Java function that uses the CosmosDBOutput annotation
 * to return a list of ToDoItem POJOs that will be written to a CosmosDB collection.
 * 
 * The function returns the document to be saved via an OutputBinding parameter, which allows
 * the function to return an HTTP document to a calling client at the same time. 
 * 
 * This technique is used write data to multiple output streams at the same time, optimizing the
 * amount of code needed to distribute data to multiple targets.
 * 
 * The function is triggered by an HTTP request that uses query string data to set the 'description'
 * field of the saved document.
 */

public class WriteMultipleDocsOutputBinding {
    /**
     * This function listens at endpoint "/api/WriteMultipleDocsOutputBinding". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/WriteMultipleDocsOutputBinding&desc={description}
     * 2. curl "{your host}/api/WriteOneDoc?desc={description}"
     */
    @FunctionName("WriteMultipleDocsOutputBinding")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", 
              methods = {HttpMethod.GET, HttpMethod.POST}, 
              authLevel = AuthorizationLevel.ANONYMOUS) 
            HttpRequestMessage<Optional<String>> request,
            @CosmosDBOutput(name = "database", 
              databaseName = "ToDoList", 
              collectionName = "Items", 
              connectionStringSetting = "Cosmos_DB_Connection_String") 
            OutputBinding<List<ToDoItem>> outputItem,
            final ExecutionContext context) {
  
        // Parse query parameter
        String query = request.getQueryParameters().get("desc");
        String name = request.getBody().orElse(query);

        // Item list
        context.getLogger().info("Parameters are: " + request.getQueryParameters());
      
        // Generate documents
        List<ToDoItem> items = new ArrayList<>();

        for (int i = 0; i < 5; i ++) {
          // Generate random ID
          final int id = Math.abs(new Random().nextInt());

          // Create ToDoItem
          ToDoItem item = new ToDoItem(String.valueOf(id), name);
          
          items.add(item);
        }

        // Set outputItem's value to the list of POJOs to be saved
        outputItem.setValue(items);
        context.getLogger().info("Document to be saved: " + items);

        // return a different document to the browser or calling client.
        return request.createResponseBuilder(HttpStatus.OK)
                      .body("Documents created successfully.")
                      .build();
    }
}
