package com.github.attebjorner.todo_app.view.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.view.adapter.TodoListAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.github.attebjorner.todo_app.util.TestUtils.actionOnItemViewAtPosition;
import static com.github.attebjorner.todo_app.util.TestUtils.withRecyclerView;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest
{
    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenario
            = new ActivityScenarioRule<>(MainActivity.class);

    private final List<Note> TEST_NOTES = List.of(
            new Note("test", LocalDate.of(2021, 10, 7), Importance.LOW),
            new Note("test2", LocalDate.of(2021, 10, 8), Importance.IMPORTANT),
            new Note("test3", LocalDate.of(2021, 10, 9), Importance.BASIC),
            new Note("test4", LocalDate.of(2021, 10, 10), Importance.LOW)
    );

    private final int TEST_ITEM_ID = 1;

    @Test
    public void testIsMainActivityInView()
    {
        onView(withId(R.id.main)).check(matches(isDisplayed()));
    }

    @Test
    public void testTextViewMyTasksInView()
    {
        onView(withId(R.id.tvMyTasks)).check(matches(isDisplayed()));
        onView(withId(R.id.tvMyTasks)).check(matches(withText(R.string.my_tasks)));
    }

    @Test
    public void testDoneCounterInView()
    {
        onView(withId(R.id.tvDoneCounter)).check(matches(isDisplayed()));
    }

    @Test
    public void testImageButtonVisibility()
    {
        onView(withId(R.id.imbVisible)).check(matches(isDisplayed()));
        onView(withId(R.id.imbVisible)).check(matches(isClickable()));
    }

    @Test
    public void testFloatingActionButton()
    {
        onView(withId(R.id.fabAddNote)).check(matches(isDisplayed()));
        onView(withId(R.id.fabAddNote)).check(matches(isClickable()));
    }

    @Test
    public void testFloatingActionButtonStartsActivity()
    {
        onView(withId(R.id.fabAddNote)).perform(ViewActions.click());
        onView(withId(R.id.create_note)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsRecyclerViewInView()
    {
        fillRecyclerViewWithTestData();
        onView(withId(R.id.rvTodo)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewInfoButtonStartsActivity()
    {
        fillRecyclerViewWithTestData();
        onView(withId(R.id.rvTodo))
                .perform(actionOnItemViewAtPosition(TEST_ITEM_ID, R.id.imbInfo, ViewActions.click()));
        onView(withId(R.id.create_note)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewDescriptionStartsActivity()
    {
        fillRecyclerViewWithTestData();
        onView(withId(R.id.rvTodo))
                .perform(actionOnItemViewAtPosition(TEST_ITEM_ID, R.id.tvDescription, ViewActions.click()));
        onView(withId(R.id.create_note)).check(matches(isDisplayed()));
    }

    public void testIsCheckboxClickable()
    {
        fillRecyclerViewWithTestData();
        onView(withRecyclerView(R.id.rvTodo).atPositionOnView(TEST_ITEM_ID, R.id.imbCheckbox))
                .check(matches(isClickable()));
    }

    @Test
    public void testRecyclerViewItemData()
    {
        fillRecyclerViewWithTestData();
        onView(withRecyclerView(R.id.rvTodo).atPositionOnView(TEST_ITEM_ID, R.id.tvDescription))
                .check(matches(withText("!! " + TEST_NOTES.get(TEST_ITEM_ID).getDescription())));
        onView(withRecyclerView(R.id.rvTodo).atPositionOnView(TEST_ITEM_ID, R.id.tvDeadline))
                .check(matches(withText(TEST_NOTES.get(TEST_ITEM_ID).getDeadline().toString())));
    }

    private void fillRecyclerViewWithTestData()
    {
        activityScenario.getScenario().onActivity(activity ->
        {
            RecyclerView recyclerView = activity.findViewById(R.id.rvTodo);
            recyclerView.setAdapter(new TodoListAdapter(
                    TEST_NOTES, InstrumentationRegistry.getInstrumentation().getTargetContext()
            ));
        });
    }
}
