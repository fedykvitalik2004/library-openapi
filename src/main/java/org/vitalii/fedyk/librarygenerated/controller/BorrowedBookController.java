package org.vitalii.fedyk.librarygenerated.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.librarygenerated.api.controller.BorrowedBooksApi;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.service.BorrowedBookService;

@RestController
@AllArgsConstructor
public class BorrowedBookController implements BorrowedBooksApi {
    private BorrowedBookService borrowedBookService;

    @Override
    public ReadBorrowedBookDto addBorrowedBook(CreateBorrowedBookDto createBorrowedBookDto) {
        return borrowedBookService.add(createBorrowedBookDto);
    }

    @Override
    public void deleteBorrowedBook(Long bookId, Long userId) {
        borrowedBookService.remove(bookId, userId);
    }
}
