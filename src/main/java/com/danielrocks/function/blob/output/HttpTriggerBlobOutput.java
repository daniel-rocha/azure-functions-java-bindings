package com.danielrocks.function.blob.output;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BlobInput;
import com.microsoft.azure.functions.annotation.BlobOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.StorageAccount;

/**
 * Azure Function with HttpTrigger, BlobInput, and BlobOutput.
 * 
 * The following example shows a Java function that uses the HttpTrigger
 * annotation to receive a parameter containing the name of a file
 * in a blob storage container. 
 * 
 * The BlobInput annotation then reads the file
 * and passes its contents to the function as a byte[].
 * 
 * The BlobOutput annotation binds to OutputBinding, which is then used
 * by the function to write the contents of the input file to a 
 * file in the 'samples-workitems-outputs' storage container.
 *
 */

public class HttpTriggerBlobOutput {

  @FunctionName("copyBlobHttp")
  @StorageAccount("Storage_Account_Connection_String")
  public HttpResponseMessage copyBlobHttp(
    @HttpTrigger(name = "req", 
      methods = {HttpMethod.GET}, 
      authLevel = AuthorizationLevel.ANONYMOUS) 
    HttpRequestMessage<Optional<String>> request,
    @BlobInput(
      name = "file", 
      dataType = "binary", 
      path = "samples-workitems/{Query.file}") 
    byte[] content,
    @BlobOutput(
      name = "target", 
      path = "samples-workitems-outputs/{Query.file}-CopyViaHttp")
    OutputBinding<String> outputItem,
    final ExecutionContext context) {
      // Save blob to outputItem
      outputItem.setValue(new String(content, StandardCharsets.UTF_8));

      //create response body with size of requested blob
      String fileName = request.getQueryParameters().get("file");
      String body = "The size of \"" + fileName + "\" is: " + content.length + " bytes. \n";
      
      // do HTTP response 
      return request.createResponseBuilder(HttpStatus.OK)
        .body(body)
        .build();
  }
}
