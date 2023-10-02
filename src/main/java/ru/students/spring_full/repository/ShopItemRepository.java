package ru.students.spring_full.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.students.spring_full.entity.ShopItem;
import ru.students.spring_full.entity.User;
import ru.students.spring_full.enums.ShopItemStatusEnum;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {

    @Query("select i from ShopItem i where i.owner = :owner and i.createdAt >= :startAt and i.createdAt <= :endAt and i.deletedAt is null order by i.createdAt desc")
    List<ShopItem> findActiveItems(User owner, OffsetDateTime startAt, OffsetDateTime endAt);

    @Query("select i.value from ShopItem i where i.owner = :owner and i.createdAt > :searchDepth and i.deletedAt is null group by i.value order by count(*) desc")
    List<String> suggestRecent(User owner, OffsetDateTime searchDepth);

    @Query("update ShopItem i set i.status = :toStatus, i.updatedAt = :now where i.owner = :owner and i.status in (:fromStatus) and i.deletedAt is null")
    @Modifying
    @Transactional
    int setStatusAll(User owner, Collection<ShopItemStatusEnum> fromStatus, ShopItemStatusEnum toStatus, OffsetDateTime now);
}
