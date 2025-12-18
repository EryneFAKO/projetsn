package main.java.com.example.demo.controller;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, CI/CD!";
    }
}
