package com.expenseflow.service;

import com.expenseflow.dto.FinanceActionRequest;
import com.expenseflow.dto.ManagerDecisionRequest;
import com.expenseflow.dto.PageResponse;
import com.expenseflow.dto.PurchaseRequestRequest;
import com.expenseflow.dto.PurchaseRequestResponse;
import com.expenseflow.entity.PurchaseRequest;
import com.expenseflow.entity.RequestStatus;
import com.expenseflow.exception.BadRequestException;
import com.expenseflow.exception.NotFoundException;
import com.expenseflow.mapper.ExpenseMapper;
import com.expenseflow.repository.PurchaseRequestRepository;
import com.expenseflow.spec.PurchaseRequestSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class PurchaseRequestService {

    private final PurchaseRequestRepository repository;

    public PurchaseRequestService(PurchaseRequestRepository repository) {
        this.repository = repository;
    }

    public PageResponse<PurchaseRequestResponse> list(RequestStatus status, String employeeName, Pageable pageable) {
        Specification<PurchaseRequest> spec = PurchaseRequestSpecifications.compose(status, employeeName);
        Page<PurchaseRequest> page = repository.findAll(spec, pageable);
        return new PageResponse<>(
                page.stream().map(ExpenseMapper::toResponse).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    public PurchaseRequestResponse find(Long id) {
        return ExpenseMapper.toResponse(load(id));
    }

    public PurchaseRequestResponse create(PurchaseRequestRequest request) {
        PurchaseRequest entity = new PurchaseRequest();
        applyRequest(entity, request);
        entity.setStatus(RequestStatus.DRAFT);
        return ExpenseMapper.toResponse(repository.save(entity));
    }

    public PurchaseRequestResponse update(Long id, PurchaseRequestRequest request) {
        PurchaseRequest entity = load(id);
        if (entity.getStatus() != RequestStatus.DRAFT && entity.getStatus() != RequestStatus.RETURNED) {
            throw new BadRequestException("Only draft or returned purchase requests can be edited");
        }
        applyRequest(entity, request);
        return ExpenseMapper.toResponse(repository.save(entity));
    }

    public PurchaseRequestResponse submit(Long id) {
        PurchaseRequest entity = load(id);
        if (entity.getStatus() != RequestStatus.DRAFT && entity.getStatus() != RequestStatus.RETURNED) {
            throw new BadRequestException("Only draft or returned purchase requests can be submitted");
        }
        entity.setStatus(RequestStatus.SUBMITTED);
        entity.setSubmittedAt(Instant.now());
        return ExpenseMapper.toResponse(repository.save(entity));
    }

    public PurchaseRequestResponse managerDecision(Long id, ManagerDecisionRequest request) {
        PurchaseRequest entity = load(id);
        if (entity.getStatus() != RequestStatus.SUBMITTED) {
            throw new BadRequestException("Only submitted purchase requests can be processed");
        }
        if (request.status() != RequestStatus.APPROVED && request.status() != RequestStatus.RETURNED) {
            throw new BadRequestException("Manager decision must be APPROVED or RETURNED");
        }
        if (request.status() == RequestStatus.RETURNED && (request.comment() == null || request.comment().isBlank())) {
            throw new BadRequestException("Comment is required when returning a purchase request");
        }
        entity.setStatus(request.status());
        entity.setManagerComment(request.comment());
        return ExpenseMapper.toResponse(repository.save(entity));
    }

    public PurchaseRequestResponse markPaid(Long id, FinanceActionRequest request) {
        PurchaseRequest entity = load(id);
        if (entity.getStatus() != RequestStatus.APPROVED) {
            throw new BadRequestException("Only approved purchase requests can be marked as paid");
        }
        entity.setStatus(RequestStatus.PAID);
        entity.setPaidAt(Instant.now());
        entity.setFinanceNote(request.note());
        return ExpenseMapper.toResponse(repository.save(entity));
    }

    private void applyRequest(PurchaseRequest entity, PurchaseRequestRequest request) {
        entity.setEmployeeName(request.employeeName());
        entity.setVendor(request.vendor());
        entity.setTotal(request.total());
        entity.setJustification(request.justification());
        entity.setAttachmentPath(request.attachmentPath());
    }

    private PurchaseRequest load(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase request %d not found".formatted(id)));
    }
}
