package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ThirdParty;

public interface ThirdPartyRepository extends CrudRepository<ThirdParty, Integer>
{

    /**
     * Creates a new third party entity using the provided information.
     *
     * @param name         The name of the new third party.
     * @param passwordHash The password hash of the new third party.
     * @return The newly created third party entity.
     */
    ThirdParty create(String name, String passwordHash);

    /**
     * Returns the third party with the provided name.
     *
     * @param name The name of the third party to return.
     * @return The third party with the provided name.
     */
    ThirdParty getByName(String name);
}
