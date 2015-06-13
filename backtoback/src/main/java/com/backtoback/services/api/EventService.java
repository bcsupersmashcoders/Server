package com.backtoback.services.api;

import java.io.IOException;
import java.util.List;

import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.images.ImageEntity;
import com.backtoback.entities.products.ProductEntity;
import com.backtoback.entities.users.UserEntity;
import com.backtoback.pojos.LoginResource;
import com.backtoback.pojos.URLResource;
import com.backtoback.services.events.EventsHandlingService;
import com.backtoback.services.images.ImageHandlingService;
import com.backtoback.services.notifications.NotificationsHandlingService;
import com.backtoback.services.users.UsersHandlingService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

/** An endpoint class we are exposing */
@Api(name = "backtoback", version = "v1", title = "Back To Back API", namespace = @ApiNamespace(ownerDomain = "sschackathon.appspot.com", ownerName = "sschackathon", packagePath = ""))
public class EventService {

	private ImageHandlingService imageService = new ImageHandlingService();
	private EventsHandlingService eventService = new EventsHandlingService();
	private UsersHandlingService userService = new UsersHandlingService();
	private NotificationsHandlingService notificationService = new NotificationsHandlingService();

	@ApiMethod(name = "createEvent", httpMethod = HttpMethod.POST, path = "event")
	public EventEntity createEvent(EventEntity event) {
		return eventService.createEvent(event);
	}

	@ApiMethod(name = "getEvent", httpMethod = HttpMethod.GET, path = "event/{id}")
	public EventEntity getEvent(@Named("id") String id) throws NotFoundException {
		return eventService.getEvent(id);
	}

	@ApiMethod(name = "getEvents", httpMethod = HttpMethod.GET, path = "events")
	public List<EventEntity> getEvents() throws NotFoundException {
		return eventService.getEvents();
	}

	@ApiMethod(name = "getEventsCreated", httpMethod = HttpMethod.GET, path = "users/events/created")
	public List<EventEntity> getEventsCreated(@Named("username") String username) throws NotFoundException,
			ConflictException {
		return userService.getEventsCreated(username);
	}

	@ApiMethod(name = "getEventsAttending", httpMethod = HttpMethod.GET, path = "users/events/attended")
	public List<EventEntity> getEventsAttending(@Named("username") String username) throws NotFoundException,
			ConflictException {
		return userService.getEventsAttending(username);
	}

	@ApiMethod(name = "getUrl", path = "images/url", httpMethod = HttpMethod.GET)
	public URLResource getURL(@Named("eventId") String eventId) {
		return imageService.getImageURL(eventId);
	}

	@ApiMethod(name = "getImages", path = "images/", httpMethod = HttpMethod.GET)
	public List<ImageEntity> getImages(@Named("eventId") String eventId) {
		return imageService.getImagesFromEvent(eventId);
	}

	@ApiMethod(name = "attendEvent", path = "events/attendants", httpMethod = HttpMethod.PUT)
	public void attendEvent(@Named("eventId") String eventId, @Named("username") String username)
			throws ConflictException, NotFoundException {
		EventEntity event = getEvent(eventId);
		UserEntity user = userService.getUser(username);
		if (user == null) {
			throw new NotFoundException("No user found with username " + username);
		}
		eventService.attendEvent(event, user);
		userService.attendEvent(event, user);
	}

	@ApiMethod(name = "removeAttendance", path = "events/attendants", httpMethod = HttpMethod.DELETE)
	public void removeAttendance(@Named("eventId") String eventId, @Named("username") String username)
			throws ConflictException, NotFoundException {
		EventEntity event = getEvent(eventId);
		UserEntity user = userService.getUser(username);
		if (user == null) {
			throw new NotFoundException("No user found with username " + username);
		}
		eventService.removeAttendance(event, user);
		userService.removeAttendance(event, user);
	}

	@ApiMethod(name = "loginUser", path = "users/login", httpMethod = HttpMethod.POST)
	public UserEntity loginUser(LoginResource loginResource) throws NotFoundException, ConflictException,
			JsonParseException, JsonMappingException, IOException {
		return userService.loginUser(loginResource);
	}

	@ApiMethod(name = "getProducts", path = "event/products/{categoryId}", httpMethod = HttpMethod.GET)
	public List<ProductEntity> getProducts(@Named("categoryId") String categoryId) throws NotFoundException,
			ConflictException {
		return eventService.getProducts(categoryId);
	}
	
	@ApiMethod(name = "pushNotification", path = "notification/{userId}", httpMethod = HttpMethod.POST)
	public void pushNotification(@Named("userId") String userId) throws NotFoundException,
			ConflictException {
		notificationService.pushNotification(userId);
	}
}
