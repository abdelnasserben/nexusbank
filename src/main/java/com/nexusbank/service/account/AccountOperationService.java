package com.nexusbank.service.account;

import com.nexusbank.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class AccountOperationService {

    @Autowired
    AccountService accountService;

    public void debit(AccountDTO account, double amount) {
        account.setBalance(account.getBalance() - AmountFormatter.format(amount));
        accountService.save(account);
    }

    public void credit(AccountDTO account, double amount) {
        account.setBalance(account.getBalance() + AmountFormatter.format(amount));
        accountService.save(account);
    }

    private static class AmountFormatter {
        public static double format(double amount) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            return Double.parseDouble(decimalFormat.format(amount));
        }
    }
}
