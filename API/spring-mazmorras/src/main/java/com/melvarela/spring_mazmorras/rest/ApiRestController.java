package com.melvarela.spring_mazmorras.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.CharacterEntity;
import com.melvarela.spring_mazmorras.entities.CreatureEntity;
import com.melvarela.spring_mazmorras.entities.NoteEntity;
import com.melvarela.spring_mazmorras.entities.ObjectEntity;
import com.melvarela.spring_mazmorras.entities.PlaceEntity;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CampaignDto;
import com.melvarela.spring_mazmorras.rest.dtos.CharacterDto;
import com.melvarela.spring_mazmorras.rest.dtos.CreatureDto;
import com.melvarela.spring_mazmorras.rest.dtos.NoteDto;
import com.melvarela.spring_mazmorras.rest.dtos.ObjectDto;
import com.melvarela.spring_mazmorras.rest.dtos.PlaceDto;
import com.melvarela.spring_mazmorras.rest.dtos.UserDto;
import com.melvarela.spring_mazmorras.rest.dtos.UserRelationDto;
import com.melvarela.spring_mazmorras.rest.mappers.CampaignDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.CharacterDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.CreatureDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.NoteDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.ObjectDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.PlaceDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.UserDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.UserRelationDtoMapper;
import com.melvarela.spring_mazmorras.services.CampaignService;
import com.melvarela.spring_mazmorras.services.CharacterService;
import com.melvarela.spring_mazmorras.services.CreatureService;
import com.melvarela.spring_mazmorras.services.NoteService;
import com.melvarela.spring_mazmorras.services.ObjectService;
import com.melvarela.spring_mazmorras.services.PlaceService;
import com.melvarela.spring_mazmorras.services.UserRelationService;
import com.melvarela.spring_mazmorras.services.UserService;

@RestController
@RequestMapping("/api/v1/")
public class ApiRestController {

    @Autowired
    UserService userService;
    @Autowired
    CampaignService campaignService;
    @Autowired
    CharacterService characterService;
    @Autowired
    CreatureService creatureService;
    @Autowired
    NoteService noteService;
    @Autowired
    ObjectService objectService;
    @Autowired
    PlaceService placeService;
    @Autowired
    UserRelationService userRelationService;

