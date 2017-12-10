
package app.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author extends AbstractPersistable<Long>{
    
    private String name;
    @ManyToMany(mappedBy = "authorList")
    private List<Story> storyList;
    
    public Author(String name) {
        this.name = name;
        this.storyList = new ArrayList();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setStoryList(List<Story> storyList) {
        this.storyList = storyList;
    }
    
    public List<Story> getStoryList() {
        return this.storyList;
    }
}
