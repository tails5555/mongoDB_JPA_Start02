package net.kang.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.kang.domain.Music;
import net.kang.model.MusicForm;
import net.kang.repository.MusicRepository;

@Service
public class MusicService {
	@Autowired MusicRepository musicRepository;
	public List<Music> findAll(){
		return musicRepository.findAll();
	}
	public Optional<Music> findById(String id){
		return musicRepository.findById(id);
	}
	public List<Music> findByYearBetween(int year1, int year2){
		return musicRepository.findByYearBetween(year1, year2);
	}
	public List<Music> findBySinger(String singer){
		return musicRepository.findBySinger(singer);
	}
	public void insert(Music music) {
		musicRepository.insert(music);
	}
	public void saveAll(List<Music> musicList) {
		musicRepository.saveAll(musicList);
	}
	public void update(Music music) {
		musicRepository.save(music);
	}
	public void delete(String id) {
		musicRepository.deleteById(id);
	}
	public void deleteAll() {
		musicRepository.deleteAll();
	}
	public List<Music> insertAfterFindAll(MusicForm musicForm){
		Music newMusic=new Music();
		newMusic.setTitle(musicForm.getTitle());
		newMusic.setSinger(musicForm.getSinger());
		newMusic.setYear(musicForm.getYear());
		newMusic.setGenre(musicForm.getGenre());
		musicRepository.insert(newMusic);
		return musicRepository.findAll();
	}
	public Optional<Music> updateAfterFindOne(String id, MusicForm musicForm){
		Optional<Music> findOneMusic=musicRepository.findById(id);
		Music updateMusic=findOneMusic.orElse(new Music());
		updateMusic.setTitle(musicForm.getTitle());
		updateMusic.setSinger(musicForm.getSinger());
		updateMusic.setYear(musicForm.getYear());
		updateMusic.setGenre(musicForm.getGenre());
		musicRepository.save(updateMusic);
		return musicRepository.findById(id);
	}
	public Music findTopByYearDesc() {
		List<Music> musicList=musicRepository.findAll(new Sort(Sort.Direction.DESC, "year"));
		return musicList.get(0);
	}
}