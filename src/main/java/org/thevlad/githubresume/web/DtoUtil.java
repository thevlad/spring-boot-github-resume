package org.thevlad.githubresume.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPersonSet;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.thevlad.githubresume.model.GithubOrganization;
import org.thevlad.githubresume.model.GithubRepository;
import org.thevlad.githubresume.model.GithubUser;
import org.thevlad.githubresume.model.GithubUserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DtoUtil {

	public static GithubUser buildUserDTO(GHUser user) throws IOException {
		GithubUser userDTO = from(user);
		buildByLangRepos(user, userDTO);
		return userDTO;
	}

	private static void buildByLangRepos(GHUser user, GithubUser userDTO) throws IOException {
		Map<String, GHRepository> repos = user.getRepositories();
		List<GHRepository> byPopularityOrig = new ArrayList<GHRepository>();
		List<GHRepository> byPopularity;
		List<GHRepository> byActivity;
		byPopularityOrig.addAll(repos.values());
		Map<String, Integer> byLang = new HashMap<String, Integer>();
		int total = 0;
		for (Iterator<Entry<String, GHRepository>> iterator = repos.entrySet().iterator(); iterator.hasNext();) {
			GHRepository repo = iterator.next().getValue();
//			log.info("Repo: {}, Owner: {}", repo.getName(), repo.getOwnerName());
			String lang = repo.getLanguage();
			if (lang == null)
				continue;
			Integer count = byLang.get(lang);
			if (count == null) {
				byLang.put(lang, 1);
			} else {
				byLang.put(lang, count + 1);
			}
			total++;
		}
		SortedSet<Map.Entry<String, Integer>> sortedRepos = sortEntriesByValues(byLang);
		Map<String, Integer> byLangResult = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : sortedRepos) {
			byLangResult.put(entry.getKey(), entry.getValue());
		}
		userDTO.setByLangRepos(byLangResult);
		userDTO.setTotalRepoCount(total);

		byPopularityOrig
				.sort(Comparator
						.comparing(GHRepository::getStargazersCount)
						.thenComparing(GHRepository::getForks)
						.reversed());
		if (byPopularityOrig.size() > 5) {
			byPopularity = new ArrayList<GHRepository>(byPopularityOrig.subList(0, 5));

		} else {
			byPopularity = byPopularityOrig;
		}
		userDTO.setByPopularityRepos(fromGHRepository(byPopularity));

		TreeMap<Date, GHRepository> byLastActivity = new TreeMap<Date, GHRepository>();
		for (GHRepository ghRepository : byPopularity) {

			Date last = ghRepository.getUpdatedAt();
			byLastActivity.put(last, ghRepository);
		}
		NavigableSet<Date> commitDatesSet = byLastActivity.descendingKeySet();
		byActivity = new ArrayList<GHRepository>();
		int count = 0;
		for (Date date : commitDatesSet) {
			if (count >= 5)
				break;
			byActivity.add(byLastActivity.get(date));
		}
		userDTO.setByActivity(fromGHRepository(byActivity));
		GHPersonSet<GHOrganization> ghOrganizationsSet = user.getOrganizations();
		List<GHOrganization> orgs = new ArrayList<GHOrganization>();
		count = 0;
		if (ghOrganizationsSet != null && ghOrganizationsSet.size() > 0) {
			for (GHOrganization ghOrganization : ghOrganizationsSet) {
				if (count >= 5)
					break;
				orgs.add(ghOrganization);
				count++;
			}
			List<GithubOrganization> organizations = fromGHOrganization(orgs);
			userDTO.setOrganizations(organizations);
		}

	}

	private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> sortEntriesByValues(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
				new Comparator<Map.Entry<K, V>>() {
					@Override
					public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
						int res = e2.getValue().compareTo(e1.getValue());
						return res != 0 ? res : 1;
					}
				});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	public static GithubUser from(GHUser user) throws IOException {
		GithubUser userDTO = new GithubUser();
		GithubUserDetail userDetail = new GithubUserDetail(user.getLogin(), user.getAvatarUrl(), user.getName(),
				user.getEmail(), user.getHtmlUrl().toString(),user.getCreatedAt());
		userDTO.setUser(userDetail);

		return userDTO;
	}

	public static GithubRepository from(GHRepository repo) throws IOException {
		GithubRepository repository = new GithubRepository(repo.getName(), repo.getDescription(), repo.getCreatedAt(),
				repo.getUpdatedAt(), repo.getStargazersCount(), repo.getForks());
		return repository;
	}

	public static List<GithubRepository> fromGHRepository(List<GHRepository> repos) throws IOException {
		List<GithubRepository> repositories = new LinkedList<GithubRepository>();
		for (GHRepository ghRepository : repos) {
			GithubRepository repository = from(ghRepository);
			repositories.add(repository);
		}
		return repositories;
	}

	public static GithubOrganization from(GHOrganization org) throws IOException {
		GithubOrganization organization = new GithubOrganization(org.getName(), org.getLocation(),
				org.getHtmlUrl().toString(), org.getCreatedAt());
		return organization;
	}

	public static List<GithubOrganization> fromGHOrganization(List<GHOrganization> orgs) throws IOException {
		List<GithubOrganization> organizations = new LinkedList<GithubOrganization>();
		for (GHOrganization ghOrganization : orgs) {
			String htmlUrl = ghOrganization.getHtmlUrl().toString();
			String name = ghOrganization.getName();
			if (StringUtils.isEmpty(name) && !StringUtils.isEmpty(htmlUrl)) {
				int idx = htmlUrl.lastIndexOf('/');
				name = htmlUrl.substring(idx + 1);
			}
			GithubOrganization organization = new GithubOrganization(name, ghOrganization.getLocation(), htmlUrl,
					ghOrganization.getCreatedAt());

			organizations.add(organization);
		}
		return organizations;
	}
}
