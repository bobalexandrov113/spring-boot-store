package com.cba.store.users;

import com.cba.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}