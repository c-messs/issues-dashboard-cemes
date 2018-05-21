package io.spring.demo.issuesdashboard.events;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author messaoud
 *
 */
public interface GithubProjectRepository extends PagingAndSortingRepository<GithubProject, Long>{

	GithubProject findByRepoName(String repoName);
}
