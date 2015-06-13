package com.backtoback.services.notifications;

import javax.ws.rs.core.MediaType;

import com.backtoback.helpers.JerseyClient;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.WebResource;


public class NotificationsHandlingService {
	private final String notificationsPath = "notifications/{userId}";
	
	public void pushNotification(String userId){
		JsonObject body = new JsonObject();
		body.addProperty("message","hey! your event is on 3 days");
		body.addProperty("createDate", System.currentTimeMillis());
		body.addProperty("notificationType", "strava-miles");
		body.addProperty("notificationId", "b2b" + System.currentTimeMillis());
		
		String sourcePath = notificationsPath;
		sourcePath = sourcePath.replace("{userId}", userId);
		
		WebResource wr = JerseyClient.getNotificationService().path(sourcePath).queryParam("siteId", "1");
		wr.accept(MediaType.APPLICATION_JSON).post(body);
	}
	
}
