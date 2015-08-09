package com.example.tutorial.pages;

import com.example.tutorial.i18n.Caption;
import com.example.tutorial.i18n.DescriptionKey;
import com.example.tutorial.i18n.LabelKey;
import com.example.tutorial.i18n.MessageKey;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import uk.q3c.krail.core.eventbus.SessionBus;
import uk.q3c.krail.core.eventbus.SubscribeTo;
import uk.q3c.krail.core.shiro.SubjectProvider;
import uk.q3c.krail.core.user.notify.UserNotifier;
import uk.q3c.krail.core.user.opt.*;
import uk.q3c.krail.core.view.Grid3x3ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.*;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

@Listener
@SubscribeTo(SessionBus.class)
public class MyNews extends Grid3x3ViewBase implements OptionContext {

    public static final OptionKey<Boolean> ceoVisible = new OptionKey<>(true, MyNews.class, LabelKey.CEO_News_Channel);
    public static final OptionKey<Boolean> itemsForSaleVisible = new OptionKey<>(true, MyNews.class, LabelKey.Items_For_Sale_Channel);
    public static final OptionKey<Boolean> vacanciesVisible = new OptionKey<>(true, MyNews.class, LabelKey.Vacancies_Channel);
    private final SubjectProvider subjectProvider;
    private final UserNotifier userNotifier;
    private final Translate translate;
    private final Provider<PatternDao> patternDaoProvider;
    private final PatternSource patternSource;
    private Label bannerLabel;
    private Label ceoNews;
    private TextField i18NTextBox;
    private Label itemsForSale;
    private Option option;
    private OptionPopup optionPopup;
    @Caption(caption = LabelKey.Options, description = DescriptionKey.Select_your_options)
    private Button popupButton;
    private Button submitButton;
    private Button systemOptionButton;
    private Label vacancies;

    @Inject
    public MyNews(Option option, OptionPopup optionPopup, SubjectProvider subjectProvider, UserNotifier userNotifier, Translate translate, @InMemory
    Provider<PatternDao> patternDaoProvider, PatternSource patternSource) {
        this.option = option;
        this.optionPopup = optionPopup;
        this.subjectProvider = subjectProvider;
        this.userNotifier = userNotifier;
        this.translate = translate;
        this.patternDaoProvider = patternDaoProvider;
        this.patternSource = patternSource;
    }

    @Override
    protected void doBuild(ViewChangeBusMessage busMessage) {
        super.doBuild(busMessage);
        ceoNews = new Label("CEO News");
        itemsForSale = new Label("Items for Sale");
        vacancies = new Label("Vacancies");
        ceoNews.setSizeFull();
        itemsForSale.setSizeFull();
        vacancies.setSizeFull();

        popupButton = new Button();
        popupButton.addClickListener(event -> {
            optionPopup.popup(this, LabelKey.News_Options);
        });



        systemOptionButton = new Button("system option");
        systemOptionButton.addClickListener(event -> {
            option.set(false, 1, ceoVisible);
            optionValueChanged(null);
        });
        if (subjectProvider.get()
                           .isPermitted("option:edit:SimpleUserHierarchy:*:1:*:*")) {
            systemOptionButton.setVisible(true);
        } else {
            systemOptionButton.setVisible(false);
        }


        Button payRiseButton = new Button("request a pay rise");
        payRiseButton.addClickListener(event -> requestAPayRise());


        bannerLabel = new Label();


        i18NTextBox = new TextField();
        i18NTextBox.setCaption("enter a value for LabelKey.is_selected");

        PatternCacheKey cacheKeyUK = new PatternCacheKey(LabelKey.is_selected, Locale.UK);
        submitButton = new Button("submit");
        submitButton.addClickListener(event -> {
            patternSource.clearCache();
            patternDaoProvider.get()
                              .write(cacheKeyUK, i18NTextBox.getValue());
            populateBanner();
        });
        FormLayout formLayout = new FormLayout(i18NTextBox, submitButton, vacancies);

        getGridLayout().addComponent(bannerLabel, 0, 0, 1, 0);

        setTopRight(formLayout);

        setMiddleLeft(itemsForSale);
        setCentreCell(ceoNews);
        setMiddleRight(vacancies);


        setBottomLeft(payRiseButton);
        setBottomCentre(popupButton);
        setBottomRight(systemOptionButton);
        optionValueChanged(null);
    }

    @Override
    public void optionValueChanged(Property.ValueChangeEvent event) {
        ceoNews.setVisible(option.get(ceoVisible));
        itemsForSale.setVisible(option.get(itemsForSaleVisible));
        vacancies.setVisible(option.get(vacanciesVisible));
        populateBanner();
    }

    private void populateBanner() {
        LabelKey selection = (option.get(ceoVisible)
                                    .booleanValue()) ? LabelKey.is_selected : LabelKey.is_not_selected;
        int temperature = (new Random().nextInt(40)) - 10;
        bannerLabel.setValue(translate.from(MessageKey.Banner, selection, temperature));
    }

    @RequiresPermissions("pay:request-increase")
    protected void requestAPayRise() {
        userNotifier.notifyInformation(DescriptionKey.You_just_asked_for_a_pay_increase);
    }

    @Nonnull
    @Override
    public Option getOption() {
        return option;
    }

    @Handler
    protected void localeChanged(LocaleChangeBusMessage busMessage) {
        populateBanner();
    }
}

