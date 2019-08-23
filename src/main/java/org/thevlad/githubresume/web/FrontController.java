package org.thevlad.githubresume.web;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thevlad.githubresume.model.GithubUser;
import org.thevlad.githubresume.services.IGithubUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FrontController {

	@Autowired
	private IGithubUserService githubUserService;
	
	@GetMapping("/")
	public String home(Model model) {
		SearchForm form = new SearchForm("");
		model.addAttribute("searchForm", form);
		return "search";
	}
	@PostMapping("/search")
	public String search(@Valid @ModelAttribute("searchForm") SearchForm searchForm, BindingResult bindingResult, Model model) {
		try {
			if (bindingResult.hasFieldErrors()) {
				return "search";
			}
			GithubUser userDTO =  githubUserService.findGithubUser(searchForm.getUserName());
			model.addAttribute("userDTO", userDTO);
			return "/resume";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error to get user {} from Github", searchForm.getUserName(), e);
			model.addAttribute("errorMessage",
							String.format("Error to get user {%s} from Github\n Error message: %s", searchForm.getUserName(), e.getMessage()));
			return "search";
		}
	}
}
