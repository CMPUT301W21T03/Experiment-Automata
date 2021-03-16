package com.example.experiment_automata;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.experiment_automata.ExperimentFragments.AddExperimentFragment;
import com.example.experiment_automata.Experiments.ExperimentModel.BinomialExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.CountExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentManager;
import com.example.experiment_automata.ExperimentFragments.NavExperimentDetailsFragment;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentType;
import com.example.experiment_automata.Experiments.ExperimentModel.MeasurementExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.NaturalCountExperiment;
import com.example.experiment_automata.UserInformation.User;
import com.example.experiment_automata.trials.BinomialTrial;
import com.example.experiment_automata.trials.CountTrial;
import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.NaturalCountTrial;
import com.example.experiment_automata.ui.Screen;
import com.example.experiment_automata.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Role/Pattern:
 *     The main launch activity where everything is being held and maintained. 
 *
 *  Known Issue:
 *
 *      1. None
 */

public class NavigationActivity extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;
    public final ExperimentManager experimentManager = new ExperimentManager();

    private Screen currentScreen;
    public Fragment currentFragment;
    public final User loggedUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        FloatingActionButton addExperimentButton = findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentScreen) {
                    case ExperimentList:
                        new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
                        break;
                    case ExperimentDetails:
                        switch (experimentManager.getCurrentExperiment().getType()) {
                            case Count:
                                navController.navigate(R.id.nav_add_count_trial);
                                break;
                            case NaturalCount:
                                navController.navigate(R.id.nav_add_natural_count_trial);
                                break;
                            case Binomial:
                                navController.navigate(R.id.nav_add_binomial_trial);
                                break;
                            case Measurement:
                                navController.navigate(R.id.nav_add_measurement_trial);
                                break;
                        }
                        currentScreen = Screen.Trial;
                        break;
                    case Trial:
                        Experiment experiment = experimentManager.getCurrentExperiment();
                        Snackbar snackbar = Snackbar.make(view, "No value was given", Snackbar.LENGTH_SHORT);
                        try {
                            switch (experiment.getType()) {
                                case Count:
                                    CountExperiment countExperiment = (CountExperiment) experiment;
                                    CountTrial countTrial = new CountTrial(loggedUser.getUserId());
                                    countExperiment.recordTrial(countTrial);
                                    break;
                                case NaturalCount:
                                    NaturalCountExperiment naturalCountExperiment = (NaturalCountExperiment) experiment;
                                    // get value
                                    EditText naturalCountInput = (EditText) findViewById(R.id.add_natural_count_value);
                                    final int naturalCount = Integer.parseInt(naturalCountInput.getText().toString());
                                    NaturalCountTrial naturalCountTrial = new NaturalCountTrial(loggedUser.getUserId(), naturalCount);
                                    naturalCountExperiment.recordTrial(naturalCountTrial);
                                    break;
                                case Binomial:
                                    BinomialExperiment binomialExperiment = (BinomialExperiment) experiment;
                                    // get value
                                    CheckBox passedInput = (CheckBox) findViewById(R.id.add_binomial_value);
                                    final boolean passed = passedInput.isChecked();
                                    BinomialTrial binomialTrial = new BinomialTrial(loggedUser.getUserId(), passed);
                                    binomialExperiment.recordTrial(binomialTrial);
                                    break;
                                case Measurement:
                                    MeasurementExperiment measurementExperiment = (MeasurementExperiment) experiment;
                                    // get value
                                    EditText measurementInput = (EditText) findViewById(R.id.add_measurement_value);
                                    final float measurement = Float.parseFloat(measurementInput.getText().toString());
                                    MeasurementTrial measurementTrial = new MeasurementTrial(loggedUser.getUserId(), measurement);
                                    measurementExperiment.recordTrial(measurementTrial);
                                    break;
                            }
                            currentScreen = Screen.ExperimentDetails;
                            navController.navigateUp();
                        } catch (NumberFormatException ignored) {
                            // if no value was given
                            snackbar.show();
                        }
                }
            }
        });
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
        experimentManager.add(experiment.getExperimentId(), experiment);
        loggedUser.addExperiment(experiment.getExperimentId());
        if (currentScreen == Screen.ExperimentList) {
            ((HomeFragment) currentFragment).updateScreen();
        }
    }

    @Override
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

    public ExperimentManager getExperimentManager() {
        return experimentManager;
    }
}