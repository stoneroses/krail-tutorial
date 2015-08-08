package com.example.tutorial.pages;

import uk.q3c.krail.core.navigate.sitemap.View;
import uk.q3c.krail.core.shiro.PageAccessControl;
import uk.q3c.krail.core.view.Grid3x3ViewBase;

@View(uri = "private/finance-department/purchasing", pageAccessControl = PageAccessControl.PERMISSION, labelKeyName = "Purchasing")
public class PurchasingView extends Grid3x3ViewBase {


}