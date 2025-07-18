package br.com.net.sqlab_backend.domain.professor.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.shared.models.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor; // Manter esta anotação
import lombok.AllArgsConstructor; // ADICIONAR esta anotação se quiser o construtor com todos os argumentos via Lombok

@Data
@Table(name = "professor")
@Entity
@NoArgsConstructor // Manter para o construtor padrão exigido pelo JPA/Hibernate
@AllArgsConstructor // Adicionar esta anotação para gerar um construtor com todos os argumentos via Lombok
public class Professor implements UserEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private transient String confirmPassword;

    @ManyToMany
    @JoinTable(
        name = "professor_grade",
        joinColumns = @JoinColumn(name = "professor_id"),
        inverseJoinColumns = @JoinColumn(name = "grade_id")
    )
    private Set<Grade> grades = new HashSet<>();

    // Se você tem um construtor manual para Professor que recebe nome, email, senha, etc.,
    // e ele não inclui o ID ou coleções como 'grades', pode haver a mesma questão.
    // Com @AllArgsConstructor do Lombok, um construtor com todos os campos persistidos
    // e com o 'id' e 'grades' seria gerado.
    // Se o construtor manual existe e é o único, o Lombok não gerará o padrão sem argumentos.
    // A melhor prática é depender do Lombok para gerar os construtores se eles são simples.
    // Exemplo de um construtor manual que poderia existir e causar problemas se fosse o único:
    /*
    public Professor(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.grades = new HashSet<>(); // Inicialização de coleção
    }
    */
    // Se não há um construtor manual explícito além do que Lombok gera, as anotações
    // @NoArgsConstructor e @AllArgsConstructor devem ser suficientes.
    // Deixei o @AllArgsConstructor no Professor por segurança, caso um construtor manual
    // estivesse implicitamente impedindo o @NoArgsConstructor.

    // Implementações de UserDetails (via UserEntity)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
    }
    
    @Override
    public String getUsername() {
        return email;
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

    // Getters e Setters (já gerados por @Data)
    // Listados aqui apenas para referência, se você os removeu do código
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public Set<Grade> getGrades() { return grades; }
    public void setGrades(Set<Grade> grades) { this.grades = grades; }
}