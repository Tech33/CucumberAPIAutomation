package com.api.stepdefs;


import com.api.utlis.ConfigProperties;
import com.api.utlis.UtilsClass;

import cucumber.api.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ScnContext {

	private Response _RESP;
	private RequestSpecification _RESP_SPEC;
	private UtilsClass utilsClass;
	private ConfigProperties prop;
	protected Scenario _SCN;

	public void set_SCN(Scenario _SCN) {
		this._SCN = _SCN;
	}

	public void setProp(ConfigProperties prop) {
		this.prop = prop;
	}

	private String id_token;

	public String getId_token() {
		return id_token;
	}

	public void setId_token(String id_token) {
		this.id_token = id_token;
	}


	public ScnContext(){
		utilsClass = new UtilsClass();
	}
	
	
	public UtilsClass getUtilsClass() {
		return utilsClass;
	}

	public Response get_RESP() {
		return _RESP;
	}

	public void set_RESP(Response _RESP) {
		this._RESP = _RESP;
	}


	public RequestSpecification get_RESP_SPEC() {
		return _RESP_SPEC;
	}

	public ConfigProperties getProp() {
		return prop;
	}

	public Scenario get_SCN() {
		return _SCN;
	}


	public void set_RESP_SPEC(RequestSpecification rspec) {
		this._RESP_SPEC=rspec;
		
	}

	
}
