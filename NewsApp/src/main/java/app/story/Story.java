
package app.story;

import javax.persistence.Entity;
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
}
