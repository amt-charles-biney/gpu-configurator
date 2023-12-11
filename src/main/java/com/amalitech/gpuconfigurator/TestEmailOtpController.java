package com.amalitech.gpuconfigurator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestEmailOtpController {

    /*
    * take user request
    * validate email
    * create user
    * generate token and assign to user
    *
    *
    * /endpoint to verify token
    * get the token associated with user email
    * compare token if valid token check User as isValid
    * if valid token and correct taken make the user to be verified
    * create jwt for user
    * /get the email - find user
    *
    * */

}
