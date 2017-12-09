package app.domain;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractPersistable<Long> {

    @OneToOne
    @JoinColumn
    private Story story;

    @NotBlank
    @Length(min = 1, max = 100)
    private String name;

    @NotBlank
    @Length(min = 1, max = 100)
    private String contentType;

    private Long contentLength;
    private LocalDateTime localDateTime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    public Image(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }

    public LocalDateTime getReleaseTime() {
        return this.localDateTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.localDateTime = releaseTime;
    }

    public Story getStory() {
        return this.story;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getLocalTime() {
        return localDateTime;
    }

    public void setLocalTime(LocalDateTime localTime) {
        this.localDateTime = localTime;
    }

}
