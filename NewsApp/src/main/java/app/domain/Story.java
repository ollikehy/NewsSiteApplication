
package app.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Story extends AbstractPersistable<Long>{

    public String heading;
    public String lead;
    public String story;
    public LocalDateTime date;
    
    @OneToOne
    @JoinColumn
    public Image image;
    
    public Story(String heading, String lead, String story, LocalDateTime date) {
        this.heading = heading;
        this.lead = lead;
        this.story = story;
        this.date = date;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
