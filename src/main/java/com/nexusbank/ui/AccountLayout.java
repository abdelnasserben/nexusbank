package com.nexusbank.ui;

import com.nexusbank.dto.AccountDTO;
import com.nexusbank.service.account.AccountService;
import com.nexusbank.service.branch.BranchService;
import com.nexusbank.ui.form.AccountForm;
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
@Route(value = "accounts", layout = MainLayout.class)
@PageTitle("Accounts | Nexus Bank")
public class AccountLayout extends VerticalLayout {

    Grid<AccountDTO> grid = new Grid<>(AccountDTO.class);
    TextField filterText = new TextField();
    AccountForm form;

    AccountService accountService;
    BranchService branchService;

    public AccountLayout(AccountService accountService, BranchService branchService) {
        this.accountService = accountService;
        this.branchService = branchService;
        setSizeFull();

        configureGrid();
        add(new H2("All accounts"), getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button newAccountButton = new Button("New Account");
        newAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAccountButton.addClickListener(a -> form.setAccount(new AccountDTO()));


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
        grid.setColumns("accountName", "accountNumber", "accountType", "accountProfile", "balance", "currency", "status");
        grid.addColumn(account -> account.getBranch().getBranchName()).setHeader("Branch");
        grid.addColumn(account -> "Action").setHeader("Actions").setRenderer(new ComponentRenderer<>(item -> {
            Button button = new Button("details", event -> getUI().ifPresent(ui -> ui.navigate(DashboardLayout.class)));
            button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            return button;
        }));
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editAccount(e.getValue()));
    }

    private void configureForm() {
        form = new AccountForm(branchService.findAll(null));
        form.setWidth("25rem");
        form.addSaveListener(e -> {
            accountService.save(e.getAccountDTO());
            updateList();
        });
        form.addDeleteListener(e -> {
            accountService.delete(e.getAccountDTO());
            updateList();
        });
    }

    private void updateList() {
        grid.setItems(accountService.findAll(filterText.getValue()));
        form.setAccount(new AccountDTO());
    }

    private void editAccount(AccountDTO accountDTO) {
        form.setAccount(accountDTO);
    }

}
