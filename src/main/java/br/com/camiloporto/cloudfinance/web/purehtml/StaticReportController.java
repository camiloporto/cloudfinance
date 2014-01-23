package br.com.camiloporto.cloudfinance.web.purehtml;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.utils.DateUtils;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;
import br.com.camiloporto.cloudfinance.web.BalanceSheetForm;
import br.com.camiloporto.cloudfinance.web.ReportController;
import br.com.camiloporto.cloudfinance.web.ReportOperationResponse;

@RequestMapping("/report")
@Controller
@SessionAttributes(value = {"logged", "activeAccountSystem", "statementBeginDate", "statementEndDate", "formAccountId"})
public class StaticReportController {
	
	@Autowired
	private ReportController jsonController;
	
	@Autowired
	private AccountSystemController accountController;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView reportHome() {
		return new ModelAndView("mobile-reportHome");
	}
	
	@RequestMapping(value = "/balanceSheet", method = RequestMethod.GET)
	public ModelAndView getBalanceSheet(
			HttpServletRequest request,
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			@ModelAttribute(value = "balanceSheetForm") BalanceSheetForm form, BindingResult errors) {
		ModelAndView mav = new ModelAndView("mobile-balanceSheet");
		ReportOperationResponse response = jsonController.getBalanceSheet(request, logged, activeAccountSystem, form, errors);
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET)
	public ModelAndView getAccountStatementForm(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem) {
		AccountOperationResponse aor = accountController.getLeavesAccounts(logged, activeAccountSystem.getRootAccount().getId());
		ReportOperationResponse ror = new ReportOperationResponse(true);
		ror.setAccountList(aor.getLeafAccounts());
		ModelAndView mav = new ModelAndView("mobile-statement");
		mav.getModelMap().addAttribute("response", ror);
		return mav;
	}
	
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET, params={"accountId", "begin", "end"})
	public ModelAndView getAccountStatement(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			Long accountId,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end,
			@ModelAttribute(value="statementAccountId") Long sessionAccountId,
			@ModelAttribute(value="statementBeginDate") @DateTimeFormat(pattern="dd/MM/yyyy") Date sessionBegin,
			@ModelAttribute(value="statementEndDate") @DateTimeFormat(pattern="dd/MM/yyyy") Date sessionEnd) {
		
		begin = begin == null ? sessionBegin : begin;
		end = end == null ? sessionEnd : end;
		accountId = accountId == null ? sessionAccountId : accountId;
		
		ModelAndView mav = new ModelAndView("mobile-statement");
		AccountOperationResponse aor = accountController.getLeavesAccounts(logged, activeAccountSystem.getRootAccount().getId());
		ReportOperationResponse ror = jsonController.getAccountStatement(logged, activeAccountSystem, accountId, begin, end);
		ror.setAccountList(aor.getLeafAccounts());
		mav.getModelMap().addAttribute("response", ror);
		
		//save form input on session
		mav.getModelMap().addAttribute("formAccountId", accountId);
		mav.getModelMap().addAttribute("statementBeginDate", begin);
		mav.getModelMap().addAttribute("statementEndDate", end);
		
		return mav;
	}
	
	@ModelAttribute("statementBeginDate")
	public Date defaultBeginDateFilterDate(@DateTimeFormat(pattern="dd/MM/yyyy") Date begin) {
		return new DateUtils().firstDayOfCurrentMonth();
	}
	
	@ModelAttribute("statementEndDate")
	public Date defaultEndDateFilterDate(@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {
		return new DateUtils().lastDayOfCurrentMonth();
	}
	
	@ModelAttribute("statementAccountId")
	public Long initStatementAccountId() {
		return null;
	}
	
}
