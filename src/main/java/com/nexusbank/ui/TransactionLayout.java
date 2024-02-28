package com.nexusbank.ui;

import com.nexusbank.dto.BasicDTO;
import com.nexusbank.dto.TransactionDTO;
import com.nexusbank.service.account.AccountService;
import com.nexusbank.service.branch.BranchService;
import com.nexusbank.service.transaction.TransactionFacadeService;
import com.nexusbank.service.transaction.TransactionService;
import com.nexusbank.ui.form.TransactionForm;
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
@Route(value = "transactions", layout = MainLayout.class)
@PageTitle("Transactions | Nexus Bank")
public class TransactionLayout extends VerticalLayout {

    Grid<TransactionDTO> grid = new Grid<>(TransactionDTO.class);
    TextField filterText = new TextField();
    TransactionForm form;

    TransactionFacadeService transactionFacadeService;
    TransactionService transactionService;
    AccountService accountService;
    BranchService branchService;

    public TransactionLayout(TransactionFacadeService transactionFacadeService, TransactionService transactionService, AccountService accountService, BranchService branchService) {
        this.transactionFacadeService = transactionFacadeService;
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.branchService = branchService;
        setSizeFull();

        configureGrid();
        add(new H2("All transactions"), getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button newAccountButton = new Button("Init transaction");
        newAccountButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAccountButton.addClickListener(a -> clearEditor());


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
        grid.setColumns("transactionId", "transactionType", "amount", "currency");
        grid.addColumn(transaction -> transaction.getBranch().getBranchName()).setHeader("Branch");
        grid.addColumn(BasicDTO::getStatus).setHeader("Status");
        grid.addColumn(transaction -> "Action").setHeader("Actions").setRenderer(new ComponentRenderer<>(item -> {
            Button button = new Button("details", event -> getUI().ifPresent(ui -> ui.navigate(DashboardLayout.class)));
            button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            return button;
        }));
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private void configureForm() {
        form = new TransactionForm(branchService.findAll(null));
        form.setWidth("25rem");
        form.addSaveListener(e -> {
            transactionFacadeService.init(e.getTransactionDTO());
            updateList();
        });
        form.addDeleteListener(e -> {
            transactionService.delete(e.getTransactionDTO());
            updateList();
        });
        form.addInitiatorAccountFieldListener(e -> form.setInitiatorAccount(accountService.findTrunk(e.getValue()).getAccount()));
        form.addBeneficiaryAccountFieldListener(e -> form.setBeneficiaryAccount(accountService.findTrunk(e.getValue()).getAccount()));
    }

    private void updateList() {
        grid.setItems(transactionService.findAll());
        ;
        clearEditor();
    }

    private void clearEditor() {
        form.setTransaction(new TransactionDTO());
        form.reset();
    }

}
