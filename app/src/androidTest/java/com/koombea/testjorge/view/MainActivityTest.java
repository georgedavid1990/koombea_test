package com.koombea.testjorge.view;

import android.app.Instrumentation;
import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import com.koombea.testjorge.R;
import com.koombea.testjorge.adapters.PostsAdapter;
import com.koombea.testjorge.common.OnClickListener;
import com.koombea.testjorge.data.dao.DataBaseManager;
import com.koombea.testjorge.data.model.UserPost;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() {
        mainActivity = activityTestRule.getActivity();
    }

    @Test
    public void testMainActivity(){
        RecyclerView recyclerViewSearchResults = mainActivity.findViewById(R.id.recyclerViewSearchResults);
        assertNotNull(recyclerViewSearchResults);
    }

    @Test
    public void dbConnected(){
        DataBaseManager appDatabase = DataBaseManager.getSharedInstance();
        String currentUser = appDatabase.getCurrentUser();
        assertNotNull(currentUser);
    }

    @Test
    public void checkAdapter(){
        RecyclerView recyclerViewSearchResults = mainActivity.findViewById(R.id.recyclerViewSearchResults);
        PostsAdapter adapter = (PostsAdapter)recyclerViewSearchResults.getAdapter();
        assertNotNull(adapter);
        OnClickListener listener = adapter.getClickListener();
        assertNotNull(listener);
        List<UserPost> posts = adapter.getItems();
        assertNotNull(posts);
        assertFalse(posts.isEmpty());
    }

    @After
    public void tearDown(){
        mainActivity = null;
    }
}
