package com.ps.github_dash_example.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Data container link repo owner, name and branch-list together.
 */
public class RepoOwnerNameBranch {
    private String owner;


    private String name;
    private List<String> branches = new ArrayList<>();



    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBranches() {
        return branches;
    }
}
