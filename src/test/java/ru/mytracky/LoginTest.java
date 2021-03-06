package ru.mytracky;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mytracky.controller.v1.AuthenticationRestControllerV1;
import ru.mytracky.dto.AuthenticationRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mytracky.util.Convert.asJsonString;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LoginTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthenticationRestControllerV1 controller;

	@Test
	public void successfulLoginTest() throws Exception{
		this.mockMvc.perform(post("/api/v1/login")
				.contentType("application/json")
				.content(asJsonString(new AuthenticationRequestDto("pkyfen","test"))))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("pkyfen"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
	}

    @Test
    public void wrongPasswordLoginTest() throws Exception{
        this.mockMvc.perform(post("/api/v1/login")
                .contentType("application/json")
                .content(asJsonString(new AuthenticationRequestDto("pkyfen","wrongPassword"))))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    public void wrongUsernameLoginTest() throws Exception{
        this.mockMvc.perform(post("/api/v1/login")
                .contentType("application/json")
                .content(asJsonString(new AuthenticationRequestDto("wrongName","wrongPassword"))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with username: wrongName not found"));
    }



}
