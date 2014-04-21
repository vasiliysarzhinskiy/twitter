package com.sarzhinskiy.twitter.bean;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Image implements Serializable {
	private static final long serialVersionUID = -6809979337488598410L;
	
	private String name;
	private File imageFile;
	private BufferedImage bufImage;
	
	public Image() {
	}
	
	public Image(String imageName) {
		this.name = imageName;
	}
	
	@Override
	public String toString() {
		return "Image(" + name + ")";
	}

	public void saveImage() {
		try {
			if (bufImage == null) {  
				bufImage = tryToGetBufImageFromImageFile();
			}
			ImageIO.write(bufImage, "jpg", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveImage(String url) {
		try { 
			if (bufImage == null) {  
				bufImage = tryToGetBufImageFromImageFile();
			}
			ImageIO.write(bufImage, "jpg", new File(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage tryToGetBufImageFromImageFile() throws IOException {
		return ImageIO.read(new FileInputStream(imageFile));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	
	public void setImageFile(String imagePath) {
		this.imageFile = new File(imagePath);
		
	}

	public BufferedImage getBufImage() {
		return bufImage;
	}

	public void setBufImage(BufferedImage bufImage) {
		this.bufImage = bufImage;
	}
	
}
