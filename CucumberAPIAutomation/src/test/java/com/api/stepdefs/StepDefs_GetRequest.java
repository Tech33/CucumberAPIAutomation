package com.api.stepdefs;


import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;


public class StepDefs_GetRequest {

	private String apiEndPointUri;
	private ScnContext SCN_CONTEXT = null;
	private RequestSpecification rspec;
	private JsonPath jsonPath;
	private Response response;
	//private Scenario _SCN = SCN_CONTEXT.get_SCN();
	
	
	
	public StepDefs_GetRequest(ScnContext scn_context) {
		this.SCN_CONTEXT = scn_context;
	}	

    @When("^user submit a GET Request on the (.+) endpoint$")
    public void i_submit_a_something_request_on_the_endpoint(String endpoint) throws Throwable {

    	String apiHostName = SCN_CONTEXT.getProp().getApiHostName();
		apiEndPointUri = String.format("%s%s", apiHostName, endpoint);	
		
		rspec = new RequestSpecBuilder().setBaseUri(apiEndPointUri).addHeader("Content-Type", "application/json").addHeader("Authorization", SCN_CONTEXT.getId_token()).build();	 
		response = RestAssured.given().spec(rspec).when().log().all().get(apiEndPointUri);
		SCN_CONTEXT.set_RESP(response);
    }

    @And("^the response body will have (.+)$")
    public void the_response_body_will_have(String id) throws Throwable {

	 	jsonPath =new JsonPath(response.body().asString());
	 	assertThat(jsonPath.getString("[0].id"), is(equalTo(id)));
	 	SCN_CONTEXT.get_SCN().write("id: " + jsonPath.getString("[0].id"));
    	
    }

    @And("^the response body should contain lable:$")
    public void the_response_body_should_contain_lable(DataTable dataTable) throws Throwable {
    	
    	List<String> actualLable = dataTable.asList(String.class);
    	List<String> responseLable = new ArrayList<String>(); 
    	responseLable.add(jsonPath.getString("[0].label"));
    	
    	List<HashMap<String, Object>> jsonObjectsInArray =jsonPath.getList("[0].answers");
		for (HashMap<String, Object> jsonObject : jsonObjectsInArray) {
			responseLable.add(jsonObject.get("label").toString());
			}

		assertThat(CollectionUtils.isEqualCollection(responseLable, actualLable), equalTo(true));
    
    }	
}
