/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import domain.Chorbi;
import forms.ChorbiForm;

@Controller
@RequestMapping("/security")
public class SigninController extends AbstractController {
	//-----------Services----------------
	@Autowired
	private ChorbiService chorbiService;
	
	
	// Constructors -----------------------------------------------------------

	public SigninController() {
		super();
	}

	@RequestMapping(value="/signin", method = RequestMethod.GET)
	public ModelAndView signinUser(){
		ModelAndView result;
		ChorbiForm chorbi = new ChorbiForm();
		
		result = new ModelAndView("security/signin");
		result.addObject("chorbi2", chorbi);
		
		return result;
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST, params = "signin")
	public ModelAndView user(ChorbiForm actor, BindingResult binding) {
		ModelAndView result;
		Chorbi chorbi;
		chorbi = chorbiService.reconstruct(actor, binding);

//		int years=(actor.getCreditCard().getExpirationYear()+2000)-1970;
//		int month=actor.getCreditCard().getExpirationMonth();
//		long exp = years*31540000000l+month*2628000000l;
//		long finale = exp-Calendar.getInstance().getTime().getTime();
		
		if (binding.hasErrors() || chorbi.getName().equals("Pass") || chorbi.getName().equals("Cond")) {
			result = new ModelAndView("security/signin");
			result.addObject("authority", "lessor2");
			result.addObject("lessor2", actor);
			if(chorbi.getName().equals("Pass")){
				result.addObject("message", "security.password.failed");
			}else if(chorbi.getName().equals("Cond")){
				result.addObject("message", "security.condition.failed");
			}
			else{
				result.addObject("errors", binding.getAllErrors());
			}
		} else {
			try{
				chorbiService.save(chorbi);
				result = new ModelAndView("redirect:login.do");
			}catch (Throwable oops) {
				result = new ModelAndView("security/signin");
				result.addObject("chorbi2", actor);
				result.addObject("message", "security.signin.failed");
			}
		}

		return result;
	}

}
