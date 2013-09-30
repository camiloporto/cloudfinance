package br.com.camiloporto.cloudfinance.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
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
	public static final File TRANSACTION_UI_FORM_DATA = new File("src/test/resources/br/com/camiloporto/cloudfinance/ui/mobile/TransactionFormTestData");
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private Account rootAccount;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private AccountSystem accountSystem;
	
	public DataInsertionHelper(AccountSystem accountSystem) {
		this.accountSystem = accountSystem;
		this.rootAccount = accountSystem.getRootAccount();
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
		
		Account origin = accountRepository.findByNameAndRootAccount(originAccountName, this.rootAccount);
		Account dest = accountRepository.findByNameAndRootAccount(destAccountName, this.rootAccount);
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
	
	public List<String[]> getDataAsArray(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(file)).useDelimiter("\\n");
		List<String[]> result = new ArrayList<String[]>();
		while(scanner.hasNext()) {
			String lineRead = scanner.next();
			if(lineRead.startsWith("#")) {
				continue;//ignore commented lines
			}
			result.add(lineRead.replace("\n", "").split(";"));
		}
		return result;
	}

	private void insertAccount(Profile p, String[] fields) {
		String fatherName = fields[PARENT_NAME_INDEX].trim();
		String accountName = fields[ACCOUNT_NAME_INDEX].trim();
		Account fatherAccount = accountRepository.findByNameAndRootAccount(fatherName, this.rootAccount);
		Account a = new Account(accountName, fatherAccount);
		a.setRootAccount(rootAccount);
		accountManager.saveAccount(p, a, accountSystem);
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
