package net.kang.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.kang.domain.Music;
import net.kang.service.MusicService;

@RestController
@CrossOrigin
@RequestMapping("music")
public class MusicController {
	@Autowired MusicService musicService;
	@RequestMapping("findAll")
	public List<Music> findAll(){
		return musicService.findAll();
	}
	@RequestMapping("findOne/{id}")
	public Music findOne(@PathVariable("id") String id) {
		Optional<Music> result=musicService.findById(id);
		return result.orElse(new Music());
	}
	@RequestMapping("findBySinger/{singer}")
	public List<Music> findBySinger(@PathVariable("singer") String singer){
		return musicService.findBySinger(singer);
	}
	@RequestMapping("findByYearBetween/{before}/{after}")
	public List<Music> findByYearBetween(@PathVariable("before") int before, @PathVariable("after") int after){
		return musicService.findByYearBetween(before, after);
	}
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public String insert(@RequestBody Music music) {
		if(!musicService.exists(music)) {
			musicService.insert(music);
			return "Music Insert Complete";
		}
		return "Music Insert Failure";
	}
	@RequestMapping(value="update", method=RequestMethod.PUT)
	public String update(@RequestBody Music music) {
		if(musicService.exists(music)) {
			musicService.update(music);
			return "Music Update Complete";
		}
		return "Music Update Failure";
	}
	@RequestMapping(value="delete/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") String id) {
		if(!musicService.findById(id).orElse(new Music()).equals(new Music())) {
			musicService.delete(id);
			return "Music Delete Complete";
		}
		return "Music Delete Failure";
	}
}