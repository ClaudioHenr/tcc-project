package br.com.net.sqlab_backend.domain.student.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.relatory.dto.RelatoryResponseDTO;
import br.com.net.sqlab_backend.domain.shared.models.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor; // Mantenha esta anotação
import lombok.AllArgsConstructor; // ADICIONAR esta anotação se quiser o construtor com todos os argumentos via Lombok

@Data
@Table(name = "student")
@Entity
@NoArgsConstructor // Manter para o construtor padrão exigido pelo JPA/Hibernate
@AllArgsConstructor // Adicionar esta anotação para gerar um construtor com todos os argumentos via Lombok
public class Student implements UserEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "registration_number", nullable = false, length = 100, unique = true)
    private String registrationNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String confirmEmail;

    @Transient
    private String confirmPassword;

    @ManyToMany
    @JoinTable(
        name = "student_grade",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "grade_id")
    )
    private Set<Grade> grades = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnswerStudent> studentAnswers = new HashSet<>();
    
    public Student(String name, String email, String registrationNumber, String password, String confirmEmail,
                   String confirmPassword) {
        this.name = name;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.password = password;
        this.confirmEmail = confirmEmail;
        this.confirmPassword = confirmPassword;
        this.grades = new HashSet<>();
        this.studentAnswers = new HashSet<>();
    }
    
    public Student() {
		super();
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public Set<AnswerStudent> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(Set<AnswerStudent> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    // Se RelatoryResponseDTO não for uma coleção em Student, este método pode ser ajustado
    public Collection<RelatoryResponseDTO> getAttempts() {
        // Exemplo: se você precisa de uma lista de tentativas formatadas,
        // isso precisaria de lógica para transformar 'studentAnswers' em 'RelatoryResponseDTO'
        return null; // Ou retornar uma lista vazia ou implementar a lógica de transformação
    }
}