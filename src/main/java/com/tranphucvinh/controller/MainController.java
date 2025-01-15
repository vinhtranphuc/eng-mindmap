package com.tranphucvinh.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) throws JsonProcessingException {
        return "sgp_index";
    }
}
