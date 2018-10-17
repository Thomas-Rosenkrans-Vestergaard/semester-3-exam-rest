package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.repositories.TransactionalRegionRepository;
import com.group3.sem3exam.rest.exceptions.RegionNotFoundException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class RegionFacade
{

    private EntityManagerFactory emf;

    public RegionFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    /**
     * Returns a complete list of the regions in the system
     *
     * @return The complete list of the regions in the system.
     */
    public List<Region> all()
    {
        try (TransactionalRegionRepository trr = new TransactionalRegionRepository(emf)) {
            return trr.get();
        }
    }

    /**
     * Returns the region with the provided id.
     *
     * @param id The id of the region to return.
     * @return The region with the provided id.
     * @throws RegionNotFoundException When the region with the provided id does not exist.
     */
    public Region get(Integer id) throws RegionNotFoundException
    {
        try (TransactionalRegionRepository trr = new TransactionalRegionRepository(emf)) {
            Region region = trr.get(id);
            if (region == null)
                throw new RegionNotFoundException(id);

            return region;
        }
    }

    /**
     * Returns the regions in the country with the provided id.
     *
     * @param country The id of the country to return the regions of.
     * @return The regions in the country with the provided id.
     */
    public List<Region> getByCountry(int country)
    {
        try(TransactionalRegionRepository trr = new TransactionalRegionRepository(emf)){
            return trr.getByCountry(country);
        }
    }
}
