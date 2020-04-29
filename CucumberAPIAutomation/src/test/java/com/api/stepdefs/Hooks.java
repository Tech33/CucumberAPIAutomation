package com.api.stepdefs;

import java.io.IOException;

import org.aeonbits.owner.ConfigFactory;

import com.api.utlis.ConfigProperties;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class Hooks {
	
	
	private ConfigProperties prop;
	private Scenario _SCN;
	
	private ScnContext scn_context;
	
	public Hooks(ScnContext scn_context) {
		this.scn_context = scn_context;
	}	

	@Before
	public void SetUp(Scenario s) throws IOException {
		this._SCN = s;	
		_SCN.write("Scn started: " + _SCN.getName());
		prop = ConfigFactory.create(ConfigProperties.class);
		
		scn_context.set_SCN(s);
		scn_context.setProp(prop);

	}
	
	
}
