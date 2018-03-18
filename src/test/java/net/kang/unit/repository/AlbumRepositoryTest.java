package net.kang.unit.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import net.kang.domain.Album;
import net.kang.repository.AlbumRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JUnitConfig.class, MongoConfig.class})
@EnableMongoRepositories(basePackageClasses = net.kang.repository.AlbumRepository.class)
@EntityScan(basePackageClasses = net.kang.domain.Album.class)
public class AlbumRepositoryTest {
	@Autowired AlbumRepository albumRepository;
	static final int QTY=15;
	static final int MIN_YEAR=1900;
	static final int MAX_YEAR=2000;
	static Random random=new Random();
	List<Album> currentAlbumList;

	@Before
	public void initialize() {
		currentAlbumList=albumRepository.findAll();
		albumRepository.deleteAll();
		for(int k=0;k<QTY;k++) {
			Album album=new Album();
			album.setTitle(String.format("Title%02d", k));
			album.setReleaseDate(new Date());
			albumRepository.insert(album);
		}
	}

	@Test
	public void findAllTest() {
		List<Album> findAllAlbum=albumRepository.findAll();
		assertEquals(QTY, findAllAlbum.size());
	}

	@Test
	public void findOneTest() {
		int getIndex=random.nextInt(QTY);
		List<Album> findAllAlbum=albumRepository.findAll();
		Album getAlbum=findAllAlbum.get(getIndex);
		Optional<Album> findOneAlbum=albumRepository.findById(getAlbum.getId());
		assertEquals(getAlbum, findOneAlbum.get());
	}

	@Test
	public void findByReleaseDateAfter() throws ParseException {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date date=format.parse("2000-01-01");
		List<Album> findByReleaseDate=albumRepository.findByReleaseDateAfter(date);
		assertTrue(findByReleaseDate.size()>0);
	}

	@Test
	@Rollback(true)
	public void insertTest() {
		Album album=new Album();
		album.setTitle(String.format("Title%02d", QTY));
		album.setReleaseDate(new Date());
		albumRepository.insert(album);
		List<Album> findAllAlbum=albumRepository.findAll();
		assertEquals(findAllAlbum.size(), QTY+1);
	}

	@Test
	@Rollback(true)
	public void updateTest() {
		int getIndex=random.nextInt(QTY);
		List<Album> findAllAlbum=albumRepository.findAll();
		Album updateAlbum=findAllAlbum.get(getIndex);
		updateAlbum.setTitle("tempTitle");
		updateAlbum.setReleaseDate(new Date());
		albumRepository.save(updateAlbum);
		Optional<Album> findOneAlbum=albumRepository.findById(updateAlbum.getId());
		assertEquals(findOneAlbum.get(), updateAlbum);
	}

	@Test
	@Rollback(true)
	public void deleteTest() {
		int getIndex=QTY-1;
		List<Album> findAllAlbum=albumRepository.findAll();
		Album deleteAlbum=findAllAlbum.get(getIndex);
		albumRepository.deleteById(deleteAlbum.getId());
		Optional<Album> findOneAlbum=albumRepository.findById(deleteAlbum.getId());
		assertTrue(!findOneAlbum.isPresent());
	}

	@Test
	public void countTest() {
		long length=albumRepository.count();
		assertEquals(QTY, length);
	}

	@Test
	public void existsTest() {
		int getIndex=random.nextInt(QTY-1);
		List<Album> findAllAlbum=albumRepository.findAll();
		Album existAlbum=findAllAlbum.get(getIndex);
		assertTrue(albumRepository.existsById(existAlbum.getId()));
	}

	@After
	public void afterTest() {
		albumRepository.deleteAll();
		albumRepository.saveAll(currentAlbumList);
	}


}
