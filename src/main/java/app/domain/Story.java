package app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Story extends AbstractPersistable<Long> {

    @Size(max = 1000)
    private String heading;
    @Size(max = 5000)
    private String lead;

    @OneToOne
    @Lob
    private Image image;
    private Long kuvaId;

    @Size(max = 10000)
    private String text;
    private LocalDateTime localTime;

    @ManyToMany
    private List<Author> authorList;
    @ManyToMany
    private List<Category> categoryList;
    
    private int visits;
    
    public Story(String heading, String lead, String text, LocalDateTime localTime) {
        this.heading = heading;
        this.lead = lead;
        this.text = text;
        this.localTime = localTime;
        this.visits = 0;
        this.authorList = new ArrayList();
        this.categoryList = new ArrayList();
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        this.kuvaId = image.getId();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getReleaseTime() {
        return localTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.localTime = releaseTime;
    }
    
    public List<Author> getAuthorList() {
        return this.authorList;
    }
    
    public void setAuthorList(List<Author> authors) {
        this.authorList = authors;
    }
    
    public List<Category> getCategoryList() {
        return this.categoryList;
    }
    
    public void setCategoryList(List<Category> categories) {
        this.categoryList = categories;
    }

    public void incrementVisists() {
        this.visits++;
    }
}
