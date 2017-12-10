
package app.repository;

import app.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long>{

    public Author findByName(String author);
    
}
