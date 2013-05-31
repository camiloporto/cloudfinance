package br.com.camiloporto.cloudfinance.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@RequestMapping("/user")
@Controller
public class AccountSystemController {
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse signUp(String userName, String pass, String confirmPass) {
		//FIXME implementar logica aqui e no negocio
		return new UserOperationResponse(true);
	}
}
