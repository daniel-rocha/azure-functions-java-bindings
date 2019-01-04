package com.danielrocks.function.blob.output;

import java.nio.charset.StandardCharsets;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BlobInput;
import com.microsoft.azure.functions.annotation.BlobOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;
import com.microsoft.azure.functions.annotation.StorageAccount;

/**
 * Azure Function with QueueTrigger, BlobInput, and BlobOutput.
 * 
 * The following example shows a Java function that uses the QueueTrigger
 * annotation to receive a message containing the name of a file
 * in a blob storage container. 
 * 
 * The BlobInput annotation then reads the file
 * and passes its contents to the function as a byte[].
 * 
 * The return value of the function is written to the  
 * file in the 'samples-workitems-outputs' storage container described in the BlobOutput annotation.
 *
 */

public class QueueTriggerBlobOutput {

  @FunctionName("copyBlobQueueTrigger")
  @StorageAccount("Storage_Account_Connection_String")
  @BlobOutput(
    name = "target", 
    path = "samples-workitems-outputs/{queueTrigger}-Copy")
  public String copyBlobQueue(
    @QueueTrigger(
      name = "filename", 
      dataType = "string",
      queueName = "myqueue-items") 
    String filename,
    @BlobInput(
      name = "file", 
      path = "samples-workitems/{queueTrigger}") 
    String content,
    final ExecutionContext context) {
      context.getLogger().info("The content of \"" + filename + "\" is: " + content);
      return content;
  }
}
