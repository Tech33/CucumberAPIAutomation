package com.api.common;

import io.restassured.http.ContentType;

public class CommonConstant {

	//RestAssured Configuration
	ContentType CONTENT_TYPE = ContentType.JSON;
	public static final String BODY_SCHEMA_PATH = "src/test/resources/postBody/";
	//public static String _SCN = SCN_CONTEXT.get_SCN();;
}
