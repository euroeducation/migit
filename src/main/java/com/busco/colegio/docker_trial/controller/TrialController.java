package com.busco.colegio.docker_trial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrialController {

	@RequestMapping("/")
	public String home() {
		return "Hello Docker World";
	}

}
