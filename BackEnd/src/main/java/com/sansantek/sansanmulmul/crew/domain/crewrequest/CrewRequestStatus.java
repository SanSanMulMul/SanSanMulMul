package com.sansantek.sansanmulmul.crew.domain.crewrequest;

public enum CrewRequestStatus {
    R("Requesting"),
    A("Approved"),
    D("Denied");

    private final String description;

    CrewRequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}