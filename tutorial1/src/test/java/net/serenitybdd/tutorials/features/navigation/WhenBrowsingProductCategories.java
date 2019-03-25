package net.serenitybdd.tutorials.features.navigation;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.page.TheWebPage;
import net.serenitybdd.tutorials.steps.NavigatingUser;
import net.serenitybdd.tutorials.tasks.NavigateTo;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.tutorials.model.Category.Сохраненo;
import static org.hamcrest.Matchers.containsString;

@RunWith(SerenityRunner.class)
public class WhenBrowsingProductCategories {

    @Steps
    NavigatingUser mark;

    @Managed
    WebDriver browser;

    @Test
    public void shouldBeAbleToNavigateToTheFeedCategory() {
        //Given
        mark.isOnTheHomePage();
        //When
        mark.navigatesToCategory(Сохраненo);
        //Then
        mark.shouldSeePageTittleContaining("Войдите или зарегистрируйтесь | eBay");
    }

    @Test
    public void shouldBeAbleToViewMotoProducts() {

        Actor mike = Actor.named("Mike");
        mike.can(BrowseTheWeb.with(browser));
        when(mike).attemptsTo(NavigateTo.theCategory(Сохраненo));
        then(mike).should(seeThat(TheWebPage.title(), containsString("Войдите или зарегистрируйтесь | eBay")));
    }
}
