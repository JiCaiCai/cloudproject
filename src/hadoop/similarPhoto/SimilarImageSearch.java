package hadoop.similarPhoto;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SimilarImageSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> hashCodes = new ArrayList<String>();
	    
	    String filename = ImageHelper.path + "\\images\\";
	    String hashCode = null;
		
	    for (int i = 0; i < 5; i++)
        {
		    hashCode = produceFingerPrint(filename + "example" + (i + 1) + ".jpg");
		    hashCodes.add(hashCode);
        }	    
	    System.out.println("Resources: ");
	    System.out.println(hashCodes);
	    System.out.println();
	    
		String sourceHashCode = produceFingerPrint(filename + "source.jpg");
		System.out.println("Source: ");
		System.out.println(sourceHashCode);
		System.out.println();
		
		List<Integer> differences = new ArrayList<Integer>();
		for (int i = 0; i < hashCodes.size(); i++)
        {
		    int difference = hammingDistance(sourceHashCode, hashCodes.get(i));
		    differences.add(difference);
        }
		
		System.out.println(differences);
	}

	/**
	 * Calculate the Hamming distance by the fingerprint from the  
	 * @param sourceHashCode hashCode
	 */
	public static int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();
		
		for (int i = 0; i < len; i++) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				difference ++;
			} 
		}
		
		return difference;
	}

	/**
	 * Produce a photo finger print
	 * @param filename
	 * @return fingerprint hash code
	 */
	public static String produceFingerPrint(String filename) {
		BufferedImage source = ImageHelper.readImage(filename);//Read image file

		int width = 8;
		int height = 8;
		
		BufferedImage thumb = ImageHelper.thumb(source, width, height, false);
		
		int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i * height + j] = ImageHelper.rgbToGray(thumb.getRGB(i, j));
			}
		}
		
		int thresholdPixel = ImageHelper.ostu(pixels);
		
		int[] comps = new int[width * height];
		for (int i = 0; i < comps.length; i++) {
			if (pixels[i] >= thresholdPixel) {
				comps[i] = 1;
			} else {
				comps[i] = 0;
			}
		}
		
		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < comps.length; i+= 4) {
			int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
			hashCode.append(binaryToHex(result));
		}
		
		return hashCode.toString();
	}

	/**
	 * convert binary value to hex value
	 * @param int binary
	 * @return char hex
	 */
	private static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary)
		{
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}

}
