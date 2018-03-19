package net.kang.unit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.kang.config.JUnitConfig;
import net.kang.config.MongoConfig;
import net.kang.domain.Music;
import net.kang.model.MusicForm;
import net.kang.service.MusicService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JUnitConfig.class, MongoConfig.class})
@EnableMongoRepositories(basePackageClasses = net.kang.repository.MusicRepository.class)
@EntityScan(basePackageClasses = net.kang.domain.Music.class)
public class MusicServiceTest {
	@Autowired MusicService musicService;
	private List<Music> currentMusicList;
	static Random random=new Random();

	@Before
	public void initialize() {
		currentMusicList=musicService.findAll();
	}

	@Test
	public void insertAfterFindAllTest() {
		MusicForm musicForm=new MusicForm();
		musicForm.setTitle("insertTitle");
		musicForm.setSinger("insertSinger");
		musicForm.setYear(2000);
		musicForm.setGenre("insertGenre");
		List<Music> insertAfter=musicService.insertAfterFindAll(musicForm);
		assertEquals(currentMusicList.size()+1, insertAfter.size());
	}

	@Test
	public void updateAfterFindOneTest() {
		int getIndex=random.nextInt(currentMusicList.size());
		Music updateMusic=currentMusicList.get(getIndex);
		String updateId=updateMusic.getId();
		MusicForm musicForm=new MusicForm();
		musicForm.setTitle("updateTitle");
		musicForm.setSinger("updateSinger");
		musicForm.setYear(1000);
		musicForm.setGenre("updateGenre");
		Optional<Music> updateAfterMusic=musicService.updateAfterFindOne(updateId, musicForm);
		assertNotEquals(updateMusic, updateAfterMusic.get());
	}

	@Test
	public void findTopByYearDescTest() {
		int getIndex=random.nextInt(currentMusicList.size());
		Music compareMusic=currentMusicList.get(getIndex);
		Music latestMusic=musicService.findTopByYearDesc();
		assertTrue(compareMusic.getYear()<=latestMusic.getYear());
	}

	@After
	public void afterTest() {
		musicService.deleteAll();
		musicService.saveAll(currentMusicList);
	}
}
