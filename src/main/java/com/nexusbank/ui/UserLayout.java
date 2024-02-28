package com.nexusbank.ui;

import com.nexusbank.dto.UserDTO;
import com.nexusbank.service.branch.BranchService;
import com.nexusbank.service.security.UserService;
import com.nexusbank.ui.form.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed(value = "ADMIN")
@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users | Nexus Bank")
public class UserLayout extends VerticalLayout {

    Grid<UserDTO> grid = new Grid<>(UserDTO.class);
    TextField filterText = new TextField();
    UserForm form;

    UserService userService;
    BranchService branchService;

    public UserLayout(UserService userService, BranchService branchService) {
        this.userService = userService;
        this.branchService = branchService;
        setSizeFull();

        configureGrid();
        add(new H2("All users"), getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button newAccountButton = new Button("New Account");
        newAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAccountButton.addClickListener(a -> form.setUser(new UserDTO()));


        var toolbar = new HorizontalLayout(filterText, newAccountButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return toolbar;
    }

    private HorizontalLayout getContent() {
        configureGrid();
        configureForm();

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();

        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("username", "enabled", "authorities");
        grid.addColumn(user -> user.getBranch().getBranchName()).setHeader("Branch");
        grid.addColumn(user -> "Action").setHeader("Actions").setRenderer(new ComponentRenderer<>(item -> {
            Button button = new Button("details", event -> getUI().ifPresent(ui -> ui.navigate(DashboardLayout.class)));
            button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            return button;
        }));
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editAccount(e.getValue()));
    }

    private void configureForm() {
        form = new UserForm(branchService.findAll(null));
        form.setWidth("25rem");
        form.addSaveListener(e -> {
            userService.create(e.getUserDTO());
            updateList();
        });
        form.addDeleteListener(e -> {
            userService.delete(e.getUserDTO());
            updateList();
        });
    }

    private void updateList() {
        grid.setItems(userService.findAll(filterText.getValue()));
        form.setUser(new UserDTO());
    }

    private void editAccount(UserDTO userDTO) {
        form.setUser(userDTO);
    }

}
