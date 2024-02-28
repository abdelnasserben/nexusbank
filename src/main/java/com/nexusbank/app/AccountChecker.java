package com.nexusbank.app;

import com.nexusbank.constant.Status;
import com.nexusbank.dto.AccountDTO;

public final class AccountChecker {

    public static boolean isActive(AccountDTO accountDTO) {
        return accountDTO.getStatus().equals(Status.ACTIVE.name());
    }

}
