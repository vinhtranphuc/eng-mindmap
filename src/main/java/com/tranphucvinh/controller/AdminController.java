package com.tranphucvinh.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        return "admin/index";
    }
}
