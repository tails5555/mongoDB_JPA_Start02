package net.kang.unit.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.kang.config.JUnitConfig;
import net.kang.config.MongoConfig;
import net.kang.controller.MusicController;
import net.kang.domain.Music;
import net.kang.service.MusicService;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {JUnitConfig.class, MongoConfig.class})
@WebAppConfiguration
public class MusicControllerTest {
	static int QTY=10;
	static Random random=new Random();
	MockMvc mockMvc;
	@Mock MusicService musicService;
	@InjectMocks MusicController musicController;
	private List<Music> tempMusicList;
	private MockHttpServletRequest mockHttpServletRequest;
	private MockHttpServletResponse mockHttpServletResponse;

	public Music initMusic(String id, String title, String singer, int year, String genre) {
		Music music=new Music();
		music.setId(id);
		music.setTitle(title);
		music.setSinger(singer);
		music.setYear(year);
		music.setGenre(genre);
		return music;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc=MockMvcBuilders.standaloneSetup(musicController).build();
		mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletResponse = new MockHttpServletResponse();
		tempMusicList=new ArrayList<Music>();
		for(int k=0;k<QTY;k++) {
			tempMusicList.add(initMusic(Integer.toString(k+1), String.format("Music%02d", k), String.format("Singer%02d", k), 1900+random.nextInt(100), String.format("Genre%02d", k)));
		}
	}

	private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

	@Test
	public void findAllTest() throws Exception{
		List<Music> findAllResult=tempMusicList;
		when(musicService.findAll()).thenReturn(findAllResult);
		String toJSON=this.jsonStringFromObject(findAllResult);
		System.out.println(findAllResult);
		mockMvc.perform(get("/music/findAll")).andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)));
	}

	@Test
	public void findOneTest() throws Exception{
		Music findOneResult=tempMusicList.get(1);
		when(musicService.findById(Integer.toString(1))).thenReturn(Optional.of(findOneResult));
		String toJSON=this.jsonStringFromObject(findOneResult);
		mockMvc.perform(get("/music/findOne/{id}", 1)).andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)));
	}
}