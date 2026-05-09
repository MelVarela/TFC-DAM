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
import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;
import com.melvarela.spring_mazmorras.entities.NoteEntity;
import com.melvarela.spring_mazmorras.entities.ObjectEntity;
import com.melvarela.spring_mazmorras.entities.PlaceEntity;
import com.melvarela.spring_mazmorras.entities.SuggestionEntity;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CampaignDto;
import com.melvarela.spring_mazmorras.rest.dtos.CharacterDto;
import com.melvarela.spring_mazmorras.rest.dtos.CreatureDto;
import com.melvarela.spring_mazmorras.rest.dtos.LoginDto;
import com.melvarela.spring_mazmorras.rest.dtos.NoteDto;
import com.melvarela.spring_mazmorras.rest.dtos.ObjectDto;
import com.melvarela.spring_mazmorras.rest.dtos.PlaceDto;
import com.melvarela.spring_mazmorras.rest.dtos.SuggestionDto;
import com.melvarela.spring_mazmorras.rest.dtos.UserDto;
import com.melvarela.spring_mazmorras.rest.dtos.UserRelationDto;
import com.melvarela.spring_mazmorras.rest.mappers.CampaignDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.CharacterDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.CreatureDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.NoteDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.ObjectDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.PlaceDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.SuggestionDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.UserDtoMapper;
import com.melvarela.spring_mazmorras.rest.mappers.UserRelationDtoMapper;
import com.melvarela.spring_mazmorras.services.CampaignService;
import com.melvarela.spring_mazmorras.services.CharacterService;
import com.melvarela.spring_mazmorras.services.CreatureService;
import com.melvarela.spring_mazmorras.services.NoteService;
import com.melvarela.spring_mazmorras.services.ObjectService;
import com.melvarela.spring_mazmorras.services.PlaceService;
import com.melvarela.spring_mazmorras.services.SuggestionService;
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
    @Autowired
    SuggestionService suggestionService;

    //Login
    @PostMapping("login")
    public ResponseEntity<LoginDto> login(@RequestBody UserDto user){
        System.out.println("Trying to login: " + user.getEmail());

        if(userService.login(user)){
            return new ResponseEntity<>(new LoginDto(true), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new LoginDto(false), HttpStatus.FORBIDDEN);
        }
    }

    //Users
    @GetMapping("user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") String userId){
        System.out.println("Getting user: " + userId);

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

    @DeleteMapping("user/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") String id){
        System.out.println("Delete user: " + id);

        try{
            return new ResponseEntity<>(UserDtoMapper.userEntityToUserDto(userService.deleteUser(id)), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Campaigns
    @GetMapping("campaigns/{playerId}")
    public ResponseEntity<List<CampaignDto>> getAllCampaignsOf(@PathVariable("playerId") String playerId){
        System.out.println("Getting player: " + playerId);

        List<CampaignDto> campaignsDto = new ArrayList<>();
        List<CampaignEntity> campaigns = campaignService.findAllByPlayer(playerId);

        for (CampaignEntity campaign : campaigns) {
            campaignsDto.add(CampaignDtoMapper.campaignEntityToDto(campaign));
        }

        return new ResponseEntity<>(campaignsDto, HttpStatus.OK);
    }

    @GetMapping("campaign/{id}")
    public ResponseEntity<CampaignDto> getCampaign(@PathVariable("id") String id){
        System.out.println("Getting campaign: " + id);

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

    @DeleteMapping("campaign/{id}")
    public ResponseEntity<CampaignDto> deleteCampaign(@PathVariable("id") String id){
        System.out.println("Delete campaign: " + id);

        try{
            return new ResponseEntity<>(CampaignDtoMapper.campaignEntityToDto(
                campaignService.deleteCampaign(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CampaignDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Characters
    @GetMapping("characters/{campaignId}")
    public ResponseEntity<List<CharacterDto>> getAllCharactersFrom(@PathVariable("campaignId") String campaignId){
        System.out.println("Getting characters of: " + campaignId);

        CampaignEntity campaign = campaignService.findById(campaignId);
        if(campaign.getName() == null) return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);

        List<CharacterDto> charactersDto = new ArrayList<>();
        List<CharacterEntity> characters = characterService.findAllByCampaign(campaign);

        for (CharacterEntity character : characters) {
            charactersDto.add(CharacterDtoMapper.characterEntityToDto(character));
        }

        return new ResponseEntity<>(charactersDto, HttpStatus.OK);
    }

    @GetMapping("character/{id}")
    public ResponseEntity<CharacterDto> getCharacter(@PathVariable("id") String id){
        System.out.println("Getting character: " + id);

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
                characterService.createCharacter(CharacterDtoMapper.characterDtoToEntity(character, campaignService.findById(character.getCampaign())))
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
                characterService.updateCharacter(CharacterDtoMapper.characterDtoToEntity(character, campaignService.findById(character.getCampaign())))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CharacterDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("character/{id}")
    public ResponseEntity<CharacterDto> deleteCharacter(@PathVariable("id") String id){
        System.out.println("Delete character: " + id);

        try{
            return new ResponseEntity<>(CharacterDtoMapper.characterEntityToDto(
                characterService.deleteCharacter(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CharacterDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Creatures
    @GetMapping("creatures/{campaignId}")
    public ResponseEntity<List<CreatureDto>> getAllCreatureFrom(@PathVariable("campaignId") String campaignId){
        System.out.println("Getting creatures of campaign: " + campaignId);

        CampaignEntity campaign = campaignService.findById(campaignId);
        if(campaign.getName() == null) return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);

        List<CreatureDto> creaturesDto = new ArrayList<>();
        List<CreatureEntity> creatures = creatureService.findAllByCampaign(campaign);

        for (CreatureEntity creature : creatures) {
            creaturesDto.add(CreatureDtoMapper.creatureEntityToDto(creature));
        }

        return new ResponseEntity<>(creaturesDto, HttpStatus.OK);
    }

    @GetMapping("creature/{id}")
    public ResponseEntity<CreatureDto> getCreature(@PathVariable("id") String id){
        System.out.println("Getting creature: " + id);

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
                creatureService.createCreature(CreatureDtoMapper.creatureDtoToEntity(creature, campaignService.findById(creature.getCampaign())))
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
                creatureService.updateCreature(CreatureDtoMapper.creatureDtoToEntity(creature, campaignService.findById(creature.getCampaign())))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CreatureDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("creature/{id}")
    public ResponseEntity<CreatureDto> deleteCreature(@PathVariable("id") String id){
        System.out.println("Delete creature: " + id);

        try{
            return new ResponseEntity<>(CreatureDtoMapper.creatureEntityToDto(
                creatureService.deleteCreature(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new CreatureDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Notes
    @GetMapping("notes/{owner}")
    public ResponseEntity<List<NoteDto>> getAllNotesOf(@PathVariable("owner") String owner){
        System.out.println("Getting all notes of: " + owner);

        List<NoteDto> notesDto = new ArrayList<>();
        List<NoteEntity> notes = noteService.findAllByOwner(owner);

        for (NoteEntity note : notes) {
            notesDto.add(NoteDtoMapper.noteEntityToDto(note));
        }

        return new ResponseEntity<>(notesDto, HttpStatus.OK);
    }

    @GetMapping("note/{id}")
    public ResponseEntity<NoteDto> getNote(@PathVariable("id") String id){
        System.out.println("Getting note: " + id);

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

    @DeleteMapping("note/{owner}")
    public ResponseEntity<NoteDto> deleteNote(@PathVariable("id") String id){
        System.out.println("Delete note: " + id);

        try{
            return new ResponseEntity<>(NoteDtoMapper.noteEntityToDto(
                noteService.deleteNote(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new NoteDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Objects
    @GetMapping("objects/{campaignId}")
    public ResponseEntity<List<ObjectDto>> getAllObjectsFrom(@PathVariable("campaignId") String campaignId){
        System.out.println("Getting objects from: " + campaignId);

        CampaignEntity campaign = campaignService.findById(campaignId);
        if(campaign.getName() == null) return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);

        List<ObjectDto> objectsDto = new ArrayList<>();
        List<ObjectEntity> objects = objectService.findAllByCampaign(campaign);

        for (ObjectEntity obxecto : objects) {
            objectsDto.add(ObjectDtoMapper.objectEntityToDto(obxecto));
        }

        return new ResponseEntity<>(objectsDto, HttpStatus.OK);
    }

    @GetMapping("object/{id}")
    public ResponseEntity<ObjectDto> getObject(@PathVariable("id") String id){
        System.out.println("Getting object: " + id);

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
                objectService.createObject(ObjectDtoMapper.objectDtoToEntity(obxecto, campaignService.findById(obxecto.getCampaign())))
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
                objectService.updateObject(ObjectDtoMapper.objectDtoToEntity(obxecto, campaignService.findById(obxecto.getCampaign())))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new ObjectDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("object/{id}")
    public ResponseEntity<ObjectDto> deleteObject(@PathVariable("id") String id){
        System.out.println("Delete object: " + id);

        try{
            return new ResponseEntity<>(ObjectDtoMapper.objectEntityToDto(
                objectService.deleteObject(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new ObjectDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Places

    @GetMapping("places/{campaignId}")
    public ResponseEntity<List<PlaceDto>> getAllPlacesFrom(@PathVariable("campaignId") String campaignId){
        System.out.println("Getting places from: " + campaignId);

        CampaignEntity campaign = campaignService.findById(campaignId);
        if(campaign.getName() == null) return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);

        List<PlaceDto> placesDto = new ArrayList<>();
        List<PlaceEntity> places = placeService.findAllByCampaign(campaign);

        for (PlaceEntity place : places) {
            placesDto.add(PlaceDtoMapper.placeEntityToDto(place));
        }

        return new ResponseEntity<>(placesDto, HttpStatus.OK);
    }

    @GetMapping("place/{id}")
    public ResponseEntity<PlaceDto> getPlace(@PathVariable("id") String id){
        System.out.println("Getting place: " + id);

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
                placeService.createPlace(PlaceDtoMapper.placeDtoToEntity(place, campaignService.findById(place.getCampaign())))
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
                placeService.updatePlace(PlaceDtoMapper.placeDtoToEntity(place, campaignService.findById(place.getCampaign())))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new PlaceDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("place/{id}")
    public ResponseEntity<PlaceDto> deletePlace(@PathVariable("id") String id){
        System.out.println("Delete place: " + id);

        try{
            return new ResponseEntity<>(PlaceDtoMapper.placeEntityToDto(
                placeService.deletePlace(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new PlaceDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //User relations

    @GetMapping("userRelation/{campaignId}")
    public ResponseEntity<List<UserRelationDto>> getRelationsByCampaign(@PathVariable("campaignId") String campaignId){
        System.out.println("Getting user relations for campaign: " + campaignId);
        List<UserRelationDto> relationsDto = new ArrayList<>();

        try{

            List<UserRelationEntity> relations = userRelationService.findByCampaign(
                campaignService.findById(campaignId)
            );

            for (UserRelationEntity relation : relations) {
                relationsDto.add(UserRelationDtoMapper.userRelationEntityToDto(relation));
            }

            return new ResponseEntity<>(relationsDto, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(relationsDto, HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("userRelation/user/{userId}")
    public ResponseEntity<List<UserRelationDto>> getRelationsByUser(@PathVariable("userId") String userId){
        System.out.println("Getting user relations for user: " + userId);

        List<UserRelationDto> relationsDto = new ArrayList<>();
        List<UserRelationEntity> relations = userRelationService.findByUser(userId);

        for (UserRelationEntity relation : relations) {
            relationsDto.add(UserRelationDtoMapper.userRelationEntityToDto(relation));
        }

        return new ResponseEntity<>(relationsDto, HttpStatus.OK);
    }

    @GetMapping("userRelation/invites/{playerId}")
    public ResponseEntity<List<UserRelationDto>> getPendingInvitesFor(@PathVariable("playerId") String playerId){
        System.out.println("Getting invites for: " + playerId);

        List<UserRelationDto> relationsDto = new ArrayList<>();
        List<UserRelationEntity> relations = userRelationService.findByUserPending(playerId);

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
                userRelationService.createUser(UserRelationDtoMapper.userRelationDtoToEntity(userRelation,
                    campaignService.findById(userRelation.getCampaign()), userService.findById(userRelation.getUser())))
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
                userRelationService.updateUser(UserRelationDtoMapper.userRelationDtoToEntity(userRelation,
                    campaignService.findById(userRelation.getCampaign()), userService.findById(userRelation.getUser())))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserRelationDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("userRelation/{id}")
    public ResponseEntity<UserRelationDto> deleteUserRelation(@PathVariable("id") String id){
        System.out.println("Delete user relation: " + id);

        String[] idSplit = id.split("-");

        try{
            return new ResponseEntity<>(UserRelationDtoMapper.userRelationEntityToDto(
                userRelationService.deleteUser(new UserRelationId(idSplit[0], idSplit[1]))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new UserRelationDto(), HttpStatus.BAD_REQUEST);
        }
    }

    //Suggestions

    @GetMapping("suggestions")
    public ResponseEntity<List<SuggestionDto>> getAllSuggestions(){
        System.out.println("Getting suggestions.");
        
        List<SuggestionEntity> suggestions = suggestionService.findAll();
        List<SuggestionDto> listDto = new ArrayList<>();

        for (SuggestionEntity suggestion : suggestions) {
            listDto.add(SuggestionDtoMapper.entityToDto(suggestion));
        }

        return new ResponseEntity<>(listDto, HttpStatus.OK);
    }

    @PostMapping("suggestion")
    public ResponseEntity<SuggestionDto> postSuggestion(@RequestBody SuggestionDto suggestion){
        System.out.println("Creating suggestion: " + suggestion.toString());

        try{
            return new ResponseEntity<>(SuggestionDtoMapper.entityToDto(
                suggestionService.createSuggestion(SuggestionDtoMapper.dtoToEntity(suggestion))
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new SuggestionDto(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("suggestion/{id}")
    public ResponseEntity<SuggestionDto> deleteSuggestion(@PathVariable("id") int id){
        System.out.println("Deleting suggestion: " + id);

        try{
            return new ResponseEntity<>(SuggestionDtoMapper.entityToDto(
                suggestionService.deleteSuggestion(id)
            ), HttpStatus.OK);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(new SuggestionDto(), HttpStatus.BAD_REQUEST);
        }
    }

}
