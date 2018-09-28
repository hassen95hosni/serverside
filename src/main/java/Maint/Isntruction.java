package Maint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

public class Isntruction {
	String id ;
	String type ;
	String add;
	String sender ;
	String result;
	String date;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public Isntruction() {
		super();
	}
	
	public Isntruction findInstructionByID(Connection connection , RethinkDB database , String id) {
		List<Isntruction> fi = new ArrayList<Isntruction>();
		//fi=database.db("maintennance").table("user").filter(row ->row.g("id").eq(id)).coerceTo("array").run(connection);
		Cursor<Isntruction>cursor = database.db("maintennance").table("instruction").filter(row ->row.g("id").eq(id.toString())).run(connection);
		fi =cursor.toList();
		System.out.println(fi.toString());
		for(int nn =0 ; nn<fi.size();nn++) {
			System.out.println(fi.get(nn));
		}
		for (Isntruction user :cursor) {
			System.out.println("usr:"+user);
		}
		Isntruction instruction = new Isntruction();
		ObjectMapper mapper = new ObjectMapper();
		//System.out.println(fi.size());
		JSONObject json = new JSONObject();
		json.putAll((Map) fi.get(0));
		System.out.println(json);
		try {
			//JSONArray tlala = (JSONArray)fi.get(0).toString().substring(1,fi.get(0).toString().length());
			
			Isntruction j = mapper.readValue(json.toString(), Isntruction.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   	
		
	   	return instruction;
	}

	
	

}
