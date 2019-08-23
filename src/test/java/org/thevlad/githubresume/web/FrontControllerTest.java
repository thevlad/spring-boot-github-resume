package org.thevlad.githubresume.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.thevlad.githubresume.model.GithubUser;
import org.thevlad.githubresume.model.GithubUserDetail;
import org.thevlad.githubresume.services.IGithubUserService;


@WebMvcTest(controllers = FrontController.class)
public class FrontControllerTest {

	private static GithubUser user;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IGithubUserService githubUserService;
	
	@BeforeAll
	static void setup() {
		user = new GithubUser();
		GithubUserDetail userDetail = new GithubUserDetail("mockUser", "http://a.b.c", "Mock User",
				"mockUser@mockmail.com", "http://a.b.c", new Date());
		user.setUser(userDetail);
	}

	@BeforeEach
	void setMockOutput() throws IOException {
		when(githubUserService.findGithubUser(Mockito.anyString())).thenReturn(user);
	}

	@Test
	public void testHome() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Github User here")));
	}

	@Test
	public void testSearch() throws Exception {
		
		Mockito.when(githubUserService.findGithubUser(ArgumentMatchers.any(String.class))).thenReturn(user);

		mockMvc
				.perform(post("/search").param("userName", "any").contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Mock User")));
		
	}

	@Test
	public void testSearchValidateError() throws Exception {
		
		Mockito.when(githubUserService.findGithubUser(ArgumentMatchers.any(String.class))).thenReturn(user);

		mockMvc
				.perform(post("/search").param("userName", "a").contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("User Name cannot be less than 2 characters!")));
		
	}

}
