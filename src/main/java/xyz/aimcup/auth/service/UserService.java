package xyz.aimcup.auth.service;


import xyz.aimcup.auth.data.entity.User;

public interface UserService {

    User getUserByOsuId(String osuId);

}
