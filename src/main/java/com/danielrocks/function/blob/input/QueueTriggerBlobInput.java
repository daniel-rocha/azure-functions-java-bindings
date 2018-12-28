package com.danielrocks.function.blob.input;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BlobInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;
import com.microsoft.azure.functions.annotation.StorageAccount;

 /**
 * Azure Function with QueueTrigger and BlobInput.
 * 
 * The following example shows a Java function that uses the QueueTrigger
 * annotation to receive a message containing the name of a file
 * in a blob storage container. The BlobInput annotation then reads the file
 * and passes its contents to the function as a byte[].
 *
 */

public class QueueTriggerBlobInput {

  @FunctionName("getBlobSize")
  @StorageAccount("Storage_Account_Connection_String")
  public void blobSize(
    @QueueTrigger(
      name = "filename", 
      queueName = "myqueue-items-sample") 
    String filename,
    @BlobInput(
      name = "file", 
      dataType = "binary", 
      path = "samples-workitems/{queueTrigger}") 
    byte[] content,
    final ExecutionContext context) {
      context.getLogger().info("The size of \"" + filename + "\" is: " + content.length + " bytes");
  }
}
