package it.loreii.pingme;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple rest controller able to publish a text on the MQTT queue as configured in application.properties
 * */
@RestController
public class GreetingController {

	@Autowired
	ClientCloud cc;
	
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="text") String text) throws MqttPersistenceException, MqttException {
    	//take the message and publish as is on the MQTT queue 
    	//TODO add some enforcement on content and format
    	cc.publish(text);
    	return "ok";
    }
}