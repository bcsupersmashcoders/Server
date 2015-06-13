package com.backtoback.services.images;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backtoback.entities.images.ImageEntity;
import com.backtoback.pojos.URLResource;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.ObjectifyService;

public class ImageHandlingService {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public URLResource getImageURL(String id) {

		String url = "/images?eventId=" + id;
		return new URLResource(blobstoreService.createUploadUrl(url));
	}

	public List<ImageEntity> getImagesFromEvent(String eventId) {
		Filter eventIdFilter = new FilterPredicate("eventId", FilterOperator.EQUAL, eventId);
		Query query = new Query("ImageEntity").setFilter(eventIdFilter);
		List<Entity> entities = DatastoreServiceFactory.getDatastoreService().prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		List<ImageEntity> images = new ArrayList<ImageEntity>();
		for (Entity entity : entities) {
			images.add((ImageEntity) ObjectifyService.ofy().toPojo(entity));
		}
		return images;
	}

	public JSONObject postImage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		List<BlobKey> blobs = blobstoreService.getUploads(req).get("image");
		BlobKey blobKey = blobs.get(0);

		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);

		String servingUrl = imagesService.getServingUrl(servingOptions);

		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json");
		JSONObject json = new JSONObject();
		try {
			json.put("servingUrl", servingUrl);
			json.put("blobKey", blobKey.getKeyString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageEntity entity = new ImageEntity();
		entity.setEventId((String) req.getParameter("eventId"));
		entity.setBlobKey(blobKey);
		entity.setServingURL(servingUrl);

		ObjectifyService.ofy().save().entity(entity).now();
		return json;
	}
}
