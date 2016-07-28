package com.confluent.feed.controller;

import com.confluent.feed.Util.ValidatorUtil;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(value = "article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveArticle(@Valid @RequestBody ArticleModel articleModel) {
        if (ValidatorUtil.validateArticle(articleModel)) {
            return new ResponseEntity<>(articleService.save(articleModel), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Request", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<ArticleModel>> getAllArticles() {
        return new ResponseEntity<>(articleService.getAllArticles(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{articleGuid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArticleModel> getFeed(@PathVariable("articleGuid") @NotNull @Valid String articleGuid) {
        return new ResponseEntity<>(articleService.getArticleByGuid(articleGuid), HttpStatus.OK);
    }


}
