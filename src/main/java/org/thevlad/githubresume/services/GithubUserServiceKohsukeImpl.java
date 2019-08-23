package org.thevlad.githubresume.services;

import java.io.IOException;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;
import org.thevlad.githubresume.model.GithubUser;
import org.thevlad.githubresume.web.DtoUtil;

@Component
public class GithubUserServiceKohsukeImpl implements IGithubUserService {


	@Override
	public GithubUser findGithubUser(String userName) throws IOException {
		GitHub github = GitHub.connectAnonymously();
		GHUser user = github.getUser(userName);
		GithubUser userDTO = DtoUtil.buildUserDTO(user);
		return userDTO;
	}

}
