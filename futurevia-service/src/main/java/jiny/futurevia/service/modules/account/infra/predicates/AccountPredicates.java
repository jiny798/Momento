package jiny.futurevia.service.modules.account.infra.predicates;

import com.querydsl.core.types.Predicate;
import jiny.futurevia.service.modules.account.domain.entity.QAccount;
import jiny.futurevia.service.modules.account.domain.entity.Zone;
import jiny.futurevia.service.modules.tag.domain.entity.Tag;


import java.util.Set;

public class AccountPredicates {
    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;
        return account.zones.any().in(zones).and(account.tags.any().in(tags));
    }
}