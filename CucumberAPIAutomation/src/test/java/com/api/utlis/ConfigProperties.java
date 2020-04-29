package com.api.utlis;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({ 
	"file:src/test/resources/config.properties" // mention the property file name
})
public interface ConfigProperties extends Config{

	@Key("external_uri")
	String getExternalUri();
	
	@Key("client_id")
	String getClientId();
	
	@Key("client_secret")
	String getClientSecret();
	
	@Key("api_Host_Name")
	String getApiHostName();
}
