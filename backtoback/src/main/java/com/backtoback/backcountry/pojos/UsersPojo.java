package com.backtoback.backcountry.pojos;

import java.util.List;

public class UsersPojo {

	private String userId;
	private String displayName;
	private String profileUrl;
	private String bio;
	private List<String> passions;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
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
}
