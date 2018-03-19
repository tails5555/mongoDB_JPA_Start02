package net.kang.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.kang.domain.Album;
import net.kang.model.AlbumForm;
import net.kang.repository.AlbumRepository;

@Service
public class AlbumService {
	@Autowired AlbumRepository albumRepository;
	public List<Album> findAll(){
		return albumRepository.findAll();
	}
	public Optional<Album> findOne(String id) {
		return albumRepository.findById(id);
	}
	public void insert(Album album) {
		albumRepository.insert(album);
	}
	public void update(Album album) {
		albumRepository.save(album);
	}
	public void saveAll(List<Album> albumList) {
		albumRepository.saveAll(albumList);
	}
	public void delete(String id) {
		albumRepository.deleteById(id);
	}
	public void deleteAll() {
		albumRepository.deleteAll();
	}
	public List<Album> findByReleaseDateAfter(Date date) {
		return albumRepository.findByReleaseDateAfter(date);
	}
	public List<Album> insertAfterFindAll(AlbumForm albumForm){
		Album newAlbum=new Album();
		newAlbum.setTitle(albumForm.getTitle());
		newAlbum.setReleaseDate(albumForm.getReleaseDate());
		albumRepository.insert(newAlbum);
		return albumRepository.findAll();
	}
	public Optional<Album> updateAfterFindOne(String id, AlbumForm albumForm){
		Optional<Album> findOneAlbum=albumRepository.findById(id);
		Album updateAlbum=findOneAlbum.orElse(new Album());
		updateAlbum.setTitle(albumForm.getTitle());
		updateAlbum.setReleaseDate(albumForm.getReleaseDate());
		albumRepository.save(updateAlbum);
		return albumRepository.findById(id);
	}
}
