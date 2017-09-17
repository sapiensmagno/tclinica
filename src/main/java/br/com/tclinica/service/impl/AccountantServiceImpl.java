package br.com.tclinica.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.Accountant;
import br.com.tclinica.domain.Authority;
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.domain.User;
import br.com.tclinica.repository.AccountantRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.service.AccountantService;
import br.com.tclinica.service.UserService;
import br.com.tclinica.service.mapper.UserMapper;
import br.com.tclinica.service.util.ExistenceUtil;

/**
 * Service Implementation for managing Accountant.
 */
@Service
@Transactional
public class AccountantServiceImpl implements AccountantService{

    private final Logger log = LoggerFactory.getLogger(AccountantServiceImpl.class);

    private final AccountantRepository accountantRepository;
        
    private final UserService userService;
    
    public AccountantServiceImpl(AccountantRepository accountantRepository, UserService userService) {
        this.accountantRepository = accountantRepository;
        this.userService = userService;
    }

    /**
     * Save an Accountant
     *
     * @param Accountant the entity to save
     * @return the persisted entity
     */
    @Override
    public Accountant save(Accountant accountant) {
        log.debug("Request to save Accountant : {}", accountant);
        if (ExistenceUtil.entityDoesntExist(accountant.getId(), accountantRepository)) {
        	return create(accountant);
        }
        return accountantRepository.save(accountant);
    }
    
    // when creating a new schedule, set default values and create a schedule
    public Accountant create (Accountant accountant) {
    	accountant.getUser().setAuthorities(addDefaultAuthorities(accountant.getUser()));
    	UserMapper mapper = new UserMapper();
    	this.userService.updateUser(mapper.userToUserDTO(accountant.getUser()));
    	
    	accountant.setNickname(defineDefaultNickname(accountant));
    	accountant = accountantRepository.save(accountant);
    	    	
    	return accountant;
    }
    
    private String defineDefaultNickname (Accountant accountant) {
    	String nickname = accountant.getNickname();
    	if (nickname == null || nickname == "") {
    		nickname = (String.format("Sr. %s", accountant.getUser().getFirstName()));
    	}
    	return nickname;
    }
    private Set<Authority> addDefaultAuthorities (User user) {
    	Set<Authority> authorities = userService.getUserWithAuthorities(user.getId()).getAuthorities();
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.ACCOUNTANT);
		authorities.add(authority);
		return authorities;
    }
    
    /**
     *  Get all the accountants.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Accountant> findAll() {
        log.debug("Request to get all accountants");
        return accountantRepository.findAll();
    }

    /**
     *  Get one accountant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Accountant findOne(Long id) {
        log.debug("Request to get accountant : {}", id);
        return accountantRepository.findOne(id);
    }

    /**
     *  Delete the  accountant by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete accountant : {}", id);
        accountantRepository.delete(id);
    }
}