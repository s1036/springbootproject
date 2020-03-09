package com.padakeria.project.springbootproject.boards;

import com.padakeria.project.springbootproject.boards.dto.BoardDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

 class BoardDtoResource extends Resource<BoardDto> {
     BoardDtoResource(BoardDto board, Long id, Link... links) {
        super(board, links);
        add(linkTo(BoardController.class).slash(id).withSelfRel());
    }
}
