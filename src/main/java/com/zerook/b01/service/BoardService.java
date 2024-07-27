package com.zerook.b01.service;

import com.zerook.b01.dto.BoardDTO;
import com.zerook.b01.dto.PageRequestDTO;
import com.zerook.b01.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

}
