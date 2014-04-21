package com.sarzhinskiy.twitter.bean.news;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

import com.sarzhinskiy.twitter.bean.Image;

public class NewsImage implements Serializable {
	private static final long serialVersionUID = -8516372408968844973L;
	
	private Image image;
	private Long imageId;
	private Long newsId;
	private String comment;

	public NewsImage() {
		image = new Image();
	}
	
	public NewsImage(String name, Long newsId) {
		this();
		image.setName(name);
		this.newsId = newsId;
	}
	
	public NewsImage(Long id, String name, Long newsId) {
		this(name, newsId);
		this.imageId = id;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof NewsImage)) {
			return false;
		}
		if (imageId == null) {
			return false;
		}
		return (imageId.equals(((NewsImage) object).getId()));
	}
	
	public void saveImage() {
		image.saveImage();
	}
	
	public void saveImage(String url) {
		image.saveImage(url);
	}
	
	@Override
	public String toString() {
		return "NewsImage(" + image.getName() +", newsId: " + newsId + ")";
	}
	
	public Long getId() {
		return imageId;
	}

	public void setId(Long id) {
		this.imageId = id;
	}

	public String getName() {
		return image.getName();
	}

	public void setName(String name) {
		image.setName(name);
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public BufferedImage getBufImage() {
		return image.getBufImage();
	}
	
	public File getImageFile() {
		return image.getImageFile();
	}

	public void setImageFile(File imageFile) {
		image.setImageFile(imageFile);
	}
	
	public void setImageFile(String imagePath) {
		image.setImageFile(imagePath);
	}

	public void setBufImage(BufferedImage bufImage) {
		image.setBufImage(bufImage);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
