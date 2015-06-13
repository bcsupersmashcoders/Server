package com.backtoback.services.users;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.backtoback.backcountry.pojos.UsersPojo;
import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.users.UserEntity;
import com.backtoback.helpers.JerseyClient;
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

	private UserEntity getUser(String username) throws ConflictException {
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

	public List<EventEntity> getEventsAttending(String id) throws NotFoundException {
		UserEntity user = getUserById(id);
		return user.getEventsAttendants();
	}

	public List<EventEntity> getEventsCreated(String id) throws NotFoundException {
		UserEntity user = getUserById(id);
		return user.getEventsCreated();
	}

	public UserEntity loginUser(String username, String password) throws ConflictException {
		if (StringUtils.equals(username, "bcsupersmashcoders@gmail.com")
				&& StringUtils.equals(password, "superpassword")) {
			return retrieveAndPersistUser("2029770186", username);
		}
		if (StringUtils.equals(username, "supersmashcoders@gmail.com") && StringUtils.equals(password, "ikeestoa")) {
			// retrieve user
			return null;
		}
		return null;
	}

	private UserEntity retrieveAndPersistUser(String userId, String username) throws ConflictException {
		WebResource wr = JerseyClient.getCommunityService().path("users/" + userId).queryParam("siteId", "1")
				.queryParam("hasIcEmail", "false").queryParam("callback", "null");

		String userString = wr.accept(MediaType.APPLICATION_JSON).get(String.class);
		ObjectMapper mapper = new ObjectMapper();
		UsersPojo userPojo = null;
		try {
			userPojo = mapper.readValue(userString, UsersPojo.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		UserEntity userEntity = getUser(username);
		if (userEntity == null) {
			userEntity = new UserEntity(username, userPojo);
			ObjectifyService.ofy().save().entity(userEntity).now();
		}
		return userEntity;
	}
}
