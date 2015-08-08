package com.example.tutorial.pages;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import uk.q3c.krail.core.view.Grid3x3ViewBase;
import uk.q3c.krail.core.view.component.AfterViewChangeBusMessage;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;

public class ContactDetailView extends Grid3x3ViewBase {
    private Label idLabel;
    private Label nameLabel;

    @Override
    protected void doBuild(ViewChangeBusMessage busMessage) {
        super.doBuild(busMessage);
        idLabel = new Label();
        idLabel.setCaption("id");
        nameLabel = new Label();
        nameLabel.setCaption("name");
        setCentreCell(new FormLayout(idLabel, nameLabel));
    }

    @Override
    protected void loadData(AfterViewChangeBusMessage busMessage) {
        idLabel.setValue(busMessage.getToState()
                                   .getParameterValue("id"));
        nameLabel.setValue(busMessage.getToState()
                                     .getParameterValue("name"));
    }
}