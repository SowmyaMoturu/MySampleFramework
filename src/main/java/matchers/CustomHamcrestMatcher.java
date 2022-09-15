package matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.IsEqual.equalTo;


public class CustomHamcrestMatcher extends TypeSafeMatcher<String> {

    private final String subString;

    private CustomHamcrestMatcher(final String subString) {
        this.subString = subString;
    }

    @Override
    protected boolean matchesSafely(final String actualString) {
        return actualString.toLowerCase().contains(this.subString.toLowerCase());
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Custom Hamcrest Matcher");
    }

    @Factory
    public static Matcher<String> containsIgnoringCase(final String subString) {
        return new CustomHamcrestMatcher(subString);
    }


    @Factory
    public static Matcher<String>[] matchAnyContaining(final String[] subStrings) {

        Matcher<String>[] matchersList = new Matcher[subStrings.length];
        for(int i = 0; i < subStrings.length; i++){
            matchersList[i] = containsIgnoringCase(subStrings[i]);
        }
        return matchersList;
    }

    @Factory
    public static Matcher<String> matchAny(final String[] subStrings) {

        Matcher<String>[] matchersList = new Matcher[subStrings.length];
        for(int i = 0; i < subStrings.length; i++){
            matchersList[i] = equalTo(subStrings[i]);
        }

        return anyOf(matchersList);
    }


}