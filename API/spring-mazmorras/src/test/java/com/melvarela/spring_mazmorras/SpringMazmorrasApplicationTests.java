package com.melvarela.spring_mazmorras;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.melvarela.spring_mazmorras.entities.UserEntity;
import com.melvarela.spring_mazmorras.rest.ApiRestController;
import com.melvarela.spring_mazmorras.rest.dtos.ClassDto;
import com.melvarela.spring_mazmorras.rest.dtos.LoginDto;
import com.melvarela.spring_mazmorras.rest.dtos.UserDto;
import com.melvarela.spring_mazmorras.rest.mappers.UserDtoMapper;
import com.melvarela.spring_mazmorras.services.CampaignService;
import com.melvarela.spring_mazmorras.services.CharacterService;
import com.melvarela.spring_mazmorras.services.CreatureService;
import com.melvarela.spring_mazmorras.services.DndApiService;
import com.melvarela.spring_mazmorras.services.NoteService;
import com.melvarela.spring_mazmorras.services.ObjectService;
import com.melvarela.spring_mazmorras.services.PlaceService;
import com.melvarela.spring_mazmorras.services.SuggestionService;
import com.melvarela.spring_mazmorras.services.UserRelationService;
import com.melvarela.spring_mazmorras.services.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class SpringMazmorrasApplicationTests {

	@InjectMocks
	private ApiRestController restController;

	@Mock
    UserService userService;
    @Mock
    CampaignService campaignService;
    @Mock
    CharacterService characterService;
    @Mock
    CreatureService creatureService;
    @Mock
    NoteService noteService;
    @Mock
    ObjectService objectService;
    @Mock
    PlaceService placeService;
    @Mock
    UserRelationService userRelationService;
    @Mock
    SuggestionService suggestionService;
    @Mock
    DndApiService dndApiService;

	//Entidades para referencia rápida
	UserDto user = new UserDto(
		"test@user.com",
		"Test User",
		"123321",
		"https://deltarune.com/assets/images/tv2.gif"
	);
	UserEntity nullUser = new UserEntity(null, null, null, null);

	@Nested
	@DisplayName("Tests for login function")
	class TestLogin{

		@Test
		@DisplayName("Test that should succesfully log in")
		void shouldLogin(){

			when(userService.login(user)).thenReturn(true);
			
			ResponseEntity<LoginDto> response = restController.login(user);
			assertEquals(HttpStatus.OK, response.getStatusCode());

		}

		@Test
		@DisplayName("Test that should not succesfully log in")
		void shouldNotLogin(){

			when(userService.login(user)).thenReturn(false);
			
			ResponseEntity<LoginDto> response = restController.login(user);
			assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

		}

	}

	@Nested
	@DisplayName("Tests for user endpoints")
	class TestUser{

		@Test
		@DisplayName("Test that should succesfully retrieve a user")
		void shouldGetUser(){

			when(userService.findById(user.getEmail())).thenReturn(UserDtoMapper.userDtoToUserEntity(user));

			ResponseEntity<UserDto> response = restController.getUser(user.getEmail());
			assertEquals(HttpStatus.OK, response.getStatusCode());

		}

		@Test
		@DisplayName("Test that should unsuccesfully retrieve a user")
		void shouldNotGetUser(){
			
			when(userService.findById(user.getEmail())).thenReturn(nullUser);

			ResponseEntity<UserDto> response = restController.getUser(user.getEmail());
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		}

		@Test
		@DisplayName("Test that should succesfully create a user")
		void shouldPostUser(){

			when(userService.createUser(any())).thenReturn(UserDtoMapper.userDtoToUserEntity(user));

			ResponseEntity<UserDto> response = restController.postUser(user);
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
		}

		@Test
		@DisplayName("Test that should unsuccesfully create a user")
		void shouldNotPostUser(){
			
			when(userService.createUser(any())).thenThrow(new IllegalArgumentException());

			ResponseEntity<UserDto> response = restController.postUser(user);
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		}

		@Test
		@DisplayName("Test that should succesfully update a user")
		void shouldPutUser(){

			when(userService.updateUser(any())).thenReturn(UserDtoMapper.userDtoToUserEntity(user));
			
			ResponseEntity<UserDto> response = restController.updateUser(user);
			assertEquals(HttpStatus.OK, response.getStatusCode());

		}

		@Test
		@DisplayName("Test that should unsuccesfully update a user")
		void shouldNotPutUser(){

			when(userService.updateUser(any())).thenThrow(new IllegalArgumentException());
			
			ResponseEntity<UserDto> response = restController.updateUser(user);
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			
		}

		@Test
		@DisplayName("Test that should succesfully delete a user")
		void shouldDeleteUser(){

			when(userService.deleteUser(user.getEmail())).thenReturn(UserDtoMapper.userDtoToUserEntity(user));

			ResponseEntity<UserDto> response = restController.deleteUser(user.getEmail());
			assertEquals(HttpStatus.OK, response.getStatusCode());
			
		}

		@Test
		@DisplayName("Test that should unsuccesfully delete a user")
		void shouldNotDeleteUser(){

			when(userService.deleteUser(user.getEmail())).thenThrow(new IllegalArgumentException());

			ResponseEntity<UserDto> response = restController.deleteUser(user.getEmail());
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			
		}

	}

	@Nested
	@DisplayName("Tests for dnd endpoints")
	class TestDndApi {

		@Test
		@DisplayName("")
		void shouldGetAllClases(){

			when(dndApiService.getAllClases()).thenReturn(List.of(
				new ClassDto()
			));

			ResponseEntity<List<ClassDto>> response = restController.getAllClases();
			assertEquals(HttpStatus.OK, response.getStatusCode());

		}

		@Test
		@DisplayName("")
		void shouldGetClase(){

			when(dndApiService.getClase("barbarian")).thenReturn(new ClassDto());

			ResponseEntity<ClassDto> response = restController.getClase("barbarian");
			assertEquals(HttpStatus.OK, response.getStatusCode());
			
		}

		@Test
		@DisplayName("")
		void shouldGetSubclases(){

			when(dndApiService.getSubclasesForClass("barbarian")).thenReturn(List.of("Berserker"));

			ResponseEntity<List<String>> response = restController.getSubclasesFor("barbarian");
			assertEquals(HttpStatus.OK, response.getStatusCode());
			
		}

	}

}
