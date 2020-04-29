package com.api.stepdefs;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import com.api.common.APIResources;
import com.api.common.CommonConstant;

import io.cucumber.datatable.*;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class StepDefs_PutRequest {

	private ScnContext SCN_CONTEXT = null;
	private String id_token;
	private String apiEndPointUri;
	private RequestSpecification rspec;
	private Response response;
	private String uuid;
	private JsonPath jsonPath;
	RestAssuredConfig rac;
	//private Scenario _SCN;
	//Local variable
	private String payload;
	
	
	//DI constructor
	public StepDefs_PutRequest(ScnContext scn_context) {
		this.SCN_CONTEXT = scn_context;
	}

	@Given("^the user has a valid authentication token for credentials$")
    public void the_user_with_credentials_something_and_something_has_a_valid_authentication_token(DataTable usercredentials) throws Throwable {
	 
		//_SCN = SCN_CONTEXT.get_SCN();
		
		List<List<String>> data = usercredentials.asLists();
		 String email= data.get(1).get(0);
		 String password= data.get(1).get(1);
		
		 	RestAssured.baseURI=SCN_CONTEXT.getProp().getExternalUri();
			Response accessTokenResponse = given()
					 .header("Content-Type", "application/x-www-form-urlencoded")
	                 .queryParams("client_id", SCN_CONTEXT.getProp().getClientId())
	                 .queryParams("client_secret", SCN_CONTEXT.getProp().getClientSecret())
	                 .queryParams("grant_type", "password")
	                 .queryParams("username", email)
	                 .queryParams("password", password)
	                 .queryParams("scope", "xxx-health-assistant")
	                 .when().log().all().post(APIResources.valueOf("oAuthAPI").getValue());
			 
			 System.out.println(accessTokenResponse.getStatusCode());
			 SCN_CONTEXT.get_SCN().write("accessTokenResponse: " + accessTokenResponse.getStatusCode());

			  JsonPath js=new io.restassured.path.json.JsonPath(accessTokenResponse.body().asString());
				id_token="Bearer " + js.getString("id_token");
				SCN_CONTEXT.setId_token(id_token);
				SCN_CONTEXT._SCN.write("Authentication Token is: " + id_token);
				
	 	}
	
	    @And("^user submit a request body with (.+) and body parameters$")
	    public void i_have_a_request_body_with_something_and_body_parameters(String bodytemplate, DataTable table) throws Throwable {
	       
	    	List<Map<String,String>> data = table.asMaps(String.class,String.class);
			 
	    	String template = new String(Files.readAllBytes(Paths.get( CommonConstant.BODY_SCHEMA_PATH + bodytemplate))); //Move To Common
			 StringSubstitutor sub = new StringSubstitutor(data.get(0));
			 payload=sub.replace(template);
			 SCN_CONTEXT.get_SCN().write("Payload is: " + payload);
			 System.out.println(payload);
	    	
	    }

	 	@When("^user submit a PUT Request on the (.+) endpoint with body$")
	    public void i_submit_a_request_on_the_endpoint(String endpoint) throws Throwable {

	    	//rac = RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames().relaxedHTTPSValidation("TLSv1.2"));
			
			String apiHostName =   SCN_CONTEXT.getProp().getApiHostName();
			apiEndPointUri = String.format("%s%s", apiHostName, endpoint);

			//Move to common
			rspec = new RequestSpecBuilder().setBaseUri(apiEndPointUri).addHeader("Content-Type", "application/json").addHeader("Authorization", id_token).build();	 
			response = given().spec(rspec).when().log().all().body(payload).put();
			
			SCN_CONTEXT.set_RESP_SPEC(rspec);
			SCN_CONTEXT.set_RESP(response);
	    
			/*JSONParser jsonParser = new JSONParser();
			FILE_PATH = System.getProperty("user.dir") + "//src//test//java//com//factory//cucumber//"
					+ "testdata/test.json"; */
			/*Response res=given().config(rac)
					.header("Authorization", id_token) 
					.queryParams("Accept", "application/json")
					.when().log().all()
					.get(apiEndPointUri);
	    	
			SCN_CONTEXT.set_RESP(res);
			System.out.println(SCN_CONTEXT.get_RESP().asString());*/
			
			
			
		/*	 List<Map<String, String>> maplist1=SCN_CONTEXT.getUtilsClass().read(new File("src/test/resources/data/Initilize.csv"));
			 for(Map<String,String> map1:maplist1)
			 {
				 System.out.println(map1);
			 }
			 
				String template = new String(Files.readAllBytes(Paths.get("src/test/resources/postBody/file.json")));
				for( int i=0; i< maplist1.size() ; i++)
				{
				 StringSubstitutor sub = new StringSubstitutor(maplist1.get(i));
				 String payload=sub.replace(template);
				System.out.println(payload);
				} */
	    }

	    @Then("^API should return Status code (.+)$")
	    public void api_should_return_status_code(int responsecode) throws Throwable {
	        
	    	SCN_CONTEXT.get_RESP().then().assertThat().statusCode(responsecode);
	    	SCN_CONTEXT.get_SCN().write("Status code appearing as: " + responsecode);
	    }


	    @And("^API response schema matches (.+)$")
	    public void the_response_schema_matches(String schema) throws Throwable {
			
	    	if(schema.equals("json")){
	    		SCN_CONTEXT.get_RESP().then().assertThat().contentType(ContentType.JSON).and()
				.body(matchesJsonSchemaInClasspath(schema));
			}
	    }
	
	    @And("^Verify that id is not blank$")
	    public void verify_that_id_is_not_blank() throws Throwable {
			 	jsonPath =new JsonPath(response.body().asString());  // Move to Common
			 	assertThat(jsonPath.getString("id"), is(not(emptyString())));
			 	SCN_CONTEXT.get_SCN().write("id: " + jsonPath.getString("id"));
			 	//System.out.println(jsonPath.getString("id"));
	    }

	    @And("^Verify that uuid is not blank$")
	    public void verify_that_uuid_is_not_blank() throws Throwable {
	    	uuid = jsonPath.getString("uuid");
	    	assertThat(jsonPath.getString("uuid"), is(not(emptyString())));
	    	SCN_CONTEXT.get_SCN().write("uuid: " + jsonPath.getString("uuid"));
	    }
	    
	    
	    @Then("^the body response content should be matched$")
		public void the_body_response_content_should_be_matched(Map<String,String> table) throws Throwable {

	    	for (Map.Entry<String, String> field : table.entrySet()) {
				if(StringUtils.isNumeric(field.getValue())){
					response.then().assertThat().body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
				}
				if(StringUtils.equalsIgnoreCase((field.getValue()),"true") || StringUtils.equalsIgnoreCase((field.getValue()),"false")){
					response.then().assertThat().body(field.getKey(), equalTo(Boolean.parseBoolean(field.getValue())));
				}
				else{
					response.then().assertThat().body(field.getKey(), equalTo(field.getValue()));
				}
			}
		}
}
