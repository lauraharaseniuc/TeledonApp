package com.example.networking.objectprotocol;

import model.Volunteer;

public class VolunteerLogInResponse implements Response{
    private Volunteer volunteerLoggedIn;

    public VolunteerLogInResponse(Volunteer volunteerLoggedIn) {
        this.volunteerLoggedIn = volunteerLoggedIn;
    }

    public Volunteer getVolunteerLoggedIn() {
        return volunteerLoggedIn;
    }
}
