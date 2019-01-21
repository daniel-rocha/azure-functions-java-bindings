package com.danielrocks.function.event.trigger;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

/**
 * Azure Functions with Event Grid Trigger.
 * 
 * The following example shows a Java function that uses the EventGridTrigger
 * annotation to be notified of a certain event.
 *
 */

public class TriggerString {

  @FunctionName("eventGridMonitorString")
  public void logEvent(
    @EventGridTrigger(
      name = "event"
    ) 
    String content, 
    final ExecutionContext context) {
      // log 
      context.getLogger().info("Event content: " + content);      
  }
}
