package org.thevlad.githubresume.web;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {

	@Size(min = 1, message = "User Name cannot be less than 1 characters!")
	@Pattern(message = "User name ${validatedValue} for search not valid!", regexp = "^[a-z\\d](?:[a-z\\d]|-(?=[a-z\\d])){1,38}$", flags = {
			Pattern.Flag.CASE_INSENSITIVE })
	private String userName;

}
