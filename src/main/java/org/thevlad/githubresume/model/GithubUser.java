package org.thevlad.githubresume.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GithubUser {

	private GithubUserDetail user;
	private int totalRepoCount;
	private Map<String,Integer> byLangRepos;
	private List<GithubRepository> byPopularityRepos;
	private List<GithubRepository> byActivity;
	private List<GithubOrganization> organizations;
}
