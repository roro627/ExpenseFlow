package com.expenseflow.spec;

import com.expenseflow.entity.PurchaseRequest;
import com.expenseflow.entity.RequestStatus;
import org.springframework.data.jpa.domain.Specification;

public final class PurchaseRequestSpecifications {

    private PurchaseRequestSpecifications() {
    }

    public static Specification<PurchaseRequest> compose(RequestStatus status, String employeeName) {
        Specification<PurchaseRequest> spec = Specification.where(null);
        if (status != null) {
            Specification<PurchaseRequest> s = (root, query, cb) -> cb.equal(root.get("status"), status);
            spec = spec == null ? s : spec.and(s);
        }
        if (employeeName != null && !employeeName.isBlank()) {
            String pattern = "%" + employeeName.toLowerCase() + "%";
            Specification<PurchaseRequest> s = (root, query, cb) ->
                    cb.like(cb.lower(root.get("employeeName")), pattern);
            spec = spec == null ? s : spec.and(s);
        }
        return spec;
    }
}
