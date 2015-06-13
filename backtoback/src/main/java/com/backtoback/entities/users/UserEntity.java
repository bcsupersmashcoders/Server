package com.backtoback.entities.users;

import java.util.ArrayList;
import java.util.List;

import com.backtoback.backcountry.pojos.UsersPojo;
import com.backtoback.entities.events.EventEntity;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UserEntity {

	@Id
	private Long id;
	private String backcountryUserId;
	@Index
	private String username;
	private String bio;
	private List<String> passions;
	private List<Long> eventsAttendants;
	private List<Long> eventsCreated;

	public UserEntity() {
		this.setEventsAttendants(new ArrayList<Long>());
		this.setEventsCreated(new ArrayList<Long>());
	}

	public UserEntity(String username, UsersPojo userPojo) {
		this.username = username;
		this.bio = userPojo.getBio();
		this.passions = userPojo.getPassions();
		this.backcountryUserId = userPojo.getUserId();
	}

	public void merge(UsersPojo userPojo) {
		this.bio = userPojo.getBio();
		this.passions = userPojo.getPassions();
		this.backcountryUserId = userPojo.getUserId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBackcountryUserId() {
		return backcountryUserId;
	}

	public void setBackcountryUserId(String backcountryUserId) {
		this.backcountryUserId = backcountryUserId;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<String> getPassions() {
		return passions;
	}

	public void setPassions(List<String> passions) {
		this.passions = passions;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public List<Long> getEventsAttendants() {
		return eventsAttendants;
	}

	public void setEventsAttendants(List<Long> eventsAttendants) {
		this.eventsAttendants = eventsAttendants;
	}

	public List<Long> getEventsCreated() {
		return eventsCreated;
	}

	public void setEventsCreated(List<Long> eventsCreated) {
		this.eventsCreated = eventsCreated;
	}

	public void attendEvent(EventEntity eventEntity) {
		if (!this.eventsAttendants.contains(eventEntity.getId())) {
			this.eventsAttendants.add(eventEntity.getId());
		}
	}

	public void removeAttendant(EventEntity eventEntity) {
		this.eventsAttendants.remove(eventEntity.getId());
	}

	public void createEvent(EventEntity eventEntity) {
		this.eventsCreated.add(eventEntity.getId());
	}
}