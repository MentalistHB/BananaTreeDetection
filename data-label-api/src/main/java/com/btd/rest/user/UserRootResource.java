package com.btd.rest.user;

import java.util.List;
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

@RestController
@CrossOrigin(origins = "*")
public class UserRootResource {

    @Inject
    UserService userService;

    @RequestMapping(path = ApiConstant.USER_COLLECTION_PATH, method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User create(@PathVariable("token") UUID token, @RequestBody User user) {

        return userService.create(user, token);
    }

    @RequestMapping(path = ApiConstant.USER_COLLECTION_PATH, method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list(@PathVariable("token") String token) {

        return userService.list(token);
    }
}
