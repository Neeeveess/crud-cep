package br.com.crudcep.repository

import br.com.crudcep.domain.Address
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, String>
