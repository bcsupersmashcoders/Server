package com.backtoback.entities.products;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Embed
public class ProductEntity {

	@Id
	private String id;
	private String name;
	private String productURL;
	private String photoURL;

	public ProductEntity() {
		super();
	}

	public ProductEntity(String id, String name, String productURL, String photoURL) {
		super();
		this.id = id;
		this.name = name;
		this.productURL = productURL;
		this.photoURL = photoURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

}
