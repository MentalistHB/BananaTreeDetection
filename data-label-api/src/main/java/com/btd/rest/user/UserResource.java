package com.btd.rest.user;

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
@RequestMapping(ApiConstant.USER_ITEM_PATH)
@CrossOrigin(origins = "*")
public class UserResource {

    @Inject
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathVariable("userId") UUID userId, @PathVariable("token") UUID token) {

        return userService.findOne(userId, token);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User update(@PathVariable("userId") UUID userId, @PathVariable("token") UUID token,
            @RequestBody User user) {

        return userService.update(user, userId, token);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@PathVariable("userId") UUID userId, @PathVariable("token") UUID token) {

        userService.delete(userId, token);
    }
}
