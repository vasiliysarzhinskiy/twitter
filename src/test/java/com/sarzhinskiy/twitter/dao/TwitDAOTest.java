package com.sarzhinskiy.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sarzhinskiy.twitter.bean.Image;
import com.sarzhinskiy.twitter.bean.twit.Twit;
import com.sarzhinskiy.twitter.dao.TwitDAO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/SpringXMLConfigTest.xml"})
public class TwitDAOTest {

	private static final String DEFAULT_TWIT_INFO = "twit!";
	private static long DEFAULT_OWNER_ID = 1L;
	private static final String SELECT_COUNT_TWITS = "select count(*) from twit";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TwitDAO twitDAO;
	
	@Before
	public void clearDBTable() {
		jdbcTemplate.execute("delete from twit");
	}
	
	@Test
	public void testCreateNoException() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
	}
	
	@Test
	public void testCreate() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		
		int size = jdbcTemplate.queryForObject(SELECT_COUNT_TWITS, Integer.class);
		assertEquals(1, size);
	}
	
	@Test
	public void testCreateAutoGeneratedId() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		assertNotNull(twit.getId());
	}
	
	@Test
	public void testCreateCorrectFilledField() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		int likeNumber = 5;
		twit.setLikeNumber(likeNumber);
		int status = 1;
		twit.setStatus(status);	
		twitDAO.create(twit);
		
		int createdStatus = jdbcTemplate.queryForObject("select status from twit", Integer.class);
		assertEquals(status, createdStatus);
		
		int createdNLike = jdbcTemplate.queryForObject("select like_number from twit", Integer.class);
		assertEquals(likeNumber, createdNLike);
		
		String createdInfo = jdbcTemplate.queryForObject("select text from twit", String.class);
		assertEquals(DEFAULT_TWIT_INFO, createdInfo);
		
		Timestamp timestamp = jdbcTemplate.queryForObject("select timestamp from twit", Timestamp.class);
		DateTime createdDateTime = new DateTime(timestamp);
		assertEquals(twit.getDateTime(), createdDateTime);
	};
	
	
	
	@Test
	public void testCreateTwitWithImage() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		Image image = new Image("img");
		image.setImageFile("images//image1.jpg");
		twit.setImage(image);
		twitDAO.create(twit);
		
		int size = jdbcTemplate.queryForObject(SELECT_COUNT_TWITS, Integer.class);
		assertEquals(1, size);
	}
	
	@Test
	public void testInsertImageInExistTwit() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		
		Image image = new Image("img");
		image.setImageFile("images//image1.jpg");
		twitDAO.insertImage(twit, image);
		assertNotNull(twit.getImage());
	}
	
	@Test
	public void testUpdateTwit() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		
		int status = 3;
		twit.setStatus(status);
		String newInfo = "V";
		twit.setText(newInfo);
		int likeNumber = 5;
		twit.setLikeNumber(likeNumber);
		twitDAO.update(twit);
		
		int updatedStatus = jdbcTemplate.queryForObject("select status from twit", Integer.class);
		int updatedNLike = jdbcTemplate.queryForObject("select like_number from twit", Integer.class);
		String updatedInfo = jdbcTemplate.queryForObject("select text from twit", String.class);
		assertEquals(status, updatedStatus);
		assertEquals(newInfo, updatedInfo);
		assertEquals(likeNumber, updatedNLike);
	}
	
	@Test
	public void testUpdateLikeNumber() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twit.setLikeNumber(1);
		twitDAO.create(twit);
		
		int likeNumber = 5;
		twit.setLikeNumber(likeNumber);
		twitDAO.updateLikeNumber(twit);
		int updatedNLike = jdbcTemplate.queryForObject("select like_number from twit", Integer.class);
		assertEquals(likeNumber, updatedNLike);
	}
	
	@Test
	public void testFindTwitById() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		
		Twit findedTwit = twitDAO.findById(twit.getId());
		assertEquals(twit, findedTwit);
	}
	
	@Test
	public void testFindAllTwitsByOwnerId() {
		Twit twit1 = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit1);
		Twit twit2 = new Twit(DEFAULT_OWNER_ID, "AAA");
		twitDAO.create(twit2);
		
		List<Twit> twits = twitDAO.findAllByOwnerId(DEFAULT_OWNER_ID);
		assertEquals(2, twits.size());
	}
	
	@Test
	public void testFindLastTwitsByOwnerId() {
		//must be delay because hsql save only milliseconds, and two twits could be added in DB with the same milliseconds
		Twit twit1 = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit1);
		sleepOneMillisecond();
		Twit twit2 = new Twit(DEFAULT_OWNER_ID, "AAA");
		twitDAO.create(twit2);
		sleepOneMillisecond();
		Twit twit3 = new Twit(DEFAULT_OWNER_ID, "B");
		twitDAO.create(twit3);
		
		int size = 2;
		List<Twit> twits = twitDAO.findLastByOwnerId(DEFAULT_OWNER_ID, size);
		assertEquals(size, twits.size());
		
		assertEquals(twit3, twits.get(0));
		assertEquals(twit2, twits.get(1));
	}
	
	@Test
	public void testFindImageByTwitId() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		
		Image image = new Image("img");
		image.setImageFile("images//image1.jpg");
		String imageName = "smile";
		image.setName(imageName);
		twit.setImage(image);
		twitDAO.insertImage(twit, image);
		
		Image findedImage = twitDAO.findImage(twit.getId());
		assertNotNull(findedImage);
	}
	
	@Test
	public void testRemoveTwitNoException() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		twitDAO.remove(twit.getId());
	}
	
	@Test
	public void testRemoveTwitById() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		twitDAO.remove(twit.getId());
		
		int size = jdbcTemplate.queryForObject(SELECT_COUNT_TWITS, Integer.class);
		assertEquals(0, size);
	}
	
	@Test
	public void testTryRemoveNotExistTwitNoException() {
		long notExistID = 1000000;
		twitDAO.remove(notExistID);
	}
	
	@Test
	public void testRemoveTwitByIdNotDeleteOtherTwits() {
		Twit twit1 = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit1);
		sleepOneMillisecond();
		Twit twit2 = new Twit(DEFAULT_OWNER_ID, "AAA");
		twitDAO.create(twit2);
		
		twitDAO.remove(twit1.getId());
		int size = jdbcTemplate.queryForObject(SELECT_COUNT_TWITS, Integer.class);
		assertEquals(1, size);
		
		int countTwit2 = jdbcTemplate.queryForObject("select count(*) from twit where id=" + twit2.getId(), Integer.class);
		assertEquals(1, countTwit2);
	}
	
	@Test
	public void testRemoveAllTwits() {
		Twit twit1 = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit1);
		sleepOneMillisecond();
		Twit twit2 = new Twit(DEFAULT_OWNER_ID, "AAA");
		twitDAO.create(twit2);
		
		twitDAO.removeAll();
		int size = jdbcTemplate.queryForObject(SELECT_COUNT_TWITS, Integer.class);
		assertEquals(0, size);
	}

	@Test
	public void testRemoveImageFromTwit() {
		Twit twit = new Twit(DEFAULT_OWNER_ID, DEFAULT_TWIT_INFO);
		twitDAO.create(twit);
		
		Image image = new Image("img");
		image.setImageFile("images//image1.jpg");
		twitDAO.insertImage(twit, image);

		twitDAO.removeImage(twit);
		assertEquals(null, twit.getImage());//not exist assetNull?
		int size = jdbcTemplate.queryForObject(SELECT_COUNT_TWITS, Integer.class);
		assertEquals(1, size);
	}
	
	
	private void sleepOneMillisecond() {
		try {
			Thread.sleep(1);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
}