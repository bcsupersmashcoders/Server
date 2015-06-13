package com.backtoback.services.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.backtoback.backcountry.pojos.UsersPojo;
import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.users.UserEntity;
import com.backtoback.helpers.JerseyClient;
import com.backtoback.pojos.LoginResource;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.googlecode.objectify.ObjectifyService;
import com.sun.jersey.api.client.WebResource;

public class UsersHandlingService {

	public UserEntity getUserById(String id) throws NotFoundException {
		Key key = KeyFactory.createKey("UserEntity", Long.decode(id));
		Entity entity = null;
		try {
			entity = DatastoreServiceFactory.getDatastoreService().get(key);
			UserEntity user = ObjectifyService.ofy().toPojo(entity);
			return user;
		} catch (EntityNotFoundException e) {
			throw new NotFoundException("No users exists with id " + id);
		}
	}

	public UserEntity getUser(String username) throws ConflictException {
		Filter usernameFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
		Query query = new Query("UserEntity").setFilter(usernameFilter);
		List<Entity> entities = DatastoreServiceFactory.getDatastoreService().prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		UserEntity user = null;
		if (entities.size() == 1) {
			user = (UserEntity) ObjectifyService.ofy().toPojo(entities.get(0));
		} else if (entities.size() > 1) {
			throw new ConflictException("More than 1 user exists with username " + username);
		}
		return user;
	}

	public void attendEvent(EventEntity eventEntity, UserEntity userEntity) throws NotFoundException {
		userEntity.attendEvent(eventEntity);
		ObjectifyService.ofy().save().entity(userEntity).now();
	}

	public void removeAttendance(EventEntity eventEntity, UserEntity userEntity) throws NotFoundException {
		userEntity.removeAttendant(eventEntity);
		ObjectifyService.ofy().save().entity(userEntity).now();
	}

	public List<EventEntity> getEventsAttending(String username) throws NotFoundException, ConflictException {
		UserEntity user = getUser(username);
		List<Long> eventsIds = user.getEventsAttendants();
		return idsToEntity(eventsIds);
	}

	public List<EventEntity> getEventsCreated(String username) throws NotFoundException, ConflictException {
		UserEntity user = getUser(username);
		List<Long> eventsIds = user.getEventsCreated();
		return idsToEntity(eventsIds);
	}

	private List<EventEntity> idsToEntity(List<Long> eventIds) throws NotFoundException {
		List<EventEntity> events = new ArrayList<EventEntity>();
		if (eventIds.size() > 0) {
			for (Long entityId : eventIds) {

				Key key = KeyFactory.createKey("EventEntity", entityId);
				Entity entity = null;
				try {
					entity = DatastoreServiceFactory.getDatastoreService().get(key);
					EventEntity event = ObjectifyService.ofy().toPojo(entity);
					events.add(event);
				} catch (EntityNotFoundException e) {
					throw new NotFoundException("No event exists with id " + entityId);
				}
			}
		}
		return events;
	}

	public UserEntity loginUser(LoginResource loginResource) throws ConflictException, JsonParseException,
			JsonMappingException, IOException, NotFoundException {
		if (StringUtils.equals(loginResource.getUsername(), "bcsupersmashcoders@gmail.com")
				&& StringUtils.equals(loginResource.getPassword(), "12345678")) {
			return retrieveAndPersistUser("2381013009", loginResource.getUsername());
		}
		if (StringUtils.equals(loginResource.getUsername(), "supersmashcoders@gmail.com")
				&& StringUtils.equals(loginResource.getPassword(), "12345678")) {
			return retrieveAndPersistUser("2381005179", loginResource.getUsername());
		}
		return null;
	}

	private UserEntity retrieveAndPersistUser(String userId, String username) throws ConflictException,
			JsonParseException, JsonMappingException, IOException, NotFoundException {
		UsersPojo userPojo = null;
		try {
			WebResource wr = JerseyClient.getCommunityService().path("users/" + userId).queryParam("siteId", "1")
					.queryParam("hasIcEmail", "false");

			String userString = wr.accept(MediaType.APPLICATION_JSON).get(String.class);
			ObjectMapper mapper = new ObjectMapper();
			userPojo = mapper.readValue(userString, UsersPojo.class);
		} catch (Exception e) {
			userPojo = new UsersPojo();
			userPojo.setUserId(userId);
			userPojo.setBio("Esta es mi bio");
		}
		UserEntity userEntity = getUser(username);
		if (userEntity == null) {
			userEntity = new UserEntity(username, userPojo);
		} else {
			userEntity.merge(userPojo);
		}
		ObjectifyService.ofy().save().entity(userEntity).now();

		return userEntity;
	}
}
