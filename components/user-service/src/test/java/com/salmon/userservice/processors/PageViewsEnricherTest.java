// package com.salmon.userservice.processors;
//
// import org.junit.BeforeClass;
// import org.junit.ClassRule;
// import org.junit.Ignore;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.cloud.stream.messaging.Processor;
// import org.springframework.cloud.stream.test.binder.MessageCollector;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.test.rule.KafkaEmbedded;
// import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// @SpringBootTest
// @RunWith(SpringJUnit4ClassRunner.class)
// public class PageViewsEnricherTest
// {
// 	private static final Logger LOG = LoggerFactory.getLogger(PageViewsEnricherTest.class);
//
// 	@ClassRule
// 	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "UsersById", "PageViewEvents", "EnrichedPageViewsEve");
//
// 	@BeforeClass
// 	public static void setup() {
// 		LOG.info("{}", embeddedKafka.getBrokersAsString());
// 		System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
// 		System.setProperty("spring.kafka.producer.bootstrap-servers", embeddedKafka.getBrokersAsString());
// 		System.setProperty("spring.kafka.consumer.bootstrap-servers", embeddedKafka.getBrokersAsString());
// 		System.setProperty("spring.cloud.streams.binder.brokers", embeddedKafka.getBrokersAsString());
// 		System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", embeddedKafka.getZookeeperConnectionString());
// 	}
//
// 	@Autowired
// 	private MessageCollector messageCollector;
//
// 	@Autowired
// 	private Processor processor;
//
// 	@Test
// 	public void name() throws Exception
// 	{
// 		assertThat(messageCollector).isNotNull();
// 		assertThat(processor).isNotNull();
// 	}
// }