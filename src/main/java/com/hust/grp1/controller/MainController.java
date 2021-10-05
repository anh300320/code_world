package com.hust.grp1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "")
public class MainController {

    @RequestMapping("/")
    public String home(){
        return "dashboard/index.html";
    }

    @RequestMapping("/login")
    public String login(){
        return "login/login.html";
    }

    @RequestMapping("/register")
    public String register(){
        return "login/register.html";
    }

    @RequestMapping("/activity")
    public Object activity(Authentication authentication){
        if(authentication != null)
            return "profile/activity.html";
        else
            return new RedirectView("/login");
    }

    @RequestMapping("/question")
    public String question(){
        return "question/question.html";
    }

    @RequestMapping("/tags")
    public String tags(){
        return "tag/tag.html";
    }

    @RequestMapping("/users")
    public String users(){
        return "user/user.html";
    }

    @RequestMapping("/question/{id}")
    public String detail(){
        return "question/questionDetail.html";
    }

    @RequestMapping("/ask-question")
    public Object askQuestion(Authentication authentication){
        if(authentication != null)
            return "question/askQuestion.html";
        else
            return new RedirectView("/login");
    }

    @RequestMapping("/user/{id}")
    public String profile(){
        return "profile/profile.html";
    }

    @RequestMapping("/user/edit")
    public Object editProfile(Authentication authentication){
        if(authentication != null)
            return "profile/editProfile.html";
        else
            return new RedirectView("/login");
    }
}
