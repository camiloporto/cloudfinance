package br.com.camiloporto.cloudfinance.builders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

public class WebUserManagerOperationBuilder {

	private MockMvc mockMvc;
	private MockHttpSession mockSession;

	public WebUserManagerOperationBuilder(MockMvc mockMvc, MockHttpSession mockSession) {
		this.mockMvc = mockMvc;
		this.mockSession = mockSession;
	}

	public WebUserManagerOperationBuilder signup(String userName, String userPass, String userConfirmPass) throws Exception {
		mockMvc.perform(post("/user/signup")
				.session(mockSession)
				.param("userName", userName)
				.param("pass", userPass)
				.param("confirmPass", userConfirmPass)
				.accept(MediaType.APPLICATION_JSON)
			);
		return this;
	}

	public WebUserManagerOperationBuilder login(String userName, String userPass) throws Exception {
		mockMvc.perform(post("/user/login").param("userName", userName).param("pass", userPass).accept(MediaType.APPLICATION_JSON).session(mockSession));
		return this;
	}

	public WebUserManagerOperationBuilder logoutCurrentUser() throws Exception {
		mockMvc.perform(post("/user/logoff")
				.session(mockSession));
		return this;
	}

}
