package org.thevlad.githubresume.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepository {
	private String name, description;
	private Date createdAt, updatedAt;
	private int stargazersCount, forks;
}
