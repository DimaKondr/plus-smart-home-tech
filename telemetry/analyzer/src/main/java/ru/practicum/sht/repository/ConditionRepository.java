package ru.practicum.sht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.sht.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {}