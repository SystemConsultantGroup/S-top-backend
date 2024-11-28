package com.scg.stop.proposal.persistence;

import static com.scg.stop.proposal.domain.Proposal.createProposal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.scg.stop.file.domain.File;
import com.scg.stop.global.config.JpaAuditingConfig;
import com.scg.stop.global.config.QueryDslConfig;
import com.scg.stop.proposal.domain.Proposal;
import com.scg.stop.proposal.repository.ProposalRepository;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({ QueryDslConfig.class, JpaAuditingConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Disabled
class ProposalRepositoryCustomImplTest {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        User user1 = new User("socialId1");
        User user2 = new User("socialId2");
        user1.register("user1","email.com","010", UserType.ADMIN, "source");
        user2.register("user2","email.com","010", UserType.ADMIN, "source");
        em.persist(user1);
        em.persist(user2);

        List<File> files = Arrays.asList(
                File.of("uuid1", "name1", "application/pdf"),
                File.of("uuid2", "name2", "application/pdf"),
                File.of("uuid3", "name3", "application/pdf")
        );
        Proposal proposal1 = createProposal(user1, "A", "LAB", "email.com", "website.com", "특정내용 있", true, true, files);
        Proposal proposal2= createProposal(user1, "B", "LAB", "email.com", "website.com", "내용 없", true, true, files);
        Proposal proposal3 = createProposal(user2, "C", "LAB", "email.com", "website.com", "A있없", true, true, files);
        em.persist(proposal1);
        em.persist(proposal2);
        em.persist(proposal3);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("제목으로 필터링을 걸 수 있다.")
    void filterByTitle() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String scope = "title";
        String term = "A";

        // when
        Page<Proposal> result = proposalRepository.filterProposals(scope, term, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("A");
    }

    @Test
    @DisplayName("내용으로 필터링을 걸 수 있다.")
    void filterByContent() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String scope = "content";
        String term = "특정내용";

        // when
        Page<Proposal> result = proposalRepository.filterProposals(scope, term, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getContent()).contains("특정내용");
    }

    @Test
    @DisplayName("올바르지 않은 필터는 에러를 반환한다.")
    void filterInvalidScope() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String scope = "invalid";
        String term = "content";

        // when & then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> proposalRepository.filterProposals(scope, term, pageable));
    }

    @Test
    @DisplayName("필터 결과가 없는 경우 빈 페이지를 반환한다.")
    void noMatchingProposals() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String scope = "title";
        String term = "Nonexistent";

        // when
        Page<Proposal> result = proposalRepository.filterProposals(scope, term, pageable);

        // then
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("필터가 제목+내용 인 경우 필터링 할 수 있다.")
    void filterByBoth() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String scope = "both";
        String term = "A";

        // when
        Page<Proposal> result = proposalRepository.filterProposals(scope, term, pageable);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("필터가 작성자인 경우 필터링 할 수 있다.")
    void filterByAuthor() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String scope = "author";
        String term = "user2";

        // when
        Page<Proposal> result = proposalRepository.filterProposals(scope, term, pageable);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getUser().getName()).isEqualTo("user2");
    }
}