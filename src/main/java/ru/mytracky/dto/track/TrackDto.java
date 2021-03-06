package ru.mytracky.dto.track;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.mytracky.model.Status;
import ru.mytracky.model.Track;
import ru.mytracky.model.User;

import javax.validation.constraints.NotBlank;
import java.util.Date;

import static ru.mytracky.model.EntityConstrainConstants.NOT_EMPTY;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackDto {

    private Long id;

    @NotBlank(message = "Track name " + NOT_EMPTY)
    private String name;

    private boolean personal;


    public Track toTrack(User user){
        Track track = new Track();
        track.setId(id);
        track.setPersonal(personal);
        track.setName(name);
        track.setCreated(new Date());
        track.setUpdated(new Date());
        track.setStatus(Status.ACTIVE);
        track.setUser(user);
        return track;
    }

    public static TrackDto fromTrack(Track track){
        TrackDto trackDto = new TrackDto();
        trackDto.setName(track.getName());
        trackDto.setId(track.getId());
        trackDto.setPersonal(track.isPersonal());

        return trackDto;
    }

}
