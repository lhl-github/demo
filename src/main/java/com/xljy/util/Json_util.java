
package com.xljy.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json_util {

	/**
	 * json to map
	 * 
	 * @return
	 */
	public static HashMap<String, Object> jsonToMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> readValue = null;
		try {
			readValue = mapper.readValue(json, HashMap.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException("json转map失败!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException("json转map失败!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("json转map失败!");
		}
		return readValue;
	}

	/**
	 * json to list
	 * 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> jsonToList(String json) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<HashMap<String, Object>> readValue = null;
		try {
			readValue = mapper.readValue(json, ArrayList.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException("json转list失败!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException("json转list失败!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("json转list失败!");
		}
		return readValue;
	}
	
	/**
	 * Object to String_json
	 * 
	 * @return
	 */
	public static String ArrayListToJsonString(ArrayList arraylist) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
		
			json = mapper.writeValueAsString(arraylist);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("转jsonString失败!");
		}
		
		return json;
	}

	public static String MapToJsonString(Map hashMap) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(hashMap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("转jsonString失败!");
		}
		
		return json;
	}

}
