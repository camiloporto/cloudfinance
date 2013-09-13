package br.com.camiloporto.cloudfinance.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

@Configurable
public class DataInsertionHelper {
	private final int PARENT_NAME_INDEX = 0;
	private final int ACCOUNT_NAME_INDEX = 1;
	
	private final int TRANSACTION_FROM_INDEX = 0;
	private final int TRANSACTION_TO_INDEX = 1;
	private final int TRANSACTION_DATE_INDEX = 2;
	private final int TRANSACTION_AMOUNT_INDEX = 3;
	private final int TRANSACTION_DESCRIPTION_INDEX = 4;
	
	public static final File ACCOUNT_DATA = new File("src/test/resources/br/com/camiloporto/cloudfinance/service/AccountInsertionTestData");
	public static final File TRANSACTION_DATA = new File("src/test/resources/br/com/camiloporto/cloudfinance/service/TransactionInsertionTestData");
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private Account rootAccount;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public DataInsertionHelper(Account rootAccount) {
		this.rootAccount = rootAccount;
	}
	
	public void insertTransactionsFromFile(Profile p, File file) throws IOException, ParseException {
		List<String[]> records = scanFile(file, ";");
		for (String[] fields : records) {
			insertTransaction(p, fields);
		}
	}
	
	private void insertTransaction(Profile p, String[] fields) throws ParseException {
		String originAccountName = fields[TRANSACTION_FROM_INDEX].trim();
		String destAccountName = fields[TRANSACTION_TO_INDEX].trim();
		String dateStr = fields[TRANSACTION_DATE_INDEX].trim();
		String amount = fields[TRANSACTION_AMOUNT_INDEX].trim();
		String description = fields[TRANSACTION_DESCRIPTION_INDEX].trim();
		
		Account origin = accountRepository.findByName(originAccountName);
		Account dest = accountRepository.findByName(destAccountName);
		Date date = simpleDateFormat.parse(dateStr);
		BigDecimal value = new BigDecimal(amount);
		
		AccountTransaction saveAccountTransaction = transactionManager.saveAccountTransaction(p, origin.getId(), dest.getId(), date, value, description);
	}

	public void insertAccountsFromFile(Profile p, File file) throws IOException {
		List<String[]> records = scanFile(file, ";");
		for (String[] fields : records) {
			insertAccount(p, fields);
		}
	}

	private void insertAccount(Profile p, String[] fields) {
		String fatherName = fields[PARENT_NAME_INDEX].trim();
		String accountName = fields[ACCOUNT_NAME_INDEX].trim();
		Account fatherAccount = accountRepository.findByName(fatherName);
		Account a = new Account(accountName, fatherAccount);
		a.setRootAccount(rootAccount);
		accountManager.saveAccount(p, a);
	}

	private List<String[]> scanFile(File file, final String fieldPatternSeparator) throws IOException {
		BufferedReader in = new BufferedReader(
				new FileReader(file));
		List<String[]> result = new ArrayList<String[]>(); 
		try {
			while (in.ready()) {
				String nextLine = in.readLine();
				if(nextLine.startsWith("#")) {
					continue;
				}
				String[] fields = nextLine.split(fieldPatternSeparator);
				result.add(fields);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			in.close();
		}
		
		return result;
	}

}
