package org.thevlad.githubresume.services;

import java.io.IOException;

import org.thevlad.githubresume.model.GithubUser;

public interface IGithubUserService {

	GithubUser findGithubUser(String userName) throws IOException;
}
