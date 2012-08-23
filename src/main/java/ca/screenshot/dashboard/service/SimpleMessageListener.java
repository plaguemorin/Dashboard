package ca.screenshot.dashboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * User: plaguemorin
 * Date: 14/08/12
 * Time: 4:57 PM
 */
@Component
public class SimpleMessageListener implements MessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessageListener.class);

	@Override
	public void onMessage(final Message message) {
		LOGGER.info("Message = " + message.toString());
	}
}
