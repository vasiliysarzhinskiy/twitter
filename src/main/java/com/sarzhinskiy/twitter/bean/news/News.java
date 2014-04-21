package com.sarzhinskiy.twitter.bean.news;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;

public class News implements Serializable { 
	private static final long serialVersionUID = -6679570168628091673L;
	
	private Long id;
	private String text;
	private DateTime dateTime;
	private Integer status = new Integer(0);
	private volatile AtomicInteger likeNumber; //how much make like
	private List<NewsImage> images;
	
	public News() {
		dateTime = new DateTime();
		likeNumber = new AtomicInteger(0);
		images = new ArrayList<NewsImage>();
	}
		
	public News(String text) {		
		this();
		this.text = text;
	}
	
	public News(Long id, String text, DateTime dateTime) {
		this(text);
		this.id = id;
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		String info = "News(" + text + ", creation date: " + dateTime.toString() + ")";
		return info;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof News)) {
			return false;
		}
		if (id == null) {
			return false;
		}
		return (id.equals(((News) object).getId()));
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

	public List<NewsImage> getImages() {
		return images;
	}

	public void setImages(List<NewsImage> images) {
		this.images = images;
	}
	
	public void addImage(NewsImage image) {
		images.add(image);
	}
	
	public void removeImage(NewsImage image) {
		images.remove(image);
	}
	
	public void removeImage(long imageId) {
		for (Iterator<NewsImage> iterator = images.iterator(); iterator.hasNext();) {
			if (iterator.next().getId() == imageId) {
				iterator.remove();
				return;
			}
		}
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	

}
