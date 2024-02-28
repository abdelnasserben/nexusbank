package com.nexusbank.ui.form;

import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.Status;
import com.nexusbank.dto.BranchDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public abstract class CustomForm<T> extends FormLayout {

    final Class<T> classOfBeanParameter;

    ComboBox<BranchDTO> branch = new ComboBox<>();
    ComboBox<String> status = new ComboBox<>();

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    Binder<T> binder;

    public CustomForm(List<BranchDTO> branches, Class<T> classOfBeanParameter) {
        this.classOfBeanParameter = classOfBeanParameter;

        binder = new BeanValidationBinder<>(this.classOfBeanParameter);
        binder.bindInstanceFields(this);

        branch.setLabel("Branch");
        branch.setItems(branches);
        branch.setItemLabelGenerator(BranchDTO::getBranchName);

        status.setLabel("Status");
        status.setItems(Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());

        add(fields(), branch, status, actionButtonsLayout());
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

    public void setBean(T t) {
        binder.setBean(t);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
//        addListener(SaveEvent.class, listener);
    }

    public void addDeleteListener() {

    }

    abstract Component fields();


    //events

    @Getter
    public abstract static class CustomFormEvent<T> extends ComponentEvent<CustomForm<T>> {

        private final T value;

        public CustomFormEvent(CustomForm source, T value) {
            super(source, false);
            this.value = value;
        }

    }

    public class SaveEvent extends CustomFormEvent<T> {
        public SaveEvent(CustomForm source, T t) {
            super(source, t);
        }
    }

    public class DeleteEvent extends CustomFormEvent<T> {
        public DeleteEvent(CustomForm source, T t) {
            super(source, t);
        }
    }
}