    //Users
    @GetMapping("user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") String userId){
        UserDto user = UserDtoMapper.userEntityToUserDto(userService.findById(userId));
        if(user.getName() != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("user")
    public ResponseEntity<UserDto> postUser(@RequestBody UserDto user){
        System.out.println("Create user: " + user.toString());

        try{
            return new ResponseEntity<>(UserDtoMapper.userEntityToUserDto(
                userService.createUser(UserDtoMapper.userDtoToUserEntity(user))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("user")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user){
        System.out.println("Update user: " + user.toString());

        try{
            return new ResponseEntity<>(UserDtoMapper.userEntityToUserDto(
                userService.updateUser(UserDtoMapper.userDtoToUserEntity(user))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("user")
    public ResponseEntity<UserDto> deleteUser(@RequestBody UserDto user){
        System.out.println("Delete user: " + user.toString());

        try{
            return new ResponseEntity<>(UserDtoMapper.userEntityToUserDto(
                userService.deleteUser(UserDtoMapper.userDtoToUserEntity(user))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Campaigns
    @GetMapping("campaigns/{playerId}")
    public ResponseEntity<List<CampaignDto>> getAllCampaignsOf(@PathVariable("playerId") String playerId){
        List<CampaignDto> campaignsDto = new ArrayList<>();
        List<CampaignEntity> campaigns = campaignService.findAllByPlayer(playerId);

        for (CampaignEntity campaign : campaigns) {
            campaignsDto.add(CampaignDtoMapper.campaignEntityToDto(campaign));
        }

        return new ResponseEntity<>(campaignsDto, HttpStatus.OK);
    }

    @GetMapping("campaign/{id}")
    public ResponseEntity<CampaignDto> getCampaign(@PathVariable("id") String id){
        CampaignEntity campaign = campaignService.findById(id);
        if(campaign.getName() != null){
            return new ResponseEntity<>(CampaignDtoMapper.campaignEntityToDto(campaign), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CampaignDto(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("campaign")
    public ResponseEntity<CampaignDto> postCampaign(@RequestBody CampaignDto campaign){
        System.out.println("Create campaign: " + campaign.toString());

        try{
            return new ResponseEntity<>(CampaignDtoMapper.campaignEntityToDto(
                campaignService.createCampaign(CampaignDtoMapper.campaignDtoToEntity(campaign))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CampaignDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("campaign")
    public ResponseEntity<CampaignDto> updateCampaign(@RequestBody CampaignDto campaign){
        System.out.println("Update campaign: " + campaign.toString());

        try{
            return new ResponseEntity<>(CampaignDtoMapper.campaignEntityToDto(
                campaignService.updateCampaign(CampaignDtoMapper.campaignDtoToEntity(campaign))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CampaignDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("campaign")
    public ResponseEntity<CampaignDto> deleteCampaign(@RequestBody CampaignDto campaign){
        System.out.println("Delete campaign: " + campaign.toString());

        try{
            return new ResponseEntity<>(CampaignDtoMapper.campaignEntityToDto(
                campaignService.deleteCampaign(CampaignDtoMapper.campaignDtoToEntity(campaign))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CampaignDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Characters
    @GetMapping("characters/{campaignId}")
    public ResponseEntity<List<CharacterDto>> getAllCharactersFrom(@PathVariable("campaignId") String campaignId){
        List<CharacterDto> charactersDto = new ArrayList<>();
        List<CharacterEntity> characters = characterService.findAllByCampaign(campaignId);

        for (CharacterEntity character : characters) {
            charactersDto.add(CharacterDtoMapper.characterEntityToDto(character));
        }

        return new ResponseEntity<>(charactersDto, HttpStatus.OK);
    }

    @GetMapping("character/{id}")
    public ResponseEntity<CharacterDto> getCharacter(@PathVariable("id") String id){
        CharacterEntity character = characterService.findById(id);
        if(character.getName() != null){
            return new ResponseEntity<>(CharacterDtoMapper.characterEntityToDto(character), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CharacterDto(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("character")
    public ResponseEntity<CharacterDto> postCharacter(@RequestBody CharacterDto character){
        System.out.println("Create character: " + character.toString());

        try{
            return new ResponseEntity<>(CharacterDtoMapper.characterEntityToDto(
                characterService.createCharacter(CharacterDtoMapper.characterDtoToEntity(character))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CharacterDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("character")
    public ResponseEntity<CharacterDto> updateCharacter(@RequestBody CharacterDto character){
        System.out.println("Update character: " + character.toString());

        try{
            return new ResponseEntity<>(CharacterDtoMapper.characterEntityToDto(
                characterService.updateCharacter(CharacterDtoMapper.characterDtoToEntity(character))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CharacterDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("character")
    public ResponseEntity<CharacterDto> deleteCharacter(@RequestBody CharacterDto character){
        System.out.println("Delete character: " + character.toString());

        try{
            return new ResponseEntity<>(CharacterDtoMapper.characterEntityToDto(
                characterService.deleteCharacter(CharacterDtoMapper.characterDtoToEntity(character))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CharacterDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Creatures
    @GetMapping("creatures/{campaignId}")
    public ResponseEntity<List<CreatureDto>> getAllCreatureFrom(@PathVariable("campaignId") String campaignId){
        List<CreatureDto> creaturesDto = new ArrayList<>();
        List<CreatureEntity> creatures = creatureService.findAllByCampaign(campaignId);

        for (CreatureEntity creature : creatures) {
            creaturesDto.add(CreatureDtoMapper.creatureEntityToDto(creature));
        }

        return new ResponseEntity<>(creaturesDto, HttpStatus.OK);
    }

    @GetMapping("creature/{id}")
    public ResponseEntity<CreatureDto> getCreature(@PathVariable("id") String id){
        CreatureEntity creature = creatureService.findById(id);
        if(creature.getName() != null){
            return new ResponseEntity<>(CreatureDtoMapper.creatureEntityToDto(creature), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CreatureDto(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("creature")
    public ResponseEntity<CreatureDto> postCreature(@RequestBody CreatureDto creature){
        System.out.println("Create creature: " + creature.toString());

        try{
            return new ResponseEntity<>(CreatureDtoMapper.creatureEntityToDto(
                creatureService.createCreature(CreatureDtoMapper.creatureDtoToEntity(creature))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CreatureDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("creature")
    public ResponseEntity<CreatureDto> updateCreature(@RequestBody CreatureDto creature){
        System.out.println("Update creature: " + creature.toString());

        try{
            return new ResponseEntity<>(CreatureDtoMapper.creatureEntityToDto(
                creatureService.updateCreature(CreatureDtoMapper.creatureDtoToEntity(creature))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CreatureDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("creature")
    public ResponseEntity<CreatureDto> deleteCreature(@RequestBody CreatureDto creature){
        System.out.println("Delete creature: " + creature.toString());

        try{
            return new ResponseEntity<>(CreatureDtoMapper.creatureEntityToDto(
                creatureService.deleteCreature(CreatureDtoMapper.creatureDtoToEntity(creature))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CreatureDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Notes
    @GetMapping("notes/{ownerId}")
    public ResponseEntity<List<NoteDto>> getAllNotesOf(@PathVariable("owner") String owner){
        List<NoteDto> notesDto = new ArrayList<>();
        List<NoteEntity> notes = noteService.findAllByOwner(owner);

        for (NoteEntity note : notes) {
            notesDto.add(NoteDtoMapper.noteEntityToDto(note));
        }

        return new ResponseEntity<>(notesDto, HttpStatus.OK);
    }

    @GetMapping("note/{id}")
    public ResponseEntity<NoteDto> getNote(@PathVariable("id") String id){
        NoteEntity note = noteService.findById(id);
        if(note.getName() != null){
            return new ResponseEntity<>(NoteDtoMapper.noteEntityToDto(note), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new NoteDto(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("note")
    public ResponseEntity<NoteDto> postNote(@RequestBody NoteDto note){
        System.out.println("Create note: " + note.toString());

        try{
            return new ResponseEntity<>(NoteDtoMapper.noteEntityToDto(
                noteService.createNote(NoteDtoMapper.noteDtoToEntity(note))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new NoteDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("note")
    public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto note){
        System.out.println("Update note: " + note.toString());

        try{
            return new ResponseEntity<>(NoteDtoMapper.noteEntityToDto(
                noteService.updateNote(NoteDtoMapper.noteDtoToEntity(note))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new NoteDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("note")
    public ResponseEntity<NoteDto> deleteNote(@RequestBody NoteDto note){
        System.out.println("Delete note: " + note.toString());

        try{
            return new ResponseEntity<>(NoteDtoMapper.noteEntityToDto(
                noteService.deleteNote(NoteDtoMapper.noteDtoToEntity(note))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new NoteDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Objects
    @GetMapping("objects/{campaignId}")
    public ResponseEntity<List<ObjectDto>> getAllObjectsFrom(@PathVariable("campaignId") String campaignId){
        List<ObjectDto> objectsDto = new ArrayList<>();
        List<ObjectEntity> objects = objectService.findAllByCampaign(campaignId);

        for (ObjectEntity obxecto : objects) {
            objectsDto.add(ObjectDtoMapper.objectEntityToDto(obxecto));
        }

        return new ResponseEntity<>(objectsDto, HttpStatus.OK);
    }

    @GetMapping("object/{id}")
    public ResponseEntity<ObjectDto> getObject(@PathVariable("id") String id){
        ObjectEntity obxecto = objectService.findById(id);
        if(obxecto.getName() != null){
            return new ResponseEntity<>(ObjectDtoMapper.objectEntityToDto(obxecto), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ObjectDto(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("object")
    public ResponseEntity<ObjectDto> postObject(@RequestBody ObjectDto obxecto){
        System.out.println("Create object: " + obxecto.toString());

        try{
            return new ResponseEntity<>(ObjectDtoMapper.objectEntityToDto(
                objectService.createObject(ObjectDtoMapper.objectDtoToEntity(obxecto))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new ObjectDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("object")
    public ResponseEntity<ObjectDto> updateObject(@RequestBody ObjectDto obxecto){
        System.out.println("Update object: " + obxecto.toString());

        try{
            return new ResponseEntity<>(ObjectDtoMapper.objectEntityToDto(
                objectService.updateObject(ObjectDtoMapper.objectDtoToEntity(obxecto))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new ObjectDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("object")
    public ResponseEntity<ObjectDto> deleteObject(@RequestBody ObjectDto obxecto){
        System.out.println("Delete object: " + obxecto.toString());

        try{
            return new ResponseEntity<>(ObjectDtoMapper.objectEntityToDto(
                objectService.deleteObject(ObjectDtoMapper.objectDtoToEntity(obxecto))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new ObjectDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Places
    @GetMapping("places/{campaignId}")
    public ResponseEntity<List<PlaceDto>> getAllPlacesFrom(@PathVariable("campaignId") String campaignId){
        List<PlaceDto> placesDto = new ArrayList<>();
        List<PlaceEntity> places = placeService.findAllByCampaign(campaignId);

        for (PlaceEntity place : places) {
            placesDto.add(PlaceDtoMapper.placeEntityToDto(place));
        }

        return new ResponseEntity<>(placesDto, HttpStatus.OK);
    }

    @GetMapping("place/{id}")
    public ResponseEntity<PlaceDto> getPlace(@PathVariable("id") String id){
        PlaceEntity place = placeService.fndById(id);
        if(place.getName() != null){
            return new ResponseEntity<>(PlaceDtoMapper.placeEntityToDto(place), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new PlaceDto(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("place")
    public ResponseEntity<PlaceDto> postPlace(@RequestBody PlaceDto place){
        System.out.println("Create place: " + place.toString());

        try{
            return new ResponseEntity<>(PlaceDtoMapper.placeEntityToDto(
                placeService.createPlace(PlaceDtoMapper.placeDtoToEntity(place))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new PlaceDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("place")
    public ResponseEntity<PlaceDto> updatePlace(@RequestBody PlaceDto place){
        System.out.println("Update place: " + place.toString());

        try{
            return new ResponseEntity<>(PlaceDtoMapper.placeEntityToDto(
                placeService.updatePlace(PlaceDtoMapper.placeDtoToEntity(place))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new PlaceDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("place")
    public ResponseEntity<PlaceDto> deletePlace(@RequestBody PlaceDto place){
        System.out.println("Delete place: " + place.toString());

        try{
            return new ResponseEntity<>(PlaceDtoMapper.placeEntityToDto(
                placeService.deletePlace(PlaceDtoMapper.placeDtoToEntity(place))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new PlaceDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("userRelation/{campaignId}")
    public ResponseEntity<List<UserRelationDto>> getRelationsByCampaign(@PathVariable("campaignId") String campaignId){
        List<UserRelationDto> relationsDto = new ArrayList<>();
        List<UserRelationEntity> relations = userRelationService.findByCampaign(campaignId);

        for (UserRelationEntity relation : relations) {
            relationsDto.add(UserRelationDtoMapper.userRelationEntityToDto(relation));
        }

        return new ResponseEntity<>(relationsDto, HttpStatus.OK);
    }

    @PostMapping("userRelation")
    public ResponseEntity<UserRelationDto> postUserRelation(@RequestBody UserRelationDto userRelation){
        System.out.println("Create user relation: " + userRelation.toString());

        try{
            return new ResponseEntity<>(UserRelationDtoMapper.userRelationEntityToDto(
                userRelationService.createUser(UserRelationDtoMapper.userRelationDtoToEntity(userRelation))
            ), HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserRelationDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("userRelation")
    public ResponseEntity<UserRelationDto> updateUserRelation(@RequestBody UserRelationDto userRelation){
        System.out.println("Update user relation: " + userRelation.toString());

        try{
            return new ResponseEntity<>(UserRelationDtoMapper.userRelationEntityToDto(
                userRelationService.updateUser(UserRelationDtoMapper.userRelationDtoToEntity(userRelation))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserRelationDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("userRelation")
    public ResponseEntity<UserRelationDto> deleteUserRelation(@RequestBody UserRelationDto userRelation){
        System.out.println("Delete user relation: " + userRelation.toString());

        try{
            return new ResponseEntity<>(UserRelationDtoMapper.userRelationEntityToDto(
                userRelationService.deleteUser(UserRelationDtoMapper.userRelationDtoToEntity(userRelation))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserRelationDto(), HttpStatus.BAD_REQUEST);
        }
    }

}
