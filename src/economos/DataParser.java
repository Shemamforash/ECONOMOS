package economos;

import java.io.*;
import java.util.*;

public class DataParser {
	private File resourceDataFile = new File("ResourceData.csv");
	private static ArrayList<RawResourceData> rawResourceData = new ArrayList<RawResourceData>();
	private static ArrayList<String> types = new ArrayList<String>();
	
	public DataParser() throws IOException{
		FileReader reader = null;
		try {
			reader = new FileReader(resourceDataFile);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find resource data file.");
		}
		if(reader != null){
			BufferedReader bReader = new BufferedReader(reader);
			String next = " ";
			while(true){
				next = bReader.readLine();
				if(next == null){
					break;
				}
				String[] resourceStrArr = next.split("#");
				String name = resourceStrArr[0];
				String type = resourceStrArr[1];
				String description = "";
				if(resourceStrArr.length > 2){
					description = resourceStrArr[2];
				}
				RawResourceData r = new RawResourceData(name, type, description); //FIXME
				if(!types.contains(type)){
					types.add(type);
				}
				rawResourceData.add(r);
			}
			reader.close();
		}
	}
	
	public static ArrayList<Resource> getResourceData(String owner){
		ArrayList<Resource> arr = new ArrayList<Resource>();
		
		for(int i = 0; i < rawResourceData.size(); ++i){
			Resource r = null;
			RawResourceData rawDatum = rawResourceData.get(i);
			
			if(owner.equals("User")){
				r = new UserResource(rawDatum.name(), rawDatum.description(), rawDatum.type()); 
			} else if(owner.equals("Market")){
				r = new MarketResource(rawDatum.name(), rawDatum.description(), rawDatum.type(), 0);
			}
			if(r != null){
				arr.add(r);
			}	
		}
		
		return arr;
	}
	
	public static ArrayList<String> getTypes(){
		return types;
	}
	
	class RawResourceData{
		private String name, description, type;
		
		public RawResourceData(String name, String type, String description){
			this.name = name;
			this.description = description;
			this.type = type;
		}
		
		public String name(){
			return name;
		}
		
		public String description(){
			return description;
		}
		
		public String type(){
			return type;
		}
	}
}
