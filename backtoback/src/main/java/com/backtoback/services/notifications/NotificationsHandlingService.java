package com.backtoback.services.notifications;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.backtoback.helpers.JerseyClient;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class NotificationsHandlingService {
	private final String notificationsPath = "notifications/{userId}";
	
	public String pushNotification(String userId, String name){
		MultivaluedMap body = new MultivaluedMapImpl();
		body.add("message", name);
		body.add("createDate", String.valueOf(System.currentTimeMillis()));
		body.add("notificationType", "strava-miles");
		body.add	("notificationId", "b2b" + System.currentTimeMillis());
		
		String sourcePath = notificationsPath;
		sourcePath = sourcePath.replace("{userId}", userId);
		
		WebResource wr = JerseyClient.getNotificationService().path(sourcePath).queryParam("siteId", "1");
		String result = wr.accept(MediaType.APPLICATION_JSON).post(String.class, body);
		return result;
	}
	
}
