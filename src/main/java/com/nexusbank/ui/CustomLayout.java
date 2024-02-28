package com.nexusbank.ui;

import com.nexusbank.dto.AccountDTO;
import com.nexusbank.ui.form.CustomForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Collections;
import java.util.List;

public abstract class CustomLayout<T> extends VerticalLayout {

    protected Grid<T> grid = new Grid<>();
    protected TextField filterText = new TextField();
//    protected CustomForm<T> form;

    private final String textOfButtonNew;
    private final List<String> columnsNames;

    abstract void refreshList();

    abstract void filterChangeEvent();

    abstract void editValue(T value);

    abstract void saveEvent(T value);

    abstract void deleteEvent(T value);


    public CustomLayout(String textOfButtonNew, List<String> columnsNames) {
        this.textOfButtonNew = textOfButtonNew;
        this.columnsNames = columnsNames;
        setSizeFull();

        configureGrid();
        add(new H2("All accounts"), getToolbar(), getContent());
        refreshList();
    }

    private HorizontalLayout getContent() {
        configureGrid();
        configureForm();

        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
//        content.setFlexGrow(1, form);
        content.setSizeFull();

        return content;
    }

    protected HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> filterChangeEvent());

        Button newAccountButton = new Button(textOfButtonNew);
        newAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        newAccountButton.addClickListener(a -> form.setBean(new AccountDTO()));


        var toolbar = new HorizontalLayout(filterText, newAccountButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return toolbar;
    }

    protected void configureGrid() {
        grid.setSizeFull();
        grid.setColumns(columnsNames.toArray(String[]::new));
        grid.addColumn(account -> "Action").setHeader("Actions").setRenderer(new ComponentRenderer<>(item -> {
            Button button = new Button("details", event -> getUI().ifPresent(ui -> ui.navigate(DashboardLayout.class)));
            button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            return button;
        }));
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editValue(e.getValue()));
    }

    protected void configureForm() {
//        form = new CustomForm<>(Collections.emptyList(), classOfBeanParameter);
//        form.setWidth("25rem");
//        form.addSaveListener(e -> saveEvent(e.getValue()));
//        form.addDeleteListener(e -> deleteEvent(e.getValue()));
    }
}
