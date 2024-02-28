package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.dto.UserDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

import java.util.List;

public class UserForm extends FormLayout {
    TextField username = new TextField("Username");
    MultiSelectComboBox<String> authorities = new MultiSelectComboBox<>("Roles");
    ComboBox<Boolean> enabled = new ComboBox<>("Status");
    ComboBox<BranchDTO> branch = new ComboBox<>("Branch");

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    Binder<UserDTO> binder = new BeanValidationBinder<>(UserDTO.class);

    public UserForm(List<BranchDTO> branches) {

        binder.bindInstanceFields(this);

        authorities.setItems(List.of("USER", "ADMIN"));

        branch.setLabel("Branch");
        branch.setItems(branches);
        branch.setItemLabelGenerator(BranchDTO::getBranchName);

        enabled.setLabel("Status");
        enabled.setItems(List.of(true, false));

        add(username, authorities, enabled, branch, actionButtonsLayout());
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

    public void setUser(UserDTO userDTO) {
        binder.setBean(userDTO);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }


    //events

    @Getter
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {

        private final UserDTO userDTO;

        public UserFormEvent(UserForm source, UserDTO userDTO) {
            super(source, false);
            this.userDTO = userDTO;
        }

    }

    public static class SaveEvent extends UserFormEvent {

        public SaveEvent(UserForm source, UserDTO userDTO) {
            super(source, userDTO);
        }
    }

    public static class DeleteEvent extends UserFormEvent {

        public DeleteEvent(UserForm source, UserDTO userDTO) {
            super(source, userDTO);
        }
    }
}
