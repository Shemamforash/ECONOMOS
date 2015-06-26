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
				String[] resourceStrArr = next.split(",");
				RawResourceData r = new RawResourceData(resourceStrArr[0], resourceStrArr[1], resourceStrArr[2]); //FIXME
				if(!types.contains(resourceStrArr[2])){
					types.add(resourceStrArr[2]);
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
			
			switch (owner){
				case "User":
					r = new UserResource(rawDatum.name(), rawDatum.description(), rawDatum.type()); 
					break;
				case "Market":
					r = new MarketResource(rawDatum.name(), rawDatum.description(), rawDatum.type(), 0);
					break;
				default:
					break;
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
		
		public RawResourceData(String name, String description, String type){
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
