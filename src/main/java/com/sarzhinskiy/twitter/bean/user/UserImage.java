package com.sarzhinskiy.twitter.bean.user;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

import com.sarzhinskiy.twitter.bean.Image;

public class UserImage implements Serializable {
	private static final long serialVersionUID = -781294975017130900L;
	
	private Image image;
	private Long imageId;
	private Long userId;
	private String comment;
	private Integer status = new Integer(0);
	private String albumName;
	
	public UserImage() {
		image = new Image();
	}
	
	public UserImage(String name, Long userId) {
		this();
		image.setName(name);
		this.userId = userId;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof UserImage)) {
			return false;
		}
		if (imageId == null) {
			return false;
		}
		return (imageId.equals(((UserImage) object).getImageId()));
	}
	
	@Override
	public String toString() {
		return "UserImage(" + image.getName() +", userId: " + userId + ")";
	}

	public void saveImage() {
		image.saveImage();
	}
	
	public void saveImage(String url) {
		image.saveImage(url);
	}
	
	public String getName() {
		return image.getName();
	}

	public void setName(String name) {
		image.setName(name);
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
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

}
