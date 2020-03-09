package com.padakeria.project.springbootproject.boards;


import com.padakeria.project.springbootproject.accounts.Account;
import com.padakeria.project.springbootproject.accounts.AccountRepository;
import com.padakeria.project.springbootproject.boards.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/board", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class BoardController {

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;

    private final AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity createBoard(@RequestBody @Valid BoardDto boardDto, Errors errors, @AuthenticationPrincipal OAuth2User user) {

        Account account = accountRepository.findByEmail((String) user.getAttributes().get("email")).get();

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();
        board.addAccount(account);


        Board savedBoard = boardRepository.save(board);
        BoardDto responseBoardDto = modelMapper.map(savedBoard, BoardDto.class);

        BoardDtoResource boardDtoResource = new BoardDtoResource(responseBoardDto, savedBoard.getId());
        return ResponseEntity.ok(boardDtoResource);

        // TODO: 2020-03-10 에러 처리, 서비스 분리, 이상한거 수정
    }

}
