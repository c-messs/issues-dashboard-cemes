package io.spring.demo.issuesdashboard.events;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import io.spring.demo.issuesdashboard.github.GithubClient;
import io.spring.demo.issuesdashboard.github.RepositoryEvent;

@Controller
public class EventsController {
	
	@Autowired
	private  GithubClient githubClient;
	
	@Autowired
	private GithubProjectRepository repository;

	@GetMapping("/events/{repoName}")
	@ResponseBody
	public RepositoryEvent[] fetchEvents(@PathVariable String repoName) {
		
		GithubProject githubProject = repository.findByRepoName(repoName);
		
		return githubClient.fetchEvents(githubProject.getOrgName(), githubProject.getRepoName()).getBody();
	}
	

	@GetMapping("/")
	public String dashboard(Model model) {
		List<DashboardEntry> entries = StreamSupport
				.stream(this.repository.findAll().spliterator(), true)
				.map(p -> new DashboardEntry(p, githubClient.fetchEventsList(p.getOrgName(), p.getRepoName())))
				.collect(Collectors.toList());
		model.addAttribute("entries", entries);
		return "dashboard";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("projects", repository.findAll());
		return "admin";
	}
}
