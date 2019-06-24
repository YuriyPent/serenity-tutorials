# Serenity BDD

## Requirements
In Serenity, requirements are organized into three levels:

    capabilities
    features
    stories
  
Typically, a project implements high-level capabilities, e.x. order management and membership management capabilities in an e-commerce project. Each capability is comprised of many features, and features are explained in detail by user stories.

## Steps and Tests
Steps contain a group of resource manipulation operations. It can be an action, verification or a context related operation. The classic Given_When_Then format can be reflected in the steps.

And tests go hand in hand with Steps. Each test tells a simple user story, which is carried out using certain Step.

## Reports
Serenity not only reports the test results but also uses them for producing living documentation describing the requirements and application behaviors.

## Testing with SerenityBDD
To run our Serenity tests with JUnit, we need to @RunWith the SerenityRunner, test runner. SerenityRunner instruments the step libraries and ensures that the test results will be recorded and reported on by the Serenity reporters.

Maven Dependencies
To make use of Serenity with JUnit, we should include serenity-core and serenity-junit in the pom.xml:
```xml
<dependency>
    <groupId>net.serenity-bdd</groupId>
    <artifactId>serenity-core</artifactId>
    <version>1.2.5-rc.11</version>
</dependency>
<dependency>
    <groupId>net.serenity-bdd</groupId>
    <artifactId>serenity-junit</artifactId>
    <version>1.2.5-rc.11</version>
</dependency>
```
We also need serenity-maven-plugin to have reports aggregated from test results:
```xml
<plugin>
    <groupId>net.serenity-bdd.maven.plugins</groupId>
    <artifactId>serenity-maven-plugin</artifactId>
    <version>1.2.5-rc.6</version>
    <executions>
        <execution>
            <id>serenity-reports</id>
            <phase>post-integration-test</phase>
            <goals>
                <goal>aggregate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
If we want Serenity to generate reports even if there’s a test failure, add the following to the pom.xml:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.20</version>
    <configuration>
        <testFailureIgnore>true</testFailureIgnore>
    </configuration>
</plugin>
```
## A Membership Points Example
Initially, our tests are based on the typical membership points feature in an e-commerce application. A customer can join the member program. As the customer purchases goods on the platform, the membership points will increase, and the customer’s membership grade would grow accordingly.

Now let’s write several tests against the scenarios described above and see how Serenity works.

First, let’s write the test for membership initialisation and see which steps do we need:
```java
@RunWith(SerenityRunner.class)
public class MemberStatusLiveTest {
 
    @Steps
    private MemberStatusSteps memberSteps;
 
    @Test
    public void membersShouldStartWithBronzeStatus() {
        memberSteps.aClientJoinsTheMemberProgram();
        memberSteps.theMemberShouldHaveAStatusOf(Bronze);
    }
}
```
Then we implement the two steps as follows:
```java
public class MemberStatusSteps {
 
    private Member member;
 
    @Step("Given a member has {0} points")
    public void aMemberHasPointsOf(int points) {
        member = Member.withInitialPoints(points);
    }
 
    @Step("Then the member grade should be {0}")
    public void theMemberShouldHaveAStatusOf(MemberGrade grade) {
        assertThat(member.getGrade(), equalTo(grade));
    }
}
```
Now we are ready to run an integration test with `mvn clean verify`. The reports will be located at target/site/serenity/index.html
If we have some steps to implement, we can mark them as @Pending:
```java
@Pending
@Step("When the member exchange {}")
public void aMemberExchangeA(Commodity commodity){
    //TODO
}
```
## Integration with JBehave.
To integrate with JBehave, one more dependency serenity-jbehave is needed in the POM:
```java
<dependency>
    <groupId>net.serenity-bdd</groupId>
    <artifactId>serenity-jbehave</artifactId>
    <version>1.24.0</version>
</dependency>
```
## JBehave Github REST API Test Continued
As we have introduced how to do REST API testing with JBehave, we can continue with our JBehave REST API test and see how it fits in Serenity.

