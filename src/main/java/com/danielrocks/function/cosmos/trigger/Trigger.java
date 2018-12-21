package com.danielrocks.function.cosmos.trigger;

import com.danielrocks.function.common.ToDoItem;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.CosmosDBTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

/**
 * Azure Functions with CosmosDB Trigger.
 * 
 * The following example shows a Java function that uses the CosmosDBTrigger
 * annotation to be notified of changes to a certain collection.
 *
 */

public class Trigger {
    /**
     * This function listens for changes in the Items collection and gets passed the documents
     * that have changed (added or updated)
     */
    @FunctionName("cosmosDBMonitor")
    public void cosmosDbProcessor(
      @CosmosDBTrigger(name = "items",
        databaseName = "ToDoList",
        collectionName = "Items",
        leaseCollectionName = "leases",
        createLeaseCollectionIfNotExists = true,
        connectionStringSetting = "Cosmos_DB_Connection_String") 
      ToDoItem[] items,
      final ExecutionContext context) {
        context.getLogger().info(items.length + " item(s) is/are changed.");

        for(ToDoItem item: items) {
          context.getLogger().info(item.toString());
        }
    }
}
