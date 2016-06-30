package MarketSimulator;

import java.util.LinkedList;
import java.util.Random;

public class NoisyCurveGenerator {
	private Random					random	= new Random();
	private int[] hash = new int[256];
	private float frequency, amplitude;

	private int hashMask = 255, time = 0;

	private void createHash(){
		for(int i = 0; i < 256; ++i){
			hash[i] = i;
		}
		shuffleArray(hash);
	}
	
	private void shuffleArray(int[] array)
	{
	    int index;
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        if (index != i)
	        {
	            array[index] ^= array[i];
	            array[i] ^= array[index];
	            array[index] ^= array[i];
	        }
	    }
	}
	
	public float simplexValuePart(float point, int ix) {
		float x = point - ix;
		float f = 1f - x * x;
		float f2 = f * f;
		float f3 = f * f2;
		float h = hash[ix & hashMask];
		f3 *= h;
		return f3 / 30;
	}

	public float createNoise() {
		float point = time * frequency;
		int ix = (int) Math.floor(point);
		float part = simplexValuePart(point, ix);
		part += simplexValuePart(point, ix + 1);
		part = part * (1f / (hashMask * 2 / 120)) - 1;
		return part;
	}
	
	public NoisyCurveGenerator(float frequency, float amplitude) {
		this.amplitude = amplitude;
		this.frequency = frequency;
		createHash();
	}
	
	public float getPoint() {
		if(time % 256 == 0){
//			createHash();
		}
		++time;
		return createNoise();
	}
}
