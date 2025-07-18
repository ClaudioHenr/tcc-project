package br.com.net.sqlab_backend.authentication.repository;

import br.com.net.sqlab_backend.domain.shared.models.UserEntity;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;
import br.com.net.sqlab_backend.domain.professor.repository.SelfRegistrationProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final StudentRepository studentRepo;
    private final SelfRegistrationProfessorRepository professorRepo;

    public UserRepositoryImpl(StudentRepository studentRepo, SelfRegistrationProfessorRepository professorRepo) {
        this.studentRepo = studentRepo;
        this.professorRepo = professorRepo;
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return studentRepo.findByEmail(email)
                .map(s -> (UserEntity) s)
                .or(() -> professorRepo.findByEmail(email)
                .map(p -> (UserEntity) p));
    }
}
