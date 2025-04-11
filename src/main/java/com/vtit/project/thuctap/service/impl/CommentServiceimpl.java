package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.CreateCommentRequest;
import com.vtit.project.thuctap.dto.request.UpdateCommentRequest;
import com.vtit.project.thuctap.dto.response.CommentDTO;
import com.vtit.project.thuctap.entity.Comment;
import com.vtit.project.thuctap.entity.Post;
import com.vtit.project.thuctap.entity.User;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.CommentRepository;
import com.vtit.project.thuctap.repository.PostRepository;
import com.vtit.project.thuctap.repository.UserRepository;
import com.vtit.project.thuctap.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceimpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Page<CommentDTO> findCommentsByPostId(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.POST));

        Page<Comment> commentPage = commentRepository.findByPost(postId, pageable);
        List<CommentDTO> commentDTOS = commentPage.getContent().stream()
                .map(comment -> {
                    return modelMapper.map(comment, CommentDTO.class);
                        }
                        ).toList();
        return new PageImpl<>(commentDTOS, pageable, commentPage.getTotalElements());
    }

    @Override
    public CommentDTO findCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.COMMENT));
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO save(CreateCommentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.POST));
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPostId(post);
        comment.setUserId(user);
        commentRepository.save(comment);

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO updateComment(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.getId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.COMMENT));
        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.COMMENT);
        }
        commentRepository.deleteById(id);
    }
}
