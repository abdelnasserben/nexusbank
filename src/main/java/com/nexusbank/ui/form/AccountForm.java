package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.AccountProfile;
import com.nexusbank.constant.AccountType;
import com.nexusbank.constant.Currency;
import com.nexusbank.constant.Status;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.BranchDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class AccountForm extends FormLayout {
    TextField accountName = new TextField("Account name");
    TextField accountNumber = new TextField("Account number");
    ComboBox<String> accountType = new ComboBox<>();
    ComboBox<String> accountProfile = new ComboBox<>();
    ComboBox<String> currency = new ComboBox<>();
    ComboBox<BranchDTO> branch = new ComboBox<>();
    ComboBox<String> status = new ComboBox<>();

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    Binder<AccountDTO> binder = new BeanValidationBinder<>(AccountDTO.class);

    public AccountForm(List<BranchDTO> branches) {

        binder.bindInstanceFields(this);

        accountType.setLabel("Account type");
        accountType.setItems(Arrays.stream(AccountType.values())
                .map(Enum::name).toList());

        accountProfile.setLabel("Account profile");
        accountProfile.setItems(Arrays.stream(AccountProfile.values())
                .map(Enum::name)
                .toList());

        currency.setLabel("Currency");
        currency.setItems(Arrays.stream(Currency.values())
                .map(Enum::name)
                .toList());

        branch.setLabel("Branch");
        branch.setItems(branches);
        branch.setItemLabelGenerator(BranchDTO::getBranchName);

        status.setLabel("Status");
        status.setItems(Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());

        add(accountName, accountNumber, accountType, accountProfile, currency, branch, status, actionButtonsLayout());
    }

    private HorizontalLayout actionButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(e -> validateAndSave());

        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, binder.getBean())));

        return new HorizontalLayout(save, delete);
    }

    private void validateAndSave() {
        if (binder.isValid())
            fireEvent(new SaveEvent(this, binder.getBean()));
        else CustomNotification.show("Incorrect values", NotificationVariant.LUMO_ERROR);
    }


    public void setAccount(AccountDTO accountDTO) {
        binder.setBean(accountDTO);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }


    //events

    @Getter
    public static abstract class AccountFormEvent extends ComponentEvent<AccountForm> {

        private final AccountDTO accountDTO;

        public AccountFormEvent(AccountForm source, AccountDTO accountDTO) {
            super(source, false);
            this.accountDTO = accountDTO;
        }

    }

    public static class SaveEvent extends AccountFormEvent {

        public SaveEvent(AccountForm source, AccountDTO accountDTO) {
            super(source, accountDTO);
        }
    }

    public static class DeleteEvent extends AccountFormEvent {

        public DeleteEvent(AccountForm source, AccountDTO accountDTO) {
            super(source, accountDTO);
        }
    }
}
