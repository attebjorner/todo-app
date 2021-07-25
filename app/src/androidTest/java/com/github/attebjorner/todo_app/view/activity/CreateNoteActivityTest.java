package com.github.attebjorner.todo_app.view.activity;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class CreateNoteActivityTest
{
    private final Note note = new Note("test", LocalDate.of(2021, 10, 7), Importance.LOW);

    @Test
    public void testIsCreateNoteActivityInView()
    {
        launchNoteCreation();
        onView(withId(R.id.create_note)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsFragmentInView()
    {
        launchNoteCreation();
        onView(withId(R.id.mainFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsSaveButtonClickable()
    {
        launchNoteCreation();
        onView(withId(R.id.btnSave)).check(matches(isClickable()));
    }

    @Test
    public void testIsDeletionDisabled()
    {
        launchNoteCreation();
        pressBack();
        onView(withId(R.id.imbDelete)).perform(ViewActions.click());
        onView(withId(R.id.create_note)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDelete)).perform(ViewActions.click());
        onView(withId(R.id.create_note)).check(matches(isDisplayed()));
    }

    @Test
    public void testAreDataFieldsEmpty()
    {
        launchNoteCreation();
        pressBack();
        onView(withId(R.id.etDescription)).check(matches(withText("")));
        onView(withId(R.id.tvDate)).check(matches(withText("")));
    }

    @Test
    public void testIsDeletionEnabled()
    {
        launchNoteEdition();
        pressBack();
        onView(withId(R.id.imbDelete)).check(matches(isClickable()));
        onView(withId(R.id.tvDelete)).check(matches(isClickable()));
    }

    @Test
    public void testIsNoteDataSet()
    {
        launchNoteEdition();
        pressBack();
        onView(withId(R.id.etDescription)).check(matches(withText(note.getDescription())));
        onView(withId(R.id.tvDate)).check(matches(withText(note.getDeadline().toString())));
    }

    private void launchNoteCreation()
    {
        ActivityScenario<CreateNoteActivity> activityScenario
                = ActivityScenario.launch(CreateNoteActivity.class);
    }

    private void launchNoteEdition()
    {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TinyDB tinyDB = new TinyDB(context);
        Intent intent = new Intent(context, CreateNoteActivity.class);
        intent.putExtra("isNew", false);
        tinyDB.putObject("editNote", note);
        tinyDB.putString("editNoteDeadline", note.getDeadline().toString());
        ActivityScenario<CreateNoteActivity> activityScenario
                = ActivityScenario.launch(intent);
    }
}