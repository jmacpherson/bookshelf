package ca.pen.sieve.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import ca.pen.sieve.R;
import ca.pen.sieve.databinding.FragmentStoriesBinding;

public class StoriesFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentStoriesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_stories, container, false);

        return binding.getRoot();
    }
}
