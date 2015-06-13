package com.backtoback.services.events;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.backtoback.backcountry.api.ProductAPI;
import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.products.ProductEntity;
import com.backtoback.entities.users.UserEntity;
import com.backtoback.helpers.JerseyClient;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.googlecode.objectify.ObjectifyService;
import com.sun.jersey.api.client.WebResource;

public class EventsHandlingService {
	
	private final String productsPath = "categories/{categoryId}/products";
	
	public EventEntity createEvent(EventEntity event) {
		event.setProducts(getProducts(event.getTag()));
		ObjectifyService.ofy().save().entity(event).now();
		return event;
	}

	public EventEntity getEvent(String id) throws NotFoundException {
		try {
			Key key = KeyFactory.createKey("EventEntity", Long.decode(id));
			Entity entity = DatastoreServiceFactory.getDatastoreService().get(key);
			EventEntity event = ObjectifyService.ofy().toPojo(entity);
			return event;
		} catch (EntityNotFoundException e) {
			throw new NotFoundException("No event exists with id " + id);
		}	
	}

	public List<EventEntity> getEvents() {
		Query query = new Query("EventEntity").addSort("startDate", Query.SortDirection.DESCENDING);
		List<Entity> entities = DatastoreServiceFactory.getDatastoreService().prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		System.out.println(entities.size());
		List<EventEntity> events = new ArrayList<EventEntity>();
		for (Entity entity : entities) {
			events.add((EventEntity) ObjectifyService.ofy().toPojo(entity));
		}
		return events;
	}

	public void attendEvent(EventEntity eventEntity, UserEntity userEntity) throws NotFoundException {
		eventEntity.addAttendant(userEntity);
		ObjectifyService.ofy().save().entity(eventEntity).now();
	}

	public void removeAttendance(EventEntity eventEntity, UserEntity userEntity) throws NotFoundException {
		eventEntity.removeAttendant(userEntity);
		ObjectifyService.ofy().save().entity(eventEntity).now();
	}
	
	public List<ProductEntity> getProducts(String categoryId){
		String sourcePath = productsPath;
		sourcePath = sourcePath.replace("{categoryId}", categoryId);
		List<ProductEntity> products = new ArrayList<ProductEntity>();
		WebResource wr = JerseyClient.getProductService().path(sourcePath)
															.queryParam("site", "bcs")
															.queryParam("outlet", "false")
															.queryParam("offset","0")
															.queryParam("limit", "10")
															.queryParam("preview", "false")
															.queryParam("debug", "false")
															.queryParam("sort", "reviewAverage desc");
		try{
			String responseString = wr.accept(MediaType.APPLICATION_JSON).get(String.class);
			JsonParser parser = new JsonParser();
			JsonObject response = (JsonObject) parser.parse(responseString);
			JsonArray productsArray = response.getAsJsonArray("products");
			for (int i = 0; i < productsArray.size() ; i ++){
				JsonObject product = productsArray.get(i).getAsJsonObject();
				String id = product.get("id").getAsString();
				String name = product.get("title").getAsString();
				String productURL = product.get("skus").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
				String photoURL = product.get("skus").getAsJsonArray().get(0).getAsJsonObject().get("image").getAsJsonObject().get("url").getAsString();
				products.add(new ProductEntity(id, name, productURL, photoURL));
			}
		}catch( Exception e){
			return ProductAPI.getProducts();
		}
		return products;
	}
	
}
