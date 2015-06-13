package com.backtoback.entities.images;

import com.google.appengine.api.blobstore.BlobKey;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ImageEntity {
	@Id
	private Long id;
	private BlobKey blobKey;
	private String servingURL;
	@Index
	private String eventId;

	public BlobKey getBlobKey() {
		return blobKey;
	}

	public void setBlobKey(BlobKey blobKey) {
		this.blobKey = blobKey;
	}

	public String getServingURL() {
		return servingURL;
	}

	public void setServingURL(String servingURL) {
		this.servingURL = servingURL;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

}
