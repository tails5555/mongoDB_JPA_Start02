package net.kang.unit.repository;

import static org.junit.Assert.assertEquals;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.kang.config.JUnitConfig;
import net.kang.config.MongoConfig;
import net.kang.domain.Music;
import net.kang.repository.MusicRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JUnitConfig.class, MongoConfig.class})
@EnableMongoRepositories(basePackageClasses = net.kang.repository.MusicRepository.class)
@EntityScan(basePackageClasses = net.kang.domain.Music.class)
public class MusicRepositoryTest {
	@Autowired MusicRepository musicRepository;
	static final int QTY=15;
	static final int MIN_YEAR=1900;
	static final int MAX_YEAR=2000;
	static Random random=new Random();
	List<Music> currentMusicList;

	@Before
	public void initialize() {
		currentMusicList=musicRepository.findAll();
		musicRepository.deleteAll();
		for(int k=0;k<QTY;k++) {
			int newYear=MIN_YEAR+random.nextInt(100);
			Music music=new Music();
			music.setTitle(String.format("Title%02d", k));
			music.setSinger(String.format("Singer%02d", k));
			music.setGenre(String.format("Genre%02d", k));
			music.setYear(newYear);
			musicRepository.insert(music);
		}
	}

	@Test
	public void findAllTest() {
		List<Music> musicList=musicRepository.findAll();
		assertEquals(QTY, musicList.size());
	}

	@Test
	public void findOneTest() {
		int getIndex=random.nextInt(QTY);
		List<Music> musicList=musicRepository.findAll();
		Music findOneMusic=musicList.get(getIndex);
		Optional<Music> resultMusic=musicRepository.findById(findOneMusic.getId());
		assertEquals(findOneMusic, resultMusic.get());
	}

	@Test
	public void findBySingerTest() {
		int getIndex=random.nextInt(QTY);
		String singerName=String.format("Singer%02d", getIndex);
		List<Music> findBySingerMusic=musicRepository.findBySinger(singerName);
		assertTrue(findBySingerMusic.size()>0);
	}

	@Test
	public void findByYearBetweenTest() {
		List<Music> findByYearBetweenMusic=musicRepository.findByYearBetween(MIN_YEAR-1, MAX_YEAR+1);
		assertEquals(findByYearBetweenMusic.size(), QTY);
	}

	@Test
	@Rollback(true)
	public void insertTest() {
		int newYear=MIN_YEAR+random.nextInt(100);
		Music newMusic=new Music();
		newMusic.setTitle(String.format("Title%02d", QTY));
		newMusic.setSinger(String.format("Singer%02d", QTY));
		newMusic.setGenre(String.format("Genre%02d", QTY));
		newMusic.setYear(newYear);
		musicRepository.insert(newMusic);
		List<Music> findAllMusic=musicRepository.findAll();
		assertEquals(findAllMusic.size(), QTY+1);
	}

	@Test
	@Rollback(true)
	public void updateTest() {
		int getIndex=random.nextInt(QTY);
		int newYear=MIN_YEAR+random.nextInt(100);
		List<Music> findAllMusic=musicRepository.findAll();
		Music updateMusic=findAllMusic.get(getIndex);
		updateMusic.setTitle("tempMusic");
		updateMusic.setSinger("tempSinger");
		updateMusic.setGenre("tempGenre");
		updateMusic.setYear(newYear);
		musicRepository.save(updateMusic);
		Optional<Music> findOneMusic=musicRepository.findById(updateMusic.getId());
		assertEquals(updateMusic, findOneMusic.get());
	}

	@Test
	@Rollback(true)
	public void deleteTest() {
		int getIndex=QTY-1;
		List<Music> findAllMusic=musicRepository.findAll();
		Music deleteMusic=findAllMusic.get(getIndex);
		musicRepository.deleteById(deleteMusic.getId());
		Optional<Music> findOneMusic=musicRepository.findById(deleteMusic.getId());
		assertEquals(findOneMusic.orElse(new Music()), new Music());
	}

	@Test
	public void countTest() {
		long length=musicRepository.count();
		assertEquals(QTY, length);
	}

	@Test
	public void existsTest() {
		int getIndex=random.nextInt(QTY-1);
		List<Music> findAllMusic=musicRepository.findAll();
		Music existMusic=findAllMusic.get(getIndex);
		assertTrue(musicRepository.existsById(existMusic.getId()));
	}

	@After
	public void afterTest() {
		musicRepository.deleteAll();
		musicRepository.saveAll(currentMusicList);
	}
}
