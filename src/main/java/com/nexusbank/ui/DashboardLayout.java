package com.nexusbank.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Nexus Bank")
public class DashboardLayout extends VerticalLayout {

    public DashboardLayout() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(new H1("Dashboard page"));
    }


}
