package com.example.bookstore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.bookstore.config.AES;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.Books;
import com.example.bookstore.model.Constants;
import com.example.bookstore.model.Orders;
import com.example.bookstore.model.UserInfomation;
import com.example.bookstore.model.Users;
import com.example.bookstore.model.UsersOrderHistory;
import com.example.bookstore.repository.UsersOrderHistoryRepository;
import com.example.bookstore.repository.UsersRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UsersController {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private UsersOrderHistoryRepository usersOrderHistoryRepository;
	
	private List<Books> booksAll = new ArrayList<Books>();;
	
//	@Autowired
//	private BCryptPasswordEncoder bcryptEncoder;

	/**
	 * POST: /login
	 * 
	 * This is the user login authentication API. The request and response should be
	 * over a secured communication.
	 * 
	 * Request: {"username":"john.doe", "password": "thisismysecret"}
	 *
	 * @param users
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/login")
	public Users login(@Valid @RequestBody Users users) throws Exception {
		System.out.println("[login] start...");
//		Example = new 
//		Users usersTmp = new Users();
//		users.setUsername(users.getUsername());
		
//		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
//			      .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
//			      .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		

//	    Example<Users> example = Example.of(Users.from("Fred", "Bloggs", null));
//	    
//		usersRepository.findOne(Example.create(exampleEntity));
		String encryptedString = AES.encrypt(users.getPassword(), Constants.SECRET_KEY) ;
//		String decryptedString = AES.decrypt(encryptedString, Constants.SECRET_KEY) ;
		
		Users user = usersRepository.findByUsernameAndPassword(users.getUsername(), encryptedString);
		
		if (user == null) {
			throw new Exception("Username/Password Invalid");
		}
		
		return user;
		
//		return usersRepository.save(users);
	}
	
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Users user = usersRepository.findByUsername(username);
//		if(user == null){
//			throw new UsernameNotFoundException("Invalid username or password.");
//		}
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
//	}
//	
//	private List<SimpleGrantedAuthority> getAuthority() {
//		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
//	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/users")
	public UserInfomation getUsersDetails(@RequestHeader HttpHeaders headers) {
		System.out.println("[getUsersDetails] start... ");
		
		System.out.println(headers);
		
//		System.out.println(headers.get("authorization"));
		
//		headers.get("authorization").get(0);
		long id = Long.valueOf(headers.get("authorization").get(0));
		System.out.println("[getUsersDetails] findById -> " + id);
		
		Optional<Users> optional = usersRepository.findById(id);
		Users user = optional.get();
		
		UserInfomation userInfomation = new UserInfomation();
		String fullname[] = user.getUsername().split("\\.");
		userInfomation.setName(fullname[0]);
		userInfomation.setSurname(fullname[1]);
		userInfomation.setDate_of_birth(user.getDate_of_birth());
		userInfomation.setId(user.getId());
		
		long[] books = {};
		List<UsersOrderHistory> usersOrderHistories = usersOrderHistoryRepository.findByOwnerUserId(user.getId());
		
		if (usersOrderHistories != null && usersOrderHistories.size() > 0) {
			int i = 0;
			books = new long[usersOrderHistories.size()];
			for (UsersOrderHistory usersOrderHistory : usersOrderHistories) {
				books[i] = usersOrderHistory.getId();
				i++;
			}
			
			userInfomation.setBooks(books);
		}
		
		return userInfomation;
	}

	/**
	 * GET: /users
	 * 
	 * (Login required) Gets information about the logged in user. A successfully
	 * authenticated request returns information related to the user and the books
	 * ordered.
	 * 
	 * Response: { "name": "john", "surname": "doe", "date_of_birth": "15/01/1985",
	 * "books": [1, 4] }
	 *
	 * @param userId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/users/{id}")
	public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		Users users = usersRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		return ResponseEntity.ok().body(users);
	}

	/**
	 * POST: /users
	 * 
	 * (Login not required) Create a user account and store user’s information in
	 * Users table (DB).
	 * 
	 * Request: {"username": "john.doe", "password": "thisismysecret",
	 * "date_of_birth": "15/01/1985"}
	 * 
	 * @param users
	 * @return
	 */
	@PostMapping("/users")
	public Users createUser(@Valid @RequestBody Users users) {
		String rawPassword = users.getPassword();
//		users.setPassword(bcryptEncoder.encode(users.getPassword()));
//		System.out.println("bcryptEncoder matches -> " + bcryptEncoder.matches(rawPassword, users.getPassword()));
		
	    String encryptedString = AES.encrypt(rawPassword, Constants.SECRET_KEY) ;
//	    String decryptedString = AES.decrypt(encryptedString, Constants.SECRET_KEY) ;
	    
//	    System.out.println("decryptedString test -> "+ decryptedString);
	    
	    users.setPassword(encryptedString);
		
		return usersRepository.save(users);
	}

	/**
	 * DELETE: /users
	 * 
	 * @param userId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@DeleteMapping("/users")
	public Map<String, Boolean> deleteUserRecordAndHistory(@RequestHeader HttpHeaders headers)
			throws ResourceNotFoundException {
//		Users users = usersRepository.findById(userId)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
//
//		usersRepository.delete(users);
		
		long id = Long.valueOf(headers.get("authorization").get(0));
		System.out.println("[deleteUserRecordAndHistory] findById -> " + id);
		
//		usersOrderHistoryRepository.deleteOrderHistory(id);
		List<UsersOrderHistory> usersOrderHistories = usersOrderHistoryRepository.findByOwnerUserId(id);
		
		for (UsersOrderHistory usersOrderHistory : usersOrderHistories) {
			usersOrderHistoryRepository.deleteById(usersOrderHistory.getId());
		}
		
		Map<String, Boolean> response = new HashMap<>();
//		response.put("deleted", Boolean.TRUE);
		return response;
	}
	
	@PostMapping("/users/orders")
	public Map<String, Object> userOrderBook(@Valid @RequestBody Orders orders, @RequestHeader HttpHeaders headers) {
		System.out.println(headers);
		
//		System.out.println(headers.get("authorization"));
		
		long ownerUserId = Long.valueOf(headers.get("authorization").get(0));
		System.out.println("[userOrderBook] ownerUserId -> " + ownerUserId);
		
		BigDecimal priceTotal = new BigDecimal(0);
		UsersOrderHistory usersOrderHistory = null;
		for (int i=0; i<orders.getOrders().length; i++) {
			
			for (Books book : booksAll) {
				if (book.getId() == orders.getOrders()[i]) {
					usersOrderHistory = new UsersOrderHistory();
					usersOrderHistory.setAuthor_name(book.getAuthor_name());
					usersOrderHistory.setBook_name(book.getBook_name());
					usersOrderHistory.setIs_recommended(book.isIs_recommended());
					usersOrderHistory.setOwnerUserId(ownerUserId);
					usersOrderHistory.setPrice(book.getPrice());
					usersOrderHistory.setBookId(book.getId());
					
					System.out.println(book.getPrice());
					priceTotal = priceTotal.add(book.getPrice());
					
					break;
				}
			}
			
			if (usersOrderHistory != null) {
				usersOrderHistoryRepository.save(usersOrderHistory);
			}
		}
		
		Map<String, Object> response = new HashMap<>();
		System.out.println(priceTotal);
		response.put("price", priceTotal.doubleValue());
		return response;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/books")
//	@Secured("ROLE_VIEWER")
	public List<Books> getAllBooks() throws JsonParseException, JsonMappingException, IOException {
		List<Books> booksRecommend = getAllBooksRecommend();

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject("https://scb-test-book-publisher.herokuapp.com/books",
				String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(List.class, Books.class);
		List<Books> books = objectMapper.readValue(response, collectionType);
		System.out.println(books);

		Collections.sort(books, new Comparator() {
			@Override
			public int compare(Object booksOne, Object booksTwo) {
				return ((Books) booksOne).getBook_name().compareTo(((Books) booksTwo).getBook_name());
			}
		});

		List<Books> booksAll = new ArrayList<Books>();
		booksAll.addAll(booksRecommend);
		for (int i = 0; i < books.size(); i++) {
			if (!containsName(booksAll, books.get(i).getBook_name())) {
				booksAll.add(books.get(i));
			}
		}

		System.out.println("books size -> " + booksAll.size());
		System.out.println("booksRecommend size -> " + booksRecommend.size());

		this.booksAll = booksAll;
		
		return booksAll;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/books/recommendation")
	public List<Books> getAllBooksRecommend() throws JsonParseException, JsonMappingException, IOException {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate
				.getForObject("https://scb-test-book-publisher.herokuapp.com/books/recommendation", String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(List.class, Books.class);
		List<Books> books = objectMapper.readValue(response, collectionType);
		System.out.println(books);

		Collections.sort(books, new Comparator() {
			@Override
			public int compare(Object booksOne, Object booksTwo) {
				return ((Books) booksOne).getBook_name().compareTo(((Books) booksTwo).getBook_name());
			}
		});

		for (Books book : books) {
			book.setIs_recommended(true);
		}

		return books;
	}

	public boolean containsName(final List<Books> list, final String name) {
		return list.stream().map(Books::getBook_name).filter(name::equals).findFirst().isPresent();
	}

	public List<Books> getBooksAll() {
		return booksAll;
	}

	public void setBooksAll(List<Books> booksAll) {
		this.booksAll = booksAll;
	}

}
