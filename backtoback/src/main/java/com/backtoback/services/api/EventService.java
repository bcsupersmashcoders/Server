package com.backtoback.services.api;

import java.util.List;

import com.backtoback.entities.events.EventEntity;
import com.backtoback.entities.images.ImageEntity;
import com.backtoback.entities.users.UserEntity;
import com.backtoback.pojos.ResultMessage;
import com.backtoback.pojos.URLResource;
import com.backtoback.services.events.EventsHandlingService;
import com.backtoback.services.images.ImageHandlingService;
import com.backtoback.services.users.UsersHandlingService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;

/** An endpoint class we are exposing */
@Api(name = "backtoback", version = "v1", title = "Back To Back API", namespace = @ApiNamespace(ownerDomain = "sschackathon.appspot.com", ownerName = "sschackathon", packagePath = ""))
public class EventService {

	private ImageHandlingService imageService = new ImageHandlingService();
	private EventsHandlingService eventService = new EventsHandlingService();
	private UsersHandlingService userService = new UsersHandlingService();

	@ApiMethod(name = "create", httpMethod = HttpMethod.POST, path = "event")
	public ResultMessage create(EventEntity event) {
		Long id = eventService.createEvent(event);
		return new ResultMessage("Success", Long.toString(id));
	}

	@ApiMethod(name = "getEvent", httpMethod = HttpMethod.GET, path = "event/{id}")
	public EventEntity getEvent(@Named("id") String id) throws NotFoundException {
		return eventService.getEvent(id);
	}

	@ApiMethod(name = "getEvents", httpMethod = HttpMethod.GET, path = "events")
	public List<EventEntity> getEvents() throws NotFoundException {
		return eventService.getEvents();
	}

	@ApiMethod(name = "getEventsCreated", httpMethod = HttpMethod.GET, path = "users/{id}/events/created")
	public List<EventEntity> getEventsCreated(@Named("id") String id) throws NotFoundException {
		return userService.getEventsCreated(id);
	}

	@ApiMethod(name = "getEventsAttending", httpMethod = HttpMethod.GET, path = "users/{id}/events/attended")
	public List<EventEntity> getEventsAttending(@Named("id") String id) throws NotFoundException {
		return userService.getEventsAttending(id);
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
	public void attendEvent(@Named("eventId") String eventId, @Named("userId") String userId) throws NotFoundException {
		EventEntity event = getEvent(eventId);
		UserEntity user = userService.getUserById(userId);
		eventService.attendEvent(event, user);
		userService.attendEvent(event, user);
	}

	@ApiMethod(name = "removeAttendance", path = "events/attendants", httpMethod = HttpMethod.DELETE)
	public void removeAttendance(@Named("eventId") String eventId, @Named("userId") String userId)
			throws NotFoundException {
		EventEntity event = getEvent(eventId);
		UserEntity user = userService.getUserById(userId);
		eventService.removeAttendance(event, user);
		userService.removeAttendance(event, user);
	}

	@ApiMethod(name = "getUser", path = "users/getUser", httpMethod = HttpMethod.POST)
	public UserEntity loginUser(@Named("username") String username, @Named("password") String password)
			throws NotFoundException {
		return userService.loginUser(username, password);
	}
}
