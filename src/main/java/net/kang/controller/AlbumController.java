package net.kang.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.kang.domain.Album;
import net.kang.service.AlbumService;

@RestController
@CrossOrigin
@RequestMapping("album")
public class AlbumController {
	@Autowired AlbumService albumService;
	@RequestMapping("findAll")
	public List<Album> findAll(){
		return albumService.findAll();
	}
	@RequestMapping("findOne/{id}")
	public Album findOne(@PathVariable("id") String id) {
		Optional<Album> result=albumService.findOne(id);
		return result.orElse(new Album());
	}
	@RequestMapping("findByReleaseDateAfter/{releaseDate}")
	public List<Album> findBySinger(@PathVariable("releaseDate") @DateTimeFormat(pattern = "yyyyMMdd") Date releaseDate){
		return albumService.findByReleaseDateAfter(releaseDate);
	}
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public String insert(@RequestBody Album album) {
		if(!albumService.exists(album)) {
			albumService.insert(album);
			return "Album Insert Complete";
		}
		return "Album Insert Failure";
	}
	@RequestMapping(value="update", method=RequestMethod.PUT)
	public String update(@RequestBody Album album) {
		if(albumService.exists(album)) {
			albumService.update(album);
			return "Album Update Complete";
		}
		return "Album Update Failure";
	}
	@RequestMapping(value="delete/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") String id) {
		if(!albumService.findOne(id).orElse(new Album()).equals(new Album())) {
			albumService.delete(id);
			return "Album Delete Complete";
		}
		return "Album Delete Failure";
	}
}
