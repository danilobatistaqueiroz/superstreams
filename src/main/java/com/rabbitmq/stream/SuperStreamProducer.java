package com.rabbitmq.stream;

public class SuperStreamProducer {
  public static void main(String[] args) throws InterruptedException {
    Address entryPoint = new Address("localhost", 5550);
    try (Environment environment = Environment.builder().host(entryPoint.host()).port(entryPoint.port())
              .username("rabbit_admin").password(".123-321.").addressResolver(address -> entryPoint)
              .maxConsumersByConnection(1).maxTrackingConsumersByConnection(1).build()) {

      String superStream = "invoices";

      System.out.println("Starting producer");

      Producer producer = environment.producerBuilder().superStream(superStream).routing(msg -> msg.getProperties().getMessageIdAsString()).producerBuilder().build();

      long idSequence = 0;
      while (true) {
        producer.send(producer.messageBuilder().properties().messageId(idSequence++).messageBuilder().addData("minha info".getBytes()).build(), confirmationStatus -> {});
        Thread.sleep(1000);
      }
    }
  }
}
