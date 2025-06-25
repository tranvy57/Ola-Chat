package vn.edu.iuh.fit.olachatbackend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.CommentHierarchyResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.PostResponse;
import vn.edu.iuh.fit.olachatbackend.entities.Comment;
import vn.edu.iuh.fit.olachatbackend.entities.Like;
import vn.edu.iuh.fit.olachatbackend.entities.Post;
import vn.edu.iuh.fit.olachatbackend.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "likedUsers", expression = "java(mapLikedUsers(post.getLikes()))")
    @Mapping(target = "comments", expression = "java(commentListToCommentHierarchyResponseList(post.getComments()))")
    @Mapping(target = "originalPostId", source = "originalPost.postId")
    @Mapping(target = "originalPost", expression = "java(post.getOriginalPost() != null ? toPostResponse(post.getOriginalPost()) : null)")
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);

    List<CommentHierarchyResponse> commentListToCommentHierarchyResponseList(List<Comment> comments);

    default List<User> mapLikedUsers(List<Like> likes) {
        if (likes == null) {
            return List.of(); // Return an empty list if likes is null
        }
        return likes.stream()
                .map(Like::getLikedBy)
                .collect(Collectors.toList());
    }

}