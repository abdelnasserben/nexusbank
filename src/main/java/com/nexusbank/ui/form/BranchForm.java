package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.Status;
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

public class BranchForm extends FormLayout {
    TextField branchName = new TextField("Branch Name");
    TextField branchAddress = new TextField("Address");
    ComboBox<String> status = new ComboBox<>();

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    Binder<BranchDTO> binder = new BeanValidationBinder<>(BranchDTO.class);

    public BranchForm() {

        binder.bindInstanceFields(this);

        status.setLabel("Status");
        status.setItems(Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());

        add(branchName, branchAddress, status, actionButtonsLayout());
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

    public void setBranch(BranchDTO branchDTO) {
        binder.setBean(branchDTO);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }


    //events

    @Getter
    public static abstract class BranchFormEvent extends ComponentEvent<BranchForm> {

        private final BranchDTO branchDTO;

        public BranchFormEvent(BranchForm source, BranchDTO branchDTO) {
            super(source, false);
            this.branchDTO = branchDTO;
        }

    }

    public static class SaveEvent extends BranchFormEvent {

        public SaveEvent(BranchForm source, BranchDTO branchDTO) {
            super(source, branchDTO);
        }
    }

    public static class DeleteEvent extends BranchFormEvent {

        public DeleteEvent(BranchForm source, BranchDTO branchDTO) {
            super(source, branchDTO);
        }
    }
}
