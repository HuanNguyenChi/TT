package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.dto.response.PostDTO;
import com.vtit.project.thuctap.entity.Book;
import com.vtit.project.thuctap.entity.Post;
import com.vtit.project.thuctap.repository.BookRepository;
import com.vtit.project.thuctap.repository.PostRepository;
import com.vtit.project.thuctap.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;


    @Override
    public List<BookDTO> statisticBookByCategory(Long categoryId) {
        List<Book> books = bookRepository.findAllByCategory(categoryId);
        List<BookDTO> dtos = books.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<PostDTO> getPostByReact(Pageable pageable) {
        List<Post> posts = postRepository.findAllByReact(pageable);
        List<PostDTO> dtos = posts.stream().map(post -> modelMapper.map(post, PostDTO.class)).collect(Collectors.toList());
        return dtos;
    }
}
