package com.danielrocks.function.blob.trigger;

import java.nio.charset.StandardCharsets;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;

/**
 * Azure Functions with Blob Storage Trigger.
 * 
 * The following example shows a Java function that uses the BlobTrigger
 * annotation to be notified of changes to a certain storage container.
 *
 */

public class TriggerString {
    /**
     * This function listens for changes in the Items collection and gets passed the documents
     * that have changed (added or updated)
     */
    @FunctionName("blobprocessorString")
    public void run(
      @BlobTrigger(name = "file",
                   dataType = "string",
                   path = "myblob/{name}",
                   connection = "Storage_Account_Connection_String") String content,
      @BindingName("name") String filename,
      final ExecutionContext context) {
        // log 
        context.getLogger().info("Name: " + filename + " Content: " + content);
    }
}
