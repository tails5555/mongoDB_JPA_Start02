package net.kang.unit.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

	private Music initMusic(String id, String title, String singer, int year, String genre) {
		Music music=new Music();
		music.setId(id);
		music.setTitle(title);
		music.setSinger(singer);
		music.setYear(year);
		music.setGenre(genre);
		return music;
	}

	private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

	private List<Music> makeTempData(){
		List<Music> tmpList=new ArrayList<Music>();
		for(int k=0;k<QTY;k++) {
			tmpList.add(initMusic(Integer.toString(k+1), String.format("Music%02d", k), String.format("Singer%02d", k), 1900+random.nextInt(100), String.format("Genre%02d", k)));
		}
		return tmpList;
	}

	private List<Music> findBySingerTempData(){
		List<Music> tmpList=new ArrayList<Music>();
		for(int k=0;k<QTY;k++) {
			tmpList.add(initMusic(Integer.toString(k+1), String.format("Music%02d", k), "findSinger", 1900+random.nextInt(100), String.format("Genre%02d", k)));
		}
		return tmpList;
	}

	private List<Music> findByYearTempData(){
		List<Music> tmpList=new ArrayList<Music>();
		for(int k=0;k<QTY;k++) {
			tmpList.add(initMusic(Integer.toString(k+1), String.format("Music%02d", k), String.format("Singer%02d", k), 1980+random.nextInt(10), String.format("Genre%02d", k)));
		}
		return tmpList;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc=MockMvcBuilders.standaloneSetup(musicController).build();
	}

	@Test
	public void findAllTest() throws Exception{
		List<Music> findAllResult=makeTempData();
		when(musicService.findAll()).thenReturn(findAllResult);
		String toJSON=this.jsonStringFromObject(findAllResult);
		mockMvc.perform(get("/music/findAll"))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)))
		.andDo(print());

		verify(musicService, times(1)).findAll();
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void findOneTest() throws Exception{
		Music findOneResult=makeTempData().get(1);
		when(musicService.findById(Integer.toString(1))).thenReturn(Optional.of(findOneResult));
		String toJSON=this.jsonStringFromObject(findOneResult);
		mockMvc.perform(get("/music/findOne/{id}", 1))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)))
		.andDo(print());

		verify(musicService, times(1)).findById("1");
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void findBySingerTest() throws Exception{
		List<Music> findBySingerResult=findBySingerTempData();
		when(musicService.findBySinger("findSinger")).thenReturn(findBySingerResult);
		String toJSON=this.jsonStringFromObject(findBySingerResult);

		mockMvc.perform(get("/music/findBySinger/{singer}", "findSinger"))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)))
		.andDo(print());

		verify(musicService, times(1)).findBySinger("findSinger");
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void findByYearBetweenTest() throws Exception{
		List<Music> findByYearResult=findByYearTempData();
		when(musicService.findByYearBetween(1980, 1990)).thenReturn(findByYearResult);
		String toJSON=this.jsonStringFromObject(findByYearResult);

		mockMvc.perform(get("/music/findByYearBetween/{year1}/{year2}", 1980, 1990))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)))
		.andDo(print());

		verify(musicService, times(1)).findByYearBetween(1980, 1990);
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void insertTest() throws Exception{
		Music insertMusic=initMusic(Integer.toString(QTY+1), String.format("Title%02d", QTY), String.format("Singer%02d", QTY), 1900+random.nextInt(100), String.format("Genre%02d", QTY));
		when(musicService.exists(insertMusic)).thenReturn(false); // when은 반환형 데이터가 존재하면 가능함.
		doNothing().when(musicService).insert(insertMusic); // doNothing은 void 함수에 한해 가능함.

		mockMvc.perform(
				post("/music/insert")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(insertMusic)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(musicService, times(1)).exists(insertMusic);
		verify(musicService, times(1)).insert(insertMusic);
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void insertTestFail() throws Exception{
		Music insertMusic=initMusic(Integer.toString(QTY+1), String.format("Title%02d", QTY), String.format("Singer%02d", QTY), 1900+random.nextInt(100), String.format("Genre%02d", QTY));
		when(musicService.exists(insertMusic)).thenReturn(true);

		mockMvc.perform(
				post("/music/insert")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(insertMusic)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(musicService, times(1)).exists(insertMusic);
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void updateTest() throws Exception{
		Music updateMusic=initMusic("1", "tmpTitle", "tmpSinger", 1900+random.nextInt(100), "tmpGenre");
		when(musicService.exists(updateMusic)).thenReturn(true);
		doNothing().when(musicService).update(updateMusic);

		mockMvc.perform(
				put("/music/update")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(updateMusic)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(musicService, times(1)).exists(updateMusic);
		verify(musicService, times(1)).update(updateMusic);
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void updateTestFail() throws Exception{
		Music updateMusic=initMusic("1", "tmpTitle", "tmpSinger", 1900+random.nextInt(100), "tmpGenre");
		when(musicService.exists(updateMusic)).thenReturn(false);

		mockMvc.perform(
				put("/music/update")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(updateMusic)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(musicService, times(1)).exists(updateMusic);
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void deleteTest() throws Exception{
		Music deleteMusic=initMusic("1", "tmpTitle", "tmpSinger", 1900+random.nextInt(100), "tmpGenre");
		when(musicService.findById(deleteMusic.getId())).thenReturn(Optional.of(deleteMusic));
		doNothing().when(musicService).delete(deleteMusic.getId());

		mockMvc.perform(
				delete("/music/delete/{id}", deleteMusic.getId()))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(musicService, times(1)).findById(deleteMusic.getId());
		verify(musicService, times(1)).delete(deleteMusic.getId());
		verifyNoMoreInteractions(musicService);
	}

	@Test
	public void deleteTestFail() throws Exception{
		when(musicService.findById("1")).thenReturn(Optional.of(new Music()));

		mockMvc.perform(
				delete("/music/delete/{id}", "1"))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(musicService, times(1)).findById("1");
		verifyNoMoreInteractions(musicService);
	}
}