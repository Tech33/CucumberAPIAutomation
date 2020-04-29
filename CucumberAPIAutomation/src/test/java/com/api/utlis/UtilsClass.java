package com.api.utlis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.text.StringSubstitutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


public class UtilsClass {

	//*************************UTILS**************************
	//To get random Key
	public String GetRandomString(int n) {
		// lower limit for LowerCase Letters 
		int lowerLimit = 97; 

		// lower limit for LowerCase Letters 
		int upperLimit = 122; 

		Random random = new Random(); 

		// Create a StringBuffer to store the result 
		StringBuffer r = new StringBuffer(n); 

		for (int i = 0; i < n; i++) { 

			// take a random value between 97 and 122 
			int nextRandomChar = lowerLimit 
					+ (int)(random.nextFloat() 
							* (upperLimit - lowerLimit + 1)); 

			// append a character at the end of bs 
			r.append((char)nextRandomChar); 
		} 

		// return the resultant string 
		return r.toString(); 
	} 
	
	//converting csv file data to map
	public List<Map<String, String>> read(File file) throws JsonProcessingException, IOException {
	    List<Map<String, String>> response = new LinkedList<Map<String, String>>();
	    CsvMapper mapper = new CsvMapper();
	    CsvSchema schema = CsvSchema.emptySchema().withHeader();
	    MappingIterator<Map<String, String>> iterator = mapper.reader(Map.class)
	            .with(schema)
	            .readValues(file);
	    while (iterator.hasNext()) {
	        response.add(iterator.next());
	    }
	    return response;
	}
	
/*	public static void main(String args[]) throws JsonProcessingException, IOException
	{
		
		
		 List<Map<String, String>> maplist1=read(new File("src/test/resources/data/Initilize.csv"));
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
		}
	}
*/	
}
