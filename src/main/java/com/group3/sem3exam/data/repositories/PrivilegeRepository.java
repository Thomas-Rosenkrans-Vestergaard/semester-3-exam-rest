package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Privilege;
import com.group3.sem3exam.data.entities.ThirdParty;
import com.group3.sem3exam.data.entities.User;

import java.util.Set;

/**
 * Retrieves and modifies privileges for third parties representing users.
 */
public interface PrivilegeRepository
{

    /**
     * Checks whether or not the third party can perform the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privilege  The privilege to check for.
     * @return {@code true} when the third party can perform the provided privilege on behalf of the provided user.
     */
    boolean can(ThirdParty thirdParty, User user, Privilege privilege);

    /**
     * Checks whether or not the third party can perform all of the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privileges The privileges to check for.
     * @return {@code true} when the third party can perform all of the provided privilege on behalf of the provided
     * user.
     */
    boolean can(ThirdParty thirdParty, User user, Set<Privilege> privileges);

    /**
     * Allows the provided third party to perform the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privilege  The privilege to allow the third party to perform.
     */
    void allow(ThirdParty thirdParty, User user, Privilege privilege);

    /**
     * Allows the provided third party to perform the provided privileges on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privileges The privileges to allow the third party to perform.
     */
    void allow(ThirdParty thirdParty, User user, Set<Privilege> privileges);

    /**
     * Denies the provided third party to perform the provided privilege on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privilege  The privilege to deny the third party.
     */
    void deny(ThirdParty thirdParty, User user, Privilege privilege);

    /**
     * Denies the provided third party to perform the provided privileges on behalf of the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @param privileges The privileges to deny the third party.
     */
    void deny(ThirdParty thirdParty, User user, Set<Privilege> privileges);

    /**
     * Returns all the privileges the provided third party has when representing the provided user.
     *
     * @param thirdParty The third party.
     * @param user       The user.
     * @return All the privileges the provided third party has when representing the provided user.
     */
    Set<Privilege> get(ThirdParty thirdParty, User user);
}
