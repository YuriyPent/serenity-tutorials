package net.serenitybdd.tutorials.ui;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.tutorials.model.Category;

/**
 * Created by yurap on 23.03.2019.
 */
public class CategoryNavigationBar extends PageObject{

    public void selectCategory(Category category) {

        $("*[class=hl-cat-nav__container] *[class=saved]")
                .find(By.linkText(category.name())).click();


    }
}
