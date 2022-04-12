package com.mini.miniproject.repository;

import com.mini.miniproject.model.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {
    List<Good> findAllByPostid(long pid);
    List<Good> findAllByPostidAndUsername(long pid,String name);

}
