package br.com.camiloporto.cloudfinance.web;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/accountsystems")
@Controller
@RooWebScaffold(path = "accountsystems", formBackingObject = AccountSystem.class)
public class AccountSystemController {
}
