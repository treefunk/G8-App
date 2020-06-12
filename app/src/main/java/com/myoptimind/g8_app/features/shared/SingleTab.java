package com.myoptimind.g8_app.features.shared;

import androidx.fragment.app.Fragment;

public class SingleTab{

    private String title;
    private Fragment fragmentInstance;

    public SingleTab(String title, Fragment fragmentInstance) {
        this.title = title;
        this.fragmentInstance = fragmentInstance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragmentInstance() {
        return fragmentInstance;
    }

    public void setFragmentInstance(Fragment fragmentInstance) {
        this.fragmentInstance = fragmentInstance;
    }
}
