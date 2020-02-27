package com.hongqisi.web.localAuth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {


    @RequestMapping(value = "/accountbind",method = RequestMethod.GET)
    public String accountbind(){
         return "local/accountbind";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "local/login";
    }

    @RequestMapping(value = "/changepsw",method = RequestMethod.GET)
    public String changepsw(){
        return "local/changepsw";
    }


}
