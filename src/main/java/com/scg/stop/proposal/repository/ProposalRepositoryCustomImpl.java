package com.scg.stop.proposal.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scg.stop.proposal.domain.Proposal;
import com.scg.stop.proposal.domain.QProposal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProposalRepositoryCustomImpl implements ProposalRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Proposal> filterProposals(String scope, String term, Pageable pageable) {
        QProposal proposal = QProposal.proposal;

        BooleanExpression predicate = null;
        if (scope != null && term != null) {
            predicate = switch (scope) {
                case "author" -> proposal.user.name.contains(term);
                case "title" -> proposal.title.contains(term);
                case "both" -> proposal.title.contains(term).or(proposal.content.contains(term)).or(proposal.user.name.contains(term));
                case "content" -> proposal.content.contains(term);
                default -> throw new IllegalArgumentException("Invalid scope");
            };
        }

        //TODO: offset 기반 페이징 수정
        List<Proposal> content = queryFactory.selectFrom(proposal)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(proposal.count())
                        .from(proposal)
                        .where(predicate)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
