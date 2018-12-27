package com.danielrocks.function.blob.trigger;

import com.danielrocks.function.common.ToDoItem;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

/**
 * Azure Functions with Blob Storage Trigger.
 * 
 * The following example shows a Java function that uses the BlobTrigger
 * annotation to be notified of changes to a certain storage container.
 * 
 * https://github.com/Azure/azure-functions-java-worker/issues/256 
 * 
 */

public class TriggerPojo {
    /**
     * This function listens for changes in the Items collection and gets passed the documents
     * that have changed (added or updated)
     */
    @FunctionName("blobprocessorPojo")
    public void run(
      @BlobTrigger(name = "file",
                   dataType = "",
                   path = "myblob/{name}",
                   connection = "Storage_Account_Connection_String") ToDoItem content,
      @BindingName("name") String filename,
      final ExecutionContext context) {
        // log 
        context.getLogger().info("Name: " + filename);

        // do something with blob
        context.getLogger().info("ToDoItem is: " + content.getId() + " - " + content.getDescription());
    }
}
