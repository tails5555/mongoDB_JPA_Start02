package net.kang.unit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.kang.config.JUnitConfig;
import net.kang.config.MongoConfig;
import net.kang.domain.Album;
import net.kang.model.AlbumForm;
import net.kang.service.AlbumService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JUnitConfig.class, MongoConfig.class})
@EnableMongoRepositories(basePackageClasses = net.kang.repository.AlbumRepository.class)
@EntityScan(basePackageClasses = net.kang.domain.Album.class)
public class AlbumServiceTest {
	@Autowired AlbumService albumService;
	private List<Album> currentAlbumList;
	static Random random=new Random();

	@Before
	public void initialize() {
		currentAlbumList=albumService.findAll();
	}

	@Test
	public void insertAfterFindAllTest() {
		AlbumForm albumForm=new AlbumForm();
		albumForm.setTitle("insertTitle");
		albumForm.setReleaseDate(new Date());
		List<Album> insertAfter=albumService.insertAfterFindAll(albumForm);
		assertEquals(currentAlbumList.size()+1, insertAfter.size());
	}

	@Test
	public void updateAfterFindOneTest() {
		int getIndex=random.nextInt(currentAlbumList.size());
		Album updateAlbum=currentAlbumList.get(getIndex);
		String albumId=updateAlbum.getId();
		AlbumForm albumForm=new AlbumForm();
		albumForm.setTitle("updateTitle");
		albumForm.setReleaseDate(new Date());
		Optional<Album> updateAfterAlbum=albumService.updateAfterFindOne(albumId, albumForm);
		assertNotEquals(updateAlbum, updateAfterAlbum.orElse(new Album()));
	}

	@After
	public void afterTest() {
		albumService.deleteAll();
		albumService.saveAll(currentAlbumList);
	}
}
