package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.CreatePostRequest;
import com.vtit.project.thuctap.dto.request.UpdatePostRequest;
import com.vtit.project.thuctap.dto.response.PostDTO;
import com.vtit.project.thuctap.dto.response.UserDTO;
import com.vtit.project.thuctap.entity.Post;
import com.vtit.project.thuctap.entity.User;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.CommentRepository;
import com.vtit.project.thuctap.repository.PostRepository;
import com.vtit.project.thuctap.repository.UserRepository;
import com.vtit.project.thuctap.service.PostService;
import com.vtit.project.thuctap.service.UserService;
import com.vtit.project.thuctap.utlis.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<PostDTO> findPosts(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Page<Post> postPage = postRepository.findAllByUserId(pageable, userId);
        List<PostDTO> postDTOList = postPage.getContent().stream()
                .map(post ->{
//                    post.setCreatedBy(user);
                    return modelMapper.map(post, PostDTO.class);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(postDTOList, pageable, postPage.getTotalElements());
    }

    @Override
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.POST));
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO save(CreatePostRequest request) {
        User user = userRepository.findById((long) request.getCreatedBy())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Post post = modelMapper.map(request, Post.class);
        post.setCreatedBy(user);
        post.setPublishedAt(TimeUtil.getCurrentTimestamp());

        postRepository.save(post);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setCreatedBy(modelMapper.map(user, UserDTO.class));
        return postDTO;
    }

    @Override
    public PostDTO update(UpdatePostRequest request) {
        User user = userRepository.findById((long) request.getCreatedBy())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Post post = modelMapper.map(request, Post.class);
        post.setUpdatedBy(user);
        postRepository.save(post);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setUpdatedBy(modelMapper.map(user, UserDTO.class));
        return postDTO;
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.POST));
        postRepository.delete(post);

    }
}
