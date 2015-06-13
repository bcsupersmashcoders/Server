package com.backtoback.services.users;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.users.UserEntity;
import com.backtoback.pojos.ResultMessage;
import com.google.api.server.spi.config.Named;
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

public class UsersHandlingService {
	public ResultMessage createUser(@Named("username") String username, @Named("password") String password,
			@Named("bio") String bio, @Named("passions") List<String> passions) throws ConflictException {
		UserEntity user = null;
		Filter usernameFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
		Query query = new Query("UserEntity").setFilter(usernameFilter);
		List<Entity> entities = DatastoreServiceFactory.getDatastoreService().prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		System.out.println(entities.size());

		if (entities.size() == 0) {
			user = new UserEntity(username, password, bio, passions);
			ObjectifyService.ofy().save().entity(user).now();
		} else {
			throw new ConflictException("Username " + username + " already exists!");
		}

		return new ResultMessage("Success", Long.toString(user.getId()));
	}

	public ResultMessage validateUser(@Named("username") String username, @Named("password") String password)
			throws NotFoundException {
		Filter usernameFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
		Filter passwordFilter = new FilterPredicate("password", FilterOperator.EQUAL, password);
		Query query = new Query("UserEntity").setFilter(usernameFilter).setFilter(passwordFilter);

		List<Entity> entities = DatastoreServiceFactory.getDatastoreService().prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		System.out.println(entities.size());

		UserEntity user = null;
		if (entities.size() == 1) {
			user = (UserEntity) ObjectifyService.ofy().toPojo(entities.get(0));
		} else {
			throw new NotFoundException("No user/password combination exists with username " + username);
		}
		return new ResultMessage("Success", Long.toString(user.getId()));
	}

	public UserEntity getUser(@Named("username") String username) throws NotFoundException {
		Filter usernameFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
		Query query = new Query("UserEntity").setFilter(usernameFilter);
		List<Entity> entities = DatastoreServiceFactory.getDatastoreService().prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		System.out.println(entities.size());
		UserEntity user;
		if (entities.size() == 1) {
			user = (UserEntity) ObjectifyService.ofy().toPojo(entities.get(0));
			user.setPassword("");
		} else {
			throw new NotFoundException("No user exists with username " + username);
		}
		return user;
	}

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

	public UserEntity loginUser(String username, String password) {
		if (StringUtils.equals(username, "bcsupersmashcoders@gmail.com")
				&& StringUtils.equals(password, "superpassword")) {
			// retrieve user
			return null;
		}
		if (StringUtils.equals(username, "supersmashcoders@gmail.com") && StringUtils.equals(password, "ikeestoa")) {
			// retrieve user
			return null;
		}
		return null;
	}
}
