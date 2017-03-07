package it.loreii.pingme;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientCloud {

	private final Logger logger= LoggerFactory.getLogger(ClientCloud.class);
	
	@Value("${topic}")
	private String topic;
	@Value("${qos}")
	private int qos;
	@Value("${broker}")
	private String broker;
	@Value("${clientId}")
	private String clientId;
	@Value("${mqttUsername}")
	private String username;
	@Value("${mqttPassword}")
	private String password;

	final MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient mqttClient;
	private MqttConnectOptions connOpts;
	
	@PostConstruct
	public void init() throws MqttException {
		mqttClient = new MqttClient(broker, clientId, persistence);
		
		connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());

		logger.info("Connecting to broker: " + broker);
		mqttClient.connect(connOpts);
		logger.info("Connected");

	}
	
	public void publish(String text) throws MqttPersistenceException, MqttException{
		final MqttMessage message = new MqttMessage(text.getBytes());
		message.setQos(qos);
		if(!mqttClient.isConnected())
			mqttClient.connect();
		mqttClient.publish(topic, message);
		logger.info("Message published");
	}

	public void subscribe(MqttCallback callback) throws MqttException {
		try {
			mqttClient.subscribe(topic);
			mqttClient.setCallback(callback);
		} catch (final MqttException me) {
			logger.info("reason " + me.getReasonCode());
			logger.info("msg " + me.getMessage());
			logger.info("loc " + me.getLocalizedMessage());
			logger.info("cause " + me.getCause());
			logger.info("excep " + me);
			throw me;
		}

	}

}
