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
	// Community API
	private static Client communityClient = null;
	private static WebResource communityService = null;

	public static WebResource getHackathonService() {
		if (hackathonService == null) {
			config = new DefaultClientConfig();
			hackathonClient = Client.create(config);
			hackathonService = hackathonClient.resource(UriBuilder.fromUri(
					"http://hackathon.backcountry.com:8081/hackathon/public/").build());
		}
		return hackathonService;
	}

	public static WebResource getProductService() {
		if (productService == null) {
			config = new DefaultClientConfig();
			productClient = Client.create(config);
			productService = productClient.resource(UriBuilder.fromUri("http://productapipqa-vip.bcinfra.net:9000/v1/")
					.build());
		}
		return productService;
	}

	public static WebResource getCommunityService() {
		if (communityService == null) {
			config = new DefaultClientConfig();
			communityClient = Client.create(config);
			communityService = communityClient.resource(UriBuilder.fromUri(
					"http://communityapipqa-vip.bcinfra.net:9001/v1/").build());
		}
		return communityService;
	}

}
