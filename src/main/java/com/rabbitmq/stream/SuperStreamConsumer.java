package com.rabbitmq.stream;

import java.util.Scanner;

public class SuperStreamConsumer {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Specify the instance name!");
      System.exit(1);
    }
    String instanceName = args[0];
    Address entryPoint = new Address("localhost", 5550);
    try (Environment environment = Environment.builder().host(entryPoint.host()).port(entryPoint.port())
            .username("rabbit_admin").password(".123-321.").addressResolver(address -> entryPoint).build()) {
    //try (Environment environment = Environment.builder().host("localhost").port(5550).username("rabbit_admin").password(".123-321.")
    //        .maxConsumersByConnection(1).maxTrackingConsumersByConnection(1).build()) {

      // to exit properly when Ctrl-C-ed
      Runtime.getRuntime().addShutdownHook(new Thread(() -> environment.close()));

      String superStream = "invoices";
      String reference = "my-app";

      System.out.println("Starting consumer " + instanceName);
      environment.consumerBuilder().superStream(superStream).name(reference).singleActiveConsumer()
          .autoTrackingStrategy().messageCountBeforeStorage(2500).builder()
          .messageHandler((context, message) -> {
            System.out.println(
                "Consumer " + instanceName + " received message " + message.getBody() + message.getProperties().getMessageId() + " from stream " + context.stream() + ".");
          }).build();
      Scanner keyboard = new Scanner(System.in);
      keyboard.nextLine();
    }
  }
}
