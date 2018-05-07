package com.btd.rest.user.auth;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btd.model.User;
import com.btd.rest.ApiConstant;
import com.btd.service.UserService;
import com.btd.transfer.UserLoginTO;

@RestController
@CrossOrigin(origins = "*")
public class AuthRootResource {

    @Inject
    UserService userService;

    @RequestMapping(path = ApiConstant.USER_LOGIN_PATH, method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User login(@RequestBody UserLoginTO userLoginTO) {

        return userService.login(userLoginTO);
    }

    @RequestMapping(path = ApiConstant.USER_LOGOUT_PATH, method = RequestMethod.DELETE)
    @Consumes(MediaType.APPLICATION_JSON)
    public void logout(@PathVariable("token") UUID token) {
        userService.logout(token);
    }
}
