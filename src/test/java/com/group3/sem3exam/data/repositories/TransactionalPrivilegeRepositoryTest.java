package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.Privilege;
import com.group3.sem3exam.data.entities.ThirdParty;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.group3.sem3exam.data.entities.Privilege.COMMENT;
import static com.group3.sem3exam.data.entities.Privilege.POST;
import static org.junit.Assert.*;

public class TransactionalPrivilegeRepositoryTest
{

    private Transaction          transaction;
    private UserRepository       userRepository;
    private ThirdPartyRepository thirdPartyRepository;
    private PrivilegeRepository  privilegeRepository;

    private User       user;
    private ThirdParty thirdParty;

    @Before
    public void setUp() throws Exception
    {
        this.transaction = new Transaction(JpaTestConnection.emf);
        this.userRepository = new TransactionalUserRepository(transaction);
        this.thirdPartyRepository = new TransactionalThirdPartyRepository(transaction);
        this.privilegeRepository = new TransactionalPrivilegeRepository(transaction);

        this.transaction.begin();

        CityRepository cityRepository = new TransactionalCityRepository(transaction);
        user = userRepository.create("Name", "Email", "PasswordHash", cityRepository.get(1), Gender.MALE, LocalDate.of(1997, 9, 20));
        thirdParty = thirdPartyRepository.create("Name", "PasswordHash");
    }

    @After
    public void tearDown() throws Exception
    {
        this.transaction.rollback();
        this.transaction.close();
    }

    @Test
    public void canAllow()
    {
        assertFalse(privilegeRepository.can(thirdParty, user, POST));
        privilegeRepository.allow(thirdParty, user, POST);
        assertTrue(privilegeRepository.can(thirdParty, user, POST));
    }

    @Test
    public void canAllowAll()
    {
        Set<Privilege> set = set(POST, COMMENT);
        assertFalse(privilegeRepository.can(thirdParty, user, set));
        privilegeRepository.allow(thirdParty, user, set);
        assertTrue(privilegeRepository.can(thirdParty, user, set));
    }

    @Test
    public void canDeny()
    {
        assertFalse(privilegeRepository.can(thirdParty, user, POST));
        privilegeRepository.allow(thirdParty, user, POST);
        assertTrue(privilegeRepository.can(thirdParty, user, POST));
        privilegeRepository.deny(thirdParty, user, POST);
        assertFalse(privilegeRepository.can(thirdParty, user, POST));
    }

    @Test
    public void canDenyAll()
    {
        Set<Privilege> set = set(POST, COMMENT);
        assertFalse(privilegeRepository.can(thirdParty, user, set));
        privilegeRepository.allow(thirdParty, user, set);
        assertTrue(privilegeRepository.can(thirdParty, user, set));
        privilegeRepository.deny(thirdParty, user, set);
        assertFalse(privilegeRepository.can(thirdParty, user, set));
    }

    @Test
    public void get()
    {
        assertEquals(0, privilegeRepository.get(thirdParty, user).size());
        privilegeRepository.allow(thirdParty, user, POST);
        assertTrue(privilegeRepository.get(thirdParty, user).contains(POST));
        privilegeRepository.allow(thirdParty, user, COMMENT);
        assertTrue(privilegeRepository.get(thirdParty, user).containsAll(set(POST, COMMENT)));
        assertEquals(2, privilegeRepository.get(thirdParty, user).size());
    }

    private Set<Privilege> set(Privilege... privileges)
    {
        return new HashSet<>(Arrays.asList(privileges));
    }
}