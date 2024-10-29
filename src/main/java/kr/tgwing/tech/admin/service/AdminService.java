package kr.tgwing.tech.admin.service;

import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import kr.tgwing.tech.admin.dto.AllStudentsDto;
import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.repository.ReplyRepository;
import kr.tgwing.tech.user.entity.TempUser;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.TempUserRepository;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    public Page<AdminCheckUserDto> checkAssingments(Pageable pageable) {
        Page<TempUser> allAssignments = tempUserRepository.findAll(pageable);
        if(allAssignments.isEmpty()) return null; // 여기 값은 예외로 보내야하느건지, 어떻게 보내야하는거지?
        return allAssignments.map(assignment -> AdminCheckUserDto.of(assignment));

//        List<AdminCheckUserDto> dtoList = new ArrayList<>();
//        for(TempUser user : allUser) {
//            AdminCheckUserDto dto = user.toAdminCheckUserDto(user);
//            dtoList.add(dto);
//        }

//        return dtoList;
    }

    @Transactional
    public Long registerUsers(Long studentId) {
        TempUser notUser = tempUserRepository.findById(studentId).orElseThrow(UserNotFoundException::new);
        User user = notUser.toUser(notUser);
        tempUserRepository.deleteById(studentId);

        user.setRole("ROLE_USER");
        userRepository.save(user);

        return user.getStudentId();
    }

    public Long refuseUsers(Long studentId) {
        TempUser user = tempUserRepository.findById(studentId).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(studentId);
        return user.getStudentId();
    }

    public Page<AllStudentsDto> checkAllStudents(Pageable pageable) {
        Page<User> allStudents = userRepository.findAll(pageable);
        if(allStudents.isEmpty()) return null;
        return allStudents.map( student -> AllStudentsDto.of(student));
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        User user = userRepository.findById(studentId).orElseThrow(UserNotFoundException::new);
        List<Post> postByUser = postRepository.findByWriter(user);

        userRepository.deleteById(studentId);
        if(!postByUser.isEmpty()) postRepository.deleteAllByWriter(user);
    }
}
