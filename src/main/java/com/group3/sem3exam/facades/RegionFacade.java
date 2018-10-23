package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.repositories.RegionRepository;
import com.group3.sem3exam.rest.exceptions.RegionNotFoundException;

import java.util.List;
import java.util.function.Supplier;

public class RegionFacade
{

    /**
     * The factory that produces region repositories used by this facade.
     */
    private final Supplier<RegionRepository> regionRepositoryFactory;

    /**
     * Creates a new {@link RegionFacade}.
     *
     * @param regionRepositoryFactory The factory that produces region repositories used by this facade.
     */
    public RegionFacade(Supplier<RegionRepository> regionRepositoryFactory)
    {
        this.regionRepositoryFactory = regionRepositoryFactory;
    }

    /**
     * Returns a complete list of the regions in the system
     *
     * @return The complete list of the regions in the system.
     */
    public List<Region> all()
    {
        try (RegionRepository rr = regionRepositoryFactory.get()) {
            return rr.get();
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
        try (RegionRepository rr = regionRepositoryFactory.get()) {
            Region region = rr.get(id);
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
        try (RegionRepository rr = regionRepositoryFactory.get()) {
            return rr.getByCountry(country);
        }
    }
}
