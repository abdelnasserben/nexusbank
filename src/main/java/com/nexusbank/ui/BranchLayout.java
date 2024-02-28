package com.nexusbank.ui;

import com.nexusbank.dto.BranchDTO;
import com.nexusbank.service.branch.BranchFacadeService;
import com.nexusbank.service.branch.BranchService;
import com.nexusbank.ui.form.BranchForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed(value = "ADMIN")
@Route(value = "branches", layout = MainLayout.class)
@PageTitle("Branches | Nexus Bank")
public class BranchLayout extends VerticalLayout {

    Grid<BranchDTO> grid = new Grid<>(BranchDTO.class);
    TextField filterText = new TextField();
    BranchForm form;

    BranchFacadeService branchFacadeService;
    BranchService branchService;

    public BranchLayout(BranchFacadeService branchFacadeService, BranchService branchService) {
        this.branchFacadeService = branchFacadeService;
        this.branchService = branchService;
        setSizeFull();

        configureGrid();
        add(new H2("All branches"), getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button newAccountButton = new Button("New Branch");
        newAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAccountButton.addClickListener(a -> form.setBranch(new BranchDTO()));

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
        grid.setColumns("branchId", "branchName", "branchAddress");
        grid.addColumn(BranchDTO::getStatus).setHeader("Status");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editBranch(e.getValue()));
    }

    private void configureForm() {
        form = new BranchForm();
        form.setWidth("25rem");
        form.addSaveListener(e -> {
            branchFacadeService.create(e.getBranchDTO());
            updateList();
        });
        form.addDeleteListener(e -> {
            branchService.delete(e.getBranchDTO());
            updateList();
        });
    }

    private void updateList() {
        grid.setItems(branchService.findAll(filterText.getValue()));
        form.setBranch(new BranchDTO());
    }

    private void editBranch(BranchDTO branchDTO) {
        form.setBranch(branchDTO);
    }
}
