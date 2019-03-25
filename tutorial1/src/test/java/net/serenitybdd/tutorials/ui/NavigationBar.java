package net.serenitybdd.tutorials.ui;

import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.tutorials.model.Category;

/**
 * Created by yurap on 25.03.2019.
 */
public class NavigationBar {

    public static Target category(Category category) {
        return Target.the(category.name() + "category")
                .locatedBy("//a[contains(text(),'Сохраненo')]")
                .of(category.name());
    }
}
