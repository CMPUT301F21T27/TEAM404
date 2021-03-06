package com.example.team404;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.team404.Account.AccountActivity;
import com.example.team404.Habit.Habit;
import com.example.team404.Login.LoginActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SubscribeActivityIntentTest {
    // I use Pixel 4 API 29,
    // so I set each navigation bar for each coordinate is :
    //Home: 0, 2000
    //Subscribe: 500, 2000
    //My habit: 800, 2000
    //Account: 1000, 2000
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    // test home page
    // if there is some habit from firebase in the home page


    @Test
    public void GoToSubscribeActivityTest() throws Exception{
        //solo.assertCurrentActivity("current Activity", LoginActivity.class);
        solo.getCurrentActivity();
        System.out.println("---"+solo.getCurrentActivity());
        EditText password = (EditText) solo.getView(R.id.user_pass);
        EditText email = (EditText) solo.getView(R.id.user_email);
        solo.enterText(email, "test@test.com");
        solo.enterText(password, "123123");
        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("current Activity",MainActivity.class);
        solo.waitForActivity(MainActivity.class, 3000);
        //I set the time is 6000, it really depend on the internet
        //it needs some times to reload the list of habit from Firebase
        // if it is not pass, you just need to extends the time, until the list of habit is show in the list
        ListView habit_list_in_subscribe_page = solo.getCurrentActivity().findViewById(R.id.subscribe_list);
        Thread.sleep(6000);

        solo.clickOnScreen(500, 2000);

        solo.assertCurrentActivity("Current Activity", SubscribeActivity.class);


    }
}
