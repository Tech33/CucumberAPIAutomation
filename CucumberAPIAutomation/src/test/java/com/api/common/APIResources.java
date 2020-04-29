package com.api.common;

public enum APIResources {

	oAuthAPI("/auth/oauth/v2/token");

	
	private String value;

	APIResources(String envValue) {
		this.value = envValue;
	}

	public String getValue() {
		return value;
	}
	
}
