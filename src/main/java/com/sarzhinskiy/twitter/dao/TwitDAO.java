package com.sarzhinskiy.twitter.dao;

import java.util.List;

import com.sarzhinskiy.twitter.bean.Image;
import com.sarzhinskiy.twitter.bean.twit.Twit;


public interface TwitDAO {
	public boolean create(Twit twit);
	public Twit findById(Long id);
	public List<Twit> findAllByOwnerId(Long id);
	public List<Twit> findAllByOwnerEmail(String email);
	public List<Twit> findLastByOwnerId(Long id, int numberTwits);
	public boolean update(Twit twit);
	public boolean updateLikeNumber(Twit twit);
	public boolean remove(Long id);
	public boolean removeAll();
	public boolean insertImage(Twit twit, Image image);
	public boolean removeImage(Twit twit);
	public Image findImage(Long twitId);

}
