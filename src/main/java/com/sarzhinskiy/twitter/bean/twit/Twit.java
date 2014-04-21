package com.sarzhinskiy.twitter.bean.twit;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;

import com.sarzhinskiy.twitter.bean.Image;

public class Twit implements Serializable {
	private static final long serialVersionUID = 7350337036694490502L;
	
	private Long id; 
	private Long ownerId;
	private String text;
	private DateTime dateTime;
	private Integer status = new Integer(0); //is readable or visible for other users, or is deleted
	private volatile AtomicInteger likeNumber; //how much make likes
	private Image image;
	
	public Twit() {
		likeNumber = new AtomicInteger(0);
		dateTime = new DateTime();
	}
	
	public Twit(Long ownerId, String text) {
		this();
		this.ownerId = ownerId;
		this.text = text;
	}
	
	public Twit(Long id, Long ownerId, String text, DateTime dateTime) {
		this(ownerId, text);
		this.id = id;
		this.dateTime = dateTime;
	}
	
	
	@Override
	public String toString() {
		String info = "Twit(" + text + ", creation date: " + dateTime.toString() + ")";
		return info;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Twit)) {
			return false;
		}
		if (id == null) {
			return false;
		}
		return (id.equals(((Twit) object).getId()));
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public void removeImage() {
		this.image = null;
	}

	public Integer getLikeNumber() {
		return likeNumber.get();
	}

	public void setLikeNumber(Integer likeNumber) {
		this.likeNumber.set(likeNumber);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	

}
