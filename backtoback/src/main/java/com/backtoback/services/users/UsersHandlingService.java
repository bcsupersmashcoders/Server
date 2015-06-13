package com.backtoback.services.users;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.users.UserEntity;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.ObjectifyService;

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
