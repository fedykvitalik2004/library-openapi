package org.vitalii.fedyk.librarygenerated.service;


import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBorrowedBookDto;

import java.util.List;


public interface BorrowedBookService {
    ReadBorrowedBookDto add(CreateBorrowedBookDto createBorrowedBookDto);

    boolean isBorrowedByUser(Long userId);

    boolean isBorrowedByBook(Long bookId);

    void remove(Long bookId, Long userId);

    List<ReadBorrowedBookDto> getBorrowedBooksForUser(Long userId);
}
