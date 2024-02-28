package com.nexusbank.ui;

import com.nexusbank.dto.BasicDTO;
import com.nexusbank.dto.CustomerDTO;
import com.nexusbank.service.branch.BranchService;
import com.nexusbank.service.customer.CustomerFacadeService;
import com.nexusbank.service.customer.CustomerService;
import com.nexusbank.ui.form.CustomerForm;
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
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "customers", layout = MainLayout.class)
@PageTitle("Customers | Nexus Bank")
public class CustomerLayout extends VerticalLayout {

    Grid<CustomerDTO> grid = new Grid<>(CustomerDTO.class);
    TextField filterText = new TextField();
    CustomerForm form;

    CustomerFacadeService customerFacadeService;
    CustomerService customerService;
    BranchService branchService;

    public CustomerLayout(CustomerFacadeService customerFacadeService, CustomerService customerService, BranchService branchService) {
        this.customerFacadeService = customerFacadeService;
        this.customerService = customerService;
        this.branchService = branchService;
        setSizeFull();

        configureGrid();
        add(new H2("All customers"), getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button newAccountButton = new Button("New customer");
        newAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAccountButton.addClickListener(a -> form.setCustomer(new CustomerDTO()));


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
        grid.setColumns("firstName", "lastName", "identityNumber", "nationality", "address");
        grid.addColumn(customer -> customer.getBranch().getBranchName()).setHeader("Branch");
        grid.addColumn(BasicDTO::getStatus).setHeader("Status");
        grid.addColumn(customer -> "Action").setHeader("Actions").setRenderer(new ComponentRenderer<>(item -> {
            Button button = new Button("details");
            button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            return button;
        }));
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editAccount(e.getValue()));
    }

    private void configureForm() {
        form = new CustomerForm(branchService.findAll(null));
        form.setWidth("25rem");
        form.addSaveListener(e -> {
            customerFacadeService.create(e.getCustomerDTO());
            updateList();
        });
        form.addDeleteListener(e -> {
            customerService.delete(e.getCustomerDTO());
            updateList();
        });
    }

    private void updateList() {
        grid.setItems(customerService.findAll(filterText.getValue()));
        form.setCustomer(new CustomerDTO());
    }

    private void editAccount(CustomerDTO customerDTO) {
        form.setCustomer(customerDTO);
    }
}
