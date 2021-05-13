package org.example;

import static org.example.utils.JsonUtils.toJson;

import java.util.ArrayList;
import java.util.HashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

// JSON test
public class Test123 {
	public static void main(String[] args) {
		main1();
		main2();
	}
	public static void main1() {
		JSONObject option1 = new JSONObject();
		option1.put("name", "option1");
		option1.put("price", 0);
		option1.put("quantity", 2);
		JSONArray options = new JSONArray();
		options.add(option1);
		
		
		String json = options.toJSONString();
		System.out.println("json?" + json);
	}
	public static void main2() {
		HashMap<String, Object> option1 = new HashMap<>();
		option1.put("name", "option1");
		option1.put("price", 0);
		option1.put("quantity", 2);
		ArrayList<HashMap<String, Object>> options = new ArrayList<>();
		options.add(option1);
		
		String json = toJson(new HashMap<String, Object>() {
			{
				put("name", "상품테스트1");
				put("description", "description");
				put("price", 0);
				put("status", "OPEN");
				put("options", option1);
			}
		});
		System.out.println("json?" + json);
	}
}
