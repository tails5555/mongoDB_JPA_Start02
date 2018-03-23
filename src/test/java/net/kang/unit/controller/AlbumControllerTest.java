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
import java.util.Date;
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
import net.kang.controller.AlbumController;
import net.kang.domain.Album;
import net.kang.service.AlbumService;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {JUnitConfig.class, MongoConfig.class})
@WebAppConfiguration
public class AlbumControllerTest {
	static int QTY=10;
	static Random random=new Random();
	MockMvc mockMvc;
	@Mock AlbumService albumService;
	@InjectMocks AlbumController albumController;

	private Album initAlbum(String id, String title) {
		Album album=new Album();
		album.setId(id);
		album.setReleaseDate(new Date());
		album.setTitle(title);
		return album;
	}

	private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

	private List<Album> makeTempData(){
		List<Album> tmpList=new ArrayList<Album>();
		for(int k=0;k<QTY;k++) {
			tmpList.add(initAlbum(Integer.toString(k+1), String.format("Title%02d", k)));
		}
		return tmpList;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc=MockMvcBuilders.standaloneSetup(albumController).build();
	}

	@Test
	public void findAllTest() throws Exception{
		List<Album> findAllResult=makeTempData();
		when(albumService.findAll()).thenReturn(findAllResult);
		String toJSON=this.jsonStringFromObject(findAllResult);
		mockMvc.perform(get("/album/findAll"))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)))
		.andDo(print());

		verify(albumService, times(1)).findAll();
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void findOneTest() throws Exception{
		Album findOneResult=makeTempData().get(1);
		when(albumService.findOne(Integer.toString(1))).thenReturn(Optional.of(findOneResult));
		String toJSON=this.jsonStringFromObject(findOneResult);
		mockMvc.perform(get("/album/findOne/{id}", 1))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().string(equalTo(toJSON)))
		.andDo(print());

		verify(albumService, times(1)).findOne("1");
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void insertTest() throws Exception{
		Album insertAlbum=initAlbum(Integer.toString(QTY+1), String.format("Title%02d", QTY));
		when(albumService.exists(insertAlbum)).thenReturn(false); // when은 반환형 데이터가 존재하면 가능함.
		doNothing().when(albumService).insert(insertAlbum); // doNothing은 void 함수에 한해 가능함.

		mockMvc.perform(
				post("/album/insert")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(insertAlbum)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(albumService, times(1)).exists(insertAlbum);
		verify(albumService, times(1)).insert(insertAlbum);
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void insertTestFail() throws Exception{
		Album insertAlbum=initAlbum(Integer.toString(QTY+1), String.format("Title%02d", QTY));
		when(albumService.exists(insertAlbum)).thenReturn(true);

		mockMvc.perform(
				post("/album/insert")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(insertAlbum)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(albumService, times(1)).exists(insertAlbum);
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void updateTest() throws Exception{
		Album updateAlbum=initAlbum(Integer.toString(QTY+1), String.format("Title%02d", QTY));
		when(albumService.exists(updateAlbum)).thenReturn(true);
		doNothing().when(albumService).update(updateAlbum);

		mockMvc.perform(
				put("/album/update")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(updateAlbum)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(albumService, times(1)).exists(updateAlbum);
		verify(albumService, times(1)).update(updateAlbum);
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void updateTestFail() throws Exception{
		Album updateAlbum=initAlbum(Integer.toString(QTY+1), String.format("Title%02d", QTY));
		when(albumService.exists(updateAlbum)).thenReturn(false);

		mockMvc.perform(
				put("/album/update")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.jsonStringFromObject(updateAlbum)))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(albumService, times(1)).exists(updateAlbum);
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void deleteTest() throws Exception{
		Album deleteAlbum=initAlbum("1", String.format("Title%02d", QTY));
		when(albumService.findOne(deleteAlbum.getId())).thenReturn(Optional.of(deleteAlbum));
		doNothing().when(albumService).delete(deleteAlbum.getId());

		mockMvc.perform(
				delete("/album/delete/{id}", deleteAlbum.getId()))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(albumService, times(1)).findOne(deleteAlbum.getId());
		verify(albumService, times(1)).delete(deleteAlbum.getId());
		verifyNoMoreInteractions(albumService);
	}

	@Test
	public void deleteTestFail() throws Exception{
		when(albumService.findOne("1")).thenReturn(Optional.of(new Album()));

		mockMvc.perform(
				delete("/album/delete/{id}", "1"))
		.andExpect(status().isOk())
		.andDo(print()).andReturn();

		verify(albumService, times(1)).findOne("1");
		verifyNoMoreInteractions(albumService);
	}
}
