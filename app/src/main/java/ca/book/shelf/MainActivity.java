package ca.book.shelf;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import ca.book.shelf.databinding.ActivityMainBinding;
import ca.book.shelf.providers.Repository;
import ca.book.shelf.viewmodels.MainViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    Repository mRepository;

    MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        BookShelfApplication.getApp().getComponent().inject(this);
        mViewModel.init(mRepository);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(mViewModel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
