package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.Status;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.dto.CustomerDTO;
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

public class CustomerForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField identityNumber = new TextField("Identity number");
    TextField nationality = new TextField("Nationality");
    TextField address = new TextField("Address");
    ComboBox<BranchDTO> branch = new ComboBox<>();
    ComboBox<String> status = new ComboBox<>();

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    Binder<CustomerDTO> binder = new BeanValidationBinder<>(CustomerDTO.class);

    public CustomerForm(List<BranchDTO> branches) {

        binder.bindInstanceFields(this);

        branch.setLabel("Branch");
        branch.setItems(branches);
        branch.setItemLabelGenerator(BranchDTO::getBranchName);

        status.setLabel("Status");
        status.setItems(Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());

        add(firstName, lastName, identityNumber, nationality, address, branch, status, actionButtonsLayout());
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

    public void setCustomer(CustomerDTO customerDTO) {
        binder.setBean(customerDTO);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }


    //events

    @Getter
    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {

        private final CustomerDTO customerDTO;

        public CustomerFormEvent(CustomerForm source, CustomerDTO customerDTO) {
            super(source, false);
            this.customerDTO = customerDTO;
        }

    }

    public static class SaveEvent extends CustomerFormEvent {

        public SaveEvent(CustomerForm source, CustomerDTO customerDTO) {
            super(source, customerDTO);
        }
    }

    public static class DeleteEvent extends CustomerFormEvent {

        public DeleteEvent(CustomerForm source, CustomerDTO customerDTO) {
            super(source, customerDTO);
        }
    }
}
