package com.zerook.b01.service;

import com.zerook.b01.dto.PageRequestDTO;
import com.zerook.b01.dto.PageResponseDTO;
import com.zerook.b01.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

}
