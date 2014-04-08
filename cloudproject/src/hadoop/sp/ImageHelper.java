package hadoop.sp;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageHelper {

	public static final String path = System.getProperty("user.dir");
	
	/**
	 * to get the image thumb via this method
	 * 
	 * @param source
	 *            source buffered image 
	 * @param width
	 *            8 pixels
	 * @param height
	 *            8 pixels
	 * @param b
	 *            flag for scaling with percentage
	 * */
	public static BufferedImage thumb(BufferedImage source, int width,
			int height, boolean b) {
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) width / source.getWidth();
		double sy = (double) height / source.getHeight();

		if (b) {
			if (sx > sy) {
				sx = sy;
				width = (int) (sx * source.getWidth());
			} else {
				sy = sx;
				height = (int) (sy * source.getHeight());
			}
		}

		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(width, height, type);
		Graphics2D g = target.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	/**
	 * read image file
	 * @param filename image file path
	 * @return BufferedImage 
	 */
	public static BufferedImage readImage(String filename)
	{
		try {
			File inputFile = new File(filename);  
	        BufferedImage sourceImage = ImageIO.read(inputFile);
			return sourceImage;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * convert from rgb color to gray
	 * @param pixels rgb value in a pixel
	 * @return int gray level
	 */
	public static int rgbToGray(int pixels) {
		// int _alpha = (pixels >> 24) & 0xFF;
		int _red = (pixels >> 16) & 0xFF;
		int _green = (pixels >> 8) & 0xFF;
		int _blue = (pixels) & 0xFF;
		return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
	}
	
	/**
	 * use OSTU algorithm to get the gray level threshold
	 * @param pixels
	 * @return
	 */
	public static int ostu(int[] pixels) {
		int[] histogram = histogram(pixels);
		int total = pixels.length;
		int sum = 0;
	    for (int i = 1; i < 256; ++i)
	        sum += i * histogram[i];
	    int sumB = 0;
	    int wB = 0;
	    int wF = 0;
	    double mB;
	    double mF;
	    double max = 0.0;
	    double between = 0.0;
	    double threshold1 = 0.0;
	    double threshold2 = 0.0;
	    for (int i = 0; i < 256; ++i) {
	        wB += histogram[i];
	        if (wB == 0)
	            continue;
	        wF = total - wB;
	        if (wF == 0)
	            break;
	        sumB += i * histogram[i];
	        mB = sumB / wB;
	        mF = (sum - sumB) / wF;
	        between = wB * wF * Math.pow(mB - mF, 2);
	        if ( between >= max ) {
	            threshold1 = i;
	            if ( between > max ) {
	                threshold2 = i;
	            }
	            max = between;            
	        }
	    }
	    Double result = ( threshold1 + threshold2 ) / 2.0;
	    return Math.round(result.intValue());
	}
	
	/**
	 * get histogram from a array of gray level for an image
	 * @param outputValue
	 * @return
	 */
	public static int[] histogram (int[] outputValue) {
		int[] returnValue = new int[256];
		for (int i=0;i<outputValue.length;i++) {
			int value = Double.valueOf(outputValue[i]).intValue();
			returnValue[value]++;
		}
		return returnValue;
	}
}
