package com.backtoback.entities.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.backtoback.entities.products.ProductEntity;
import com.backtoback.entities.users.UserEntity;
import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class EventEntity {

	@Id
	private Long id;
	private String name;
	private String description;
	@Index
	private Date startDate;
	private Date endDate;
	private GeoPt geoPoint;
	private String owner;
	private String tag;
	private List<ProductEntity> products;
	private List<String> attendants;
	private List<String> photos;

	public EventEntity() {
		setProducts(new ArrayList<ProductEntity>());
		setAttendants(new ArrayList<String>());
		setPhotos(new ArrayList<String>());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public GeoPt getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPt geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getAttendants() {
		return attendants;
	}

	public void setAttendants(List<String> attendants) {
		this.attendants = attendants;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public void addAttendant(UserEntity userEntity) {
		if (!this.attendants.contains(userEntity.getUsername())) {
			this.attendants.add(userEntity.getUsername());
		}
	}

	public void removeAttendant(UserEntity userEntity) {
		this.attendants.remove(userEntity.getUsername());
	}

	public List<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductEntity> products) {
		this.products = products;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
