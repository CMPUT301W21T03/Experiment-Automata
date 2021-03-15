package com.example.experiment_automata;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;

import com.example.experiment_automata.ExperimentFragments.AddExperimentFragment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentManager;
import com.example.experiment_automata.ExperimentFragments.NavExperimentDetailsFragment;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.QuestionUI.AddQuestionFragment;
import com.example.experiment_automata.QuestionUI.QuestionDisplay;
import com.example.experiment_automata.QuestionsModel.Question;
import com.example.experiment_automata.QuestionsModel.QuestionManager;
import com.example.experiment_automata.QuestionsModel.Reply;
import com.example.experiment_automata.UserInformation.User;
import com.example.experiment_automata.ui.Screen;
import com.example.experiment_automata.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

/**
 * Role/Pattern:
 *     The main launch activity where everything is being held and maintained. 
 *
 *  Known Issue:
 *
 *      1. None
 */

public class NavigationActivity extends AppCompatActivity implements
        AddExperimentFragment.OnFragmentInteractionListener,
        AddQuestionFragment.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;
    public final ExperimentManager experimentManager = new ExperimentManager();
    public QuestionManager questionManager = QuestionManager.getInstence();

    private Screen currentScreen;
    public Fragment currentFragment;
    public final User loggedUser = new User();
    public Experiment currentExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton addExperimentButton = findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_owned_experiments, R.id.nav_subscriptions, R.id.nav_published, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);

        // https://developer.android.com/training/search/setup#create-sc
        // Associate searchable
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Called when the user submits the query.
             * @param query the query text that is to be submitted
             * @return true if the query has been handled by the listener, false to let the
             * SearchView perform the default action.
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                navController.navigate(R.id.nav_search, bundle);
                searchView.setQuery("", false);
                return false;
            }

            /**
             * Called when the query text is changed by the user.
             * @param newText the new content of the query text field.
             * @return false if the SearchView should perform the default action of showing any
             * suggestions if available, true if the action was handled by the listener.
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onOkPressed(Experiment experiment) {

        boolean added = false;
        while (!added) {
            try {
                experimentManager.add(experiment.getExperimentId(), experiment);
                added = true;
            } catch (IllegalArgumentException badUUID) {
                experiment.makeNewUUID();
            }
        }

        loggedUser.addExperiment(experiment.getExperimentId());
        if (currentScreen == Screen.ExperimentList) {
            ((HomeFragment) currentFragment).updateScreen();
        }
    }

    @Override
    // todo: this functionality should be moved into something else in the future
    public void onOKPressedEdit(String experimentDescription, int experimentTrials,
                         boolean experimentLocation, boolean experimentNewResults,
                         Experiment currentExperiment) {
        currentExperiment.setDescription(experimentDescription);
        currentExperiment.setMinTrials(experimentTrials);
        currentExperiment.setRequireLocation(experimentLocation);
        currentExperiment.setActive(experimentNewResults);
        ((NavExperimentDetailsFragment) currentFragment).updateScreen();
    }


    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public ExperimentManager getExperimentManager()
    {
        return experimentManager;
    }

    @Override
    public void onOkPressedQuestion(String question, UUID experimentId) {
        Question newQuestion = new Question(question, loggedUser.getUserId(), experimentId);
        questionManager.addQuestion(experimentId, newQuestion);

        ((QuestionDisplay)currentFragment).updateQuestionsList();

        Log.d("Question", newQuestion.getQuestion());
        Log.d("Question Experiment Id", experimentId.toString());
        Log.d("Size of Question List", "" + questionManager.getTotalQuestions(experimentId));
    }

    @Override
    public void onOkPressedReply(String reply, UUID questionId) {
        Reply newReply = new Reply(reply, questionId);
        questionManager.addReply(questionId, newReply);
    }
}