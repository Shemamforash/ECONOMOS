package MarketSimulator;

import java.util.ArrayList;

public class Debug {
	public static <T> void log(T s){
		System.out.println(s);
	}
	public static <T> void log(ArrayList<T> arrlist){
		for(T t : arrlist) {
			System.out.println(t);
		}
	}
	public static <T> void log(T[] arr){
		for(T t : arr) {
			System.out.println(t);
		}
	}
}
