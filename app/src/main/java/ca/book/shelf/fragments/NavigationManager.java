package ca.book.shelf.fragments;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NavigationManager {

    FragmentManager mFragmentManager;
    int mFragmentHolderId;

    public NavigationManager(FragmentManager fragmentManager, int fragmentHolderId) {
        mFragmentManager = fragmentManager;
        mFragmentHolderId = fragmentHolderId;
    }

    public void showFeed() {
        Fragment fragment = new StoriesFragment();
        showTarget(fragment, StoriesFragment.TAG);
    }

    public void showSearchResults() {
        Fragment fragment = new SearchResultsFragment();
        showTarget(fragment, SearchResultsFragment.TAG);
    }

    public String getTopFragment() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        Fragment topFragment = fragments.get(fragments.size() - 1);
        return topFragment != null ? topFragment.getTag() : StoriesFragment.TAG;
    }

    public int getBackStackCount() {
        return mFragmentManager.getBackStackEntryCount();
    }

    private void showTarget(Fragment fragment, String tag) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(mFragmentHolderId, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
