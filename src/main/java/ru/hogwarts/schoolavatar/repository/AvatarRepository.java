package ru.hogwarts.schoolavatar.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.schoolavatar.entity.Avatar;

public interface AvatarRepository extends JpaRepository <Avatar, Long> {
        Optional <Avatar> findByStudentId(Long studentId);

}
