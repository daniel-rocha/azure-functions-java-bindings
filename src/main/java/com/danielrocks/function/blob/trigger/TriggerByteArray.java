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

public class TriggerByteArray {

    @FunctionName("blobprocessor")
    public void run(
      @BlobTrigger(name = "file",
                   dataType = "binary",
                   path = "myblob/{name}",
                   connection = "Storage_Account_Connection_String") byte[] content,
      @BindingName("name") String filename,
      final ExecutionContext context) {
        // log 
        context.getLogger().info("Name: " + filename + " Size: " + content.length + " bytes");

        // do something with blob
        String file = new String(content, StandardCharsets.UTF_8);
        context.getLogger().info("File content is: " + file);
    }
}
