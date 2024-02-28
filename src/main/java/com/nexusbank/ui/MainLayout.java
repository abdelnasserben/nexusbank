package com.nexusbank.ui;

import com.nexusbank.service.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        addToNavbar(getHeader());
        addToDrawer(getNav());
    }

    private HorizontalLayout getHeader() {
        H1 logo = new H1("Nexus Bank");
        logo.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Margin.MEDIUM);

        String loggedUsername = securityService.getLoggedUser().getUsername();
        Button logoutButton = new Button("Logout, " + loggedUsername, e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutButton);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        return header;
    }

//    private VerticalLayout getSideNav() {
//        SideNav generalNav = new SideNav();
//        generalNav.setLabel("General");
//        generalNav.addItem(new SideNavItem("Dashboard", DashboardView.class));
//        generalNav.addItem(new SideNavItem("Branches", BranchView.class));
//        generalNav.addItem(new SideNavItem("Accounts", AccountView.class));
//        generalNav.addItem(new SideNavItem("Customers", CustomerView.class));
//        generalNav.addItem(new SideNavItem("Transactions", TransactionView.class));
//
//        SideNav adminNav = new SideNav();
//        adminNav.setLabel("Admin");
//        adminNav.setCollapsible(true);
//        adminNav.addItem(new SideNavItem("Branches", BranchView.class));
//        adminNav.addItem(new SideNavItem("Accounts", AccountView.class));
//        adminNav.addItem(new SideNavItem("Customers", CustomerView.class));
//
//        return new VerticalLayout(generalNav, adminNav);
//    }

    private VerticalLayout getNav() {
        return new VerticalLayout(
                new RouterLink("Dashboard", DashboardLayout.class),
                new RouterLink("Branches", BranchLayout.class),
                new RouterLink("Accounts", AccountLayout.class),
                new RouterLink("Customers", CustomerLayout.class),
                new RouterLink("Transactions", TransactionLayout.class),
                new RouterLink("Users", UserLayout.class),
                new RouterLink("Vault adjustment", VaultAdjustmentLayout.class));

    }

}
