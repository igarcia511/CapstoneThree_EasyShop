package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao){
        this.profileDao = profileDao;
        this.userDao = userDao;
    }



    @GetMapping()
    public Profile  getProfile(Principal principal){
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        return profileDao.getProfile(userId);
    }


    @PostMapping
    public Profile createProfile(@RequestBody Profile profile){
        return profileDao.create(profile);
    }

    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal){
        int userId = userDao.getIdByUsername(principal.getName());

        return profileDao.updateProfile(profile, userId);
    }
}
