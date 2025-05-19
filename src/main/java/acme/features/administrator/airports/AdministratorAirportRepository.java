/*
 * AdministratorAirportRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.airports;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.airports.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from Airline a where a.iataCode = :code")
	Airline findAirlineByIataCode(String code);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airport a WHERE a.id =:id")
	Airport findAirportById(int id);

	//	@Query("select a from Airline a where a.moment > :deadline")
	//Collection<Airline> findAnnouncementsByMoment(Date deadline);

}
