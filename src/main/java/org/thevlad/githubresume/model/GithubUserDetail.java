package org.thevlad.githubresume.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUserDetail {

	private String login;
	private String avatarUrl;
	private String Name;
	private String email;
	private String htmlUrl;
	private Date createdAt;

}
