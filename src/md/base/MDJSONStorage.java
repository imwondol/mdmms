package md.base;

import java.io.*;
import java.util.*;
import org.json.simple.*;

/*
 * dataset
 * 
 * hook
 * 
 * auth
 */
public class MDJSONStorage {
	private JSONObject prop = null;
	private String filename = null;
	private boolean isError = true;
	
	public MDJSONStorage(String filename) {
		this.filename = filename;
		try {
			load();
			isError = false;
		} catch(Exception e) {
			
		}
	}
	
	public void load() throws IOException {
		try (BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(this.filename), "UTF-8"))){
			load(brIn);
		} catch(Exception e) {
			throw e;
		}
	}

	public void save() throws IOException {
		if(isError) throw new IOException("MDJSONStorage is not init");
		try (BufferedWriter bwOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filename), "UTF-8"))) {
			save(bwOut);
		} catch(Exception e) {
			throw e;
		}
	}

	private void load(BufferedReader brIn) throws IOException {
		JSONObject jObj = (JSONObject)JSONValue.parse(brIn);
		if(prop != null) prop.clear();
		prop = jObj;
	}

	private void save(BufferedWriter bwOut) throws IOException {
		if(isError) throw new IOException("MDJSONStorage is not init");
		bwOut.write(prop.toJSONString());
	}

	public Object get(String key) {
		String[] keys = key.split("\\.");
		try {
			Object obj = prop;
			for(String k : keys) {
				obj = ((JSONObject)obj).get(k);
			}
			return obj;
		} catch(Exception e) {
			return null;
		}
	}

	public Object remove(String key) {
		String[] keys = key.split("\\.");
		try {
			JSONObject parent = null;
			Object obj = prop;
			for(String k : keys) {
				parent = (JSONObject)obj;
				obj = ((JSONObject)obj).get(k);
			}
			parent.remove(keys[keys.length-1]);
			return obj;
		} catch(Exception e) {
			return null;
		}
	}

	public boolean put(String key, Object value) {
		boolean rtn = false;
		String[] keys = key.split("\\.");
		try {
			JSONObject parent = null;
			Object obj = prop;
			for(String k : keys) {
				parent = (JSONObject)obj;
				obj = ((JSONObject)obj).get(k);
			}
			parent.put(keys[keys.length-1], value);
			rtn = true;
		} catch(Exception e) {
		}
		return rtn;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader brIn = new BufferedReader(new InputStreamReader(MDJSONStorage.class.getResourceAsStream("MDJSONStorage.test_json"), "UTF-8"));
		MDJSONStorage js = new MDJSONStorage("");
		js.filename = "test.json";
		js.isError = false;
		js.load(brIn);
		js.put("test", "test");

		JSONObject p = new JSONObject();
	
		System.out.println(js.put("test", p));
		System.out.println(js.put("test.ttt", "HELLO"));
		System.out.println(js.get("test.ttt"));
		js.save();
	}

}
