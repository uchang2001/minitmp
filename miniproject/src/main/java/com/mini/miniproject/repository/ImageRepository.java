package com.mini.miniproject.repository;

import com.mini.miniproject.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image>findAllByPostid(long pid);
    List<Image>deleteAllByPostid(long pid);

}
