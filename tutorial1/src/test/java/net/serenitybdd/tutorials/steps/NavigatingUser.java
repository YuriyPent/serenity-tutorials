package net.serenitybdd.tutorials.steps;

import net.serenitybdd.tutorials.model.Category;
import net.serenitybdd.tutorials.ui.CategoryNavigationBar;
import net.serenitybdd.tutorials.ui.CurrentPage;
import net.serenitybdd.tutorials.ui.EbayHomePage;
import net.thucydides.core.annotations.Step;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by yurap on 23.03.2019.
 */
public class NavigatingUser {

    EbayHomePage ebayHomePage;
    CurrentPage currentPage;
    CategoryNavigationBar categoryNavigationBar;

    @Step
    public void isOnTheHomePage() {
        ebayHomePage.open();
    }

    @Step
    public void shouldSeePageTittleContaining(String expectedTittle) {
        assertThat(currentPage.getTitle()).containsIgnoringCase(expectedTittle);
    }

    @Step
    public void navigatesToCategory(Category category) {
        categoryNavigationBar.selectCategory(category);

    }
}