Our story was:
```cucumber
Scenario: Github user's profile should have a login payload same as username

Given github user profile api
When I look for eugenp via the api
Then github's response contains a 'login' payload same as eugenp

The Given_When_Then steps can be migrated to as @Steps without any changes:
```
```java
public class GithubRestUserAPISteps {
 
    private String api;
    private GitHubUser resource;
 
    @Step("Given the github REST API for user profile")
    public void withUserProfileAPIEndpoint() {
        api = "https://api.github.com/users/%s";
    }
 
    @Step("When looking for {0} via the api")
    public void getProfileOfUser(String username) throws IOException {
        HttpResponse httpResponse = getGithubUserProfile(api, username);
        resource = retrieveResourceFromResponse(httpResponse, GitHubUser.class);
    }
 
    @Step("Then there should be a login field with value {0} in payload of user {0}")
    public void profilePayloadShouldContainLoginValue(String username) {
        assertThat(username, Matchers.is(resource.getLogin()));
    }
}
```
To make JBehave’s story-to-code mapping work as expected, we need to implement JBehave’s step definition using @Steps:
```java
public class GithubUserProfilePayloadStepDefinitions {
 
    @Steps
    GithubRestUserAPISteps userAPISteps;
 
    @Given("github user profile api")
    public void givenGithubUserProfileApi() {
        userAPISteps.withUserProfileAPIEndpoint();
    }
 
    @When("looking for $user via the api")
    public void whenLookingForProfileOf(String user) throws IOException {
        userAPISteps.getProfileOfUser(user);
    }
 
    @Then("github's response contains a 'login' payload same as $user")
    public void thenGithubsResponseContainsAloginPayloadSameAs(String user) {
        userAPISteps.profilePayloadShouldContainLoginValue(user);
    }
}
```
With SerenityStories, we can run JBehave tests both from within our IDE and in the build process:
```java
import net.serenitybdd.jbehave.SerenityStory;
 
public class GithubUserProfilePayload extends SerenityStory {}
```
## Integration with REST-assured
Maven Dependencies
To make use of REST-assured with Serenity, the serenity-rest-assured dependency should be included:
```xml
<dependency>
    <groupId>net.serenity-bdd</groupId>
    <artifactId>serenity-rest-assured</artifactId>
    <version>1.2.5-rc.11</version>
</dependency>
```
## Use REST-assured in Github REST API Test
Now we can replace our web client with REST-assured utilities:
```java
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;
 
public class GithubRestAssuredUserAPISteps {
 
    private String api;
 
    @Step("Given the github REST API for user profile")
    public void withUserProfileAPIEndpoint() {
        api = "https://api.github.com/users/{username}";
    }
 
    @Step("When looking for {0} via the api")
    public void getProfileOfUser(String username) throws IOException {
        rest().get(api, username);
    }
 
    @Step("Then there should be a login field with value {0} in payload of user {0}")
    public void profilePayloadShouldContainLoginValue(String username) {
        then().body("login", Matchers.equalTo(username));
    }
}
```
After replacing the implementation of userAPISteps in the StepDefition, we can re-run the verify build:
```java
public class GithubUserProfilePayloadStepDefinitions {
 
    @Steps
    GithubRestAssuredUserAPISteps userAPISteps;
    //...
}
```
## Integration with JIRA
Maven Dependencies
To integrate with JIRA, we need another dependency: serenity-jira-requirements-provider.
```xml
<dependency>
    <groupId>net.serenity-bdd</groupId>
    <artifactId>serenity-jira-requirements-provider</artifactId>
    <version>1.1.3-rc.5</version>
</dependency>
```
## One-way Integration
To add JIRA links in the story, we can add the JIRA issue using story’s meta tag:

    Meta:
    @issue #BDDTEST-1
Besides, JIRA account and links should be specified in the file serenity.properties at the root of the project:

    jira.url=<jira-url>
    jira.project=<jira-project>
    jira.username=<jira-username>
    jira.password=<jira-password>
    
* Build project `mvn archetype:generate -Dfilter=serenity`
