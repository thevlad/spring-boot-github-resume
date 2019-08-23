package org.thevlad.githubresume.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubOrganization {
	private String name, location, htmlUrl;
	private Date createdAt;
}
