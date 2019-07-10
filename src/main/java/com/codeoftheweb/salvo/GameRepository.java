package com.codeoftheweb.salvo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {


    Game findById(long id);


}