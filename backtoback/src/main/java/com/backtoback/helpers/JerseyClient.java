package com.backtoback.helpers;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class JerseyClient {
	private static ClientConfig config = null;
	// Hackathon API
	private static Client hackathonClient = null;
	private static WebResource hackathonService = null;
	// Product API
	private static Client productClient = null;
	private static WebResource productService = null;

	public static WebResource getHackathonService() {
		if (config == null) {
			config = new DefaultClientConfig();
			hackathonClient = Client.create(config);
			hackathonService = hackathonClient.resource(UriBuilder.fromUri("http://hackathon.backcountry.com:8081/hackathon/public/")
					.build());
		}
		return hackathonService;
	}
	
	public static WebResource getProductService() {
		if (config == null) {
			config = new DefaultClientConfig();
			productClient = Client.create(config);
			productService = productClient.resource(UriBuilder.fromUri("http://hackathon.backcountry.com:8081/hackathon/public/")
					.build());
		}
		return productService;
	}
}
