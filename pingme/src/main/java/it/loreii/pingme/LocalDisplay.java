package it.loreii.pingme;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.loreii.ledstripe.LedStripe;

/**
 * If a local led matrix is found, is used to display the messages on the MQTT queue. 
 * */
@Component
public class LocalDisplay {
	private final Logger logger= LoggerFactory.getLogger(ClientCloud.class);

	@Autowired
	private ClientCloud cc;
	private LedStripe led;

	@Autowired
	public LocalDisplay() throws MqttException {
		try {
			led = LedStripe.getInstance();
			led.setBrightness(0);
		} catch (Exception e) {
			//if the led matrix is not available you finish here.
			logger.error(e.getMessage());
		}
		
	}
	
	@PostConstruct
	private void init() throws MqttException{
		if(led!=null) {
			cc.subscribe(new MqttCallback() {
				
				@Override
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
						led.scrollTextLeft(arg1.toString(),300);
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	

	
}
