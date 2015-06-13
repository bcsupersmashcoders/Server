package com.backtoback.helpers;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class JerseyClient {
	private static ClientConfig config = null;
	private static Client client = null;
	private static WebResource service = null;

	public static WebResource getService() {
		if (config == null) {
			config = new DefaultClientConfig();
			client = Client.create(config);
			service = client.resource(UriBuilder.fromUri("http://hackathon.backcountry.com:8081/hackathon/public/")
					.build());
		}
		return service;
	}
}
