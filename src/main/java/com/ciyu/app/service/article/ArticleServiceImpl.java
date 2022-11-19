package com.ciyu.app.service.article;

import com.ciyu.app.pojo.Article;
import com.ciyu.app.pojo.Bond;
import com.ciyu.app.pojo.User;
import com.ciyu.app.repository.ArticleRepo;
import com.ciyu.app.service.bond.BondService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepo articleRepo;
    private final BondService bondService;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchHits<Article> searchBestArticlesByUser(User user, Pageable pageable, List<String> excludedArticleIds) {
        List<Bond> bonds = bondService.findAllBondsByUser(user);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        bonds.forEach(bond -> {
            double strength = bondService.calculateStrengthByBond(bond);
            log.info(String.format("%s: %f", bond.getWord().getId(), strength));
            double boost = strength * (1 - strength);
            boolQueryBuilder.should(new MatchQueryBuilder("content", bond.getWord().getId()).boost((float)boost));
            boolQueryBuilder.should(new MatchQueryBuilder("title", bond.getWord().getId()).boost((float)boost));
        });
        IdsQueryBuilder idsQueryBuilder = new IdsQueryBuilder();
        excludedArticleIds.forEach(idsQueryBuilder::addIds);
        boolQueryBuilder.mustNot(idsQueryBuilder);

        String preTag = "<em>";
        String postTag = "</em>";
        Query searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag).numOfFragments(0),
                        new HighlightBuilder.Field("content").preTags(preTag).postTags(postTag).numOfFragments(0))
                .build();

        return elasticsearchOperations
                        .search(searchQuery,
                                Article.class,
                                IndexCoordinates.of("ciyu"));
    }

    @Override
    public SearchHits<Article> searchArticlesByUserAndWordId(User user, String wordId, Pageable pageable) {
        List<Bond> bonds = bondService.findAllBondsByUser(user);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(new MatchQueryBuilder("content", wordId));
        boolQueryBuilder.should(new MatchQueryBuilder("title", wordId));
        bonds.forEach(bond -> {
            double strength = bondService.calculateStrengthByBond(bond);
            log.info(String.format("%s: %f", bond.getWord().getId(), strength));
            double boost = strength * (1 - strength);
            boolQueryBuilder.should(new MatchQueryBuilder("content", bond.getWord().getId()).boost(0));
            boolQueryBuilder.should(new MatchQueryBuilder("title", bond.getWord().getId()).boost(0));
        });
        IdsQueryBuilder idsQueryBuilder = new IdsQueryBuilder();
        boolQueryBuilder.mustNot(idsQueryBuilder);

        String preTag = "<em>";
        String postTag = "</em>";
        Query searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag).numOfFragments(0),
                        new HighlightBuilder.Field("content").preTags(preTag).postTags(postTag).numOfFragments(0))
                .build();

        return elasticsearchOperations
                .search(searchQuery,
                        Article.class,
                        IndexCoordinates.of("ciyu"));
    }
}
