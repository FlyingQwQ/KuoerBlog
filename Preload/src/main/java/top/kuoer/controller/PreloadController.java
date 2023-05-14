package top.kuoer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.service.HandleService;
import top.kuoer.utils.PreloadWebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PreloadController {

    private HandleService handleService;

    @Autowired
    public PreloadController(HandleService handleService) {
        this.handleService = handleService;
    }

    @GetMapping("/**")
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.handleService.handleRequest(request, response);
    }





}
