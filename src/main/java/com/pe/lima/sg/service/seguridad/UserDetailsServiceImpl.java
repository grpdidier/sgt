package com.pe.lima.sg.service.seguridad;

import com.pe.lima.sg.dao.seguridad.IUsuarioDAO;
import com.pe.lima.sg.entity.seguridad.TblUsuario;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private IUsuarioDAO usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	log.debug("[loadUserByUsername] inicio");
    	log.debug("[loadUserByUsername] username:"+username);
        TblUsuario user = usuarioDao.findOneByLogin(username);
        log.debug("[loadUserByUsername] clave:"+user.getClave());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        //for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            grantedAuthorities.add(new SimpleGrantedAuthority("INVITADO"));
        //}
            log.debug("[loadUserByUsername] Fin");
        //return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getClave(), grantedAuthorities);
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(), user.getClave(), grantedAuthorities);
             return userDetails;
    }
}