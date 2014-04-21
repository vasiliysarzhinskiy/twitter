package com.sarzhinskiy.twitter.dao;

import java.util.List;

import com.sarzhinskiy.twitter.bean.news.News;
import com.sarzhinskiy.twitter.bean.news.NewsImage;


public interface NewsDAO {
	public boolean create(News news);
	public News findById(Long id);
	public List<News> findAll();
	public List<News> findLast(int numberNews); //how much news we want to see
	public List<News> findMostPopular(int numberNews); //how much popular news we want to see
	public boolean update(News news);
	public boolean updateLikeNumber(News news);
	public boolean remove(Long id);
	public boolean removeAll();
	public boolean insertImage(News news, NewsImage image);
	public boolean updateImage(NewsImage image);
	public boolean removeAllNewsImages();
	public boolean removeImage(News news, Long imageId);
	public NewsImage findImage(Long imageId);
	public List<NewsImage> findImagesByNews(Long newsId);
}
