package ro.tuc.ds2020.Utils;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.tuc.ds2020.controllers.RabbitMQController;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.services.ConsumptionService;
import ro.tuc.ds2020.services.DeviceService;
//import ro.tuc.ds2020.services.RabbitMQListener;

@Configuration
public class RabbitMQConfig {

    static final String queueName = "rabbitmq.queue";

    static final String routingKey = "rabbitmq.routingkey";

    private final DeviceService deviceService;
    private final ConsumptionService consumptionService;
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate template;

    public RabbitMQConfig(DeviceService deviceService, ConsumptionService consumptionService, DeviceRepository deviceRepository, SimpMessagingTemplate template) {
        this.deviceService = deviceService;
        this.consumptionService = consumptionService;
        this.deviceRepository = deviceRepository;
        this.template = template;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue());
        simpleMessageListenerContainer.setMessageListener(new RabbitMQController(deviceService, consumptionService, deviceRepository, template));
        return simpleMessageListenerContainer;

    }

    //create custom connection factory
	/*@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
		cachingConnectionFactory.setUsername(username);
		cachingConnectionFactory.setUsername(password);
		return cachingConnectionFactory;
	}*/

    //create MessageListenerContainer using custom connection factory
	/*@Bean
	MessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
		simpleMessageListenerContainer.setQueues(queue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQListner());
		return simpleMessageListenerContainer;

	}*/

}
