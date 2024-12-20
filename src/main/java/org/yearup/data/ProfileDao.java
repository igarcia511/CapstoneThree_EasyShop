package org.yearup.data;


import org.yearup.models.Profile;
import org.yearup.models.User;

public interface ProfileDao
{
    Profile create(Profile profile);
    Profile getProfile(int usersId);
    Profile updateProfile(Profile profile, int userId);
}
