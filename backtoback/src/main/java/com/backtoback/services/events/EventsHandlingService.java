package com.backtoback.services.events;

import java.util.ArrayList;
import java.util.List;

import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.products.ProductEntity;
import com.backtoback.entities.users.UserEntity;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.ObjectifyService;

public class EventsHandlingService {
	
	private final String productsPath = "categories/bcsCat15000003/products?site=bcs&outlet=false&offset=0&limit=10&preview=false&debug=false&sort=reviewAverage%20desc";
	
	public EventEntity createEvent(EventEntity event) {
		com.googlecode.objectify.Key<EventEntity> key = ObjectifyService.ofy().save().entity(event).now();
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
	
	private List<ProductEntity> getProducts(String categoryId){
		String sourcePath = productsPath;
		sourcePath = sourcePath.replace("{categoryId}", categoryId);
		return null;
	}
	
}
