package br.com.tclinica.service.impl;

import br.com.tclinica.service.ReceptionistService;
import br.com.tclinica.service.UserService;
import br.com.tclinica.service.mapper.UserMapper;
import br.com.tclinica.service.util.ExistenceUtil;
import br.com.tclinica.domain.Accountant;
import br.com.tclinica.domain.Authority;
import br.com.tclinica.domain.Receptionist;
import br.com.tclinica.domain.User;
import br.com.tclinica.repository.ReceptionistRepository;
import br.com.tclinica.security.AuthoritiesConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Receptionist.
 */
@Service
@Transactional
public class ReceptionistServiceImpl implements ReceptionistService{

    private final Logger log = LoggerFactory.getLogger(ReceptionistServiceImpl.class);

    private final ReceptionistRepository receptionistRepository;
    
    private final UserService userService;
    
    public ReceptionistServiceImpl(ReceptionistRepository receptionistRepository, UserService userService) {
        this.receptionistRepository = receptionistRepository;
        this.userService = userService;
    }

    /**
     * Save a receptionist.
     *
     * @param receptionist the entity to save
     * @return the persisted entity
     */
    @Override
    public Receptionist save(Receptionist receptionist) {
        log.debug("Request to save Receptionist : {}", receptionist);
        if (ExistenceUtil.entityDoesntExist(receptionist.getId(), receptionistRepository)) {
        		return create(receptionist);
        }
        return receptionistRepository.save(receptionist);
    }
    
    public Receptionist create (Receptionist receptionist) {
    	receptionist.getUser().setAuthorities(addDefaultAuthorities(receptionist.getUser()));
    	UserMapper mapper = new UserMapper();
    	this.userService.updateUser(mapper.userToUserDTO(receptionist.getUser()));
    	
    	receptionist.setNickname(defineDefaultNickname(receptionist));
    	receptionist = receptionistRepository.save(receptionist);
    	    	
    	return receptionist;
    }
    
    private String defineDefaultNickname (Receptionist receptionist) {
    	String nickname = receptionist.getNickname();
    	if (nickname == null || nickname == "") {
    		nickname = (String.format("%s", receptionist.getUser().getFirstName()));
    	}
    	return nickname;
    }
    private Set<Authority> addDefaultAuthorities (User user) {
    	Set<Authority> authorities = userService.getUserWithAuthorities(user.getId()).getAuthorities();
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.RECEPTIONIST);
		authorities.add(authority);
		return authorities;
    }

    /**
     *  Get all the receptionists.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Receptionist> findAll() {
        log.debug("Request to get all Receptionists");
        return receptionistRepository.findAll();
    }

    /**
     *  Get one receptionist by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Receptionist findOne(Long id) {
        log.debug("Request to get Receptionist : {}", id);
        return receptionistRepository.findOne(id);
    }

    /**
     *  Delete the  receptionist by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Receptionist : {}", id);
        receptionistRepository.delete(id);
    }
}
