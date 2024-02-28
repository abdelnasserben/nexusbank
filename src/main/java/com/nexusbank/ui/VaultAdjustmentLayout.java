package com.nexusbank.ui;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.AdjustmentType;
import com.nexusbank.constant.Currency;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.dto.VaultAdjustmentDTO;
import com.nexusbank.service.account.AccountOperationService;
import com.nexusbank.service.account.AccountService;
import com.nexusbank.service.branch.BranchFacadeService;
import com.nexusbank.ui.form.VaultAdjustmentForm;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed(value = "ADMIN")
@Route(value = "adjustments", layout = MainLayout.class)
public class VaultAdjustmentLayout extends VerticalLayout {

    ComboBox<BranchDTO> branchComboBox = new ComboBox<>("Branch");
    VaultAdjustmentForm formEur = new VaultAdjustmentForm();
    VaultAdjustmentForm formUsd = new VaultAdjustmentForm();
    VaultAdjustmentForm formKmf = new VaultAdjustmentForm();

    BranchFacadeService branchService;
    AccountService accountService;
    AccountOperationService accountOperationService;

    public VaultAdjustmentLayout(BranchFacadeService branchService, AccountService accountService, AccountOperationService accountOperationService) {
        this.branchService = branchService;
        this.accountService = accountService;
        this.accountOperationService = accountOperationService;

        setSizeFull();

        branchComboBox.setItems(branchService.findAll());
        branchComboBox.setItemLabelGenerator(BranchDTO::getBranchName);
        branchComboBox.addValueChangeListener(e -> updateVaults());

        add(branchComboBox, initVaultForms());
    }

    private VerticalLayout initVaultForms() {
        resetForms();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        layout.add(formEur, formUsd, formKmf);

        formEur.addSaveListener(e -> adjustVault(e.getVaultAdjustmentDTO()));
        formUsd.addSaveListener(e -> adjustVault(e.getVaultAdjustmentDTO()));
        formKmf.addSaveListener(e -> adjustVault(e.getVaultAdjustmentDTO()));

        return layout;
    }

    private void adjustVault(VaultAdjustmentDTO vault) {
        switch (AdjustmentType.valueOf(vault.getOperationType())) {
            case DEBIT -> accountOperationService.debit(vault.getAccount(), vault.getAmount());
            case CREDIT -> accountOperationService.credit(vault.getAccount(), vault.getAmount());
        }

        resetForms();
        branchComboBox.setValue(null);
        CustomNotification.show("Adjustment successfully", NotificationVariant.LUMO_SUCCESS);
    }

    private void resetForms() {
        formEur.reset();
        formUsd.reset();
        formKmf.reset();
    }

    private void updateVaults() {
        if (branchComboBox.getValue() != null) {
            AccountDTO vaultEur = accountService.findVault(branchComboBox.getValue(), Currency.EUR.name());
            AccountDTO vaultUsd = accountService.findVault(branchComboBox.getValue(), Currency.USD.name());
            AccountDTO vaultKmf = accountService.findVault(branchComboBox.getValue(), Currency.KMF.name());

            formEur.setVault(vaultEur);
            formUsd.setVault(vaultUsd);
            formKmf.setVault(vaultKmf);
        }
    }
}
