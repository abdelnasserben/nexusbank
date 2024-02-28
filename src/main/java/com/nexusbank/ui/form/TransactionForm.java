package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.Currency;
import com.nexusbank.constant.Status;
import com.nexusbank.constant.TransactionType;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.dto.TransactionDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class TransactionForm extends FormLayout {
    ComboBox<String> transactionType = new ComboBox<>();
    TextField initiatorAccountNameField = new TextField("Initiator Acc name");
    TextField initiatorAccountNumberField = new TextField("Initiator Acc number");
    TextField beneficiaryAccountNameField = new TextField("Beneficiary Acc name");
    TextField beneficiaryAccountNumberField = new TextField("Beneficiary Acc number");
    ComboBox<String> baseCurrencyField = new ComboBox<>("Base Curr");
    TextField baseAmountField = new TextField("Amount Equiv");
    NumberField amount = new NumberField("Amount");
    ComboBox<String> currency = new ComboBox<>();
    ComboBox<BranchDTO> branch = new ComboBox<>();
    ComboBox<String> status = new ComboBox<>();

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    Binder<TransactionDTO> binder = new BeanValidationBinder<>(TransactionDTO.class);

    public TransactionForm(List<BranchDTO> branches) {

        binder.bindInstanceFields(this);

        transactionType.setLabel("Transaction type");
        transactionType.setItems(Arrays.stream(TransactionType.values())
                .map(Enum::name)
                .toList());
        transactionType.addValueChangeListener(e -> beneficiaryAccountNumberField.setReadOnly(e.getValue() != null && !e.getValue().equals(TransactionType.TRANSFER.name())));

        initiatorAccountNumberField.addValueChangeListener(e -> fireEvent(new InitiatorAccountNumberFieldEvent(this, e.getValue())));
        initiatorAccountNumberField.setValueChangeMode(ValueChangeMode.LAZY);
        initiatorAccountNameField.setValue("Undefined");
        initiatorAccountNameField.setReadOnly(true);
        HorizontalLayout initiatorLayout = new HorizontalLayout(initiatorAccountNumberField, initiatorAccountNameField);

        beneficiaryAccountNumberField.addValueChangeListener(e -> fireEvent(new BeneficiaryAccountNumberFieldEvent(this, e.getValue())));
        beneficiaryAccountNumberField.setValueChangeMode(ValueChangeMode.LAZY);
        beneficiaryAccountNumberField.setReadOnly(true);
        beneficiaryAccountNameField.setValue("Undefined");
        beneficiaryAccountNameField.setReadOnly(true);
        HorizontalLayout beneficiaryLayout = new HorizontalLayout(beneficiaryAccountNumberField, beneficiaryAccountNameField);

        baseCurrencyField.setItems(Arrays.stream(Currency.values())
                .map(Enum::name)
                .toList());
        baseCurrencyField.setValue("KMF");
        baseCurrencyField.setReadOnly(true);
        HorizontalLayout currencyConversionLayout = new HorizontalLayout(baseCurrencyField, baseAmountField);

        currency.setLabel("Currency");
        currency.setItems(Arrays.stream(Currency.values())
                .map(Enum::name)
                .toList());
        currency.addValueChangeListener(e -> {
            Div amountSuffix = new Div();
            amountSuffix.setText(e.getValue());
            amount.setSuffixComponent(amountSuffix);
        });

        HorizontalLayout amountCurrencyLayout = new HorizontalLayout(currency, amount);
        amountCurrencyLayout.setWidthFull();

        branch.setLabel("Branch");
        branch.setItems(branches);
        branch.setItemLabelGenerator(BranchDTO::getBranchName);

        status.setLabel("Status");
        status.setItems(Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());

        HorizontalLayout branchStatusLayout = new HorizontalLayout(branch, status);

        add(transactionType, initiatorLayout, beneficiaryLayout, amountCurrencyLayout, currencyConversionLayout, branchStatusLayout, actionButtonsLayout());
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

    public void setTransaction(TransactionDTO transactionDTO) {
        binder.setBean(transactionDTO);
    }

    public void setInitiatorAccount(AccountDTO accountDTO) {
        if (accountDTO != null) {
            initiatorAccountNameField.setValue(accountDTO.getAccountName());
            binder.getBean().setInitiatorAccount(accountDTO);

        } else initiatorAccountNameField.setValue("Undefined");
    }

    public void setBeneficiaryAccount(AccountDTO accountDTO) {
        if (accountDTO != null) {
            beneficiaryAccountNameField.setValue(accountDTO.getAccountName());
            binder.getBean().setReceiverAccount(accountDTO);

        } else beneficiaryAccountNameField.setValue("Undefined");
    }

    public void reset() {
        initiatorAccountNumberField.setValue("");
        beneficiaryAccountNumberField.setValue("");
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }

    public void addInitiatorAccountFieldListener(ComponentEventListener<InitiatorAccountNumberFieldEvent> listener) {
        addListener(InitiatorAccountNumberFieldEvent.class, listener);
    }

    public void addBeneficiaryAccountFieldListener(ComponentEventListener<BeneficiaryAccountNumberFieldEvent> listener) {
        addListener(BeneficiaryAccountNumberFieldEvent.class, listener);
    }


    //events

    @Getter
    public static abstract class TransactionFormEvent extends ComponentEvent<TransactionForm> {

        private final TransactionDTO transactionDTO;

        public TransactionFormEvent(TransactionForm source, TransactionDTO transactionDTO) {
            super(source, false);
            this.transactionDTO = transactionDTO;
        }

    }

    public static class SaveEvent extends TransactionFormEvent {

        public SaveEvent(TransactionForm source, TransactionDTO transactionDTO) {
            super(source, transactionDTO);
        }
    }

    public static class DeleteEvent extends TransactionFormEvent {

        public DeleteEvent(TransactionForm source, TransactionDTO transactionDTO) {
            super(source, transactionDTO);
        }
    }

    @Getter
    public static abstract class TransactionFormFieldEvent extends ComponentEvent<TransactionForm> {

        private final String value;

        public TransactionFormFieldEvent(TransactionForm source, String value) {
            super(source, false);
            this.value = value;
        }
    }

    public static class InitiatorAccountNumberFieldEvent extends TransactionFormFieldEvent {

        public InitiatorAccountNumberFieldEvent(TransactionForm source, String value) {
            super(source, value);
        }
    }

    public static class BeneficiaryAccountNumberFieldEvent extends TransactionFormFieldEvent {

        public BeneficiaryAccountNumberFieldEvent(TransactionForm source, String value) {
            super(source, value);
        }
    }
}
