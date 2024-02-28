package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.AdjustmentType;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.VaultAdjustmentDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

import java.util.Arrays;


public class VaultAdjustmentForm extends FormLayout {

    TextField accountName = new TextField("Vault name");
    NumberField currentBalance = new NumberField("Balance");
    NumberField amount = new NumberField("Amount");
    ComboBox<String> operationType = new ComboBox<>("Operation");

    Button save = new Button("Apply");

    Binder<VaultAdjustmentDTO> binder = new BeanValidationBinder<>(VaultAdjustmentDTO.class);

    public VaultAdjustmentForm() {
        binder.bindInstanceFields(this);

        accountName.setReadOnly(true);
        accountName.setValue("Undefined");
        currentBalance.setReadOnly(true);
        operationType.setItems(Arrays.stream(AdjustmentType.values())
                .map(Enum::name)
                .toList());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> validAndSave());

        HorizontalLayout layout = new HorizontalLayout(accountName, currentBalance, amount, operationType, save);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        add(layout);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void setVault(AccountDTO accountDTO) {
        binder.getBean().setAccount(accountDTO);
        accountName.setValue(accountDTO.getAccountName().isEmpty() ? "Undefined" : accountDTO.getAccountName());
        currentBalance.setValue(accountDTO.getBalance() != 0 ? accountDTO.getBalance() : 0);
    }

    private void validAndSave() {
        if (binder.isValid())
            fireEvent(new SaveEvent(this, binder.getBean()));
        else CustomNotification.show("Incorrect values", NotificationVariant.LUMO_ERROR);
    }

    public void reset() {
        binder.setBean(new VaultAdjustmentDTO());
    }

    @Getter
    public static class SaveEvent extends ComponentEvent<VaultAdjustmentForm> {
        private final VaultAdjustmentDTO vaultAdjustmentDTO;

        public SaveEvent(VaultAdjustmentForm form, VaultAdjustmentDTO vaultAdjustmentDTO) {
            super(form, false);
            this.vaultAdjustmentDTO = vaultAdjustmentDTO;
        }
    }
}
